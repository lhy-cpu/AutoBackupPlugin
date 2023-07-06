package work.lhye.autobackupplugin;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BackupFilesManager {
    private ConfHandler conf;
    public BackupFilesManager(ConfHandler conf){
        this.conf = conf;
    }

    public void checkZipBackup(){
        if(this.conf.maxWorldNum != 0){
            File file = new File(Paths.get(Paths.get(this.conf.saveFolderPath,"zip").toString()).toString());
            File [] files = file.listFiles();
            Arrays.sort(files, Comparator.reverseOrder());
            for(int i=this.conf.maxWorldNum;i<files.length;i++){
                files[i].delete();
            }
        }
    }

    public void checkRawBackup(){
        if(this.conf.maxWorldNum != 0){
            File file = new File(Paths.get(Paths.get(this.conf.saveFolderPath,"raw").toString()).toString());
            File [] files = file.listFiles();
            Arrays.sort(files, Comparator.reverseOrder());
            for(int i=this.conf.maxWorldNum;i<files.length;i++){
                List<String> tmp = this.getFilesAndFolders(files[i].getPath());
                for(String tf : tmp){
                    (new File(tf)).delete();
                }
            }
        }
    }

    public List<String> getFilesAndFolders(String folderPath){
        List<String> files = new ArrayList<>();
        File fFiles = new File(folderPath);
        for(File f : fFiles.listFiles()){
            if(f.isDirectory()){
                files.addAll(this.getFilesAndFolders(f.getPath()));
            }
            else{
                files.add(f.getPath());
            }
        }
        files.add(folderPath);
        return files;
    }
}
