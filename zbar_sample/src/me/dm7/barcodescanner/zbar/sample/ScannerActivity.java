package me.dm7.barcodescanner.zbar.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScannerActivity extends Activity implements
		ZBarScannerView.ResultHandler
{
	private ZBarScannerView mScannerView;

	@Override
	public void onCreate(Bundle state)
	{
		super.onCreate(state);
		mScannerView = new ZBarScannerView(this);
		setContentView(mScannerView);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mScannerView.setResultHandler(this);
		mScannerView.startCamera(-1);
		mScannerView.setFlash(false);
		mScannerView.setAutoFocus(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	@Override
	public void handleResult(Result rawResult)
	{
		Toast.makeText(
				this,
				"Contents = " + rawResult.getContents() + ", Format = "
						+ rawResult.getBarcodeFormat().getName(),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mScannerView.stopCamera();
	}
}
