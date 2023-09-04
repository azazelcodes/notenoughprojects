package me.azazeldev.coinui.modules;

import java.util.ArrayList;

import me.azazeldev.coinui.utility.Config;

public class Modules {

    public static ArrayList<Module> modules = new ArrayList<>();

    public static void init() {
        modules.add(new Module("Toggle", "I love booleans! This is a placeholder description to test the new GUI System.", Category.GENERAL, Type.TOGGLE, "flipper",
                Config.gson.toJsonTree(false)));
        modules.add(new Module("String", "I love strings! This is a placeholder description to test the new GUI System.", Category.PREMIUM, Type.TEXT, "webhook",
                Config.gson.toJsonTree("")));
    }
}
