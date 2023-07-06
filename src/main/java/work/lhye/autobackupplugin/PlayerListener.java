package work.lhye.autobackupplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerListener implements Listener {
    private Plugin plugin;
    private ConfHandler conf;
    private String rootPath;
    private int playerNum = 0;
    private Timer timer;

    public PlayerListener(Plugin plugin,ConfHandler conf,Timer timer,String rootPath){
        this.plugin = plugin;
        this.conf = conf;
        this.rootPath = rootPath;
        this.timer = timer;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (playerNum == 0) {
            this.timer.startInterval();
            this.plugin.getLogger().info("Backup every " + this.conf.saveIntervalTicks + " ticks");
        }
        playerNum++;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerNum--;
        if (playerNum == 0) {
            this.timer.cancel();
            this.timer.run();
            this.timer = new Timer(this.plugin,this.conf,this.rootPath);
            plugin.getLogger().info("Backup paused!");
        }
    }


}
