package ru.lion.json;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class Main extends JavaPlugin {

    private String[] plugins;
    private long[] memory;
    private long memoryUsed;
    private long cpuUsed;
    public void onEnable() {
        for (int i = 0; i <= plugins.length; i++) {
            Bukkit.broadcastMessage(plugins[i]);
        }
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

    public void returnMemory() {
        memory[0] = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        memory[1] = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        memory[2] = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        return;
    }

    public void returnProcessor() {

    }

}
