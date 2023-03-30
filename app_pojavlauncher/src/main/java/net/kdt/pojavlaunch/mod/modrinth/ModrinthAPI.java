package net.kdt.pojavlaunch.mod.modrinth;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModrinthAPI {

    public static String projectSearch(ModrinthSearchCategories searchCategories, ModrinthProjectType projectType, String query) throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        URL url = new URL("https://api.modrinth.com/v2/search?offset=0&query=" + query + "&limit=50&facets=[[\"categories:" + searchCategories + "\"],[\"versions:1.19.4\"],[\"project_type:" + projectType + "\"]]");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Raidenxd2/PojavLauncher-community/dev");
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();
        return content.toString();
    }

}

