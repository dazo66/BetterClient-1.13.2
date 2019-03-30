package com.dazo66.fasttrading.client.gui;

import com.dazo66.betterclient.util.reflection.ReflectionHelper;
import com.dazo66.fasttrading.FastTrading;
import com.dazo66.fasttrading.config.ConfigJson;
import com.dazo66.fasttrading.util.ItemStackUtils;
import com.dazo66.fasttrading.util.TradingHelper;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.dazo66.fasttrading.FastTrading.configLoader;
import static net.minecraft.client.gui.GuiScreen.isShiftKeyDown;

/**
 * @author Dazo66
 */
public class GuiMerchantModifier {

    private Minecraft mc = Minecraft.getMinecraft();
    private GuiMerchant gui;
    public TradingHelper helper;
    private MerchantRecipeList merchantRecipeList;
    private Container inventoryPlayer;
    private Method clickMethod;
    private IMerchant iMerchant;
    private List<GuiButton> buttonList;
    private int lastClickTime;
    private GuiButton lastClickButton;
    private ArrayList<GuiRecipeButton> recipeButtonList = new ArrayList<>();
    private HashMap<String, GuiIconButton> buttonMap = new HashMap<>();

    public GuiMerchantModifier(GuiMerchant guiMerchant) {
//        super(inventoryPlayer, iMerchant, worldIn);
        gui = guiMerchant;
        helper = new TradingHelper(this);
        buttonList = guiMerchant.buttonList;
        inventoryPlayer = guiMerchant.inventorySlots;
        iMerchant = guiMerchant.getMerchant();
        //handleClick
        clickMethod = getClickMethod();
        configLoader.load();
        resetButton();
    }

    public void initGui() {
        resetButton();
    }

    private void resetButton(){
        buttonList = gui.buttonList;
        recipeButtonList.clear();
        buttonMap.clear();
        addMerchantButton(merchantRecipeList);
        addFunctionButton();
    }


    //reflect method
    public void click(Slot slot, int mouseButton, ClickType type) {
        try {
            clickMethod.invoke(gui, slot, slot.slotNumber, mouseButton, type);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float p) {
        GuiButton button0 = getFirstButton();
        if (button0 instanceof GuiRecipeButton) {
            ((GuiRecipeButton) button0).tryProminent(mc, mouseX, mouseY, p, true);
            ((GuiRecipeButton) button0).tryRenderItemTooltip(mouseX, mouseY);
        }
        for (GuiButton button : buttonList) {
            if (button instanceof GuiButtonPlus) {
                ((GuiButtonPlus) button).drawTooltip(mc, mouseX, mouseY);
            }
            if (button == button0) {
                continue;
            }
            if (button instanceof GuiRecipeButton) {
                ((GuiRecipeButton) button).tryProminent(mc, mouseX, mouseY, p, false);
            }
        }

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isShiftKeyDown()) {
            Slot slot = this.getSlotAtPosition(mouseX, mouseY);
            if (null != slot && slot.getHasStack() && slot.slotNumber != 0 && slot.slotNumber != 1 && slot.slotNumber != 2) {
                if (!inventoryPlayer.getSlot(0).getHasStack()) {
                    moveItem(slot, inventoryPlayer.getSlot(0));
                } else if (!inventoryPlayer.getSlot(1).getHasStack()) {
                    moveItem(slot, inventoryPlayer.getSlot(1));
                }
            }
        }
    }

    private void moveItem(Slot from, Slot to) {
        click(from, 0, ClickType.PICKUP);
        click(to, 0, ClickType.PICKUP);
        if (!mc.player.inventory.getItemStack().isEmpty()) {
            click(from, 0, ClickType.PICKUP);
        }
    }


    private Slot getSlotAtPosition(int x, int y) {
        for (int i = 0; i < inventoryPlayer.inventorySlots.size(); ++i) {
            Slot slot = inventoryPlayer.inventorySlots.get(i);

            if (isPointInRegion(slot.xPos, slot.yPos, 16, 16, x, y) && slot.isEnabled()) {
                return slot;
            }
        }

        return null;
    }

    private GuiButton getFirstButton() {
        ArrayList<GuiButton> list = new ArrayList<>();
        for (GuiButton button : buttonList) {
            if (button.isMouseOver()) {
                list.add(button);
            }
        }
        for (GuiButton button1 : list) {
            if (button1 instanceof GuiRecipeButton && ((GuiRecipeButton) button1).hasBeenMove) {
                return button1;
            }
        }
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public void onGuiClosed() {
        configLoader.onSave();
    }

    public void updateScreen() {
        functionButtonUpdate();
    }

    public void actionPerformed(GuiButton button) {
        int currentTime = (int) System.currentTimeMillis() % Integer.MAX_VALUE;
        if (merchantRecipeList == null) {
            merchantRecipeList = iMerchant.getRecipes(mc.player);
        }
        GuiButton button1 = getFirstButton();
        if (button == button1) {
            MerchantRecipe recipe = merchantRecipeList.get(gui.selectedMerchantRecipe);
            ConfigJson.SimpleRecipe currentRecipe = helper.map.get(recipe);

            if (!button.enabled || !button.visible) {
                return;
            }
            if (recipeButtonList.contains(button)) {
                int recipeIndex = button.id - 300;
                setCurrentRecipe(recipeIndex);
                if (lastClickTime > currentTime - 500 && lastClickButton == button) {
                    if (isShiftKeyDown()) {
                        helper.trading(merchantRecipeList.get(recipeIndex), recipeIndex);
                    } else {
                        helper.tradingOnce(merchantRecipeList.get(recipeIndex), recipeIndex);
                    }
                    lastClickButton = null;
                    lastClickTime = 0;
                } else {
                    lastClickButton = button;
                    lastClickTime = currentTime;
                }
            } else if (button.id == 250) {
                FastTrading.setAuto(true);
            } else if (button.id == 251) {
                FastTrading.setAuto(false);
            } else if (button.id == 252) {
                ConfigJson.SimpleRecipe recipe1 = new ConfigJson.SimpleRecipe(false, recipe);
                configLoader.recipeList.add(recipe1);
                helper.map.put(recipe, recipe1);
            } else if (button.id == 253) {
                configLoader.recipeList.remove(currentRecipe);
                helper.map.remove(recipe);
            } else if (button.id == 254) {
                ConfigJson.SimpleRecipe recipe1 = new ConfigJson.SimpleRecipe(true, recipe);
                configLoader.recipeList.remove(currentRecipe);
                configLoader.recipeList.add(recipe1);
                helper.map.put(recipe, recipe1);
            } else if (button.id == 255) {
                if (null != currentRecipe) {
                    currentRecipe.setLockPrice(false);
                }
            }
        }

    }

    private void addFunctionButton() {
        int guiLeft = gui.getGuiLeft();
        int guiTop = gui.getGuiTop();
        GuiIconButton onButton = null;
        GuiIconButton offButton = null;
        GuiIconButton addButton = null;
        GuiIconButton subtractButton = null;
        GuiIconButton lockButton = null;
        GuiIconButton unlockButton = null;
        ResourceLocation ON = new ResourceLocation(FastTrading.MODID, "textures/gui/on.png");
        ResourceLocation OFF = new ResourceLocation(FastTrading.MODID, "textures/gui/off.png");
        ResourceLocation PLUS = new ResourceLocation(FastTrading.MODID, "textures/gui/plus.png");
        ResourceLocation SUBTRACT = new ResourceLocation(FastTrading.MODID, "textures/gui/subtract.png");
        ResourceLocation LOCK = new ResourceLocation(FastTrading.MODID, "textures/gui/lock.png");
        ResourceLocation UNLOCK = new ResourceLocation(FastTrading.MODID, "textures/gui/unlock.png");
        onButton = new GuiIconButton(250, guiLeft + 3, guiTop + 3, 10, 10, ItemStackUtils.tooltipI18n("fasttrading.tooltip.enablebutton"), ON);
        offButton = new GuiIconButton(251, guiLeft + 3, guiTop + 3, 10, 10, ItemStackUtils.tooltipI18n("fasttrading.tooltip.disablebutton"), OFF);
        addButton = new GuiIconButton(252, guiLeft + 14, guiTop + 3, 10, 10, ItemStackUtils.tooltipI18n("fasttrading.tooltip.addbutton"), PLUS);
        subtractButton = new GuiIconButton(253, guiLeft + 14, guiTop + 3, 10, 10, ItemStackUtils.tooltipI18n("fasttrading.tooltip.removebutton"), SUBTRACT);
        lockButton = new GuiIconButton(254, guiLeft + 25, guiTop + 3, 10, 10, ItemStackUtils.tooltipI18n("fasttrading.tooltip.lockbutton"), LOCK);
        unlockButton = new GuiIconButton(255, guiLeft + 25, guiTop + 3, 10, 10, ItemStackUtils.tooltipI18n("fasttrading.tooltip.unlockbutton"), UNLOCK);
        buttonList.add(onButton);
        buttonList.add(offButton);
        buttonList.add(addButton);
        buttonList.add(subtractButton);
        buttonList.add(lockButton);
        buttonList.add(unlockButton);
        buttonMap.put("onButton", onButton);
        buttonMap.put("offButton", offButton);
        buttonMap.put("addButton", addButton);
        buttonMap.put("subtractButton", subtractButton);
        buttonMap.put("lockButton", lockButton);
        buttonMap.put("unlockButton", unlockButton);
        functionButtonUpdate();
    }

    private void functionButtonUpdate() {
        if (FastTrading.isAuto.getValue()) {
            buttonMap.get("onButton").visible = false;
            buttonMap.get("offButton").visible = true;
        } else {
            buttonMap.get("onButton").visible = true;
            buttonMap.get("offButton").visible = false;
        }
        if (null == merchantRecipeList || merchantRecipeList.isEmpty()) {
            return;
        }
        MerchantRecipe recipe = merchantRecipeList.get(gui.selectedMerchantRecipe);
        ConfigJson.SimpleRecipe simpleRecipe = helper.map.get(recipe);
        if (null == simpleRecipe) {
            buttonMap.get("addButton").visible = true;
            buttonMap.get("subtractButton").visible = false;
            buttonMap.get("lockButton").visible = false;
            buttonMap.get("unlockButton").visible = false;
        } else {
            buttonMap.get("addButton").visible = false;
            buttonMap.get("subtractButton").visible = true;
            buttonMap.get("lockButton").visible = true;
            buttonMap.get("unlockButton").visible = false;
            if (simpleRecipe.lockPrice) {
                if (ConfigJson.isRecipeEqual(recipe, simpleRecipe)) {
                    buttonMap.get("unlockButton").visible = true;
                    buttonMap.get("lockButton").visible = false;
                } else {
                    buttonMap.get("unlockButton").visible = false;
                    buttonMap.get("lockButton").visible = true;
                }
            }
        }
    }

    private void addMerchantButton(MerchantRecipeList list) {
        if (list == null || list.isEmpty()) {
            list = iMerchant.getRecipes(mc.player);
            if (list != null) {
                addMerchantButton(list);
            }
            return;
        }
        int i = 0;
        int spacing = 25;
        int top = gui.getGuiTop();
        if (list.size() * 25 > 166 && list.size() * 15 < 166) {
            spacing = 166 / list.size();
        } else if (list.size() * 15 >= 166) {
            spacing = 15;
            top = top - (list.size() * 15 - 166) / 2;
        }
        for (MerchantRecipe merchantRecipe : list) {
            GuiRecipeButton button = new GuiRecipeButton(300 + i++, gui.getGuiLeft() - 89 - 1, top - spacing + i * spacing, this, merchantRecipe);
            for (GuiButton button1 : buttonList) {
                if (button1.id == button.id) {
                    return;
                }
            }
            buttonList.add(button);
            recipeButtonList.add(button);
        }
    }

    public void setCurrentRecipe(int index) {
        int selectedMerchantRecipe = gui.selectedMerchantRecipe;
        if (index != selectedMerchantRecipe) {
            selectedMerchantRecipe = index;
            gui.selectedMerchantRecipe = index;
            ((ContainerMerchant) inventoryPlayer).setCurrentRecipeIndex(selectedMerchantRecipe);
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(selectedMerchantRecipe);
            this.mc.getConnection().sendPacket(new CPacketCustomPayload("MC|TrSel", packetbuffer));
        }
    }

    public void setMerchantRecipeList(MerchantRecipeList list) {
        merchantRecipeList = list;
        addMerchantButton(merchantRecipeList);
        helper.init(merchantRecipeList);
    }

    private boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
        int i = gui.getGuiLeft();
        int j = gui.getGuiTop();
        pointX = pointX - i;
        pointY = pointY - j;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    private Method getClickMethod(){
        return ReflectionHelper.getInstance().getMethod(GuiContainer.class, "handleMouseClick");
    }

    public MerchantRecipeList getMerchantRecipeList() {
        return merchantRecipeList;
    }

    public GuiMerchant getGui() {
        return gui;
    }
}
