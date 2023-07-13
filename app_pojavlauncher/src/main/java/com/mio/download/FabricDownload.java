package com.mio.download;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FabricDownload {
    private List<String> loaderVersion;

    public FabricDownload(){
        loaderVersion=new ArrayList<>();
        init();
    }
    private void init(){
        try{
            JSONArray installer=new JSONArray(ForgeDownload.doGet("https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/installer"));
            for (int i=0;i<installer.length();i++){
                JSONObject obj=installer.getJSONObject(i);
                loaderVersion.add(obj.optString("version"));
            }
        }catch (Exception e){
            Log.e("错误",e.toString());
        }
    }
    public List<String> getInstallerVersion(){
        return loaderVersion;
    }

    public String getDownloadLink(String version) {
        return "https://maven.fabricmc.net/net/fabricmc/fabric-installer/"+version+"/fabric-installer-"+version+".jar";
    }
}
