package net.kdt.pojavlaunch.mod;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.kdt.pojavlaunch.R;

import java.util.List;

public class ModDownloaderAdapter extends RecyclerView.Adapter<ModDownloaderVH>{

    List<String> items;

    public ModDownloaderAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ModDownloaderVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_mod_row_item, parent, false);
        return new ModDownloaderVH(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ModDownloaderVH holder, int position) {
        holder.modName.setText(items.get(position));
        holder.modDescription.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class ModDownloaderVH extends RecyclerView.ViewHolder {

    TextView modName;
    TextView modDescription;

    String modDownloadURL;

    private ModDownloaderAdapter adapter;

    public ModDownloaderVH(@NonNull View itemView) {
        super(itemView);

        modName = itemView.findViewById(R.id.modName);
        modDescription = itemView.findViewById(R.id.modDescription);
        itemView.findViewById(R.id.installModButton).setOnClickListener(view -> {
            Log.e("Mod Downloader", "Not Implemented Yet!");
        });
        itemView.findViewById(R.id.uninstallModButton).setOnClickListener(view -> {
            Log.e("Mod Downloader", "Not Implemented Yet!");
        });
    }

    public ModDownloaderVH linkAdapter(ModDownloaderAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

}