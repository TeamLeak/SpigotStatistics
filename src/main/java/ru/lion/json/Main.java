package ru.lion.json;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JavaPlugin {

    private String[] plugins;


    public void onEnable() {
    }

    public void returnPlugins() {
        int i = 0;
        for(Plugin plugin : Bukkit.getPluginManager().getPlugins()) {

            //System.out.println(plugin.getName());
            plugins[i] = plugin.getName() + "\n";
            i++;
        }
        return;
    }

}
