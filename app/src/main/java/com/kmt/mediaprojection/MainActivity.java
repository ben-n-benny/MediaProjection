package com.kmt.mediaprojection;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceView;

public class MainActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    Surface surface;
    public int screenDensity;
    MediaProjectionManager mediaProjectionManager;
    MediaProjection mediaProjection;
    VirtualDisplay virtualDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);
        surface = surfaceView.getHolder().getSurface();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.densityDpi;
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        activityResultLauncher.launch(mediaProjectionManager.createScreenCaptureIntent());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode != RESULT_OK){
                return;
            }
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode,data);
            virtualDisplay = mediaProjection.createVirtualDisplay("CaptureScreen",surfaceView.getWidth(),surfaceView.getHeight(),screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,surface,null,null);

        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

                if(result.getResultCode() != RESULT_OK){
                    return;
                }
                mediaProjection = mediaProjectionManager.getMediaProjection(result.getResultCode(),result.getData());
                virtualDisplay = mediaProjection.createVirtualDisplay("CaptureScreen",surfaceView.getWidth(),surfaceView.getHeight(),screenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,surface,null,null);
                

        }
    });


}