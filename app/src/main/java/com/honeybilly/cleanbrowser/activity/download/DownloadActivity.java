package com.honeybilly.cleanbrowser.activity.download;

import android.os.Bundle;

import com.honeybilly.cleanbrowser.R;
import com.honeybilly.cleanbrowser.activity.BaseActivity;

import org.jetbrains.annotations.Nullable;

/**
 * Created by liqi on 15:18.
 */
public class DownloadActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        DownloadAdapter downloadAdapter = new DownloadAdapter();

    }
}
