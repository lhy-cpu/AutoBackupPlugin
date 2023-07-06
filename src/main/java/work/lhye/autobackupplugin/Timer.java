package work.lhye.autobackupplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Paths;

public class Timer extends BukkitRunnable {
    private Plugin plugin;
    private ConfHandler conf;
    private String rootPath;
    private BackupFilesManager backupFilesManager;
    public Timer(Plugin plugin,ConfHandler conf,String rootPath){
        this.plugin = plugin;
        this.conf = conf;
        this.rootPath = rootPath;
        this.backupFilesManager = new BackupFilesManager(this.conf);
    }
    @Override
    public void run() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
        File tf = new File(this.conf.saveFolderPath);
        tf.mkdirs();
        if(!tf.canWrite()) {
            this.conf.saveFolderPath = Paths.get(this.rootPath, "worldBackup").toString();
            this.conf.saveConfig();
        }
        if(this.conf.enableZip){
            CopyHandler.compressFilesToZip(Paths.get(this.rootPath,"world").toString(),Paths.get(this.conf.saveFolderPath,"zip").toString());
            this.backupFilesManager.checkZipBackup();
        }
        else{
            CopyHandler.copyDir(Paths.get(this.rootPath,"world").toString(),Paths.get(this.conf.saveFolderPath,"raw").toString());
            this.backupFilesManager.checkRawBackup();
        }

    }

    public void startInterval(){
        this.runTaskTimer(this.plugin,this.conf.saveIntervalTicks,this.conf.saveIntervalTicks);
    }
}
