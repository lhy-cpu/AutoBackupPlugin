package work.lhye.autobackupplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfHandler {
    public String saveFolderPath = null;
    public int saveIntervalTicks = 0;
    public int maxWorldNum = 0;
    public boolean enableZip = false;
    public String confFilePath = "";
    public ConfHandler(){
    }

    @Override
    public String toString(){
        Map<String,Object> conf = new HashMap<String,Object>();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        conf.put("saveFolderPath", this.saveFolderPath);
        conf.put("saveIntervalTicks",this.saveIntervalTicks);
        conf.put("maxWorldNum",this.maxWorldNum);
        conf.put("enableZip",this.enableZip);
        return gson.toJson(conf);
    }

    public void setConfFilePath(String confFilePath) {
        this.confFilePath = confFilePath;
    }

    public void saveConfig(){
        try {
            FileWriter fw = new FileWriter(new File(this.confFilePath));
            fw.write(this.toString());
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void loadConfig(String savePath){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(savePath)));
            String tmp = "";
            String all = "";
            while((tmp=br.readLine())!=null){
                all+=tmp;
            }
            br.close();
            Gson gson = new Gson();
            Map<String,Object> conf = gson.fromJson(all,new TypeToken<Map<String,Object>>(){}.getType());
            this.loadConfig(conf);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig(@NotNull Map<String,Object> confMap){
        this.saveFolderPath = confMap.get("saveFolderPath").toString();
        this.saveIntervalTicks = ((Double)confMap.get("saveIntervalTicks")).intValue();
        this.maxWorldNum = ((Double)confMap.get("maxWorldNum")).intValue();
        this.enableZip = ((Boolean)confMap.get("enableZip")).booleanValue();
    }

    public void loadDefaultConfig(String rootPath){
        Map<String,Object> defConf = new HashMap<String,Object>();
        defConf.put("saveFolderPath", Paths.get(rootPath, "worldBackup").toString());
        defConf.put("saveIntervalTicks",12000);
        defConf.put("maxWorldNum",0);
        defConf.put("enableZip",false);
        this.loadConfig(defConf);
    }
}
