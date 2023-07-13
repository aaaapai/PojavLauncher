package com.mio.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.mio.download.DownloadInfo;

import net.kdt.pojavlaunch.R;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private Context context;
    private List<DownloadInfo> downloads;

    public FileAdapter(Context context) {
        this.context = context;
        downloads = new ArrayList<>();
    }

    public void addDownload(DownloadInfo download) {
        downloads.add(download);
        notifyItemInserted(downloads.size() - 1);
    }

    public void onProgress(DownloadInfo download) {
        for (int i = 0; i < downloads.size(); i++) {
            DownloadInfo tmp = downloads.get(i);
            if (tmp.url.equals(download.url)) {
                downloads.set(i, download);
                notifyItemChanged(i);
            }
        }
    }

    public void onComplete(DownloadInfo download) {
        for (int i = 0; i < downloads.size(); i++) {
            DownloadInfo tmp = downloads.get(i);
            if (tmp.url.equals(download.url)) {
                downloads.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_download_progress, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadInfo mDownload = downloads.get(position);
        holder.progressBar.setProgress(mDownload.progress);
        holder.fileName.setText(mDownload.name);
    }

    @Override
    public int getItemCount() {
        return downloads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private NumberProgressBar progressBar;
        private TextView fileName;

        public ViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.number_progress_bar);
            fileName = view.findViewById(R.id.file_name);
        }
    }

}
