package com.mio.adapter;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.mio.MioUtils;
import com.mio.mod.curseforge.CurseModFiles;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.utils.DownloadUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModPackVersionAdapter extends BaseExpandableListAdapter {
    private FragmentActivity context;
    private List<String> versionList;
    private List<List<CurseModFiles.Data>> modList;
    private int modID;

    public ModPackVersionAdapter(FragmentActivity context, List<String> versionList, List<List<CurseModFiles.Data>> modList, int modID) {
        this.context = context;
        this.versionList = versionList;
        this.modList = modList;
        this.modID = modID;
    }

    @Override
    public int getGroupCount() {
        return versionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return modList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return versionList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return modList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_mio_plus_mod_download_version, parent, false);
        TextView textView = convertView.findViewById(R.id.item_mod_download_version_text);
        textView.setText(versionList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_mio_plus_mod_download_version_mod, parent, false);
        TextView textView = convertView.findViewById(R.id.item_mod_download_version_mod_text);
        textView.setText(modList.get(groupPosition).get(childPosition).getFileName());
        ImageButton imageButton = convertView.findViewById(R.id.install);
        imageButton.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("下载进度：0%");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            PojavApplication.sExecutorService.execute(() -> {
                String url = modList.get(groupPosition).get(childPosition).getDownloadUrl();
                if (Objects.isNull(url)){
                    String modid=String.valueOf(modList.get(groupPosition).get(childPosition).getId());
                    url="https://media.forgecdn.net/files/"+modid.substring(0,4)+"/"+modid.substring(4)+"/"+modList.get(groupPosition).get(childPosition).getFileName();
                }
                String path = Tools.DIR_GAME_HOME + "/整合包/" + modList.get(groupPosition).get(childPosition).getFileName();
                try {
                    DownloadUtils.downloadFileMonitored(url, path, new byte[1024], (curr, max) -> {
                        context.runOnUiThread(() -> {
                            long percent = curr * 100 / max;
                            progressDialog.setTitle("下载进度：" + percent + "%");
                            if (percent == 100) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "下载完成,开始安装", Toast.LENGTH_SHORT).show();
                                MioUtils.installModPack(context,path);
                            }
                        });
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
