package net.kdt.pojavlaunch.value;


import android.graphics.BitmapFactory;
import android.util.Log;

import net.kdt.pojavlaunch.*;

import java.io.*;

import com.google.gson.*;

import android.graphics.Bitmap;
import android.util.Base64;

import androidx.annotation.Keep;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("IOStreamConstructor")
@Keep
public class MinecraftAccount {
    public String accessToken = "0"; // access token
    public String clientToken = "0"; // clientID: refresh and invalidate
    public String profileId = "00000000-0000-0000-0000-000000000000"; // profile UUID, for obtaining skin
    public String username = "Steve";
    public String selectedVersion = "1.7.10";
    public boolean isMicrosoft = false;
    public String msaRefreshToken = "0";
    public String xuid;
    public String skinFaceBase64;
    public long expiresAt;
    public String baseUrl;
    public String account;
    public String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    void updateSkinFace(String uuid) {
        try {
            File skinFile = File.createTempFile("skin", ".png", new File(Tools.DIR_DATA, "cache"));
            Tools.downloadFile("https://mc-heads.net/head/" + uuid + "/100", skinFile.getAbsolutePath());
            skinFaceBase64 = Base64.encodeToString(IOUtils.toByteArray(new FileInputStream(skinFile)), Base64.DEFAULT);

            Log.i("SkinLoader", "Update skin face success");
        } catch (IOException e) {
            // Skin refresh limit, no internet connection, etc...
            // Simply ignore updating skin face
            Log.w("SkinLoader", "Could not update skin face", e);
        }
    }

    public boolean isLocal() {
        return accessToken.equals("0");
    }

    public void updateSkinFace() {
        updateSkinFace(profileId);
    }

    public String save(String outPath) throws IOException {
        Tools.write(outPath, Tools.GLOBAL_GSON.toJson(this));
        return username;
    }

    public String save() throws IOException {
        return save(Tools.DIR_ACCOUNT_NEW + "/" + username + ".json");
    }

    public static MinecraftAccount parse(String content) throws JsonSyntaxException {
        return Tools.GLOBAL_GSON.fromJson(content, MinecraftAccount.class);
    }

    public static MinecraftAccount load(String name) {
        if (!accountExists(name)) return null;
        try {
            MinecraftAccount acc = parse(Tools.read(Tools.DIR_ACCOUNT_NEW + "/" + name + ".json"));
            if (acc.accessToken == null) {
                acc.accessToken = "0";
            }
            if (acc.clientToken == null) {
                acc.clientToken = "0";
            }
            if (acc.profileId == null) {
                acc.profileId = "00000000-0000-0000-0000-000000000000";
            }
            if (acc.username == null) {
                acc.username = "0";
            }
            if (acc.selectedVersion == null) {
                acc.selectedVersion = "1.7.10";
            }
            if (acc.msaRefreshToken == null) {
                acc.msaRefreshToken = "0";
            }
            if (acc.skinFaceBase64 == null) {
                // acc.updateSkinFace("MHF_Steve");
            }
            if (acc.baseUrl == null) {
                acc.baseUrl = "0";
            }
            if (acc.account == null) {
                acc.account = "0";
            }
            if (acc.password == null) {
                acc.password = "0";
            }
            return acc;
        } catch (IOException | JsonSyntaxException e) {
            Log.e(MinecraftAccount.class.getName(), "Caught an exception while loading the profile", e);
            return null;
        }
    }

    public Bitmap getSkinFace() {
        if (skinFaceBase64 == null) {
            return null;
        }
        byte[] faceIconBytes = Base64.decode(skinFaceBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(faceIconBytes, 0, faceIconBytes.length);
    }

    private static boolean accountExists(String username) {
        return new File(Tools.DIR_ACCOUNT_NEW + "/" + username + ".json").exists();
    }
}
