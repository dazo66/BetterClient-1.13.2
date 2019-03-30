package com.dazo66.betterclient.coremod.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Dazo66
 */
public class SectionEventInject implements IRegisterTransformer {

    private List<String> endMethodInfo = Arrays.asList("func_76319_b", "()V", "endSection", "b");
    private List<String> startMethodInfo = Arrays.asList("startSection", "func_76320_a", "(Ljava/lang/String;)V", "a");
    private List<String> supplierStart = Arrays.asList("func_194340_a", "(Ljava/util/function/Supplier;)V", "a");

    public static AbstractInsnNode getFirstInsnNode(InsnList list) {
        ListIterator<AbstractInsnNode> iterable = list.iterator();
        AbstractInsnNode node;
        while (iterable.hasNext()) {
            node = iterable.next();
            if (node.getOpcode() != -1) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.profiler.Profiler");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        out:
        for (MethodNode method : classNode.methods) {
            if (startMethodInfo.contains(method.name) && startMethodInfo.contains(method.desc)) {
                AbstractInsnNode node = getFirstInsnNode(method.instructions);
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/SectionEvent", "postStart", "(Ljava/lang/String;)V", false));
                method.instructions.insert(node.getPrevious(), list);
            }
            if (endMethodInfo.contains(method.name) && endMethodInfo.contains(method.desc)) {
                AbstractInsnNode node = getFirstInsnNode(method.instructions);
                InsnList list = new InsnList();
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/SectionEvent", "postEnd", "()V", false));
                method.instructions.insert(node.getPrevious(), list);
            }
            if (supplierStart.contains(method.name) && supplierStart.contains(method.desc)) {
                AbstractInsnNode node = getFirstInsnNode(method.instructions);
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/SectionEvent", "supplierStart", "(Ljava/util/function/Supplier;)V", false));
                method.instructions.insert(node.getPrevious(), list);
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
