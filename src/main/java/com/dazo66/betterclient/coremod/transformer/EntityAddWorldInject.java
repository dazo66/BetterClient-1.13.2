package com.dazo66.betterclient.coremod.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.ALOAD;

/**
 * @author Dazo66
 */
public class EntityAddWorldInject implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList(new String[]{"onEntityAdded", "func_72923_a", "(Lnet/minecraft/entity/Entity;)V", "b", "(Lvg;)V"});

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList(new String[]{"net.minecraft.world.World"});
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        out:
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                AbstractInsnNode returnNode = null;
                for (AbstractInsnNode node : method.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.RETURN) {
                        returnNode = node;
                        break;
                    }
                }
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/betterclient/event/EntityEvent$EntityAddEvent", "post", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;)V", false));
                if (returnNode != null) {
                    method.instructions.insert(returnNode.getPrevious(), list);
                    method.visitMaxs(3, 3);
                }
                break out;
            }
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
