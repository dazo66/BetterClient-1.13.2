package com.dazo66.fastcrafting.transformer;

import com.dazo66.betterclient.coremod.IRegisterTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dazo66
 */
public class MouseClickTransformerClass implements IRegisterTransformer {

    private List<String> methodInfo = Arrays.asList("func_73864_a", "mouseClicked", "(III)V", "a");
    private String fieldDesc = "Lcom/dazo66/fastcrafting/gui/GuiInventoryModifier;";
    private String fieldName = "guiModifier";
    private String fieldOwner;
    private String modifierClassName = "com/dazo66/fastcrafting/gui/GuiInventoryModifier";
    private String initDesc1 = "(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V";
    private String initDesc2 = "(Lnet/minecraft/entity/player/EntityPlayer;)V";

    @Override
    public String getMcVersion() {
        return "[1.12,1.12.2]";
    }

    @Override
    public List<String> getClassName() {
        return Arrays.asList("net.minecraft.client.gui.inventory.GuiCrafting", "net.minecraft.client.gui.inventory.GuiInventory");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        fieldOwner = transformedName.replace(".", "/");
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        classNode.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, fieldName, fieldDesc, null, null));
        for (MethodNode method : classNode.methods) {
            if (methodInfo.contains(method.name) && methodInfo.contains(method.desc)) {
                InsnList insnList = method.instructions;
                AbstractInsnNode returnNode = null;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == Opcodes.RETURN) {
                        returnNode = insnNode;
                        break;
                    }
                }
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new FieldInsnNode(Opcodes.GETFIELD, fieldOwner, fieldName, fieldDesc));
                list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "com/dazo66/fastcrafting/gui/GuiInventoryModifier", "mouseClicked", "()V", false));
                if (returnNode != null) {
                    insnList.insert(returnNode.getPrevious(), list);
                }

            }
            boolean isInit = initDesc1.equals(method.desc) || initDesc2.equals(method.desc);
            if ("<init>".equals(method.name) && isInit) {
                InsnList insnList = method.instructions;
                AbstractInsnNode returnNode = null;
                for (AbstractInsnNode insnNode : insnList.toArray()) {
                    if (insnNode.getOpcode() == Opcodes.RETURN) {
                        returnNode = insnNode;
                        break;
                    }
                }
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new TypeInsnNode(Opcodes.NEW, modifierClassName));
                list.add(new InsnNode(Opcodes.DUP));
                list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "com/dazo66/fastcrafting/gui/GuiInventoryModifier", "<init>", "(Lnet/minecraft/client/gui/inventory/GuiContainer;)V", false));
                list.add(new FieldInsnNode(Opcodes.PUTFIELD, fieldOwner, fieldName, fieldDesc));
                if (returnNode != null) {
                    insnList.insert(returnNode.getPrevious(), list);
                }
            }
        }
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
