package com.example.oemers.bladeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder mHolder;
	private DrawThread mDrawThread;

	public MySurfaceView(Context context) {
		super(context);
		init();
	}
	public MySurfaceView(Context context, AttributeSet set) {
		super(context,set);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init(){
		setZOrderOnTop(true);
		mHolder = getHolder();
		mHolder.setFormat(PixelFormat.TRANSLUCENT);
		mHolder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mDrawThread = new DrawThread();
		Thread thread = new Thread(mDrawThread);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		mDrawThread.stop();
	}
	
	protected void myDraw(Canvas canvas){
		
	}
	
	private class DrawThread implements Runnable {

		private boolean mRun = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(mRun){
				Canvas canvas = mHolder.lockCanvas();
				myDraw(canvas);
				if (canvas != null) {
					mHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		public void stop(){
			mRun = false;
		}
	}
}
