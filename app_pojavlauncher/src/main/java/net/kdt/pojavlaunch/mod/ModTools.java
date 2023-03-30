package net.kdt.pojavlaunch.mod;

import static net.kdt.pojavlaunch.PojavApplication.sExecutorService;
import static net.kdt.pojavlaunch.Tools.getFileName;
import static net.kdt.pojavlaunch.Tools.getGameDirPath;
import static net.kdt.pojavlaunch.Tools.getWaitingDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.exception.NotImplementedException;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.utils.FileUtils;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ModTools {

    public static final int RUN_MRPACK_INSTALLER = 2060;
    public static final int RUN_MOD_REAL_INSTALLER = 2070;

    // Make the user select the mod jar file.
    public static void installModREAL(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String [] mimeType = {"application/java-archive", "application/zip"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
        activity.startActivityForResult(intent, RUN_MOD_REAL_INSTALLER);
    }

    public static void launchModREALInstaller(Activity activity, @NonNull Intent data) {
        sExecutorService.execute(() -> {
            try {

                // Get current Minecraft profile.
                MinecraftProfile minecraftProfile;
                if(LauncherProfiles.mainProfileJson == null) LauncherProfiles.update();
                minecraftProfile = LauncherProfiles.mainProfileJson.profiles.get(LauncherPreferences.DEFAULT_PREF.getString(LauncherPreferences.PREF_KEY_CURRENT_PROFILE,""));

                // Check if the Minecraft profile is null.
                if (minecraftProfile == null) {
                    Tools.showError(activity, new NullPointerException("The Minecraft profile is null!"));
                    Log.e("Mod REAL Installer", "The Minecraft profile is null!");
                    return;
                }

                // Create mods dir
                FileUtils.createDirectory(new File(getGameDirPath(minecraftProfile) + "/mods"));

                // Copy the mod to the current profile's gameDir.
                final Uri uri = data.getData();
                final String name = getFileName(activity, uri);
                final File modFile = new File(getGameDirPath(minecraftProfile) + "/mods", name);
                FileOutputStream fos = new FileOutputStream(modFile);
                InputStream input = activity.getContentResolver().openInputStream(uri);
                IOUtils.copy(input, fos);
                input.close();
                fos.close();

                // Pop up a toast saying that the mod has been installed.
                Looper.prepare();
                Toast.makeText(activity, "Mod \"" + name + "\" installed!", Toast.LENGTH_LONG).show();
            }catch(IOException e) {
                Tools.showError(activity, e);
            }
        });

    }

    public static void installModpack(Activity activity) {
        // Open file
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mrpack");
        if(mimeType == null) mimeType = "*/*";
        intent.setType(mimeType);
        activity.startActivityForResult(intent, RUN_MRPACK_INSTALLER);
    }

    public static void launchModpackInstaller(Activity activity, @NonNull Intent data) throws IOException {
        final ProgressDialog alertDialog = getWaitingDialog(activity);

        final Uri uri = data.getData();
        Log.d("Modpack Installer", "Installing modpack...");
        Log.d("Modpack Installer", "Uri: " + uri.toString());
        alertDialog.setMessage("Installing modpack...");

        // Cache
        Log.d("Modpack Installer", "Caching mrpack...");
        sExecutorService.execute(() -> {
            try {
                final String name = getFileName(activity, uri);
                final File modpackFile = new File(activity.getCacheDir(), name);
                FileOutputStream fos = new FileOutputStream(modpackFile);
                InputStream input = activity.getContentResolver().openInputStream(uri);
                IOUtils.copy(input, fos);
                input.close();
                fos.close();
            }catch(IOException e) {
                Tools.showError(activity, e);
            }
        });

        // Extract the mrpack
        Log.d("Modpack Installer", "Extracting the mrpack...");
        final String name = getFileName(activity, uri);
        Log.d("Modpack Installer", "File name: " + name);
        final String cacheDir = activity.getCacheDir().toString();
        Log.d("Modpack Installer", "Cache Dir: " + cacheDir);
        final File modpackFile = new File(activity.getCacheDir(), name);
        Log.d("Modpack Installer", "modpackFile: " + modpackFile);

        // Wait a few seconds so the copy can finish
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                File directory = new File(cacheDir + "/" + name + "dir");
                directory.mkdirs();
                try {
                    FileUtils.unzip(modpackFile, new File(cacheDir + "/" + name + "dir"));
                } catch (IOException e) {
                    Log.e("Modpack Installer", "ERROR: " + e);
                    Tools.showError(activity, e);
                    alertDialog.cancel();
                }
                Log.d("Modpack Installer", "Extracted!");

                // Parse JSON
                try {
                    final String JSONString = FileUtils.getStringFromFile(cacheDir + "/" + name);
//                    Modpack modpack = ModpackHelper.readModpackManifest(Paths.get(cacheDir + "/" + name), Charset.defaultCharset());
//                    Log.d("Modpack Installer", "Modpack name: " + modpack.getName());
//                    Log.d("Modpack Installer", "Modpack version: " + modpack.getVersion());
//                    Log.d("Modpack Installer", "Modpack MC version: " + modpack.getGameVersion());
//                    final String modpackPath = Tools.DIR_GAME_HOME + "/" + modpack.getName() + "-" + modpack.getVersion();
//                    File modpackDirectory = new File(modpackPath);
//                    Log.d("Modpack Installer", "Modpack dir: " + modpackPath);
//                    modpackDirectory.mkdirs();
                    // Copy files from "overrides" directory to modpack directory
//                    Log.d("Modpack Installer", "Modpack overrides dir: " + cacheDir + "/" + name + "dir/overrides");
//                    FileUtils.copyDirectory(new File(cacheDir + "/" + name + "dir/overrides"), new File(modpackPath));

//                    ModpackConfiguration modpackConfiguration = ModpackHelper.readModpackConfiguration(new File(cacheDir + "/" + name + "dir/modrinth.index.json"));


                    // for (int i; i < modpack.)
                    throw new NotImplementedException("Not implemented! (got up to extracting the modpack)");
                } catch (IOException | NotImplementedException e ) {
                    Log.e("Modpack Installer", "ERROR: " + e);
                    Tools.showError(activity, e);
                    alertDialog.cancel();
                }

            }
        }, 5000); // 5 seconds


    }

}
