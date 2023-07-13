package com.mio.fragments;

import static net.kdt.pojavlaunch.MainActivity.INTENT_MINECRAFT_VERSION;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.kdt.mcgui.ProgressLayout;
import com.mio.MioUtils;
import com.mio.download.FabricDownload;
import com.mio.download.ForgeDownload;
import com.mio.download.OptifineDownload;

import net.kdt.pojavlaunch.JMinecraftVersionList;
import net.kdt.pojavlaunch.JavaGUILauncherActivity;
import net.kdt.pojavlaunch.LauncherActivity;
import net.kdt.pojavlaunch.Logger;
import net.kdt.pojavlaunch.MainActivity;
import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.extra.ExtraCore;
import net.kdt.pojavlaunch.multirt.MultiRTUtils;
import net.kdt.pojavlaunch.multirt.Runtime;
import net.kdt.pojavlaunch.prefs.LauncherPreferences;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;
import net.kdt.pojavlaunch.tasks.AsyncMinecraftDownloader;
import net.kdt.pojavlaunch.utils.DownloadUtils;
import net.kdt.pojavlaunch.utils.JREUtils;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import org.lwjgl.glfw.CallbackBridge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MioPlusFragment extends Fragment {
    public static final String TAG = "MioPlusFragment";

    private ProgressDialog progressDialog;

    public MioPlusFragment() {
        super(R.layout.fragment_mio_plus);
    }
    public String selectedVersion;

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.begin(new File(Tools.DIR_GAME_HOME, "latestlog.txt").getAbsolutePath());
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        Button changeDownloadSourceButton = view.findViewById(R.id.change_download_source_button);
        Button downloadForgeButton = view.findViewById(R.id.download_forge_button);
        Button downloadFabricButton = view.findViewById(R.id.download_fabric_button);
        Button downloadOptfineButton = view.findViewById(R.id.download_optfine_button);
        Button customDirButton = view.findViewById(R.id.custom_dir_button);
        Button modManageButton = view.findViewById(R.id.mod_manage_button);
        Button modDownloadButton = view.findViewById(R.id.mod_download_button);
        Button modPackDownloadButton = view.findViewById(R.id.modpack_download_button);
        Button autoInstallButton = view.findViewById(R.id.auto_install_button);

        String[] sourceItems = new String[]{"官方源", "BMCLAPI", "MCBBS"};
        changeDownloadSourceButton.setText("切换下载源(当前为" + sourceItems[LauncherPreferences.DEFAULT_PREF.getInt("downloadSource", 0)] + ")");
        changeDownloadSourceButton.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择")
                    .setItems(sourceItems, (d, i) -> {
                        SharedPreferences.Editor editor = LauncherPreferences.DEFAULT_PREF.edit();
                        editor.putInt("downloadSource", i);
                        editor.apply();
                        changeDownloadSourceButton.setText("切换下载源(当前为" + sourceItems[i] + ")");
                    }).setNegativeButton("取消", null)
                    .create();
            dialog.show();
        });
        downloadForgeButton.setOnClickListener(v -> {
            progressDialog.show();
            JMinecraftVersionList versionList = (JMinecraftVersionList) ExtraCore.getValue(ExtraConstants.RELEASE_TABLE);
            List<String> list = new ArrayList<>();
            for (JMinecraftVersionList.Version version : versionList.versions) {
                if (version.type.equals("release")) {
                    list.add(version.id);
                }
            }
            String[] items = list.toArray(new String[0]);
            progressDialog.dismiss();
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择版本")
                    .setItems(items, (dialogInterface, i) -> {
                        progressDialog.show();
                        selectedVersion=items[i];
                        new Thread(() -> {
                            ForgeDownload forgeDownload = new ForgeDownload(items[i]);
                            List<String> forgeList = forgeDownload.getForgeVersions();
                            String[] forgeItems = forgeList.toArray(new String[0]);
                            Arrays.sort(forgeItems, (o1, o2) -> {
                                if (!MioUtils.isHigher(o1, o2)) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            });
                            requireActivity().runOnUiThread(() -> {
                                progressDialog.dismiss();
                                AlertDialog tempDialog = new AlertDialog.Builder(requireContext())
                                        .setTitle("请选择Forge版本")
                                        .setItems(forgeItems, (d, j) -> {
                                            download(forgeDownload.getDownloadLink(forgeItems[j]), Tools.DIR_GAME_HOME + "/MioPlus/forge-installer-"+items[i] + "-" + forgeItems[j] + ".jar", true);
                                        })
                                        .setNegativeButton("取消", null)
                                        .create();
                                tempDialog.show();
                            });
                        }).start();
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        });

        downloadFabricButton.setOnClickListener(v -> {
            progressDialog.show();
            new Thread(() -> {
                FabricDownload fabricDownload = new FabricDownload();
                List<String> fabricList = fabricDownload.getInstallerVersion();
                String[] items = fabricList.toArray(new String[0]);
                requireActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    AlertDialog dialog = new AlertDialog.Builder(requireContext())
                            .setTitle("请选择fabric版本")
                            .setItems(items, (dialogInterface, i) -> {
                                download(fabricDownload.getDownloadLink(items[i]), Tools.DIR_GAME_HOME + "/MioPlus/fabric-installer-" + items[i] + ".jar", true);
                            })
                            .setNegativeButton("取消", null)
                            .create();
                    dialog.show();
                });
            }).start();
        });
        downloadOptfineButton.setOnClickListener(v -> {
            JMinecraftVersionList versionList = (JMinecraftVersionList) ExtraCore.getValue(ExtraConstants.RELEASE_TABLE);
            List<String> list = new ArrayList<>();
            for (JMinecraftVersionList.Version version : versionList.versions) {
                if (version.type.equals("release")) {
                    list.add(version.id);
                }
            }
            String[] versions = new File(Tools.DIR_HOME_VERSION).list();
            AlertDialog dialog1 = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择高清修复安装的位置")
                    .setItems(versions, (dd, k) -> {
                        String[] items = list.toArray(new String[0]);
                        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                .setTitle("请选择版本")
                                .setItems(items, (dialogInterface, i) -> {
                                    progressDialog.show();
                                    new Thread(() -> {
                                        OptifineDownload optifineDownload = new OptifineDownload(items[i]);
                                        List<String> optList = optifineDownload.getVersion();
                                        Collections.reverse(optList);
                                        String[] optItems = optList.toArray(new String[0]);
                                        requireActivity().runOnUiThread(() -> {
                                            progressDialog.dismiss();
                                            AlertDialog tempDialog = new AlertDialog.Builder(requireContext())
                                                    .setTitle("请选择高清修复版本")
                                                    .setItems(optItems, (d, j) -> {
                                                        download(optifineDownload.getDownloadLink(optItems[j]), Tools.DIR_HOME_VERSION + "/" + versions[k] + "/mods/" + "/optfine-" + optItems[j].replace("/", "-") + ".jar", false);
                                                    })
                                                    .setNegativeButton("取消", null)
                                                    .create();
                                            tempDialog.show();
                                        });
                                    }).start();
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        dialog.show();
                    }).setNegativeButton("取消", null)
                    .create();
            dialog1.show();
        });

        customDirButton.setOnClickListener(v -> {
            Map<String, MinecraftProfile> map = LauncherProfiles.mainProfileJson.profiles;
            Set<String> keys = map.keySet();
            List<String> list = new ArrayList<>();
            for (String key : keys) {
                list.add(Objects.requireNonNull(map.get(key)).name);
            }
            String[] items = list.toArray(new String[0]);
            AlertDialog dialog = new AlertDialog.Builder(requireContext())
                    .setTitle("请选择需要版本隔离的游戏")
                    .setItems(items, (d, i) -> {
                        String mProfileKey = null;
                        MinecraftProfile mTempProfile = null;
                        for (String key : keys) {
                            if (map.get(key).name.equals(items[i])) {
                                mProfileKey = key;
                                mTempProfile = map.get(key);
                            }
                        }
                        assert mTempProfile != null;
                        mTempProfile.gameDir = "./.minecraft/versions/" + mTempProfile.lastVersionId;
                        LauncherProfiles.mainProfileJson.profiles.put(mProfileKey, mTempProfile);
                        LauncherProfiles.update();
                        ExtraCore.setValue(ExtraConstants.REFRESH_VERSION_SPINNER, mProfileKey);
                    })
                    .setNegativeButton("取消", null)
                    .create();
            dialog.show();
        });

        modManageButton.setOnClickListener(v -> {
            Tools.swapFragment(requireActivity(), ModManageFragment.class, ModManageFragment.TAG, true, null);
        });
        modDownloadButton.setOnClickListener(v -> {
            Tools.swapFragment(requireActivity(), ModSearchFragment.class, ModSearchFragment.TAG, true, null);
        });
        modPackDownloadButton.setOnClickListener(v -> {
            Tools.swapFragment(requireActivity(), ModPackSearchFragment.class, ModPackSearchFragment.TAG, true, null);
        });
        modPackDownloadButton.setOnLongClickListener(view1 -> {
            AlertDialog dialog=new AlertDialog.Builder(requireContext())
                    .setTitle("提示")
                    .setMessage("仅支持Curseforge整合包,下载完成需要自己调整才可使用,下载位置为"+Tools.DIR_GAME_HOME+"/整合包/整合包名")
                    .setPositiveButton("确定", (d,i)->{
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("zip");
                        if (mimeType == null) mimeType = "*/*";
                        intent.setType(mimeType);
                        requireActivity().startActivityForResult(intent, 1919810);
                    })
                    .setNegativeButton("取消",null)
                    .create();
            dialog.show();
            return false;
        });
        autoInstallButton.setOnClickListener(v->{
            Tools.swapFragment(requireActivity(), MioInstallFragment.class, MioInstallFragment.TAG, true, null);
        });
//        new Thread(() -> launchJavaRuntime(new File(Tools.DIR_GAME_HOME+"/forge-installer-47.0.35.jar")), "JREMainThread").start();
//        new Thread(()->{
//            while (true){
//                try {
//                    Log.e("测试",""+CallbackBridge.nativeGetInstallProgress());
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        }).start();
//        test();
    }

    private void download(String url, String dest, boolean install) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("下载进度：0%");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        PojavApplication.sExecutorService.execute(()->{
            try {
                DownloadUtils.downloadFileMonitored(url, dest, new byte[1024], (curr, max) -> requireActivity().runOnUiThread(() -> {
                    long percent = curr * 100 / max;
                    progressDialog.setTitle("下载进度：" + percent + "%");
                    if (percent == 100) {
                        progressDialog.dismiss();
                        progressDialog = new ProgressDialog(requireContext());
                        if (install) {
//                            new Thread(() -> launchJavaRuntime(new File(dest)), "JREMainThread").start();
                            Intent intent = new Intent(requireActivity(), JavaGUILauncherActivity.class);
                            intent.putExtra("modFile", new File(dest));
                            requireActivity().startActivity(intent);
                        } else {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(requireContext(), "下载完成", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                }));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public int launchJavaRuntime(File modFile) {
        JREUtils.redirectAndPrintJRELog();
        try {
            String jreName = LauncherPreferences.PREF_DEFAULT_RUNTIME;
            final Runtime runtime = MultiRTUtils.forceReread(jreName);
            List<String> javaArgList = new ArrayList<>();

            javaArgList.add("-cp");
            javaArgList.add(".:"+Tools.DIR_GAME_HOME + "/login/forge-install-bootstrapper.jar:"+modFile.getAbsolutePath()+":"+Tools.DIR_GAME_HOME+"/lwjgl3/lwjgl-glfw-classes.jar");
            javaArgList.add("com.bangbang93.ForgeInstaller");
            javaArgList.add(Tools.DIR_GAME_NEW);

            Logger.appendToLog("Info: Java arguments: " + Arrays.toString(javaArgList.toArray(new String[0])));

            return JREUtils.launchJavaVM(requireActivity(), runtime,null,javaArgList, LauncherPreferences.PREF_CUSTOM_JAVA_ARGS);
        } catch (Throwable th) {
            Tools.showError(requireContext(), th, true);
            return -1;
        }
    }
}
