package com.dazo66.fastcrafting.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.fastcrafting.crafting.CraftingLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class MineRecipe implements IRegisterTransformer {
    private final List<String> methodInfo = Arrays.asList("init", "()V");

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraftforge.common.crafting.CraftingHelper");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();

        classReader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                InsnList insnList = method.instructions;
                insnList.remove(insnList.getLast());
                MethodInsnNode m = new MethodInsnNode(Opcodes.INVOKESTATIC, CraftingLoader.class.getName().replace(".", "/"), "init", "()V", false);
                insnList.add(m);
                insnList.add(new InsnNode(Opcodes.RETURN));
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}