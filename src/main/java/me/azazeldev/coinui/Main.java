package me.azazeldev.coinui;

import me.azazeldev.coinui.utility.Config;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import me.azazeldev.coinui.commands.CoinUICommand;
import me.azazeldev.coinui.commands.sub.Subcommand;
import me.azazeldev.coinui.commands.sub.Help;

import java.io.IOException;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {
    public static String uuid;
    public static Main instance;
    public static CoinUICommand commandManager = new CoinUICommand(new Subcommand[] {
            new Help()
    });
    public static boolean justClicked;
    public static int page = 1;

    @EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        ProgressManager.ProgressBar progressBar = ProgressManager.push("CoinUI", 1);
        progressBar.step("Registering events and commands");
        Config.init();
        instance = this;
        System.out.println(instance);
        ClientCommandHandler.instance.registerCommand(commandManager);
        uuid = Minecraft.getMinecraft().getSession().getPlayerID();
        Reference.logger.info("Registered events and commands!");
        ProgressManager.pop(progressBar);
    }
}