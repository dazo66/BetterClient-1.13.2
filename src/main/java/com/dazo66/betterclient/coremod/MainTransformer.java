package com.dazo66.betterclient.coremod;

import com.dazo66.betterclient.BetterClient;
import com.dazo66.betterclient.coremod.transformer.*;
import com.google.common.base.Strings;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Dazo66
 */
public class MainTransformer implements IClassTransformer {

    public static MainTransformer mainTransformer;

    private HashMap<String, List<IRegisterTransformer>> map = new HashMap<>();
    private String MCVERSION = getMCVERSION();

    public MainTransformer() {
        mainTransformer = this;
        if (BetterClient.DEBUG) {
            BetterClient.logger.info("BetterClient Class Transformer Test Start");
        }
        register(new GuiCloseEventInject());
        register(new I18nEventInject());
        register(new EntityAddWorldInject());
        register(new SectionEventInject());
        register(new PacketProcessInject());
        register(new PlayerDestroyBlockInject());
        BetterClient.registerFunctions();
        BetterClient.registerTransformerClass(this);
    }

    public static byte[] clearMethod(String name, String transformedName, byte[] basicClass, List<String> methodInfo) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                method.instructions.clear();
                method.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public static String getMCVERSION() {
        try {
            return (String) (ForgeVersion.class.getField("mcVersion")).get("");
        } catch (Exception e) {
            return "";
        }
    }

    public static void saveClassToFile(byte[] bytes, File outPutFile) throws IOException {
        if (!outPutFile.exists()) {
            outPutFile.createNewFile();
        }
        if (outPutFile.isFile()) {
            FileOutputStream writer = new FileOutputStream(outPutFile);
            writer.write(bytes);
            writer.flush();
            writer.close();
        }
    }

    public void register(IRegisterTransformer iRegisterTransformer) {
        List<String> name = iRegisterTransformer.getClassName();
        if (isVersionAllow(iRegisterTransformer.getMcVersion())) {
            for (String s : name) {
                map.computeIfAbsent(s, k -> new ArrayList<>());
                map.get(s).add(iRegisterTransformer);

            }
            BetterClient.logger.info("{} Register SUCCESS", iRegisterTransformer.getClass().getSimpleName());
        } else {
            BetterClient.logger.warn("This MCVersion is {} but Transformer {} accept MCVersion is {} that ignore this Transformer", MCVERSION, iRegisterTransformer.getClass().getSimpleName(), iRegisterTransformer.getMcVersion());
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        byte[] clone = basicClass.clone();
        try {
            if (map.containsKey(transformedName)) {
                List<IRegisterTransformer> list = map.get(transformedName);
                for (IRegisterTransformer irtf1 : list) {
                    basicClass = irtf1.transform(name, transformedName, basicClass);
                    BetterClient.logger.info("CLASS: " + irtf1.getClass().getSimpleName() + " Transformer SUCCESS");
                }
                return basicClass;
            } else if (map.containsKey(name)) {
                List<IRegisterTransformer> list = map.get(transformedName);
                for (IRegisterTransformer irtf1 : list) {
                    basicClass = irtf1.transform(name, transformedName, basicClass);
                    BetterClient.logger.info("CLASS: " + irtf1.getClass().getSimpleName() + " Transformer SUCCESS");
                }
                return basicClass;
            } else {
                return basicClass;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return clone;
        }

    }

    public void transformerDebug() {
        if (BetterClient.DEBUG) {
            map.forEach((className, transformers) -> {
                try {
                    Launch.classLoader.findClass(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            map.clear();
        }
    }

    private boolean isVersionAllow(String transMcVersion) {
        VersionRange range;
        String mcVersionString = transMcVersion;
        if ("[1.12]".equals(mcVersionString)) {
            mcVersionString = "[1.12,1.12.2]";
        }
        if ("[1.12.1]".equals(mcVersionString) || "[1.12,1.12.1]".equals(mcVersionString)) {
            mcVersionString = "[1.12,1.12.2]";
        }

        if (!Strings.isNullOrEmpty(mcVersionString)) {
            range = VersionParser.parseRange(mcVersionString);
        } else {
            range = Loader.instance().getMinecraftModContainer().getStaticVersionRange();
        }
        return range.containsVersion(new DefaultArtifactVersion(MCVERSION));
    }

}
