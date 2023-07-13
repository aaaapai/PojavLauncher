package com.mio.download;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.mio.adapter.FileAdapter;

import net.kdt.pojavlaunch.R;
import net.kdt.pojavlaunch.utils.DownloadUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MioDownloadTask implements Runnable {

    private final Activity activity;
    private final Map<String, String> filesMap;
    private final DownloadCallback callback;
    private boolean isCancelled = false;
    private final int maxTask = 10;
    private FileAdapter fileAdapter;
    private TextView textInfo;
    public NumberProgressBar totalProgressBar;
    private AlertDialog mDialog;
    private final ConcurrentHashMap<Thread, byte[]> mThreadBuffers = new ConcurrentHashMap<>(maxTask);
    
    public MioDownloadTask(Activity activity, DownloadCallback callback) {
        this.activity = activity;
        this.callback = callback;
        filesMap = new HashMap<>();
    }

    public void addDonloadFile(String path, String url) {
        filesMap.put(path, url);
    }

    public void addDonloadFiles(Map<String, String> files) {
        filesMap.putAll(files);
    }

    private void init() {
        View v = LayoutInflater.from(activity).inflate(R.layout.dialog_mio_plus_download_modpack, null);
        totalProgressBar = v.findViewById(R.id.alert_downloadProgressBarTotalProgress);
        textInfo = v.findViewById(R.id.textInfo);
        Button cancleButton = v.findViewById(R.id.cancle_button);
        cancleButton.setOnClickListener(view -> {
            isCancelled = true;
            mDialog.dismiss();
        });
        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        fileAdapter = new FileAdapter(activity);
        recyclerView.setAdapter(fileAdapter);
        Objects.requireNonNull(recyclerView.getItemAnimator()).setAddDuration(0);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mDialog = new AlertDialog.Builder(activity)
                .setView(v)
                .create();
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    @Override
    public void run() {
        activity.runOnUiThread(this::init);
        activity.runOnUiThread(() -> textInfo.setText("文件(" + filesMap.size() + "个)"));
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(maxTask);
        ExecutorService threadPool = new ThreadPoolExecutor(maxTask, maxTask,
                0, TimeUnit.SECONDS,
                workQueue,
                new ThreadPoolExecutor.DiscardPolicy());
        Set<String> paths = filesMap.keySet();
        int j = 0;
        for (String path : paths) {
            threadPool.execute(() -> {
                String url = filesMap.get(path);
                int tryTimes = 5;
                for (int i = 0; i < tryTimes; i++) {
                    if (isCancelled) {
                        threadPool.shutdownNow();
                        return;
                    }
                    DownloadInfo info = new DownloadInfo();
                    try {

                        info.name = new File(path).getName();
                        info.url = url;
                        info.path = path;
                        activity.runOnUiThread(() -> fileAdapter.addDownload(info));
                        DownloadUtils.downloadFileMonitored(url, path, getByteBuffer(), (curr, max) -> {
                                    long p = curr * 100 / max;
                                    if (p % 10 > 8) {
                                        info.progress = (int) p;
                                        activity.runOnUiThread(() -> {
                                            fileAdapter.onProgress(info);
                                        });
                                    }
                                }
                        );
                        activity.runOnUiThread(() -> fileAdapter.onComplete(info));
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> fileAdapter.onComplete(info));
                        if (i == tryTimes - 1) {
//                            failedFile.put(url, path);
                        }
                    }
                }
            });
            while (!workQueue.isEmpty()) {
            }
            j++;
            int progress = (j + 1) * 100 / filesMap.size();
            activity.runOnUiThread(() -> totalProgressBar.setProgress(progress));
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        activity.runOnUiThread(() -> callback.onFinished(null));
    }

    private byte[] getByteBuffer() {
        byte[] buffer = mThreadBuffers.get(Thread.currentThread());
        if (buffer == null) {
            buffer = new byte[1024];
            mThreadBuffers.put(Thread.currentThread(), buffer);
        }
        return buffer;
    }
}
