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
public class GuiCloseEventInject implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList(new String[]{"displayGuiScreen", "(Lnet/minecraft/client/gui/GuiScreen;)V", "func_147108_a", "bib/a", "(Lblk;)V"});

    @Override
    public String getMcVersion() {
        return "[1.8,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList(new String[]{"net.minecraft.client.Minecraft"});
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        out:
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                ListIterator<AbstractInsnNode> iterable = method.instructions.iterator();
                AbstractInsnNode node;
                while (iterable.hasNext()) {
                    node = iterable.next();
                    if (node.getOpcode() == Opcodes.ALOAD && node.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL) {
                        if (((VarInsnNode) node).var == 2) {
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/GuiCloseEvent", "post", "(Lnet/minecraft/client/gui/GuiScreen;)V", false));
                            method.instructions.insert(node.getPrevious(), list);
                            break out;
                        }
                    }
                }
            }
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
