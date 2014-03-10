package com.umeng.activity;

import java.util.ArrayList;
import java.util.List;

import com.adobe.fre.FREContext;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.soexample.socialize.SocializeConfigDemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 摇一摇功能原生版(若flash版出错 可以切换这个试试)
 * 由于我的测试机器有限,在您的机器出错在所难免.
 * @author Rect
 * @see rectvv@gmail.com
 * @see www.shadowkong.com
 * @see github.com/platformanes
 * @version 2014-3-10
 */
public class ANETencentActivity extends Activity implements OnClickListener{
	public static final String MYACTIVITY_ACTION = "com.umeng.activity.ANETencentActivity";
	private String TAG = "ANETencentActivity";
	private LinearLayout layout;
	public static FREContext _context = null;
	public static int YIYflag = 0;
	public static String YIYstr = null;
	// 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = null;
	// 摇一摇控制器
	private UMShakeService mShakeController = null;
	private  ImageView mImageView = null;
	private static Bitmap Images = null;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case 1:
			callBack("activity finish");
			ANETencentActivity.this.finish();
			break;
		}
	}

	public  static void setBitmap(Bitmap bmp)
	{
		Images = bmp;
	}
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//构建界面
		super.onCreate(savedInstanceState);

		layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		this.setContentView(layout);
		TextView textv = new TextView(this);
		String str_2 = "点击任意返回游戏....";
		textv.setText(str_2);
		layout.addView(textv);

		WindowManager.LayoutParams wl = getWindow().getAttributes();
		wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		wl.alpha=0.3f;  
		getWindow().setAttributes(wl);

		layout.setId(1);
		layout.setOnClickListener(this);

		mImageView = (ImageView) this.findViewById(_context.getResourceId("id.scrshot_imgview"));
		if(Images != null)
		{
			mImageView.setImageBitmap(Images);
			layout.addView(mImageView);
		}


//		if(mController == null)
//		{
//			mController = UMServiceFactory.getUMSocialService(
//					SocializeConfigDemo.DESCRIPTOR, RequestType.SOCIAL);
//			mShakeController = UMShakeServiceFactory
//			.getShakeService(SocializeConfigDemo.DESCRIPTOR);
//		}

		
	}
	public  void onSwitch(int v) {
		switch(v)
		{
		case 6:
			callBack("注册摇一摇打开分享面板 - step 1 ");
			mController.setShareContent(YIYstr);
			// 摇一摇打开分享面板
			callBack("注册摇一摇打开分享面板 - step 2");
			mShakeController.registerShakeToOpenShare(this, 2000, true);
			callBack("注册摇一摇打开分享面板  - end");
			break;
		case 7:
			registerShakeToShare();
			break;

		case 8:
			// 摇一摇截图

			mShakeController.registerShakeToScrShot(this,
					new UMAppAdapter(this), 1500, true,
					mScreenshotListener);
			callBack("注册摇一摇截图");
			break;
		}
	}
	/**
	 * 截图监听器，返回屏幕截图
	 */
	private OnScreenshotListener mScreenshotListener = new OnScreenshotListener() {

		@Override
		public void onComplete(Bitmap bmp) {
			if (bmp != null) {
				//				mImageView.setImageBitmap(bmp);
				mImageView.setImageBitmap(bmp);
				layout.addView(mImageView);
			}
		}
	};
	/**
	 * @Title: registerShakeToShare
	 * @Description:
	 * @throws
	 */
	private void registerShakeToShare() {
		/**
		 * 摇一摇截图,直接分享 参数1: 当前所属的Activity 参数2: 截图适配器 参数3: 要用户可选的平台,最多支持五个平台 参数4:
		 * 传感器监听器，包括摇一摇完成后的回调函数onActionComplete, 可在此执行类似于暂停游戏、视频等操作;
		 * 还有分享完成、取消的回调函数onOauthComplete、onShareCancel。
		 */
		UMAppAdapter appAdapter = new UMAppAdapter(this);
		// 配置平台
		List<SHARE_MEDIA> platforms = new ArrayList<SHARE_MEDIA>();
		platforms.add(SHARE_MEDIA.QZONE);
		platforms.add(SHARE_MEDIA.WEIXIN);
		//		platforms.add(SHARE_MEDIA.LAIWANG);
		platforms.add(SHARE_MEDIA.FACEBOOK);
		platforms.add(SHARE_MEDIA.YIXIN_CIRCLE);
		platforms.add(SHARE_MEDIA.YIXIN);
		platforms.add(SHARE_MEDIA.LAIWANG_DYNAMIC);
		// 通过摇一摇控制器来设置文本分享内容
		mShakeController.setShareContent(YIYstr);
		mShakeController.registerShakeListender(this, appAdapter,
				2000, true, platforms, mSensorListener);
	}

	/**
	 * 传感器监听器
	 */
	private OnSensorListener mSensorListener = new OnSensorListener() {

		@Override
		public void onStart() {

		}

		/**
		 * 分享完成后回调 (non-Javadoc)
		 * 
		 * @see com.umeng.socialize.controller.listener.SocializeListeners.DirectShareListener#onOauthComplete(java.lang.String,
		 *      com.umeng.socialize.bean.SHARE_MEDIA)
		 */
		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode,
				SocializeEntity entity) {
			callBack("分享完成");
		}

		/**
		 * (非 Javadoc)
		 * 
		 * @Title: onActionComplete
		 * @Description: 摇一摇动作完成后回调 (non-Javadoc)
		 * @param event
		 * @see com.umeng.socialize.sensor.UMSensor.OnSensorListener#onActionComplete(android.hardware.SensorEvent)
		 */
		@Override
		public void onActionComplete(SensorEvent event) {
			callBack("游戏暂停");
		}

		/**
		 * (非 Javadoc)
		 * 
		 * @Title: onButtonClick
		 * @Description: 用户点击分享窗口的取消和分享按钮触发的回调
		 * @param button
		 * @see com.umeng.socialize.sensor.UMSensor.OnSensorListener#onButtonClick(com.umeng.socialize.sensor.UMSensor.WhitchButton)
		 */
		@Override
		public void onButtonClick(WhitchButton button) {
			if (button == WhitchButton.BUTTON_CANCEL) {
				callBack("取消分享,游戏重新开始");
			} else {
				// 分享中
			}
		}
	};

	public void callBack(String status){
		Log.d(TAG, "umeng:"+status);
		_context.dispatchStatusEventAsync(TAG,status);
	}
}
