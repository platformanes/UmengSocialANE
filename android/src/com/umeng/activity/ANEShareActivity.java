package com.umeng.activity;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;

import java.util.Map;
import java.util.Set;

import com.adobe.fre.FREContext;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeConfig;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMInfoAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 友盟社会化  直接分享模块  
 * 机器:Thl android2.2(不同版本的机器显示效果不尽相同)
 * @author Rect
 * @see rectvv@gmail.com
 * @see www.shadowkong.com
 * @see github.com/platformanes
 * @version 2014-3-4
 */
public class ANEShareActivity extends Activity implements OnClickListener{
	public static final String MYACTIVITY_ACTION = "com.umeng.activity.ANEShareActivity";
	private String TAG = "ANEShareActivity";
	private LinearLayout layout;

	private UMSocialService controller;
	private Context mContext = null;
	// sdk controller
	private UMSocialService mController = null;
	 private final SHARE_MEDIA mTestMedia = SHARE_MEDIA.SINA;
	 
	public static FREContext _context = null;
	public static int shareFlag = 0;
	public static String[] shareArr = null;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case 1:
			callBack("activity finish");
			ANEShareActivity.this.finish();
			break;
		}
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
		
		initConfig();
	}

	/**
     * @功能描述 : 初始化与SDK相关的成员变量
     */
    private void initConfig() {
    	//umeng data init
		controller = UMServiceFactory.getUMSocialService(DESCRIPTOR,
				RequestType.SOCIAL);
		mController = controller;
		mContext = this;
		// 添加新浪和QQ空间的SSO授权支持
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        mController.getConfig().setSsoHandler(
                new QZoneSsoHandler(this));
        // 添加腾讯微博SSO支持
        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

        /************************************* 设置授权完成后马上添加一个好友 **************************/
        final SocializeConfig config = mController.getConfig();
        config.addFollow(mTestMedia, "1914100420");
        config.setOauthDialogFollowListener(new MulStatusListener() {
            @Override
            public void onStart() {
                callBack("Follow Start");
            }

            @Override
            public void onComplete(MultiStatus multiStatus, int st,
                    SocializeEntity entity) {
                if (st == 200) {
                    Map<String, Integer> allChildren = multiStatus
                            .getAllChildren();
                    Set<String> set = allChildren.keySet();
                    for (String fid : set)
                    	callBack(fid + "    " + allChildren.get(fid));
                }
            }
        });
		
		onSwitch(shareFlag);
    }
	public void onSwitch(int v) {
		if (v == 0) {
			openShareBoard();
			this.finish();
		} else if (v == 1) {
			directShare();
		} else if (v == 2) {
			quickShare();
			this.finish();
		} else if (v == 3) {
			textAndPicShare();
		} else if (v == 4) {
			shareToMultiPlatform();
			this.finish();
		} else if (v == 5) {
			doOauth();
		} else if (v == 6) {
			deleteOauth();
			this.finish();
		} else if (v == 7) {
			sortPlatform();
			this.finish();
		} else if (v == 8) {
			removePlatform();
			this.finish();
		} else if (v == 9) {
			closeSSO();
			this.finish();
		} else if (v == 10) {
			openSSO();
			this.finish();
		}

	}

	/**
	 * @功能描述 : 分享(先选择平台)
	 */
	private void openShareBoard() {
		//    	UMImage mUMImgBitmap = new UMImage(this,
		//         "http://www.umeng.com/images/pic/banner_module_social.png");
		//        mController.setShareContent(
		//        		"友盟社会化组件还不错，让移动应用快速整合社交分享功能。http://www.umeng.com/social");
		if(shareArr.length < 2)
			callBack("openShareBoard - open share arg is error");
		else{
			UMImage mUMImgBitmap = new UMImage(this,
					shareArr[0]);
			mController.setShareContent(
					shareArr[1]);
			mController.setShareMedia(mUMImgBitmap);
			mController.openShare(_context.getActivity(), false);
		}

	}

	/**
	 * @功能描述 : 图文分享（呼出编辑页）
	 */
	private void directShare() {

		// 视频分享
//		UMVideo umVedio = new UMVideo(
//		"http://v.youku.com/v_show/id_XNTc0ODM4OTM2.html");
//		umVedio.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
//		umVedio.setTitle("友盟社会化组件视频");
//		TencentWbShareContent tencentWbShareContent = new TencentWbShareContent(umVedio);
//		tencentWbShareContent.setShareContent("腾讯微博分享内容 Direct --来自友盟社会化组件（SDK）让移动应用快速整合社交分享功能");
		if(shareArr.length < 4)
			callBack("directShare - open share arg is error");
		else
		{
			UMVideo umVedio = new UMVideo(shareArr[0]);
			umVedio.setThumb(shareArr[1]);
			umVedio.setTitle(shareArr[2]);
			TencentWbShareContent tencentWbShareContent = new TencentWbShareContent(umVedio);
			tencentWbShareContent.setShareContent(shareArr[3]);
			// 设置tencent分享内容
			mController.setShareMedia(tencentWbShareContent);

			mController.directShare(this, SHARE_MEDIA.TENCENT,
					new SnsPostListener() {

				@Override
				public void onStart() {
					callBack("directShare - 开始分享");
				}

				@Override
				public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
					callBack("directShare - 分享完成");
				}
			});
		}
		

	}
	
	/**
     * @功能描述 : 快速分享接口（呼出编辑页）
     */
    private void quickShare() {
        // 快速分享接口
    	if(shareArr.length < 1)
			callBack("quickShare - open share arg is error");
		else
		{
			String mShareContent = shareArr[0];
	        mController.shareTo(_context.getActivity(), mShareContent, null);
	        callBack("quickShare - 分享完成");
		}
    	
    }
    
    /**
     * @功能描述 : 图文分享（功能底层接口）
     */
    private void textAndPicShare() {
    	
        mController.postShare(mContext, mTestMedia, new SnsPostListener() {
            @Override
            public void onComplete(SHARE_MEDIA arg0, int arg1,
                    SocializeEntity arg2) {
            	callBack("textAndPicShare - 分享完成");
            }

            @Override
            public void onStart() {
            	callBack("textAndPicShare - 开始分享");
            }
        });
    }
    
    /**
     * @功能描述 : 授权（功能底层接口）
     */
    private void doOauth() {
    	
        if (UMInfoAgent.isOauthed(mContext, mTestMedia)) {
        	callBack("doOauth - 新浪平台已经授权.");
        } else {
            mController.doOauthVerify(mContext, mTestMedia,
                    new UMAuthListener() {
                        @Override
                        public void onError(SocializeException e,
                                SHARE_MEDIA platform) {
                        	callBack("doOauth - onError");
                        }

                        @Override
                        public void onComplete(Bundle value,
                                SHARE_MEDIA platform) {
                        	mController.directShare(mContext, SHARE_MEDIA.QQ, null);
                        	callBack("doOauth - onComplete");
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA arg0) {
                        	callBack("doOauth - onCancel");
                        }

                        @Override
                        public void onStart(SHARE_MEDIA arg0) {
                        	callBack("doOauth - onStart");
                        }

                    });
        }
    }
    
    /**
     * @功能描述 : 分享多个已授权平台(功能底层接口)
     */
    private void shareToMultiPlatform() {
        mController.postShareMulti(_context.getActivity(), new MulStatusListener() {
            @Override
            public void onStart() {
            	callBack("shareToMultiPlatform - 开始分享.");
            }

            @Override
            public void onComplete(MultiStatus snsSt, int st,
                    SocializeEntity entity) {
            	callBack("shareToMultiPlatform - " +  snsSt.toString());
            }
        }, SHARE_MEDIA.SINA, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.QZONE, SHARE_MEDIA.FACEBOOK);
    }
    
    /**
     * @功能描述 : 解除（功能底层接口）
     */
    private void deleteOauth() {
        mController.deleteOauth(_context.getActivity(), mTestMedia,
                new SocializeClientListener() {
                    @Override
                    public void onStart() {
                    	callBack("deleteOauth - sina="
                                        + UMInfoAgent.isOauthed(mContext,
                                                mTestMedia));

                    }

                    @Override
                    public void onComplete(int status, SocializeEntity entity) {
                    	callBack("deleteOauth -" + status
                                        + "      sina="
                                        + UMInfoAgent.isOauthed(mContext,
                                                mTestMedia));
                    }
                });
    }
    
    /**
     * 根据开发者设置的顺序对平台排序
     */
    private void sortPlatform() {
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.RENREN,
                SHARE_MEDIA.SINA,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.EMAIL,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.GENERIC);
        mController.openShare(_context.getActivity(), false);
    }
    
    /**
     * @功能描述 :
     */
    public void removePlatform() {
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
        mController.openShare(_context.getActivity(), false);
    }
    
    private void openSSO() {
        mController.getConfig().openQQZoneSso();
        mController.getConfig().openSinaSso();
        mController.getConfig().openTencentWBSso();
        mController.openShare(_context.getActivity(), false);
    }

    /**
     * 关闭sina微博SSO，QQ zone SSO，腾讯微博SSO，
     */
    private void closeSSO() {
        mController.getConfig().closeQQZoneSso();
        mController.getConfig().closeSinaSSo();
        mController.getConfig().closeTencentWBSso();
        mController.openShare(_context.getActivity(), false);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callBack(" --------------------------- onActivityResult - enter");
		
		callBack("#### ssoHandler.authorizeCallBack11111");
		String result = "null";
		try {
			Bundle b = data.getExtras();
			Set<String> keySet = b.keySet();
			if(keySet.size() > 0)
				result = "result size:"+keySet.size();
			for(String key : keySet){
				Object object = b.get(key);
				callBack("TestData:Result:"+key+"   "+object.toString());
			}
		}
		catch (Exception e) {

		}
		callBack("TestData:onActivityResult   " + requestCode + "   " + resultCode + "   " + result);

		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = controller.getConfig().getSsoHandler(requestCode) ;
		if( ssoHandler != null ){
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			callBack("#### ssoHandler.authorizeCallBack");
		}
		callBack("--------------------------- onActivityResult - exit");
	}

	
    
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event)  
	{  
		if (keyCode == KeyEvent.KEYCODE_BACK )  
		{  

		}  
		return super.onKeyDown(keyCode, event);

	}  

	public void callBack(String status){
		Log.d(TAG, "umeng:"+status);
		_context.dispatchStatusEventAsync(TAG,status);
	}
}
