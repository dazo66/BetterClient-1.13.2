package com.dazo66.fastcrafting.crafting;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
public class DeserializeItem {

    private static final Pattern DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
    private static final Pattern FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
    private static final Pattern BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
    private static final Pattern LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
    private static final Pattern SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
    private static final Pattern INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

    public static ItemStack deserializeItem(JsonObject json, JsonContext context) {
        String s = context.appendModId(JsonUtils.getString(json, "item"));
        Item item = Item.REGISTRY.getObject(new ResourceLocation(s));

        if (item == null) {
            throw new JsonSyntaxException("Unknown item '" + s + "'");
        } else if (item.getHasSubtypes() && !json.has("data")) {
            throw new JsonParseException("Missing data for item '" + s + "'");
        } else {
            ItemStack itemStack = new ItemStack(item);
            NBTTagCompound nbt = new NBTTagCompound();
            JsonObject json1 = null;
            if (json.has("nbt")) {
                json1 = JsonUtils.getJsonObject(json, "nbt");
            } else {
                return itemStack;
            }
            itemStack.setTagCompound(deserializeNBT(json.getAsJsonObject("nbt")));
            return itemStack;
        }
    }

    private static NBTTagCompound deserializeNBT(JsonObject json) {
        NBTTagCompound nbt = new NBTTagCompound();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            NBTTagCompound nbt1;
            if (entry.getValue().isJsonObject()) {
                nbt1 = deserializeNBT(entry.getValue().getAsJsonObject());
                nbt.setTag(entry.getKey(), nbt1);
            } else {
                nbt.setTag(entry.getKey(), type(entry.getValue().getAsString()));
            }
        }
        return nbt;

    }

    private static NBTBase type(String stringIn) {
        try {
            if (FLOAT_PATTERN.matcher(stringIn).matches()) {
                return new NBTTagFloat(Float.parseFloat(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (BYTE_PATTERN.matcher(stringIn).matches()) {
                return new NBTTagByte(Byte.parseByte(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (LONG_PATTERN.matcher(stringIn).matches()) {
                return new NBTTagLong(Long.parseLong(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (SHORT_PATTERN.matcher(stringIn).matches()) {
                return new NBTTagShort(Short.parseShort(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (INT_PATTERN.matcher(stringIn).matches()) {
                return new NBTTagInt(Integer.parseInt(stringIn));
            }

            if (DOUBLE_PATTERN.matcher(stringIn).matches()) {
                return new NBTTagDouble(Double.parseDouble(stringIn.substring(0, stringIn.length() - 1)));
            }

            if (DOUBLE_PATTERN_NOSUFFIX.matcher(stringIn).matches()) {
                return new NBTTagDouble(Double.parseDouble(stringIn));
            }

            if ("true".equalsIgnoreCase(stringIn)) {
                return new NBTTagByte((byte) 1);
            }

            if ("false".equalsIgnoreCase(stringIn)) {
                return new NBTTagByte((byte) 0);
            }
        } catch (NumberFormatException var3) {
            var3.printStackTrace();
        }

        return new NBTTagString(stringIn);
    }
}
