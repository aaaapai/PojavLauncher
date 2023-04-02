package net.kdt.pojavlaunch.mod;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.mod.modrinth.ModrinthAPI;
import net.kdt.pojavlaunch.mod.modrinth.ModrinthProjectType;
import net.kdt.pojavlaunch.mod.modrinth.ModrinthSearchCategories;
import net.kdt.pojavlaunch.progresskeeper.ProgressKeeper;

import java.io.IOException;

public class ModManagerMainFragment extends Fragment {

    public static final String TAG = "ModMenuMainFragment";

    public ModManagerMainFragment() { super(R.layout.fragment_mod_manager_main); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button mInstallMrpackButton = view.findViewById(R.id.install_mrpack_button);
        Button mDownloadModsButton = view.findViewById(R.id.download_mods_button);
        Button mInstallModButton = view.findViewById(R.id.install_mod_button);

        mInstallMrpackButton.setOnClickListener(v -> runMrpackInstaller());
        // mDownloadModsButton.setOnClickListener(v -> projectSearchFromAPI());
        mDownloadModsButton.setOnClickListener(v -> Tools.swapFragment(requireActivity(), ModManagerModDownloader.class, ModManagerModDownloader.TAG, true, null));
        mInstallModButton.setOnClickListener(v -> startInstallMod());

    }

    private void projectSearchFromAPI() {

        String modrinthProjectSearchJSON = null;
        try {
            modrinthProjectSearchJSON = ModrinthAPI.projectSearch(ModrinthSearchCategories.fabric, ModrinthProjectType.mod, "");
        }
        catch (IOException e) {
            Tools.showError(this.getContext(), e);
        }

        Log.d("Mod Downloader", "modrinthProjectSearchJSON: " + modrinthProjectSearchJSON);

    }

    private void runMrpackInstaller() {
        if (ProgressKeeper.getTaskCount() == 0)
            ModTools.installModpack(requireActivity());
        else
            Toast.makeText(requireContext(), R.string.tasks_ongoing, Toast.LENGTH_LONG).show();
    }

    private void startInstallMod() {
        ModTools.installModREAL(requireActivity());
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_mod_manager_main, container, false);
//    }

}