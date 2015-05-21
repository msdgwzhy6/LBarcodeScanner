package barcodescanner.zbar.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 扫描动画视图
 * 
 * @author LynnChurch
 * @version 创建时间:2015年5月21日 下午3:58:19
 * 
 */
public class MaskView extends SurfaceView implements SurfaceHolder.Callback
{
	private DrawThread mDrawThread;
	private int mColor;

	public MaskView(Context context)
	{
		this(context, null);
	}

	public MaskView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);
		holder.addCallback(this);
		mColor = Color.rgb(255, 91, 59);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		mDrawThread = new DrawThread(holder);
		mDrawThread.startDraw();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		mDrawThread.stopDraw();
	}

	/**
	 * 绘图线程
	 * 
	 * @author LynnChurch
	 * 
	 */
	class DrawThread extends Thread
	{
		private SurfaceHolder mHolder;
		private boolean mIsRun;
		private int mY;

		public DrawThread(SurfaceHolder holder)
		{
			// TODO Auto-generated constructor stub
			mHolder = holder;
		}

		@Override
		public void run()
		{
			while (mIsRun)
			{
				Canvas canvas = null;
				try
				{
					canvas = mHolder.lockCanvas(null);
					// 清空画布
					canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
					int width = canvas.getWidth();
					int height = canvas.getHeight();
					Paint paint = new Paint();
					paint.setColor(mColor);
					// 扫描线高度
					int rectHeight = 3;
					// 绘制扫描线
					canvas.drawRect(0, mY, width, mY + rectHeight, paint);
					mY += 2;
					int maxY = height - rectHeight;
					if (maxY == mY || maxY == mY + 1)
					{
						mY = 0;
					}
					drawCorners(canvas);
				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					if (null != canvas)
					{
						mHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		public synchronized void startDraw()
		{
			// TODO Auto-generated method stub
			mIsRun = true;
			super.start();
		}

		public void stopDraw()
		{
			mIsRun = false;
		}

		/**
		 * 绘制四个角
		 * 
		 * @param canvas
		 */
		public void drawCorners(Canvas canvas)
		{
			int width = canvas.getWidth();
			int height = canvas.getHeight();
			Paint paint = new Paint();
			paint.setColor(Color.GREEN);
			// 角的粗细
			int cornerSize = 5;
			// 角边的长度
			int cornerLength = 35;
			canvas.drawRect(0, 0, cornerLength, cornerSize, paint);
			canvas.drawRect(0, 0, cornerSize, cornerLength, paint);
			canvas.drawRect(width - cornerLength, 0, width, cornerSize, paint);
			canvas.drawRect(width - cornerSize, 0, width, cornerLength, paint);
			canvas.drawRect(0, height - cornerLength, cornerSize, height, paint);
			canvas.drawRect(0, height - cornerSize, cornerLength, height, paint);
			canvas.drawRect(width - cornerSize, height - cornerLength, width,
					height, paint);
			canvas.drawRect(width - cornerLength, height - cornerSize, width,
					height, paint);
		}
	}
}
