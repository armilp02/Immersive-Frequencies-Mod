package com.armilp.ifreq.config;

import com.google.gson.*;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WalkieConfig {

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("immersive-frequencies/walkies.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Double> defaults = new LinkedHashMap<>();
    private static JsonObject loadedConfig = null;

    public static void preRegister(String key, double defaultRange) {
        defaults.put(key, defaultRange);
    }

    public static void load() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
        } catch (IOException ignored) {}

        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                loadedConfig = JsonParser.parseReader(reader).getAsJsonObject();
            } catch (IOException e) {
                loadedConfig = null;
            }
        }
        save();
    }

    private static void save() {
        JsonObject root = new JsonObject();
        root.addProperty("_info", "More walkie talkies with different ranges will be added in the future!");
        JsonObject walkies = new JsonObject();
        for (Map.Entry<String, Double> entry : defaults.entrySet()) {
            JsonObject walkie = new JsonObject();
            walkie.addProperty("range", getRange(entry.getKey()));
            walkies.add(entry.getKey(), walkie);
        }
        root.add("walkie_talkies", walkies);
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            GSON.toJson(root, writer);
        } catch (IOException ignored) {}
        loadedConfig = root;
    }

    public static double getRange(String key) {
        try {
            if (loadedConfig != null) {
                JsonObject walkies = loadedConfig.getAsJsonObject("walkie_talkies");
                if (walkies != null && walkies.has(key)) {
                    return walkies.getAsJsonObject(key).get("range").getAsDouble();
                }
            }
        } catch (Exception ignored) {}
        return defaults.getOrDefault(key, 64.0);
    }
}