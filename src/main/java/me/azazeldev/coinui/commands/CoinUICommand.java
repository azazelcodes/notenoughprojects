package me.azazeldev.coinui.commands;

import me.azazeldev.coinui.Reference;
import me.azazeldev.coinui.commands.sub.Subcommand;
import me.azazeldev.coinui.gui.GUI;
import me.azazeldev.coinui.utility.DelayedTask;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CoinUICommand extends CommandBase {
    private final Subcommand[] subcommands;

    public CoinUICommand(Subcommand[] subcommands) {
        this.subcommands = subcommands;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("cui", "coinu");
    }

    @Override
    public String getCommandName() {
        return "coinui";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/coinui <subcommand> <arguments>";
    }

    public void sendHelp(ICommandSender sender) {
        List<String> commandUsages = new LinkedList<>();
        for (Subcommand subcommand : this.subcommands) {
            if (!subcommand.isHidden()) {
                commandUsages.add(EnumChatFormatting.GRAY + "/coinui " + subcommand.getCommandName() + " "
                        + subcommand.getCommandUsage() + EnumChatFormatting.DARK_GRAY + " - " + EnumChatFormatting.GOLD + subcommand.getCommandDescription());
            }
        }
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD + "CoinUI " + EnumChatFormatting.GREEN
                + Reference.VERSION + "\n" + String.join("\n", commandUsages)));
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            new DelayedTask((() -> Minecraft.getMinecraft().displayGuiScreen(new GUI())), 6);
            return;
        }
        for (Subcommand subcommand : this.subcommands) {
            if (Objects.equals(args[0], subcommand.getCommandName())) {
                if (!subcommand.processCommand(sender, Arrays.copyOfRange(args, 1, args.length))) {
                    // processCommand returned false
                }
                return;
            }
        }
        sendHelp(sender);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> possibilities = new LinkedList<>();
        for (Subcommand subcommand : subcommands) {
            possibilities.add(subcommand.getCommandName());
        }
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, possibilities);
        }
        return null;
    }
}