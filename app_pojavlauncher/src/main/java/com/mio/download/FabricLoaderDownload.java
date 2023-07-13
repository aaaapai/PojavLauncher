package com.mio.download;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FabricLoaderDownload {
    private List<String> loaderVersion;

    public FabricLoaderDownload(){
        loaderVersion=new ArrayList<>();
    }
    public void init() throws Exception {
        JSONArray loaders=new JSONArray(ForgeDownload.doGet("https://bmclapi2.bangbang93.com/fabric-meta/v2/versions/loader"));
        for (int i=0;i<loaders.length();i++){
            JSONObject obj=loaders.getJSONObject(i);
            if (obj.optString("separator").equals(".")){
                loaderVersion.add(obj.optString("version"));
            }
        }
    }
    public List<String> getLoaderVersion(){
        return loaderVersion;
    }
}
