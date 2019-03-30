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
public class PacketProcessInject implements IRegisterTransformer {
    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.network.PacketThreadUtil$1");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        for (MethodNode method : classNode.methods) {
            if ("run".equals(method.name)) {
                ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
                AbstractInsnNode insnNode = null;
                while (iterator.hasNext()) {
                    if (iterator.next().getOpcode() != -1) {
                        insnNode = iterator.previous();
                        InsnList list = new InsnList();
                        LabelNode label0 = new LabelNode();
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/network/PacketThreadUtil$1", "val$packetIn", "Lnet/minecraft/network/Packet;"));
                        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/network/PacketThreadUtil$1", "val$processor", "Lnet/minecraft/network/INetHandler;"));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/PacketProcessEvent", "post", "(Lnet/minecraft/network/Packet;Lnet/minecraft/network/INetHandler;)Z", false));
                        list.add(new JumpInsnNode(Opcodes.IFEQ, label0));
                        list.add(new InsnNode(Opcodes.RETURN));
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
