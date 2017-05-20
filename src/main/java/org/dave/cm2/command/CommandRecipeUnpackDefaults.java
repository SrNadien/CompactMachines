package org.dave.cm2.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import org.dave.cm2.CompactMachines2;
import org.dave.cm2.misc.ConfigurationHandler;
import org.dave.cm2.utility.JarExtract;

import java.io.File;

public class CommandRecipeUnpackDefaults extends CommandBaseExt {
    @Override
    public String getCommandName() {
        return "unpack-defaults";
    }

    @Override
    public boolean isAllowed(EntityPlayer player, boolean creative, boolean isOp) {
        return isOp;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        int extracted = JarExtract.copy("assets/cm2/config/recipes", ConfigurationHandler.recipeDirectory);
        sender.addChatMessage(new TextComponentString("Extracted " + extracted + " recipes to the config folder"));
    }
}
