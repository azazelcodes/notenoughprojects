package me.azazeldev.coinui.commands.sub;

import me.azazeldev.coinui.utility.APIHandler;
import net.minecraft.command.ICommandSender;

//Taken from Mindlessly
public class LBinHand implements Subcommand {
    @Override
    public String getCommandName() {
        return "handbin";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public String getCommandUsage() {
        return "";
    }

    @Override
    public String getCommandDescription() {
        return "Sends the lowest bin of the item in your hand";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] args) {
        APIHandler.getHandData();
        return true;
    }
}