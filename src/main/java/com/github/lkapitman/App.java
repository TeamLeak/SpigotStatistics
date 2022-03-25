package com.github.lkapitman;

import com.sun.management.OperatingSystemMXBean;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class App extends JavaPlugin {

    private ServerSocket server;

    private long sec;
    private long currentSec;
    private int ticks;

    private String usedMemory;
    private String freeMemory;
    private String totalMemory;
    private String maxMemory;
    private Double processor;
    private int tps = 0;

    @SneakyThrows
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        HashMap<String, Long> memory = new HashMap<>(returnMemory());
        usedMemory = memory.get("Used").toString();
        freeMemory = memory.get("Free").toString();
        totalMemory = memory.get("Total").toString();
        maxMemory = memory.get("Max").toString();

        processor = returnProcessor();

        server = new ServerSocket(getConfig().getInt("port"));

        new Thread() {
            @SneakyThrows
            public void run() {
                listen();
            }
        }.start();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            public void run() {
                sec = (System.currentTimeMillis() / 1000);
                if (currentSec == sec) {
                    ticks++;
                } else {
                    currentSec = sec;
                    tps = (tps == 0 ? ticks : ((tps + ticks) / 2));
                    ticks = 0;
                }
            }
        }, 0, 1);

    }

    @SneakyThrows
    @Override
    public void onDisable() {
        server.close();
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void listen() throws Exception {
        while (true) {
            Socket client = server.accept();
            new Thread() {
                @SneakyThrows
                public void run() {
                    String s = tps + "";
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    SimpleDateFormat sdfDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
                    Date now = new Date();
                    out.write("HTTP/1.0 200 OK\r\n");
                    out.write("Date: " + sdfDate.format(now) + "\r\n");
                    out.write("Server: SpigotStatistics/1.0.0\r\n");
                    out.write("Content-Type: text/html\r\n");
                    out.write("Access-Control-Allow-Origin: *\r\n");
                    out.write("Content-Length: " + s.length() + "\r\n");
                    out.write("Expires: Thu, 01 Jan 1970 00:00:00 GMT\r\n");
                    out.write("Last-modified: " + sdfDate.format(now) + "\r\n");
                    out.write("\r\n");
                    out.write("<p class='plugins'>" + String.join(";", returnPlugins()) + "</p>\n" +
                            "<p class='usedM'>" + usedMemory + "</p>\n" +
                            "<p class='freeM'>" + freeMemory + "</p>\n" +
                            "<p class='totalM'>" + totalMemory + "</p>\n" +
                            "<p class='maxM'>" + maxMemory + "</p>\n" +
                            "<p class='procL'>" + processor.toString() + "</p>");
                    out.flush();
                    out.close();
                }
            }.start();
        }
    }

    public List<String> returnPlugins() {
        return Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .map(Plugin::getName)
                .collect(Collectors.toList());
    }

    public HashMap<String, Long> returnMemory() {
        HashMap<String, Long> memory = new HashMap<>();
        memory.put("Used", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
        memory.put("Free", Runtime.getRuntime().freeMemory() / (1024 * 1024));
        memory.put("Total", Runtime.getRuntime().totalMemory() / (1024 * 1024));
        memory.put("Max", Runtime.getRuntime().maxMemory() / (1024 * 1024));
        return memory;
    }

    public double returnProcessor() {
        return Double.parseDouble(new DecimalFormat("#,##").format(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getSystemCpuLoad() *100));
    }


}
