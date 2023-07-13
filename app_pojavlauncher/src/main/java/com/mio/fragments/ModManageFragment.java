package com.mio.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mio.adapter.ModManageAdapter;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModManageFragment extends Fragment {
    public static final String TAG = "ModManageFragment";

    private Spinner versionSpinner;
    private ListView listView;
    private static ModManageAdapter modManageAdapter;
    private static List<File> fileList;
    public static String path = "";

    public ModManageFragment() {
        super(R.layout.fragment_mio_plus_mod_manage);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        versionSpinner = view.findViewById(R.id.mod_manage_version_spinner);
        listView = view.findViewById(R.id.mod_manage_listview);

        List<String> list = new ArrayList<>();
        list.add("公用目录");
        String[] ff = new File(Tools.DIR_HOME_VERSION).list();
        if (!Objects.isNull(ff)) {
            list.addAll(Arrays.asList(ff));
        }
        ArrayAdapter<String> versionSpinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, list);
        versionSpinner.setAdapter(versionSpinnerAdapter);
        versionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fileList.clear();
                File[] files;
                if (list.get(i).equals("公用目录")) {
                    path = Tools.DIR_GAME_NEW + "/mods";
                    files = new File(Tools.DIR_GAME_NEW + "/mods").listFiles();
                } else {
                    path = Tools.DIR_HOME_VERSION + "/" + list.get(i) + "/mods";
                    files = new File(Tools.DIR_HOME_VERSION + "/" + list.get(i) + "/mods").listFiles();
                }
                if (Objects.isNull(files)) {
                    files = new File[0];
                    Toast.makeText(requireContext(), "当前所选择版本无mod", Toast.LENGTH_SHORT).show();
                }
                fileList.addAll(Arrays.asList(files));

                modManageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fileList = new ArrayList<>();
        File[] files = new File(Tools.DIR_HOME_VERSION).listFiles();
        if (Objects.isNull(files)) {
            files = new File[0];
        }
        fileList.addAll(Arrays.asList(files));
        modManageAdapter = new ModManageAdapter(requireContext(), fileList);
        listView.setAdapter(modManageAdapter);

        ImageView addMod = view.findViewById(R.id.add_mod);
        addMod.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("jar");
            if (mimeType == null) mimeType = "*/*";
            intent.setType(mimeType);
            requireActivity().startActivityForResult(intent, 114514);
        });
    }

    public static void addModToList(File modFile) {
        fileList.add(modFile);
        modManageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        modManageAdapter=null;
        fileList.clear();
        fileList=null;
    }
}
