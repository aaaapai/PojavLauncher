package com.mio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.mio.fragments.ModDownloadFragment;
import com.mio.fragments.ModPackDownloadFragment;
import com.mio.mod.NameTranslator;
import com.mio.mod.curseforge.CurseAddon;

import net.kdt.pojavlaunch.PojavApplication;
import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.Tools;
import net.kdt.pojavlaunch.utils.DownloadUtils;

import java.util.List;
import java.util.Objects;


public class ModPackSearchAdapter extends BaseAdapter {

    private final FragmentActivity context;
    private List<CurseAddon.Data> dataList;

    public ModPackSearchAdapter(FragmentActivity context, List<CurseAddon.Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mio_plus_mod_search, parent, false);
            holder = new ViewHolder();
            holder.modName = convertView.findViewById(R.id.item_mod_search_name_text);
            holder.infoText = convertView.findViewById(R.id.item_mod_search_info_text);
            holder.logo = convertView.findViewById(R.id.logo);
            holder.download = convertView.findViewById(R.id.mod_search_download_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CurseAddon.Data data = dataList.get(position);
        if (Objects.isNull(data)){
            return convertView;
        }
        try {
            Glide.with(context).load(data.getLogo().getUrl()).into(holder.logo);
        }catch (Exception e){

        }
        holder.modName.setText(data.getName());
        holder.infoText.setText(data.getSummary());
        holder.download.setOnClickListener(v->{
            ModPackDownloadFragment.data=data;
            Tools.swapFragmentAdd(context, ModPackDownloadFragment.class, ModPackDownloadFragment.TAG, true, null);
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView logo;
        TextView modName;
        TextView infoText;
        ImageView download;
    }
}
