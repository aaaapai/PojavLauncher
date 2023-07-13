package com.mio.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mio.MioUtils;
import com.mio.adapter.ModPackVersionAdapter;
import com.mio.adapter.ModSearchAdapter;
import com.mio.adapter.ModVersionAdapter;
import com.mio.mod.NameTranslator;
import com.mio.mod.curseforge.CurseAddon;
import com.mio.mod.curseforge.CurseModFiles;
import com.mio.mod.curseforge.CurseforgeAPI;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModPackDownloadFragment extends Fragment {
    public static final String TAG = "ModPackDownloadFragment";
    private ProgressDialog progressDialog;
    private ExpandableListView modVersionExpandableListView;

    public static CurseAddon.Data data;
    private final CurseforgeAPI api = new CurseforgeAPI();
    private List<String> versionList;
    private List<List<CurseModFiles.Data>> modList;

    public ModPackDownloadFragment() {
        super(R.layout.fragment_mio_plus_mod_download);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        modVersionExpandableListView = view.findViewById(R.id.mod_version);

        PojavApplication.sExecutorService.execute(() -> {
            List<CurseModFiles.Data> dataList = api.getModFiles(data.getId());
            Map<String, List<CurseModFiles.Data>> map = new HashMap<>();
            if (!Objects.isNull(dataList)) {
                for (CurseModFiles.Data d : dataList) {
                    for (String version : d.getGameVersions()) {
                        if (version.contains(".")) {
                            List<CurseModFiles.Data> temp;
                            if (map.containsKey(version)) {
                                temp = map.get(version);
                            } else {
                                temp = new ArrayList<>();
                            }
                            temp.add(d);
                            map.put(version, temp);
                        }
                    }
                }
                versionList = new ArrayList<>();
                versionList.addAll(map.keySet());
                Collections.sort(versionList, (o1, o2) -> {
                    if (!MioUtils.isHigher(o1, o2)) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                modList = new ArrayList<>();
                for (String key : versionList) {
                    modList.add(map.get(key));
                }
                requireActivity().runOnUiThread(() -> {
                    ModPackVersionAdapter modPackVersionAdapter = new ModPackVersionAdapter(requireActivity(), versionList, modList, data.getId());
                    modVersionExpandableListView.setAdapter(modPackVersionAdapter);
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "获取整合包列表失败！", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }
}
