package com.umeng.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.SensorEvent;
import android.util.Log;
import android.widget.Toast;

import com.adobe.fre.FREContext;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.scrshot.UMScrShotController.OnScreenshotListener;
import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMFacebookHandler;
import com.umeng.socialize.controller.UMInstagramHandler;
import com.umeng.socialize.controller.UMLWHandler;
import com.umeng.socialize.controller.UMLWService;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.controller.UMYXHandler;
import com.umeng.socialize.controller.UMFacebookHandler.PostType;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.CircleShareContent;
import com.umeng.socialize.media.FaceBookShareContent;
import com.umeng.socialize.media.InstagramShareContent;
import com.umeng.socialize.media.LWDynamicShareContent;
import com.umeng.socialize.media.LWShareContent;
import com.umeng.socialize.media.MailShareContent;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWebPage;
import com.umeng.socialize.media.UMusic;
import com.umeng.socialize.media.WeiXinShareContent;
import com.umeng.socialize.media.YiXinCircleShareContent;
import com.umeng.socialize.media.YiXinShareContent;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.soexample.socialize.SocializeConfigDemo;

/**
 * 友盟社会化分享 微信/QQ模块  
 * 机器:Thl android2.2(不同版本的机器显示效果不尽相同)
 * @author Rect
 * @see rectvv@gmail.com
 * @see www.shadowkong.com
 * @see github.com/platformanes
 * @version 2014-3-8
 */
public class ANETencentBridge {
	private  String TAG = "ANETencentBridge";
	public static FREContext _context = null;
	private  int shareFlag = 0;
	private  String[] shareArr = null;
	private static ANETencentBridge instance = null;

	// 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = null;
	// 摇一摇控制器
	private UMShakeService mShakeController = null;
	//facebook callback handle
	private UMFacebookHandler mFacebookHandler = null;
	/**
	 * just for weixin callback.
	 */
	private IWXAPI api;
	
	public static ANETencentBridge getInstance()
	{
		if(instance == null)
			instance = new ANETencentBridge();
		return instance;
	}

	private  void initConfig()
	{
		callBack("init config");
		if(mController == null)
		{
			mController = UMServiceFactory.getUMSocialService(
					SocializeConfigDemo.DESCRIPTOR, RequestType.SOCIAL);
			mShakeController = UMShakeServiceFactory
			.getShakeService(SocializeConfigDemo.DESCRIPTOR);
		}
		callBack("for weixin call back");
		if (api != null) {// for weixin call back
			api = WXAPIFactory.createWXAPI(_context.getActivity(), "wx92e468cd3f047396");
			
			Intent intent = _context.getActivity().getIntent();
			api.handleIntent(intent, new IWXAPIEventHandler() {
				@Override
				public void onResp(BaseResp arg0) {
					SendAuth.Resp resp = (SendAuth.Resp) arg0;
					System.out.println(resp.userName);
					callBack(resp.userName);
				}

				@Override
				public void onReq(BaseReq arg0) {}
			});
		}
		callBack("test array");
		if(shareArr.length < 27)
		{
			callBack("argv is error,the array.len must be 27");
			return;
		}
		callBack("配置SSO step 1 ");
		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		callBack("配置SSO step 2");
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		callBack("配置SSO step 3");
//		mController.getConfig().supportQQPlatform(_context.getActivity(), shareArr[0],shareArr[1]);
		callBack("配置SSO step 4");
		mController.getConfig().setSsoHandler(new QZoneSsoHandler(_context.getActivity()));
		callBack("配置SSO step 5");
		mController.setShareContent(shareArr[2]);
		// 设置微信分享内容
		callBack("设置微信分享内容");
//		UMImage localImage = new UMImage(_context.getActivity(), _context.getResourceId("drawable.device"));
//		WeiXinShareContent weixinContent = new WeiXinShareContent(localImage);
//		weixinContent.setShareContent(shareArr[3]);
//		weixinContent.setTitle(shareArr[4]);
//		mController.setShareMedia(weixinContent);
		// 设置朋友圈分享的内容
		callBack("设置朋友圈分享的内容");
		CircleShareContent circleMedia = new CircleShareContent(new UMImage(
				_context.getActivity(),shareArr[5]));
		circleMedia.setShareContent(shareArr[6]);
		circleMedia.setTitle(shareArr[7]);
		mController.setShareMedia(circleMedia);
		// 设置新浪分享内容
		callBack("设置新浪分享内容");
		mController.setShareMedia(new SinaShareContent(new UMImage(
				_context.getActivity(),shareArr[8])));
		// 设置renren分享内容
		callBack("设置renren分享内容");
		RenrenShareContent renrenShareContent = new RenrenShareContent();
//		UMImage image = new UMImage(_context.getActivity(), _context.getResourceId("drawable.device"));
//		image.setTitle(shareArr[9]);
//		image.setThumb(shareArr[10]);
//		renrenShareContent.setShareImage(image);
		renrenShareContent.setShareContent(shareArr[2]);
		renrenShareContent.setAppWebSite(shareArr[11]);
		mController.setShareMedia(renrenShareContent);
		// 设置QQ空间分享内容
		callBack("设置QQ空间分享内容");
		UMImage qzoneImage = new UMImage(_context.getActivity(),shareArr[12]);
		qzoneImage.setTargetUrl(shareArr[13]);
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(shareArr[14]);
		qzone.setTargetUrl(shareArr[15]);
		qzone.setTitle(shareArr[16]);
		qzone.setShareImage(qzoneImage);
		qzone.setAppWebSite(shareArr[17]);
		mController.setShareMedia(qzone);
		//QQ
		callBack("QQ");
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(shareArr[18]);
		qqShareContent.setTitle(shareArr[19]);
		qqShareContent
		.setShareImage(new UMImage(_context.getActivity(), _context.getResourceId("drawable.icon")));
		qqShareContent.setTargetUrl(shareArr[20]);
		mController.setShareMedia(qqShareContent);
		// 设置tencent weibo分享内容
		callBack("设置tencent weibo分享内容");
		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent(shareArr[21]);
		mController.setShareMedia(tencent);
		// 设置tencent mail分享内容， 如果需要分享图片则只支持本地图片
		callBack("设置tencent mail分享内容， 如果需要分享图片则只支持本地图片");
		MailShareContent mail = new MailShareContent("mail share");
		mail.setTitle(shareArr[22]);
		mail.setShareContent(shareArr[23]);
		mController.setShareMedia(mail);
		// 设置短信分享内容
		callBack("设置短信分享内容");
//		UMImage urlImage = new UMImage(_context.getActivity(),shareArr[24]);
		SmsShareContent sms = new SmsShareContent();
//		sms.setShareImage(new UMImage(_context.getActivity(), _context.getResourceId("drawable.device")));
		sms.setShareContent(shareArr[25]);
//		sms.setShareImage(urlImage);
		mController.setShareMedia(sms);
		// test end 
		callBack("test end");
		mController.setShareMedia(new UMImage(_context.getActivity(),shareArr[26]));
		mController.getConfig().closeSinaSSo();
		callBack("ENF");
	}
	public  void onSwitch(int v,String[] arr) {
		shareFlag = v;
		shareArr = arr;
		
		
		switch(shareFlag)
		{
		//if shareFlag == 100 else go to init the share Arge 
		//you can change that,BY Rect
		case 100:
			callBack("C");
			initConfig();
			break;

		case 0:
			// 添加微信支持, 并且打开平台选择面板
			addWXPlatform();
			mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE,
					SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.TENCENT);
			mController.setShareContent(shareArr[0]);
			break;
		case 1:
			// 添加QQ平台,并且打开平台选择面板
			addQQPlatform();
			mController.setShareContent(shareArr[0]);
			mController.openShare(_context.getActivity(), false);
			break;
		case 2:
			// 添加易信平台
			addYXPlatform();
			break;
		case 3:
			//add to facebook
			addFacebook();
			break;
		case 4:
			// 添加来往平台的支持
			addLaiWang();
			break;
		case 5:
			// 添加Instagram平台的支持
			addInstagram();
			break;
			
		//--------------------------------
			
		case 6:
//			ANETencentActivity.YIYflag = 6;
//			ANETencentActivity.YIYstr = shareArr[0];
//			_context.getActivity().startActivityForResult(intent, 0);
			
			callBack("注册摇一摇打开分享面板 - step 1 ");
			mController.setShareContent(shareArr[0]);
			// 摇一摇打开分享面板
			callBack("注册摇一摇打开分享面板 - step 2");
			mShakeController.registerShakeToOpenShare(_context.getActivity(), 2000, true);
			callBack("注册摇一摇打开分享面板  - end");
			break;
		case 7:
			// 注册摇一摇截图分享
//			ANETencentActivity.YIYflag = 7;
//			ANETencentActivity.YIYstr = shareArr[1];
//			_context.getActivity().startActivityForResult(intent, 0);
			
			registerShakeToShare();
			Toast.makeText(_context.getActivity(), shareArr[0], Toast.LENGTH_SHORT).show();
			break;
		case 8:
			// 摇一摇截图
//			ANETencentActivity.YIYflag = 8;
//			_context.getActivity().startActivityForResult(intent, 0);
			
			mShakeController.registerShakeToScrShot(_context.getActivity(),
					new UMAppAdapter(_context.getActivity()), 1500, true,
					mScreenshotListener);
			callBack("注册摇一摇截图");
			break;
		case 9:
			callBack("微信视频截图,请开发者自己实现.参照友盟官方DEMO  by Rect");
			break;
		default:
			callBack("argv is error");
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
				ANETencentActivity._context = _context;
				ANETencentActivity.setBitmap(bmp);
				Intent intent = new Intent(ANETencentActivity.MYACTIVITY_ACTION);
				_context.getActivity().startActivityForResult(intent, 0);
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
		UMAppAdapter appAdapter = new UMAppAdapter(_context.getActivity());
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
		mShakeController.setShareContent(shareArr[1]);
		mShakeController.registerShakeListender(_context.getActivity(), appAdapter,
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

	
	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQPlatform() {
		// 添加QQ支持, 并且设置QQ分享内容的target url
		mController.getConfig().supportQQPlatform(_context.getActivity(), false,shareArr[1]);
		// // 图片分享
		UMImage mImage = new UMImage(_context.getActivity(), shareArr[2]);
		mImage.setTitle(shareArr[3]);
		// 音乐分享
		UMusic uMusic = new UMusic(shareArr[4]);
		uMusic.setAuthor(shareArr[5]);
		uMusic.setTitle(shareArr[6]);
		uMusic.setThumb(shareArr[7]);
		//
		// 视频分享
		UMVideo umVedio = new UMVideo(shareArr[8]);
		umVedio.setThumb(shareArr[9]);
		umVedio.setTitle(shareArr[10]);

		// 要分享的文字内容
		mController.setShareContent(shareArr[11]);
		// 设置多媒体内容
		mController.setShareMedia(mImage);
	}
	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = shareArr[1];
		// 微信图文分享,音乐必须设置一个url
		String contentUrl = shareArr[2];
		// 添加微信平台
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(
				_context.getActivity(), appId, contentUrl);
		wxHandler.setWXTitle(shareArr[3]);

		UMImage mUMImgBitmap = new UMImage(_context.getActivity(),shareArr[4]);

		UMusic uMusic = new UMusic(shareArr[5]);
		uMusic.setAuthor(shareArr[6]);
		uMusic.setTitle(shareArr[7]);
		// uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// 非url类型的缩略图需要传递一个UMImage的对象
		uMusic.setThumb(mUMImgBitmap);
		//
		// 视频分享
		UMVideo umVedio = new UMVideo(shareArr[8]);
		umVedio.setTitle(shareArr[9]);
		// umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		umVedio.setThumb(mUMImgBitmap);
		// 设置分享文字内容
		mController
		.setShareContent(shareArr[10]);
		// mController.setShareContent(null);
		// 设置分享图片
		// mController.setShareMedia(mUMImgBitmap);
		// 支持微信朋友圈
		UMWXHandler circleHandler = mController.getConfig()
		.supportWXCirclePlatform(_context.getActivity(), appId, contentUrl);
		circleHandler.setCircleTitle(shareArr[11]);

		//
		mController.getConfig().registerListener(new SnsPostListener() {

			@Override
			public void onStart() {
				callBack("weixin -- xxxx onStart");
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				callBack(platform + " code = " + eCode);
			}
		});

		mController.openShare(_context.getActivity(), false);


	}

	/**
	 * @Title: addYXPlatform
	 * @Description:
	 * @throws
	 */
	private void addYXPlatform() {
		callBack("yi xin begin");
		// 图片
//		UMImage mUMImgBitmap = new UMImage(_context.getActivity(), _context.getResourceId("drawable.device"));

		// 音乐分享
		UMusic uMusic = new UMusic(shareArr[0]);
		uMusic.setTargetUrl(shareArr[1]);
		uMusic.setAuthor(shareArr[2]);
//		uMusic.setThumb(new UMImage(_context.getActivity(), _context.getResourceId("drawable.test")));
		uMusic.setTitle(shareArr[3]);
		
		// 视频分享
		callBack("yi xin video");
		UMVideo umVedio = new UMVideo(shareArr[4]);
		umVedio.setTitle(shareArr[5]);

		// 网页类型
		callBack("yi xin web share");
		UMWebPage webPage = new UMWebPage(shareArr[6]);
		webPage.setTitle(shareArr[7]);
		webPage.setDescription(shareArr[8]);
		webPage.setThumb(new UMImage(_context.getActivity(), shareArr[9]));

		// 设置易信平台分享内容
		callBack("yi xin set share platform");
		YiXinShareContent yixin = new YiXinShareContent();

		yixin.setTitle(shareArr[10]);
		// 设置targetUrl, 图文分享时有效
		yixin.setTargetUrl(shareArr[11]);
		// yixin.setShareContent("易信平台图文分享");
//		yixin.setShareMedia(mUMImgBitmap);
		// 设置易信平台的分享内容
		mController.setShareMedia(yixin);
		callBack("yi xin friend");
		// 设置易信朋友圈分享内容
		YiXinCircleShareContent yixinCircle = new YiXinCircleShareContent();
		yixinCircle.setTitle(shareArr[12]);
		// 设置targetUrl, 图文分享时有效
		yixinCircle.setTargetUrl(shareArr[13]);
		 yixinCircle.setShareContent("易信平台图文分享");
//		yixinCircle.setShareMedia(mUMImgBitmap);
		// yixinCircle.setShareMedia(webPage);
		// 设置易信朋友圈平台的分享内容
		mController.setShareMedia(yixinCircle);

		// 设置文本分享内容
		mController.setShareContent(shareArr[14]);

		// 添加易信平台
		UMYXHandler yixinHandler = new UMYXHandler(_context.getActivity(),
				shareArr[15]);
		// 关闭分享时的等待Dialog
		yixinHandler.enableLoadingDialog(false);
		// 设置target Url, 必须以http或者https开头
		yixinHandler.setTargetUrl(shareArr[16]);
		yixinHandler.addToSocialSDK();

		// 易信朋友圈平台
		UMYXHandler yxCircleHandler = new UMYXHandler(_context.getActivity(),
				shareArr[17], true);
		yxCircleHandler.addToSocialSDK();

		mController.getConfig().registerListener(new SnsPostListener() {

			@Override
			public void onStart() {
				callBack( "yixin onStart");
			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				callBack(platform + "onComplete   ---- ." + eCode);
			}
		});

		// 打开分享面板
		mController.openShare(_context.getActivity(), false);

	}

	/**
	 * @Title: addFacebook
	 * @Description:
	 * @throws
	 */
	private void addFacebook() {

		// // 图片分享
		UMImage mUMImgBitmap = new UMImage(_context.getActivity(), new File(
				shareArr[0]));
		mFacebookHandler = new UMFacebookHandler(_context.getActivity(), PostType.FEED);
		mFacebookHandler.addToSocialSDK();
		FaceBookShareContent fbContent = new FaceBookShareContent(
				shareArr[1]);
		fbContent.setShareImage(mUMImgBitmap);
		fbContent.setShareContent(shareArr[2]
		                                   + new Date().toString());
		fbContent.setTitle(shareArr[3]);
		fbContent.setCaption(shareArr[4]);
		fbContent.setDescription(shareArr[5]);
		fbContent.setTargetUrl(shareArr[6]);
		mController.setShareMedia(fbContent);
		mController.setShareContent(shareArr[7]);
		mController.setShareMedia(mUMImgBitmap);
		mFacebookHandler.logOut();
		mController.openShare(_context.getActivity(), false);
	}

	/**
	 * 添加来往和来往动态平台</br>
	 */
	private void addLaiWang() {

//		final UMImage laiwangImg = new UMImage(_context.getActivity(),
//				_context.getResourceId("drawable.device"));

		// 来往分享内容
		LWShareContent lwShareContent = new LWShareContent();
//		lwShareContent.setShareImage(laiwangImg);
		lwShareContent.setTitle(shareArr[0]);
		lwShareContent.setMessageFrom(shareArr[1]);
		lwShareContent.setShareContent(shareArr[2]);
		mController.setShareMedia(lwShareContent);

		// 来往动态分享内容
		LWDynamicShareContent lwDynamicShareContent = new LWDynamicShareContent();
//		lwDynamicShareContent.setShareImage(new UMImage(_context.getActivity(),
//				_context.getResourceId("drawable.device")));
		lwDynamicShareContent.setTitle(shareArr[3]);
		lwDynamicShareContent.setMessageFrom(shareArr[4]);
		lwDynamicShareContent
		.setShareContent(shareArr[5]);
		mController.setShareMedia(lwDynamicShareContent);

		// 点击消息跳转的url
		String targetUrl = shareArr[6];
		// 添加来往的支持
		UMLWHandler umlwDynamicHandler = UMLWService.supportLWPlatform(
				_context.getActivity(), "", "", targetUrl);
		umlwDynamicHandler.setTitle(shareArr[7]);
		umlwDynamicHandler.setMessageFrom(shareArr[8]);

		// 添加来往动态的支持
		UMLWHandler umlwHandler = UMLWService.supportLWDynamicPlatform(
				_context.getActivity(), shareArr[9],
				shareArr[10], targetUrl);
		umlwHandler.setTitle(shareArr[11]);
		// 设置消息来源
		umlwHandler.setMessageFrom(shareArr[12]);

		mController.openShare(_context.getActivity(), false);
	}

	/**
	 * </br> Instagram只支持图片分享, 只支持纯图片分享.</br>
	 */
	private void addInstagram() {
		// 构建Instagram的Handler
		UMInstagramHandler instagramHandler = new UMInstagramHandler(
				_context.getActivity());
		instagramHandler.addToSocialSDK();

//		UMImage localImage = new UMImage(_context.getActivity(),_context.getResourceId("drawable.device"));

		// // 添加分享到Instagram的内容
//		InstagramShareContent instagramShareContent = new InstagramShareContent(
//				localImage);
//		mController.setShareMedia(instagramShareContent);
		mController.setShareContent("share to instagram");
		// 打开分享面板
		mController.openShare(_context.getActivity(), false);

	}
	
	private  void callBack(String status){
		Log.d(TAG,status);
		_context.dispatchStatusEventAsync(TAG,status);
	}

}
