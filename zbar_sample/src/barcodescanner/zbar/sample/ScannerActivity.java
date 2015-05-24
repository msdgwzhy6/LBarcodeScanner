package barcodescanner.zbar.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import me.dm7.barcodescanner.core.ViewFinderView;
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
		ViewFinderView finderView = new ViewFinderView(this);
		// 自定义扫描器View
		View view = LayoutInflater.from(this).inflate(
				R.layout.scanner_activity, null);
		// 添加自定义的扫描View
		finderView.addView(view);
		// 此处要先调用init()对自定义扫描器的View进行初始化再创建ZBarScannerView对象
		ZBarScannerView.init(finderView);
		mScannerView = new ZBarScannerView(this);
		setContentView(mScannerView);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// 相机相关参数的初始化设置
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
		// 此处可自己按项目需求对扫描结果rawResult进行处理
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
		// activity失去焦点停止相机
		mScannerView.stopCamera();
	}
}
