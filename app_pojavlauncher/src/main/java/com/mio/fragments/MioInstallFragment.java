package com.mio.fragments;

import static net.kdt.pojavlaunch.extra.ExtraCore.getValue;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mio.MioUtils;
import com.mio.adapter.VersionListAdapter;
import com.mio.download.FabricDownload;
import com.mio.download.FabricLoaderDownload;
import com.mio.download.ForgeDownload;
import com.mio.download.MioDownloadTask;
import com.mio.download.OptifineDownload;

import net.kdt.pojavlaunch.JMinecraftVersionList;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.extra.ExtraConstants;
import net.kdt.pojavlaunch.value.launcherprofiles.LauncherProfiles;
import net.kdt.pojavlaunch.value.launcherprofiles.MinecraftProfile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MioInstallFragment extends Fragment {
    public static final String TAG = "MioInstallFragment";
    private ProgressDialog progressDialog;
    private String mProfileKey;
    private MinecraftProfile mTempProfile = null;

    private Button mcVersionButton;
    private Button forgeVersionButton;
    private Button fabricVersionButton;
    private Button optVersionButton;
    private Button installButton;

    private ForgeDownload forgeDownload;
    private FabricLoaderDownload fabricDownload;
    private OptifineDownload optifineDownload;
    private String selectedForgeVersion="";
    private String selectedFabricVersion="";
    private String selectedOptVersion="";

    public MioInstallFragment() {
        super(R.layout.fragment_mio_plus_install);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        mcVersionButton=view.findViewById(R.id.mc_version_button);
        forgeVersionButton=view.findViewById(R.id.forge_version_button);
        fabricVersionButton=view.findViewById(R.id.fabric_version_button);
        optVersionButton=view.findViewById(R.id.opt_version_button);
        installButton=view.findViewById(R.id.install_button);

        mTempProfile=getProfile();

        mcVersionButton.setOnClickListener(v->{
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            ExpandableListView expandableListView = (ExpandableListView) LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_expendable_list_view , null);
            JMinecraftVersionList jMinecraftVersionList = (JMinecraftVersionList) getValue(ExtraConstants.RELEASE_TABLE);
            JMinecraftVersionList.Version[] versionArray;
            if(jMinecraftVersionList == null || jMinecraftVersionList.versions == null) versionArray = new JMinecraftVersionList.Version[0];
            else versionArray = jMinecraftVersionList.versions;
            ExpandableListAdapter adapter = new VersionListAdapter(versionArray, requireContext());

            expandableListView.setAdapter(adapter);
            builder.setView(expandableListView);
            AlertDialog dialog = builder.show();

            expandableListView.setOnChildClickListener((parent, v1, groupPosition, childPosition, id) -> {
                String version = ((String) adapter.getChild(groupPosition, childPosition));
                mTempProfile.lastVersionId = version;
                mcVersionButton.setText("MC版本:"+version);
                dialog.dismiss();
                return true;
            });
        });

        forgeVersionButton.setOnClickListener(v->{
            if (!selectedFabricVersion.equals("")){
                Toast.makeText(requireContext(),"Forge与Fabric不能同时安装",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTempProfile.lastVersionId!=null){
                progressDialog.show();
                new Thread(() -> {
                    forgeDownload = new ForgeDownload(mTempProfile.lastVersionId);
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
//                                    download(forgeDownload.getDownloadLink(forgeItems[j]), Tools.DIR_GAME_HOME + "/forge-installer-" + forgeItems[j] + ".jar", true);
                                    forgeVersionButton.setText("Forge版本:"+forgeItems[j]);
                                    selectedForgeVersion=forgeItems[j];
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        tempDialog.show();
                    });
                }).start();
            } else {
                Toast.makeText(requireContext(),"未选择MC版本",Toast.LENGTH_SHORT).show();
            }
        });

        fabricVersionButton.setOnClickListener(v->{
            if (!selectedForgeVersion.equals("")){
                Toast.makeText(requireContext(),"Forge与Fabric不能同时安装",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mTempProfile.lastVersionId!=null){
                progressDialog.show();
                new Thread(() -> {
                    fabricDownload = new FabricLoaderDownload();
                    List<String> fabricList = fabricDownload.getLoaderVersion();
                    String[] items = fabricList.toArray(new String[0]);
                    requireActivity().runOnUiThread(() -> {
                        progressDialog.dismiss();
                        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                                .setTitle("请选择fabric版本")
                                .setItems(items, (dialogInterface, i) -> {
                                    fabricVersionButton.setText("Fabric版本:"+items[i]);
                                    selectedFabricVersion=items[i];
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        dialog.show();
                    });
                }).start();
            } else {
                Toast.makeText(requireContext(),"未选择MC版本",Toast.LENGTH_SHORT).show();
            }
        });

        optVersionButton.setOnClickListener(v->{
            if (mTempProfile.lastVersionId!=null){
                progressDialog.show();
                new Thread(() -> {
                    optifineDownload = new OptifineDownload(mTempProfile.lastVersionId);
                    List<String> optList = optifineDownload.getVersion();
                    Collections.reverse(optList);
                    String[] optItems = optList.toArray(new String[0]);
                    requireActivity().runOnUiThread(() -> {
                        progressDialog.dismiss();
                        AlertDialog tempDialog = new AlertDialog.Builder(requireContext())
                                .setTitle("请选择高清修复版本")
                                .setItems(optItems, (d, j) -> {
                                    optVersionButton.setText("OptFine版本:"+optItems[j]);
                                    selectedOptVersion=optItems[j];
                                })
                                .setNegativeButton("取消", null)
                                .create();
                        tempDialog.show();
                    });
                }).start();
            } else {
                Toast.makeText(requireContext(),"未选择MC版本",Toast.LENGTH_SHORT).show();
            }
        });

        installButton.setOnClickListener(v->{

        });

    }

    private MinecraftProfile getProfile(){
        MinecraftProfile minecraftProfile;
        minecraftProfile = MinecraftProfile.createTemplate();
        minecraftProfile.lastVersionId=null;
        String uuid = UUID.randomUUID().toString();
        while(LauncherProfiles.mainProfileJson.profiles.containsKey(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        mProfileKey = uuid;
        return minecraftProfile;
    }
}
