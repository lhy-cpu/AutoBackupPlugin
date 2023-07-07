package work.lhye.autobackupplugin;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CopyHandler {

    public static void compressFilesToZip(String originFolderPath,String zipToFolderPath,String zipName) {
        File tz = new File(zipToFolderPath);
        if(!tz.exists()){
            tz.mkdirs();
        }
        List<String> files = getFiles(originFolderPath);
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(Paths.get(zipToFolderPath,zipName).toString()));
            for(String tFile : files){
                if(!tFile.endsWith("session.lock")){
                    String substring = tFile.substring(originFolderPath.length()+1, tFile.length());
                    int len = -1;
                    byte[] buf = new byte[1024];
                    InputStream input = new BufferedInputStream(new FileInputStream(tFile));
                    zos.putNextEntry(new ZipEntry(substring));
                    while ((len = input.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    input.close();
                }
            }
            zos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyDir(String originFolderPath,String copyToFolderPath){
        List<String> files = getFiles(originFolderPath);
        for(String tFile : files){
            if(!tFile.endsWith("session.lock")) {
                copyFile(tFile,Paths.get(copyToFolderPath,tFile.substring(originFolderPath.length(), tFile.length())).toString());
            }
        }
    }

    public static List<String> getFiles(String folderPath){
        List<String> files = new ArrayList<>();
        File fFiles = new File(folderPath);
        for(File f : fFiles.listFiles()){
            if(f.isDirectory()){
                files.addAll(getFiles(f.getPath()));
            }
            else{
                files.add(f.getPath());
            }
        }
        return files;
    }

    public static List<String> getFilesAndFolders(String folderPath){
        List<String> files = new ArrayList<>();
        File fFiles = new File(folderPath);
        for(File f : fFiles.listFiles()){
            if(f.isDirectory()){
                files.addAll(getFilesAndFolders(f.getPath()));
            }
            else{
                files.add(f.getPath());
            }
        }
        files.add(folderPath);
        return files;
    }

    public static void copyFile(String originFilePath,String copyToFilePath){
        File tmpF = new File(copyToFilePath);
        File tmpD = tmpF.getParentFile();
        if(!tmpD.exists()){
            tmpD.mkdirs();
        }
        try {
            FileChannel of = new FileInputStream(new File(originFilePath)).getChannel();
            FileChannel cf = new FileOutputStream(tmpF).getChannel();
            of.transferTo(0, of.size(), cf);
            of.close();
            cf.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void deleteDir(String folderPath){
        List<String> files = getFilesAndFolders(folderPath);
        for(String f : files){
            (new File(f)).delete();
        }
    }
}
