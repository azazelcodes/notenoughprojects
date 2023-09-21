package me.azazeldev.coinui.utility;

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

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.azazeldev.coinui.modules.Modules;
import net.minecraft.client.Minecraft;

public class Config {

    private static File configFile;
    private static JsonObject json;
    public final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void init() throws IOException {
        configFile = new File(Minecraft.getMinecraft().mcDataDir.getAbsolutePath() + "//coinui//config.json");
        if (configFile.exists() && !configFile.isDirectory()) {
            InputStream is = new FileInputStream(configFile);
            String jsonTxt = IOUtils.toString(is, "UTF-8");
            json = new JsonParser().parse(jsonTxt).getAsJsonObject();
        } else {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            try (Writer writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
                json = new JsonObject();
                writer.write(json.toString());
                writer.close();
            }
        }
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
            for (me.azazeldev.coinui.modules.Module m : Modules.modules) {
                if (!json.has(m.getInternalID())) {
                    json.add(m.getInternalID(), m.getDefaultValue());
                }
            }
            writer.write(json.toString());
            writer.close();
        }
    }

    public static JsonObject getConfig() {
        return json;
    }

    public static void write(String string, JsonElement jsonTree) {
        JsonObject config = getConfig();
        config.add(string, jsonTree);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), "utf-8"))) {
            writer.write(config.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}