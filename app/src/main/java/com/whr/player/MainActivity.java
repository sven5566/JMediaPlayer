package com.whr.player;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
	private MediaPlayer mMediaPlayer;
	private Surface surface;

	private ImageView videoImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TextureView textureView=(TextureView) findViewById(R.id.textureview);
		textureView.setSurfaceTextureListener(this);//设置监听函数  重写4个方法
		videoImage=(ImageView) findViewById(R.id.video_image);
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width,int height) {
		System.out.println("onSurfaceTextureAvailable");
		surface=new Surface(surfaceTexture);
		new PlayerVideo().start();//开启一个线程去播放视频
	}
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,int height) {
		System.out.println("onSurfaceTextureSizeChanged");
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
		surfaceTexture=null;
		surface=null;
		mMediaPlayer.stop();
		mMediaPlayer.release();
		return true;
	}
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//		System.out.println("onSurfaceTextureUpdated onSurfaceTextureUpdated");
	}

	private class PlayerVideo extends Thread{
		@Override
		public void run(){
			try {
				File file=new File(Environment.getExternalStorageDirectory()+"/tmp/Autonews.mp4");
				if(!file.exists()){//文件不存在
					 throw new IllegalStateException("初始化参数错误");
				}
				mMediaPlayer= new MediaPlayer();
				mMediaPlayer.setDataSource(file.getAbsolutePath());
				mMediaPlayer.setSurface(surface);
//				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp){
						videoImage.setVisibility(View.GONE);
						mMediaPlayer.start();
					}
				});
				mMediaPlayer.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
