package me.azazeldev.coinui.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;

public class Config {
    private static File configFile;

    private static JsonObject json;

    public static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static void init() throws IOException {
        configFile = new File((Minecraft.getMinecraft()).mcDataDir.getAbsolutePath() + "//coinui//config.json");
        if (configFile.exists() && !configFile.isDirectory()) {
            InputStream is = new FileInputStream(configFile);
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            json = (new JsonParser()).parse(jsonTxt).getAsJsonObject();
        } else {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
                json = new JsonObject();
                writer.write(json.toString());
                writer.close();
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
            if (!json.has("flipper"))
                json.add("flipper", gson.toJsonTree(false));
            if (!json.has("sounds"))
                json.add("sounds", gson.toJsonTree(true));
            if (!json.has("oneclickbuy"))
                json.add("oneclickbuy", gson.toJsonTree(false));
            if (!json.has("pageflipper"))
                json.add("pageflipper", gson.toJsonTree(false));
            if (!json.has("autobuy"))
                json.add("autobuy", gson.toJsonTree(false));
            if (!json.has("fullafk"))
                json.add("fullafk", gson.toJsonTree(false));
            if (!json.has("hpm"))
                json.add("hpm", gson.toJsonTree(false));
            if (!json.has("minprofit"))
                json.add("minprofit", gson.toJsonTree(250000));
            if (!json.has("mindemand"))
                json.add("mindemand", gson.toJsonTree(1));
            if (!json.has("minpercent"))
                json.add("minpercent", gson.toJsonTree(5));
            writer.write(json.toString());
            writer.close();
        }
    }

    public static JsonObject getConfig() {
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String jsonTxt = null;
        try {
            jsonTxt = IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (new JsonParser()).parse(jsonTxt).getAsJsonObject();
    }

    public static void write(String string, JsonElement jsonTree) {
        JsonObject config = getConfig();
        config.add(string, jsonTree);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
            writer.write(config.toString());
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
