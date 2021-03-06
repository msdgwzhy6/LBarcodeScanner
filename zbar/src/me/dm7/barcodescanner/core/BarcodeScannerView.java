package me.dm7.barcodescanner.core;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class BarcodeScannerView extends FrameLayout implements
		Camera.PreviewCallback
{
	private Camera mCamera;
	private CameraPreview mPreview;
	private static ViewFinderView mViewFinderView;
	private Rect mFramingRectInPreview;

	public static void init(ViewFinderView finderView)
	{
		mViewFinderView = finderView;
	}

	public BarcodeScannerView(Context context)
	{
		super(context);
		setupLayout();
	}

	public BarcodeScannerView(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		setupLayout();
	}

	public void setupLayout()
	{
		mPreview = new CameraPreview(getContext());
		addView(mPreview);

	}

	public void startCamera(int cameraId)
	{
		if (null == mViewFinderView)
		{
			mViewFinderView = new ViewFinderView(getContext());
		}
		addView(mViewFinderView);
		Camera camera = CameraUtils.getCameraInstance(cameraId);
		if (null != camera)
		{
			startCamera(camera);
		} else
		{
			mViewFinderView = null;
		}
	}

	public void startCamera(Camera camera)
	{
		mCamera = camera;
		if (mCamera != null)
		{
			mViewFinderView.setupViewFinder();
			mPreview.setCamera(mCamera, this);
			mPreview.initCameraPreview();
			mViewFinderView = null;
		}
	}

	public void startCamera()
	{
		startCamera(CameraUtils.getCameraInstance());
	}

	public void stopCamera()
	{
		if (mCamera != null)
		{
			mPreview.stopCameraPreview();
			mPreview.setCamera(null, null);
			mCamera.release();
			mCamera = null;
			mViewFinderView = null;
		}

	}

	public synchronized Rect getFramingRectInPreview(int previewWidth,
			int previewHeight)
	{
		if (mFramingRectInPreview == null)
		{
			Rect framingRect = mViewFinderView.getFramingRect();
			int viewFinderViewWidth = mViewFinderView.getWidth();
			int viewFinderViewHeight = mViewFinderView.getHeight();
			if (framingRect == null || viewFinderViewWidth == 0
					|| viewFinderViewHeight == 0)
			{
				return null;
			}

			Rect rect = new Rect(framingRect);
			rect.left = rect.left * previewWidth / viewFinderViewWidth;
			rect.right = rect.right * previewWidth / viewFinderViewWidth;
			rect.top = rect.top * previewHeight / viewFinderViewHeight;
			rect.bottom = rect.bottom * previewHeight / viewFinderViewHeight;

			mFramingRectInPreview = rect;
		}
		return mFramingRectInPreview;
	}

	public void setFlash(boolean flag)
	{
		if (mCamera != null && CameraUtils.isFlashSupported(mCamera))
		{

			Camera.Parameters parameters = mCamera.getParameters();
			if (flag)
			{
				if (parameters.getFlashMode().equals(
						Camera.Parameters.FLASH_MODE_TORCH))
				{
					return;
				}
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			} else
			{
				if (parameters.getFlashMode().equals(
						Camera.Parameters.FLASH_MODE_OFF))
				{
					return;
				}
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}
			mCamera.setParameters(parameters);
		}
	}

	public boolean getFlash()
	{
		if (mCamera != null && CameraUtils.isFlashSupported(mCamera))
		{
			Camera.Parameters parameters = mCamera.getParameters();
			if (parameters.getFlashMode().equals(
					Camera.Parameters.FLASH_MODE_TORCH))
			{
				return true;
			} else
			{
				return false;
			}
		}
		return false;
	}

	public void toggleFlash()
	{
		if (mCamera != null && CameraUtils.isFlashSupported(mCamera))
		{
			Camera.Parameters parameters = mCamera.getParameters();
			if (parameters.getFlashMode().equals(
					Camera.Parameters.FLASH_MODE_TORCH))
			{
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			} else
			{
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			}
			mCamera.setParameters(parameters);
		}
	}

	public void setAutoFocus(boolean state)
	{
		if (mPreview != null)
		{
			mPreview.setAutoFocus(state);
		}
	}
}
