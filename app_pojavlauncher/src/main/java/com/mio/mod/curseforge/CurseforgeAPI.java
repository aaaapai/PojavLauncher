package com.mio.mod.curseforge;

import android.util.Log;

import com.google.gson.Gson;

import net.kdt.pojavlaunch.Tools;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class CurseforgeAPI {
    private static final String PREFIX = "https://api.curseforge.com";
    private static final String apiKey = "$2a$10$qqJ3zZFG5CDsVHk8eV5ft.2ywg2edBtHwS3gzFnw7CDe3X2cKpWZG";

    public CurseforgeAPI() {

    }

    public String getCategory() {
        return httpGet(PREFIX + "/v1/categories?gameId=432");
    }

    public List<CurseAddon.Data> searchMod(String name, boolean hasName) {
        String json = httpGet(PREFIX + "/v1/mods/search?gameId=432&classId=6&sortField=2&sortOrder=desc" + (hasName ? ("&searchFilter=" + name) : ""));
        if (json == null) {
            return null;
        }
        CurseAddon addon = new Gson().fromJson(json, CurseAddon.class);
        return addon.getData();
    }

    public List<CurseAddon.Data> searchModPack(String name, boolean hasName) {
        String json = httpGet(PREFIX + "/v1/mods/search?gameId=432&classId=4471&sortField=2&sortOrder=desc" + (hasName ? ("&searchFilter=" + name) : ""));
        if (json == null) {
            return null;
        }
        CurseAddon addon = new Gson().fromJson(json, CurseAddon.class);
        return addon.getData();
    }

    public CurseAddon.Data getModByID(int id) {
        try {
            String json = httpGet(PREFIX + "/v1/mods/" + id);
            JSONObject jsonObject = new JSONObject(json);
            CurseAddon.Data data = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), CurseAddon.Data.class);
            return data;
        } catch (Exception e) {

        }
        return null;
    }

    public List<CurseModFiles.Data> getModFiles(int id){
        String json = httpGet(PREFIX + "/v1/mods/" + id +"/files");
        if (json == null) {
            return null;
        }
        CurseModFiles addon = new Gson().fromJson(json, CurseModFiles.class);
        return addon.getData();
    }

    public void getFeaturedMods() {
    }

    public String getDownloadUrl(int modid,int fileid){
        try {
            String json = httpGet(PREFIX + "/v1/mods/"+modid+"/files/"+fileid);
            if (json==null){
                return null;
            }
            JSONObject jsonObject = new JSONObject(json);
            CurseModFiles.Data data = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), CurseModFiles.Data.class);
            String url = data.getDownloadUrl();
            if (Objects.isNull(url)){
                String id=String.valueOf(data.getId());
                url="https://media.forgecdn.net/files/"+id.substring(0,4)+"/"+id.substring(4)+"/"+data.getFileName();
            }
            return url;
        } catch (Exception e) {
            Log.e("测试","getDownloadUrl:"+e.toString());
        }
        return null;
    }

    public String getDownloadUrlAndName(int modid,int fileid){
        try {
            String json = httpGet(PREFIX + "/v1/mods/"+modid+"/files/"+fileid);
            if (json==null){
                return null;
            }
            JSONObject jsonObject = new JSONObject(json);
            CurseModFiles.Data data = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), CurseModFiles.Data.class);
            String url = data.getDownloadUrl();
            if (Objects.isNull(url)){
                String id=String.valueOf(data.getId());
                url="https://media.forgecdn.net/files/"+id.substring(0,4)+"/"+id.substring(4)+"/"+data.getFileName();
            }
            return url+"澪"+data.getFileName();
        } catch (Exception e) {
            Log.e("测试","getDownloadUrlAndName:"+e.toString());
        }
        return null;
    }

    private String httpGet(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-API-KEY", apiKey);
            int responseCode = con.getResponseCode();
            if (responseCode != 200) {
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
            return response.toString();
        } catch (Exception e) {
            Log.e("测试",e.toString());
        }
        return null;
    }
}
