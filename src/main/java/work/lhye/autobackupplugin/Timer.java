package work.lhye.autobackupplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss.SSS");
        String timeStr = sdf.format(new Date()).toString();
        if(this.conf.enableZip){
            String tmpF = Paths.get(this.conf.saveFolderPath,"zip","tmp").toString();
            CopyHandler.copyDir(Paths.get(this.rootPath,"world").toString(),tmpF);
            CopyHandler.compressFilesToZip(tmpF,Paths.get(this.conf.saveFolderPath,"zip").toString(),timeStr+"_world.zip");
            CopyHandler.deleteDir(tmpF);
            this.backupFilesManager.checkZipBackup();
        }
        else{
            CopyHandler.copyDir(Paths.get(this.rootPath,"world").toString(),Paths.get(this.conf.saveFolderPath,"raw",timeStr).toString());
            this.backupFilesManager.checkRawBackup();
        }

    }

    public void startInterval(){
        this.runTaskTimer(this.plugin,this.conf.saveIntervalTicks,this.conf.saveIntervalTicks);
    }
}
