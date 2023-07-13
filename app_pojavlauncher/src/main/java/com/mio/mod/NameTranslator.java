package com.mio.mod;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NameTranslator {
    private Map<String,String> map;
    public NameTranslator(Context context) {
        try {
            map=new HashMap<>();
            InputStream in = context.getAssets().open("mod_data.txt");
            InputStreamReader inr=new InputStreamReader(in);
            BufferedReader bfr=new BufferedReader(inr);
            String line=null;
            StringBuilder sb=new StringBuilder();
            while ((line=bfr.readLine())!=null){
                if (!line.startsWith("#")){
                    String[] sp=line.split(";",7);
                    map.put(sp[4],sp[5].equals("")?sp[4]:sp[5]);
                }
            }
            bfr.close();
            inr.close();
            in.close();
        } catch (IOException ignored) {

        }
    }
    public String translateChToEn(String str){
        Set<String> keys=map.keySet();
        for (String key:keys){
            if (key.contains(str)){
                return map.get(key);
            }
        }
        return null;
    }

    public String translateEnToCh(String str){
        Set<String> keys=map.keySet();
        for (String key:keys){
            String value=map.get(key);
            if (value.equals(str)){
                return key;
            }
        }
        return null;
    }
}
