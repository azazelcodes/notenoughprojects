package me.azazeldev.coinui.modules;

import com.google.gson.JsonElement;

public class Module {


    public String name;
    public String description;
    public Category category;
    public Type type;
    public String internalID;
    public JsonElement defaultValue;

    public Module(String name, String description, Category category, Type type, String internalID, JsonElement defaultValue) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.type = type;
        this.internalID = internalID;
        this.defaultValue = defaultValue;
    }

    public JsonElement getDefaultValue() {
        return defaultValue;
    }

    public Type getType() {
        return type;
    }

    public String getInternalID() {
        return internalID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }
}
