package com.mio.download;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class OptifineDownload {
    private ArrayList<String> version;
    public OptifineDownload(String mcversion){
        init(mcversion);
    }
    private void init(String mcversion){
        try {
            version=new ArrayList<>();
            JSONArray temp=new JSONArray(ForgeDownload.doGet("https://bmclapi2.bangbang93.com/optifine/" + mcversion));
            for(int i=0;i<temp.length();i++){
                version.add(mcversion+"/"+temp.getJSONObject(i).getString("type")+"/"+temp.getJSONObject(i).getString("patch"));
            }
        } catch (JSONException e) {
            System.out.println("error:"+e);
        }
    }
    public ArrayList<String> getVersion(){
        return version;
    }
    public String getDownloadLink(String v){
        return "https://download.mcbbs.net/optifine/"+v;
    }

}
