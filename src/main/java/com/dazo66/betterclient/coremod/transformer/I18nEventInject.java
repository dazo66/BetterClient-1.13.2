package com.dazo66.betterclient.coremod.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author Dazo66
 */
public class I18nEventInject implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList("format", "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;");

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.client.resources.I18n");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        out:
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKESTATIC, "com/dazo66/betterclient/event/I18nEvent", "post", "(Ljava/lang/String;[Ljava/lang/Object;)Lcom/dazo66/betterclient/event/I18nEvent;", false));
                list.add(new VarInsnNode(ASTORE, 2));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/dazo66/betterclient/event/I18nEvent", "getLangKey", "()Ljava/lang/String;", false));
                list.add(new VarInsnNode(ASTORE, 0));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEVIRTUAL, "com/dazo66/betterclient/event/I18nEvent", "getArgs", "()[Ljava/lang/Object;", false));
                list.add(new VarInsnNode(ASTORE, 1));
                AbstractInsnNode firstNode = method.instructions.getFirst();
                method.instructions.insert(firstNode.getNext(), list);
                method.localVariables.add(new LocalVariableNode("event", "Lcom/dazo66/betterclient/event/I18nEvent;", null, (LabelNode) method.instructions.getFirst(), (LabelNode) method.instructions.getLast(), 2));
                method.visitMaxs(3, 3);
            }
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
