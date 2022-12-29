package ch.mixin.islandgenerator.main;

import ch.mixin.islandgenerator.command.CommandInitializer;
import ch.mixin.islandgenerator.eventListener.EventListener;
import ch.mixin.islandgenerator.islandGeneration.IslandManager;
import ch.mixin.islandgenerator.loot.LootManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public final class IslandGeneratorPlugin extends JavaPlugin {
    public static IslandGeneratorPlugin PLUGIN;
    public static String PLUGIN_NAME;
    public static String ROOT_DIRECTORY_PATH;


    static {
        String urlPath = IslandGeneratorPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = null;

        try {
            decodedPath = URLDecoder.decode(urlPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ROOT_DIRECTORY_PATH = decodedPath.substring(0, decodedPath.lastIndexOf("/"));
    }

    public boolean PluginFlawless;
    private IslandManager islandManager;
    private LootManager lootManager;
    private Random random;

    public static ArrayList<String> readFile(File file) {
        ArrayList<String> text = new ArrayList<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                text.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return text;
    }

    public static void writeFile(File file, String text) {
        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        PLUGIN = this;
        PLUGIN_NAME = getDescription().getName();
        System.out.println(PLUGIN_NAME + " enabled");
        setup();
        load();
        start();
    }

    @Override
    public void onDisable() {
        System.out.println(PLUGIN_NAME + " disabled");
    }

    private void setup() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        random = new Random();
        CommandInitializer.setupCommands(this);
        islandManager = new IslandManager(this);
        lootManager = new LootManager(this);
        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    public void reload() {
        load();
    }

    private void load() {
        super.reloadConfig();
    }

    private void start() {
        PluginFlawless = true;
    }

    public IslandManager getIslandManager() {
        return islandManager;
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public Random getRandom() {
        return random;
    }
}
