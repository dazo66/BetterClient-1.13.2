package com.dazo66.fasttrading.transformsclass;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import com.dazo66.fasttrading.event.SetMerchantListEvent;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class HookSetRecipeListEvent implements IRegisterTransformer {

    @Override
    public String getMcVersion() {
        return "[1.8,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.entity.NpcMerchant");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        MethodNode method = classNode.methods.get(4);
        InsnList insnList = method.instructions;
        insnList.remove(insnList.getLast());
        insnList.remove(insnList.getLast());
        insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        MethodInsnNode m = new MethodInsnNode(Opcodes.INVOKESTATIC, SetMerchantListEvent.class.getName().replace(".", "/"), "post", "(Lnet/minecraft/village/MerchantRecipeList;)V", false);
        insnList.add(m);
        insnList.add(new InsnNode(Opcodes.RETURN));
        insnList.add(new LabelNode());
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}