package io.github.codeutilities.commands.image;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.commands.Command;
import io.github.codeutilities.commands.arguments.ArgBuilder;
import io.github.codeutilities.images.ImageConverter;
import io.github.codeutilities.util.ChatType;
import io.github.codeutilities.util.ItemUtil;
import io.github.codeutilities.util.TemplateUtils;
import io.github.codeutilities.util.externalfile.ExternalFile;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.io.File;

public class ImageToTemplateCommand115 extends Command {

    @Override
    public void register(MinecraftClient mc, CommandDispatcher<CottonClientCommandSource> cd) {
        cd.register(ArgBuilder.literal("image")
                .then(ArgBuilder.literal("load")
                        .then(ArgBuilder.argument("location", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    String location = StringArgumentType.getString(ctx, "location");
                                    File f = new File(ExternalFile.IMAGE_FILES.getFile(), location + (location.endsWith(".png") ? "" : ".png"));

                                    if (f.exists()) {
                                        String[] strings = ImageConverter.convert115(f);

                                        ItemStack stack = new ItemStack(Items.NOTE_BLOCK);
                                        TemplateUtils.compressTemplateNBT(stack, StringArgumentType.getString(ctx, "location"), mc.player.getName().asString(), convert(strings));
                                        ItemUtil.giveCreativeItem(stack);
                                        CodeUtilities.chat("Image loaded! Change the first Set Variable to the location!", ChatType.SUCCESS);
                                    } else {
                                        CodeUtilities.chat("That image doesn't exist.", ChatType.FAIL);
                                    }
                                    return 1;
                                }))));
    }

    private String convert(String[] layers) {
        StringBuilder code = new StringBuilder();
        StringBuilder currentBlock = new StringBuilder();

        code.append(String.format("{\"id\":\"block\",\"block\":\"func\",\"args\":{\"items\":[{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"False\",\"tag\":\"Is Hidden\",\"action\":\"dynamic\",\"block\":\"func\"}},\"slot\":26}]},\"data\":\"%s\"}", "Custom Image"));

        int slot = 1;
        code.append(", {\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"lines\",\"scope\":\"local\"}},\"slot\":0}]},\"action\":\"CreateList\"}");
        for (String s : layers) {
            if (slot == 26) {
                code.append(String.format(", {\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"lines\",\"scope\":\"local\"}},\"slot\":0}%s]},\"action\":\"AppendValue\"}", currentBlock.toString()));
                currentBlock.delete(0, currentBlock.length());
                slot = 1;
            }

            currentBlock.append(String.format(", {\"item\":{\"id\":\"txt\",\"data\":{\"name\":\"%s\"}},\"slot\":%d}", s, slot));
            slot++;
        }


        code.append(String.format(", {\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"lines\",\"scope\":\"local\"}},\"slot\":0}%s]},\"action\":\"AppendValue\"}", currentBlock.toString()));

        return "{\"blocks\": [" + code + ", {\"id\":\"block\",\"block\":\"func\",\"args\":{\"items\":[{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"False\",\"tag\":\"Is Hidden\",\"action\":\"dynamic\",\"block\":\"func\"}},\"slot\":26}]},\"data\":\"spawn\"},{\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"location\",\"scope\":\"local\"}},\"slot\":0}]},\"action\":\"=\"},{\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"loc\",\"scope\":\"local\"}},\"slot\":0},{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"location\",\"scope\":\"local\"}},\"slot\":1},{\"item\":{\"id\":\"num\",\"data\":{\"name\":\"20\"}},\"slot\":2},{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"Y\",\"tag\":\"Coordinate\",\"action\":\"ShiftAxis\",\"block\":\"set_var\"}},\"slot\":26}]},\"action\":\"ShiftAxis\"},{\"id\":\"block\",\"block\":\"repeat\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"line\",\"scope\":\"local\"}},\"slot\":0},{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"lines\",\"scope\":\"local\"}},\"slot\":1},{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"False (Use Copy of List)\",\"tag\":\"Allow List Changes\",\"action\":\"ForEach\",\"block\":\"repeat\"}},\"slot\":26}]},\"action\":\"ForEach\"},{\"id\":\"bracket\",\"direct\":\"open\",\"type\":\"repeat\"},{\"id\":\"block\",\"block\":\"game_action\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"loc\",\"scope\":\"local\"}},\"slot\":0},{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"line\",\"scope\":\"local\"}},\"slot\":1},{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"Invisible (No hitbox)\",\"tag\":\"Visibility\",\"action\":\"SpawnArmorStand\",\"block\":\"game_action\"}},\"slot\":26}]},\"action\":\"SpawnArmorStand\"},{\"id\":\"block\",\"block\":\"set_var\",\"args\":{\"items\":[{\"item\":{\"id\":\"var\",\"data\":{\"name\":\"loc\",\"scope\":\"local\"}},\"slot\":0},{\"item\":{\"id\":\"num\",\"data\":{\"name\":\"-0.21\"}},\"slot\":1},{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"Y\",\"tag\":\"Coordinate\",\"action\":\"ShiftAxis\",\"block\":\"set_var\"}},\"slot\":26}]},\"action\":\"ShiftAxis\"},{\"id\":\"block\",\"block\":\"control\",\"args\":{\"items\":[{\"item\":{\"id\":\"num\",\"data\":{\"name\":\"0\"}},\"slot\":0},{\"item\":{\"id\":\"bl_tag\",\"data\":{\"option\":\"Ticks\",\"tag\":\"Time Unit\",\"action\":\"Wait\",\"block\":\"control\"}},\"slot\":26}]},\"action\":\"Wait\"},{\"id\":\"bracket\",\"direct\":\"close\",\"type\":\"repeat\"}]}";
    }
}
