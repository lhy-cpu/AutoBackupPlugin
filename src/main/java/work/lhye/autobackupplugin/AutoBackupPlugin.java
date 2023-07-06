package work.lhye.autobackupplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class AutoBackupPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        String rootPath = System.getProperty("user.dir");
        File confF = new File(Paths.get(rootPath, "plugins", "AutoBackupPlugin","conf.json").toString());
        ConfHandler conf = new ConfHandler();
        conf.setConfFilePath(confF.getPath());
        if(!confF.exists()){
            conf.loadDefaultConfig(rootPath);
            (new File(Paths.get(rootPath, "plugins", "AutoBackupPlugin").toString())).mkdirs();
            conf.saveConfig();
        }
        else{
            conf.loadConfig(confF.getPath());
        }
        Timer timer = new Timer(this,conf,rootPath);
        getServer().getPluginManager().registerEvents(new PlayerListener(this,conf,timer,rootPath), this);
        Bukkit.getPluginCommand("backup").setExecutor(new CommandHandler(this,conf,timer,rootPath));

        this.getLogger().info("AutoBackupPlugin load succeed");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
