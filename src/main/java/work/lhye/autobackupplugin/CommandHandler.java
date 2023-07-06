package work.lhye.autobackupplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CommandHandler implements CommandExecutor {
    private Plugin plugin;
    private ConfHandler conf;
    private Timer timer;
    private String rootPath;
    public CommandHandler(Plugin plugin,ConfHandler conf,Timer timer,String rootPath){
        this.plugin = plugin;
        this.conf = conf;
        this.timer = timer;
        this.rootPath = rootPath;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if("backup".equals(command.getName()) && commandSender.isOp()){
            commandProcess(commandSender,command,strings);
        }else {
            commandSender.sendMessage(ChatColor.DARK_RED +"You have no permission to use this command!");

        }
        return true;

    }

    private void commandProcess(CommandSender commandSender,Command command,String[] strings){
        if(strings.length<1){
            commandSender.sendMessage(ChatColor.YELLOW +"You have used wrong command. Correct one is as follows:");
            commandSender.sendMessage(ChatColor.YELLOW +command.getUsage());
        }else{
            switch (strings[0]){
                case "saveNow":
                    this.timer.run();
                    break;
                case "reloadConfig":
                    this.conf.loadConfig(this.conf.confFilePath);
                    this.timer.cancel();
                    this.timer = new Timer(this.plugin,this.conf,this.rootPath);
                    this.timer.startInterval();
                    commandSender.sendMessage(ChatColor.GREEN + "Successfully reloaded configurations.");
                    break;
                case "set":
                    if(strings.length<2){
                        commandSender.sendMessage(ChatColor.GOLD +"You have used wrong command. Correct one is as follows:");
                        commandSender.sendMessage(ChatColor.WHITE+"/backup set "+ChatColor.GREEN + "{ maxWorldNum | saveIntervalTicks | enableZip }");
                    }else{
                        switch (strings[1]){
                            case "maxWorldNum":
                                if(strings.length<3){
                                    commandSender.sendMessage(ChatColor.GOLD +"You have used wrong command. Correct one is as follows:");
                                    commandSender.sendMessage(ChatColor.WHITE+"/backup set maxWorldNum "+ChatColor.GREEN + "<int> (0 means no limit)");
                                }else {
                                    try{
                                        int t = Integer.parseInt(strings[2]);
                                        if(t<0){
                                            commandSender.sendMessage(ChatColor.WHITE+"/backup set maxWorldNum "+ChatColor.RED +strings[2]+" <--It must be more than -1.");
                                        }else {
                                            this.conf.maxWorldNum = t;
                                            this.conf.saveConfig();
                                            commandSender.sendMessage(ChatColor.WHITE + "Successfully changed configuration "+ChatColor.GREEN+"maxWorldNum "+ChatColor.WHITE + "to "+ChatColor.GREEN+t+ChatColor.WHITE+".");
                                        }

                                    }catch (NumberFormatException e){
                                        commandSender.sendMessage(ChatColor.WHITE+"/backup set maxWorldNum "+ChatColor.RED +strings[2]+" <--This is wrong.");
                                        commandSender.sendMessage(ChatColor.GOLD +"Correct one is as follows:");
                                        commandSender.sendMessage(ChatColor.WHITE+"/backup set maxWorldNum "+ChatColor.GREEN + "<int> (0 means no limit)");
                                    }
                                }
                                break;
                            case "saveIntervalTicks":
                                if(strings.length<3){
                                    commandSender.sendMessage(ChatColor.GOLD +"You have used wrong command. Correct one is as follows:");
                                    commandSender.sendMessage(ChatColor.WHITE+"/backup set saveIntervalTicks "+ChatColor.GREEN + "<int>");
                                }else{
                                    try{
                                        int t = Integer.parseInt(strings[2]);
                                        if(t<0){
                                            commandSender.sendMessage(ChatColor.WHITE+"/backup set saveIntervalTicks "+ChatColor.RED +strings[2]+" <--It must be more than -1.");
                                        }else {
                                            this.conf.saveIntervalTicks = t;
                                            this.conf.saveConfig();
                                            this.timer.cancel();
                                            this.timer = new Timer(this.plugin,this.conf,this.rootPath);
                                            this.timer.startInterval();
                                            commandSender.sendMessage(ChatColor.WHITE + "Successfully changed configuration "+ChatColor.GREEN+"saveIntervalTicks "+ChatColor.WHITE + "to "+ChatColor.GREEN+t+ChatColor.WHITE+".");
                                        }

                                    }catch (NumberFormatException e){
                                        commandSender.sendMessage(ChatColor.WHITE+"/backup set saveIntervalTicks "+ChatColor.RED +strings[2]+" <--This is wrong.");
                                        commandSender.sendMessage(ChatColor.GOLD +"Correct one is as follows:");
                                        commandSender.sendMessage(ChatColor.WHITE+"/backup set saveIntervalTicks "+ChatColor.GREEN + "<int>");
                                    }
                                }
                                break;
                            case "enableZip":
                                if(strings.length<3){
                                    commandSender.sendMessage(ChatColor.GOLD +"You have used wrong command. Correct one is as follows:");
                                    commandSender.sendMessage(ChatColor.WHITE+"/backup set enableZip "+ChatColor.GREEN + "<boolean>(Only 'true' or 'false')");
                                }else if("true".equals(strings[2]) || "false".equals(strings[2])){
                                    if("true".equals(strings[2])){
                                        this.conf.enableZip = true;
                                    }else {
                                        this.conf.enableZip = false;
                                    }
                                    this.conf.saveConfig();
                                    commandSender.sendMessage(ChatColor.WHITE + "Successfully changed configuration "+ChatColor.GREEN+"enableZip "+ChatColor.WHITE + "to "+ChatColor.GREEN+this.conf.enableZip+ChatColor.WHITE+".");
                                }else{
                                    commandSender.sendMessage(ChatColor.WHITE+"/backup set enableZip "+ChatColor.RED +strings[2]+" <--This is wrong.");
                                    commandSender.sendMessage(ChatColor.GOLD +"Correct one is as follows:");
                                    commandSender.sendMessage(ChatColor.WHITE+"/backup set enableZip "+ChatColor.GREEN + "<boolean>(Only 'true' or 'false')");
                                }
                                break;
                            default:
                                commandSender.sendMessage(ChatColor.WHITE+"/backup set "+ChatColor.RED +strings[1]+" <--This is wrong.");
                                commandSender.sendMessage(ChatColor.GOLD +"Correct one is as follows:");
                                commandSender.sendMessage(ChatColor.WHITE+"/backup set "+ChatColor.GREEN + "{ maxWorldNum | saveIntervalTicks | enableZip }");

                        }
                    }
                    break;
                default:
                    commandSender.sendMessage(ChatColor.WHITE+"/backup "+ChatColor.RED +strings[0]+" <--This is wrong.");
                    commandSender.sendMessage(ChatColor.GOLD +"Correct one is as follows:");
                    commandSender.sendMessage(ChatColor.WHITE+"/backup "+ChatColor.GREEN + "{ saveNow | reloadConfig | set}");
            }
        }
    }
}