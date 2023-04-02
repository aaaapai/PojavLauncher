package net.kdt.pojavlaunch;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModRowItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModRowItem extends Fragment {
    public ModRowItem() {}

    public static ModRowItem newInstance() {
        ModRowItem fragment = new ModRowItem();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mod_row_item, container, false);
    }
}