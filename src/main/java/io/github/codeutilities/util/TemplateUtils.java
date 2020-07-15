package io.github.codeutilities.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.internal.$Gson$Preconditions;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class TemplateUtils {

    public static void applyTemplateNBT(ItemStack stack, String name, String author, String codeData) {
        CompoundTag publicBukkitNBT = new CompoundTag();
        CompoundTag itemNBT = new CompoundTag();
        CompoundTag codeNBT = new CompoundTag();

        codeNBT.putString("name", name);
        codeNBT.putString("author", author);
        codeNBT.putString("code", codeData);
        codeNBT.putInt("version", 1);

        // Apply the template data to the item.
        publicBukkitNBT.putString("hypercube:codetemplatedata", codeNBT.toString());

        // Assign the bukkit container to the item. (Contains the template data)
        itemNBT.put("PublicBukkitValues", publicBukkitNBT);
        stack.setTag(itemNBT);

    }


    public static void applyCompressedTemplateNBT(ItemStack stack, String name, String author, String template) {
        try {
            byte[] b64 = CompressionUtil.toBase64(CompressionUtil.toGZIP(template.getBytes(StandardCharsets.UTF_8)));
            String exported = new String(b64);
            applyTemplateNBT(stack, name, author, exported);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
