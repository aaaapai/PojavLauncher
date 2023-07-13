package com.mio.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kdt.mcgui.MineButton;
import com.mio.adapter.ModPackSearchAdapter;
import com.mio.adapter.ModSearchAdapter;
import com.mio.mod.NameTranslator;
import com.mio.mod.curseforge.CurseAddon;
import com.mio.mod.curseforge.CurseforgeAPI;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.R;

import java.util.List;
import java.util.Objects;

public class ModPackSearchFragment extends Fragment {
    public static final String TAG = "ModPackSearchFragment";

    private ProgressDialog progressDialog;

    private EditText modSearchEdittext;
    private MineButton modSearchButton;
    private ListView modPackSearchListView;
    private ModPackSearchAdapter modPackSearchAdapter;
    private CurseforgeAPI curseforgeAPI;
    private List<CurseAddon.Data> dataList;

    public ModPackSearchFragment() {
        super(R.layout.fragment_mio_plus_mod_search);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        modSearchEdittext = view.findViewById(R.id.mod_search_edittext);
        modSearchButton = view.findViewById(R.id.mod_search_button);
        modPackSearchListView = view.findViewById(R.id.mod_search_listview);

        progressDialog=new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        curseforgeAPI=new CurseforgeAPI();
        PojavApplication.sExecutorService.execute(()->{
            dataList = curseforgeAPI.searchModPack("",false);
            requireActivity().runOnUiThread(()->{
                progressDialog.dismiss();
                if (!Objects.isNull(dataList)){
                    modPackSearchAdapter=new ModPackSearchAdapter(requireActivity(),dataList);
                    modPackSearchListView.setAdapter(modPackSearchAdapter);
                } else {
                    Toast.makeText(requireContext(),"获取整合包列表失败！",Toast.LENGTH_SHORT).show();
                }
            });
        });

        modSearchButton.setOnClickListener(v->{
            String name=modSearchEdittext.getText().toString();
            if (name.equals("")){
                return;
            }
            progressDialog.show();
            PojavApplication.sExecutorService.execute(()->{
                List<CurseAddon.Data> list = curseforgeAPI.searchModPack(name,true);
                requireActivity().runOnUiThread(()->{
                if (!Objects.isNull(list)){
                    progressDialog.dismiss();
                    dataList.clear();
                    dataList.addAll(list);
                    modPackSearchAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(),"未能获取到任何相关整合包信息！",Toast.LENGTH_LONG).show();
                }
                });
            });
        });
    }

}
