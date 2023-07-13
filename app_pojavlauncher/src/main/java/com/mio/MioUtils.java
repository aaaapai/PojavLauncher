package com.mio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.mio.download.DownloadCallback;
import com.mio.modpack.CurseforgeModpackManifest;
import com.mio.modpack.MioDownloadModPackTask;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.Tools;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class MioUtils {
    public static String getModInfo(String path){
        try{
            ZipFile zipFile = new ZipFile(path);
            ZipArchiveEntry entry = zipFile.getEntry("mcmod.info");
            try (InputStream inputStream = zipFile.getInputStream(entry)) {
                try (BufferedReader br =new BufferedReader(new InputStreamReader(inputStream))) {
                    String line="";
                    String str="";
                    while ((line =br.readLine()) !=null) {
                        str+=line;
                    }
                    JSONObject jsonobj=new JSONObject(new JSONArray(str).get(0).toString());
                    return jsonobj.getString("name");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static boolean unZip(File file, String descDir) {
        try {
            if (!descDir.endsWith("/")){
                descDir+="/";
            }
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            ZipFile zipFile = new ZipFile(file);
            byte[] buffer = new byte[2048];
            ZipArchiveEntry entry;
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                File outputFile = new File(descDir + entry.getName());
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                try(InputStream inputStream = zipFile.getInputStream(entry)){
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        int len;
                        while ((len = inputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    
    public static void installModPack(Activity activity,String path){
        ProgressDialog dialog=new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        PojavApplication.sExecutorService.execute(() -> {
            try {
                final File modPackFile = new File(path);
                if(!MioUtils.unZip(modPackFile,modPackFile.getAbsolutePath().replace(".zip","").replace(" ",""))){
                    activity.runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(activity,"解压失败",Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                File manifest=new File(modPackFile.getAbsolutePath().replace(".zip","").replace(" ",""),"manifest.json");
                if (!manifest.exists()){
                    activity.runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(activity,"未找到manifest.json文件",Toast.LENGTH_LONG).show();
                    });
                    return;
                }
                CurseforgeModpackManifest modpackManifest=new Gson().fromJson(Tools.read(manifest.getAbsolutePath()),CurseforgeModpackManifest.class);
                List<CurseforgeModpackManifest.Files> filesList=modpackManifest.getFiles();
                activity.runOnUiThread(()->dialog.dismiss());
                MioDownloadModPackTask mioDownloadModPackTask =new MioDownloadModPackTask(activity, new DownloadCallback() {
                    @Override
                    public void onFinished(Map<String, String> failedFile) {
                        CurseforgeModpackManifest.Minecraft minecraft = modpackManifest.getMinecraft();
                        AlertDialog alertDialog=new AlertDialog.Builder(activity)
                                .setTitle("提示")
                                .setMessage("整合包相关文件已下载完成当前整合包需要MC版本:"+minecraft.getVersion()+"  Mod加载器版本:"+minecraft.getModLoaders().get(0).getId()+"。整合包文件下载位置为"+Tools.DIR_GAME_HOME+"/整合包/整合包名，请手动下载目标MC版本以及Mod加载器再将文件放到正确位置。")
                                .setPositiveButton("确定",(d,i)->{
                                })
                                .create();
                        alertDialog.show();

                    }

                    @Override
                    public void onCancelled() {
                        Toast.makeText(activity,"已取消下载",Toast.LENGTH_LONG).show();
                    }
                });
                mioDownloadModPackTask.setModsPath(manifest.getParent()+"/mods/");
                mioDownloadModPackTask.execute(filesList);

            }catch(IOException e) {
                activity.runOnUiThread(dialog::dismiss);
                Tools.showError(activity, e);
            }
        });
    }
    public static boolean isHigher(String version1, String version2) {
        if (version1.equals(version2)) {
            return false;
        }
        if(version1.contains("-")){
            version1=version1.substring(0,version1.indexOf("-"));
        }
        if (version2.contains("-")){
            version2=version2.substring(0,version2.indexOf("-"));
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen && (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return true;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return false;
                }
            }
            return false;
        } else {
            return diff > 0 ? true : false;
        }
    }
}
