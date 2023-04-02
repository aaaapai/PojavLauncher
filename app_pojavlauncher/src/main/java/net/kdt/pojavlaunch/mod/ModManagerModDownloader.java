package net.kdt.pojavlaunch.mod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import net.kdt.pojavlaunch.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModManagerModDownloader#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModManagerModDownloader extends Fragment {

    public ModManagerModDownloader() {}
    public static String TAG = "ModManagerModDownloader";

    public static ModManagerModDownloader newInstance() {
        ModManagerModDownloader fragment = new ModManagerModDownloader();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_manager_mod_downloader, container, false);
    }
}