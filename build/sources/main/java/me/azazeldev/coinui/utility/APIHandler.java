package me.azazeldev.coinui.utility;

import java.util.Map;
import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class APIHandler {
    public static boolean electionWarn() {
        JsonObject mayor = Objects.requireNonNull(MainUtils.getJson("https://api.hypixel.net/resources/skyblock/election")).getAsJsonObject().get("mayor").getAsJsonObject().get("name").getAsJsonObject();
        if (mayor.getAsString() != null & mayor.getAsString() == "Derpy") {
            return false;
        } else {
            return true;
        }
    }

    public static void sendObjectEntries(JsonObject object) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            JsonObject current = (JsonObject) object.get(entry.getKey());
            System.out.println(current.toString());
        }
    }

    public static void pullBZ() {
        JsonObject apiBZObject = Objects.requireNonNull(MainUtils.getJson("https://rayorsomething.github.io/bz.json")).getAsJsonObject();
        JsonObject apiItemObject = Objects.requireNonNull(MainUtils.getJson("https://rayorsomething.github.io/items.json")).getAsJsonObject();

        for (Map.Entry<String, JsonElement> keyBZ : apiBZObject.entrySet()) {
            JsonObject bzCurrent = keyBZ.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> key : apiItemObject.entrySet()) {
                JsonObject itemCurrent = key.getValue().getAsJsonObject();
                if (itemCurrent.get("hyid").equals(bzCurrent.get("hyid"))) {
                    System.out.println("MATCHED: " + bzCurrent.get("hyid") + " for item " + itemCurrent.get("item"));
                } else {
                    System.out.println("COULDNT MATCH: " + bzCurrent.get("hyid"));
                }
            }
        }
    }

    public static void pullNPC() {
        JsonObject apiNPCObject = Objects.requireNonNull(MainUtils.getJson("https://rayorsomething.github.io/npc.json")).getAsJsonObject();
        JsonObject apiItemObject = Objects.requireNonNull(MainUtils.getJson("https://rayorsomething.github.io/items.json")).getAsJsonObject();

        for (Map.Entry<String, JsonElement> keyNPC : apiNPCObject.entrySet()) {
            JsonObject npcCurrent = keyNPC.getValue().getAsJsonObject();
            for (Map.Entry<String, JsonElement> key : apiItemObject.entrySet()) {
                JsonObject itemCurrent = key.getValue().getAsJsonObject();
                if (itemCurrent.get("hyid").equals(npcCurrent.get("hyid"))) {
                    System.out.println("MATCHED: " + npcCurrent.get("hyid") + " for item " + itemCurrent.get("item"));
                }
            }
        }
    }

    public static void pullAH() {
        JsonObject apiAHObject = Objects.requireNonNull(MainUtils.getJson("https://rayorsomething.github.io/ah.json")).getAsJsonObject();

        for (Map.Entry<String, JsonElement> keyAH : apiAHObject.entrySet()) {

        }
        sendObjectEntries(apiAHObject);
    }

    public static void getHandData() {
        ItemStack item = Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem();
        if (item != null) {
            System.out.println(item.getAttributeModifiers());
            System.out.println(item.getTooltip(Minecraft.getMinecraft().thePlayer, true));
            System.out.println(item.getMetadata());
            System.out.println(item.getTagCompound());
        }
    }
}
