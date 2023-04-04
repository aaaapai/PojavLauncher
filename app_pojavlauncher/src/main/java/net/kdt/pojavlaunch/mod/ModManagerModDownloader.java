package net.kdt.pojavlaunch.mod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.kdt.pojavlaunch.R;

import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModManagerModDownloader#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModManagerModDownloader extends Fragment {

    public ModManagerModDownloader() {}
    public static String TAG = "ModManagerModDownloader";

    int counter = 0;

    String []modNameDummy = {"Sodium", "Lithium", "No Light", "SUS", "PoopMod", "Starlight", "ESSENTIAL", "LazyDFU"};
    String []modDescriptionDummy = {"No", "No", "No", "No", "No", "No", "Shit mod", "FAST START"};

    public static ModManagerModDownloader newInstance() {
        ModManagerModDownloader fragment = new ModManagerModDownloader();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        List<String> items = new LinkedList<>();
        items.add("E");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ModDownloaderAdapter adapter = new ModDownloaderAdapter(items);
        recyclerView.setAdapter(adapter);

        items.add(modNameDummy[counter%3]);
        counter++;
        adapter.notifyItemInserted(items.size()-1);

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