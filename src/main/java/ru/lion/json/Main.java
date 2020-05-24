package ru.lion.json;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main extends JavaPlugin {
    private double[] memory;
    private double cpuUsed;
    private OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public void onEnable() {
        returnPlugins();
        returnMemory();
        returnProcessor();

        System.out.println("----------------------------------");

        System.out.println("Cpu load");
        System.out.println(cpuUsed);

        System.out.println("----------------------------------");
    }

    public void returnPlugins() throws NullPointerException {
        PluginManager pm = Bukkit.getPluginManager();

        Plugin[] plugins = pm.getPlugins();
        String[] names = new String[plugins.length];
        for (int i=0; i < plugins.length; ++i) {
            names[i] = plugins[i].getName();
        }
        Arrays.asList(names).forEach(System.out::println);

//        List<String> names =
//                Arrays.asList(pm.getPlugins()).stream()
//                        .map(p -> p.getName()).collect(Collectors.toList());
//        names.forEach(System.out::println);
    }

    public void returnMemory() {

        System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " MB" );
        System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MB");
        System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " MB");
        System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " MB");
        return;

    }

    public void returnProcessor() {
        DecimalFormat df = new DecimalFormat("#,##");
        cpuUsed = Double.parseDouble(df.format(osBean.getSystemCpuLoad()*100));
    }

}
