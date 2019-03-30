package com.dazo66.prompt.subfunction.projectileprompt.transformer;

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
public class ParticleRenferRangeInject implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList("spawnParticle0", "func_190571_b", "(IZZDDDDDD[I)Lnet/minecraft/client/particle/Particle;");

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList( "net.minecraft.client.renderer.RenderGlobal");
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
                    if (node instanceof LdcInsnNode && Double.valueOf(1024.0D).equals(((LdcInsnNode) node).cst)) {
                        MethodInsnNode methodNode = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/dazo66/prompt/subfunction/projectileprompt/util/ParticleRenderRange", "getRange", "()D", false);
                        method.instructions.set(node, methodNode);
                        break out;
                    }
                }
            }
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
