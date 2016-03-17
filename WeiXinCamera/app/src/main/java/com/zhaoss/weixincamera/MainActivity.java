package com.zhaoss.weixincamera;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    private SurfaceView sv_camera;
    private MyTextView tv_start;
    private LinearLayout ll_line;
    private String path =  Environment.getExternalStorageDirectory().getPath()+ File.separator+"WeiXinCamera";
    private SurfaceHolder holder;
    private MediaRecorder mediaRecorder;
    private int height;
    private int width;
    private Camera mCamera;
    private RecyclerView rv_video;
    private LinearLayout ll_video_list;
    private LinearLayout ll_video;
    private List<Bitmap> fileList;

    /** 是否正在录制 */
    private boolean isRecord;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initData();
    }

    private void initData() {

        fileList = new ArrayList<>();
        tv_start.setOnTouchListener(new MyTextView.OnTouchListener() {
            @Override
            public void onDownListener() {

            }
            @Override
            public void onLongListener() {
                isRecord = true;
                initMediaRecorder();
                startAnimation();
            }
            @Override
            public void onUpListener() {
                if (isRecord) {
                    isRecord = false;
                    release();
                    showVideoList();
                    Toast.makeText(MainActivity.this, "录制完成, 保存成功!", Toast.LENGTH_SHORT).show();
                    iteratesFileDir(new File(path));
                }
            }
        });
    }

    /**
     * 显示视频列表
     */
    private void showVideoList() {

        ll_video.setVisibility(View.GONE);
        ll_video_list.setVisibility(View.VISIBLE);
        fileList.clear();
        iteratesFileDir(new File(path));
        rv_video.setAdapter(new VideoListAdapter(this, fileList));
    }

    /**
     * 迭代文件夹里所有文件
     */
    public void iteratesFileDir(File file){

        if(file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for(File f: files){
                iteratesFileDir(f);
            }
        }else if(file.exists()){
            //得到视频的缩略图
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
            fileList.add(bitmap);
        }
    }

    /**
     * 释放照相机和录制对象
     */
    private void release(){
        try{
            if(file.length() == 10){
                file.delete();
            }
            mediaRecorder.release();
            mCamera.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 开始执行录制动画
     */
    private void startAnimation() {

        ValueAnimator va = ObjectAnimator.ofInt(width, 0);
        va.setDuration(7000);
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isRecord) {
                    isRecord = false;
                    release();
                    showVideoList();
                    Toast.makeText(MainActivity.this, "录制完成, 保存成功!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ViewGroup.LayoutParams params = ll_line.getLayoutParams();
                params.width = value;
                params.height = height;
                ll_line.setLayoutParams(params);
                ll_line.requestLayout();
            }
        });
        va.start();
    }

    private void initMediaRecorder() {

        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.lock();
        holder = sv_camera.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCamera.setDisplayOrientation(90);

        mCamera.stopPreview();
        mediaRecorder = new MediaRecorder();
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        // 设置录制视频源为Camera（相机）
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOrientationHint(90);
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置录制的视频编码h263 h264
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        mediaRecorder.setVideoFrameRate(4);
        file = createCameraFile();
        mediaRecorder.setOutputFile(file.getPath());
        mediaRecorder.setPreviewDisplay(holder.getSurface());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        height = ll_line.getMeasuredHeight();
        width = ll_line.getMeasuredWidth();
    }

    /**
     * 根据当前时间毫秒值创建File对象
     */
    private File createCameraFile(){

        File directoryFile = new File(path);
        if(!directoryFile.exists() && !directoryFile.isDirectory()){
            directoryFile.mkdirs();
        }
        File file = new File(path + File.separator + "WeiXinCamera_" + new Date().getTime()+".mp4");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private void initUI() {

        RelativeLayout rl_listView = (RelativeLayout) findViewById(R.id.rl_listView);
        sv_camera = (SurfaceView) findViewById(R.id.sv_camera);
        tv_start = (MyTextView) findViewById(R.id.tv_start);
        ll_line = (LinearLayout) findViewById(R.id.ll_line);
        ll_video_list = (LinearLayout) findViewById(R.id.ll_video_list);
        rv_video = (RecyclerView) findViewById(R.id.rv_video);
        ll_video = (LinearLayout) findViewById(R.id.ll_video);

        rl_listView.setOnClickListener(this);

        rv_video.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_listView:
                showVideoList();
                break;
        }
    }
}