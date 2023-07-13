package com.mio.modpack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.mio.download.DownloadCallback;
import com.mio.adapter.FileAdapter;
import com.mio.download.DownloadInfo;
import com.mio.mod.curseforge.CurseforgeAPI;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.utils.DownloadUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MioDownloadModPackTask extends AsyncTask<List<CurseforgeModpackManifest.Files>, Integer, Map<String, String>> {

    private final WeakReference<Activity> ctx;
    private Map<String, String> failedFile;//下载失败的文件
    private DownloadCallback callback;//回调
    private int maxTask = 10;//最大任务数
    private FileAdapter fileAdapter;
    private TextView textInfo;
    public NumberProgressBar totalProgressBar;
    private AlertDialog mDialog;
    private CurseforgeAPI api = new CurseforgeAPI();
    private String modsPath;
    private final ConcurrentHashMap<Thread, byte[]> mThreadBuffers = new ConcurrentHashMap<>(maxTask);


    public MioDownloadModPackTask(Activity ctx, DownloadCallback callback) {
        this.ctx = new WeakReference<>(ctx);
        this.callback = callback;
        failedFile = new ArrayMap<>();
    }

    public void setModsPath(String modsPath) {
        this.modsPath = modsPath;
    }

    private void init() {
        View v = LayoutInflater.from(ctx.get()).inflate(R.layout.dialog_mio_plus_download_modpack, null);
        totalProgressBar = v.findViewById(R.id.alert_downloadProgressBarTotalProgress);
        textInfo = v.findViewById(R.id.textInfo);
        Button cancleButton = v.findViewById(R.id.cancle_button);
        cancleButton.setOnClickListener(view -> {
            try {
                cancel(true);
            } catch (Exception e) {

            }
            mDialog.dismiss();
        });
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx.get());
        recyclerView.setLayoutManager(layoutManager);
        fileAdapter = new FileAdapter(ctx.get());
        recyclerView.setAdapter(fileAdapter);
        recyclerView.getItemAnimator().setAddDuration(0);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mDialog = new AlertDialog.Builder(ctx.get())
                .setView(v)
                .create();
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    public void onPreExecute() {

    }

    @SuppressLint("WrongThread")
    @Override
    public Map<String, String> doInBackground(List<CurseforgeModpackManifest.Files>... args) {
        ctx.get().runOnUiThread(this::init);
        List<CurseforgeModpackManifest.Files> tasks = args[0];
        ctx.get().runOnUiThread(()->textInfo.setText("正在下载mod("+tasks.size()+"个)"));
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(maxTask);
        ExecutorService threadPool = new ThreadPoolExecutor(maxTask, maxTask,
                0, TimeUnit.SECONDS,
                workQueue,
                new ThreadPoolExecutor.DiscardPolicy());

        for (int j = 0; j < tasks.size(); j++) {
            CurseforgeModpackManifest.Files files = tasks.get(j);
            threadPool.execute(() -> {
                String url = "";
                String path = "";
                int tryTimes = 5;
                for (int i = 0; i < tryTimes; i++) {
                    if (isCancelled()) {
                        threadPool.shutdownNow();
                        return;
                    }
                    DownloadInfo info = new DownloadInfo();
                    try {
                        String str = api.getDownloadUrlAndName(files.getProjectID(), files.getFileID());
                        if (str == null) {
                            continue;
                        }
                        String[] urlAndName = str.split("澪");
                        url = urlAndName[0];
                        path = modsPath + urlAndName[1];
                        info.name = new File(path).getName();
                        info.url = url;
                        info.path = path;
                        ctx.get().runOnUiThread(() -> {
                            fileAdapter.addDownload(info);
                        });
                        DownloadUtils.downloadFileMonitored(url, path, getByteBuffer(), (curr, max) -> {
                                    long p = curr * 100 / max;
                                    if (p%10>8){
                                        info.progress = (int) p;
                                        ctx.get().runOnUiThread(() -> {
                                            fileAdapter.onProgress(info);
                                        });
                                    }
                                }
                        );
                        ctx.get().runOnUiThread(() -> {
                            fileAdapter.onComplete(info);
                        });
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        ctx.get().runOnUiThread(() -> {
                            fileAdapter.onComplete(info);
                        });
                        if (i == tryTimes - 1) {
                            failedFile.put(url, path);
                        }
                    }
                }
            });
            while (!workQueue.isEmpty()) {
                ;
            }
            int progress = (j + 1) * 100 / tasks.size();
            onProgressUpdate(progress);
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return failedFile;
    }

    @Override
    protected void onProgressUpdate(Integer... p1) {
        ctx.get().runOnUiThread(() -> {
            totalProgressBar.setProgress(p1[0]);
        });
    }


    @Override
    public void onPostExecute(Map<String, String> result) {
        mDialog.dismiss();
        callback.onFinished(result);
    }

    @Override
    protected void onCancelled(Map<String, String> result) {
        mDialog.dismiss();
        callback.onCancelled();
    }
    private byte[] getByteBuffer(){
        byte[] buffer = mThreadBuffers.get(Thread.currentThread());
        if (buffer == null){
            buffer = new byte[1024];
            mThreadBuffers.put(Thread.currentThread(), buffer);
        }
        return buffer;
    }

}