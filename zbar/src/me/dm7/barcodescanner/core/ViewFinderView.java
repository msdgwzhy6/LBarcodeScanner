package me.dm7.barcodescanner.core;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class ViewFinderView extends FrameLayout
{
	private static final String TAG = "ViewFinderView";

	private Rect mFramingRect;

	private static final int MIN_FRAME_WIDTH = 200;
	private static final int MIN_FRAME_HEIGHT = 200;

	private static final float LANDSCAPE_WIDTH_RATIO = 5f / 8;
	private static final float LANDSCAPE_HEIGHT_RATIO = 5f / 8;
	private static final int LANDSCAPE_MAX_FRAME_WIDTH = (int) (1920 * LANDSCAPE_WIDTH_RATIO); // =
																								// 5/8
																								// *
																								// 1920
	private static final int LANDSCAPE_MAX_FRAME_HEIGHT = (int) (1080 * LANDSCAPE_HEIGHT_RATIO); // =
																									// 5/8
																									// *
																									// 1080

	private static final float PORTRAIT_WIDTH_RATIO = 7f / 8;
	private static final float PORTRAIT_HEIGHT_RATIO = 3f / 8;
	private static final int PORTRAIT_MAX_FRAME_WIDTH = (int) (1080 * PORTRAIT_WIDTH_RATIO); // =
																								// 7/8
																								// *
																								// 1080
	private static final int PORTRAIT_MAX_FRAME_HEIGHT = (int) (1920 * PORTRAIT_HEIGHT_RATIO); // =
																								// 3/8
																								// *
																								// 1920

	public ViewFinderView(Context context)
	{
		super(context);
	}

	public ViewFinderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setupViewFinder()
	{
		updateFramingRect();
		invalidate();
	}

	public Rect getFramingRect()
	{
		return mFramingRect;
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		if (mFramingRect == null)
		{
			return;
		}
	}

	public void addView(View v)
	{
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(v, params);
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld)
	{
		updateFramingRect();
	}

	public synchronized void updateFramingRect()
	{
		Point viewResolution = new Point(getWidth(), getHeight());
		int width;
		int height;
		int orientation = DisplayUtils.getScreenOrientation(getContext());

		if (orientation != Configuration.ORIENTATION_PORTRAIT)
		{
			width = findDesiredDimensionInRange(LANDSCAPE_WIDTH_RATIO,
					viewResolution.x, MIN_FRAME_WIDTH,
					LANDSCAPE_MAX_FRAME_WIDTH);
			height = findDesiredDimensionInRange(LANDSCAPE_HEIGHT_RATIO,
					viewResolution.y, MIN_FRAME_HEIGHT,
					LANDSCAPE_MAX_FRAME_HEIGHT);
		} else
		{
			width = findDesiredDimensionInRange(PORTRAIT_WIDTH_RATIO,
					viewResolution.x, MIN_FRAME_WIDTH, PORTRAIT_MAX_FRAME_WIDTH);
			height = findDesiredDimensionInRange(PORTRAIT_HEIGHT_RATIO,
					viewResolution.y, MIN_FRAME_HEIGHT,
					PORTRAIT_MAX_FRAME_HEIGHT);
		}

		int leftOffset = (viewResolution.x - width) / 2;
		int topOffset = (viewResolution.y - height) / 2;
		mFramingRect = new Rect(leftOffset, topOffset, leftOffset + width,
				topOffset + height);
	}

	private static int findDesiredDimensionInRange(float ratio, int resolution,
			int hardMin, int hardMax)
	{
		int dim = (int) (ratio * resolution);
		if (dim < hardMin)
		{
			return hardMin;
		}
		if (dim > hardMax)
		{
			return hardMax;
		}
		return dim;
	}

}
