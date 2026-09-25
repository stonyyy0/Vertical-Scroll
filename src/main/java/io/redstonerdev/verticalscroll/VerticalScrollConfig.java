package io.redstonerdev.verticalscroll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VerticalScrollConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("verticalscroll.json");

    public boolean gapEnabled = false;
    public double scrollSensitivity = 1.0;

    private static VerticalScrollConfig INSTANCE;

    public static VerticalScrollConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    public static void load() {
        if (Files.exists(CONFIG_FILE)) {
            try {
                INSTANCE = GSON.fromJson(Files.readString(CONFIG_FILE), VerticalScrollConfig.class);
            } catch (IOException e) {
                INSTANCE = new VerticalScrollConfig();
            }
        } else {
            INSTANCE = new VerticalScrollConfig();
        }
    }

    public static void save() {
        try {
            Files.writeString(CONFIG_FILE, GSON.toJson(INSTANCE));
        } catch (IOException ignored) {
        }
    }
}
