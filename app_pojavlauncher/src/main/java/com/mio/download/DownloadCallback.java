package com.mio.download;

import java.util.Map;

public abstract class DownloadCallback {
    public abstract void onFinished(Map<String, String> failedFile);

    public abstract void onCancelled();
}
