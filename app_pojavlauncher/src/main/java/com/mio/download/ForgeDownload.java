package com.mio.download;

import android.util.Log;


import com.mio.SSLSocketClient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgeDownload
{
    private ArrayList<String> forgeVersion;
    private HashMap<String,String> forgeList;
    public ForgeDownload(String mcversion){
        initForgeList(mcversion);
    }

    private void initForgeList(String mcversion){
        forgeVersion=new ArrayList<>();
        forgeList=new HashMap<>();
        try {
            JSONArray temp=new JSONArray(doGet("https://bmclapi2.bangbang93.com/forge/minecraft/" + mcversion));
            for(int i=0;i<temp.length();i++){
                forgeVersion.add(temp.getJSONObject(i).getString("version"));
                forgeList.put(temp.getJSONObject(i).getString("version"),temp.getJSONObject(i).getString("build"));
            }
        } catch (Exception e) {
            Log.e("错误",e.toString());
        }
    }
    public String getDownloadLink(String forgeVersion){
        return "https://bmclapi2.bangbang93.com/forge/download/"+forgeList.get(forgeVersion);
    }

    public ArrayList<String> getForgeVersions(){
        return forgeVersion;
    }
    public static String doGet(String url) {
        String result = null;
        try {
            OkHttpClient client = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),(X509TrustManager) SSLSocketClient.getTrustManager()[0])
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            if (response.code()==200) {
                result=response.body().string();
            }else {
                Log.e("错误",""+response.code());
            }
        }catch (Exception e){
            Log.e("错误",e.toString());
        }
        return result;
    }
}

