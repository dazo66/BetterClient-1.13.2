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
public class PlayerDestroyBlockInject implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList("(Lnet/minecraft/util/math/BlockPos;)Z", "onPlayerDestroyBlock", "func_187103_a", "a");

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.client.multiplayer.PlayerControllerMP");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
                AbstractInsnNode insnNode = null;
                while (iterator.hasNext()) {
                    if (iterator.next().getOpcode() != -1) {
                        insnNode = iterator.previous();
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/EventPoster", "onPlayerDestroy", "(Lnet/minecraft/util/math/BlockPos;)Z", false));
                        LabelNode label0 = new LabelNode();
                        list.add(new JumpInsnNode(Opcodes.IFEQ, label0));
                        list.add(new InsnNode(Opcodes.ICONST_0));
                        list.add(new InsnNode(Opcodes.IRETURN));
                        list.add(new LabelNode(label0.getLabel()));
                        method.instructions.insert(insnNode, list);
                        break;
                    }
                }
            }
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
