package com.umeng.func;

import static com.umeng.soexample.socialize.SocializeConfigDemo.DESCRIPTOR;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.soexample.socialize.SocializeConfigDemo;

/**
 * 初始化SDK
 * @author Rect
 * @version  Time：2013-5-8 
 */
public class UmengInit implements FREFunction {

	private String TAG = "UmengInit";
	private FREContext _context;
	@Override
	public FREObject call(final FREContext context, FREObject[] arg1) {
		// TODO Auto-generated method stub
		_context = context;
		FREObject result = null; 
		
		// TODO Auto-generated method stub
		//--------------------------------
		//改变全局为Demo可控的config 对象
		String descriptor = null;
		String appKey = null;
		boolean isLog = false;
		boolean isShowError = false;
		try{
			isLog = arg1[0].getAsBool();
			isShowError = arg1[1].getAsBool();
			appKey = arg1[2].getAsString();
			descriptor = arg1[3].getAsString();
			
			DESCRIPTOR = descriptor;
			callBack("isLog = "+isLog);
			callBack("isShowError = "+isShowError);
			callBack("appKey = "+appKey);
			callBack("descriptor = "+descriptor);
			
		}
		catch (Exception e) {
			// TODO: handle exception
			callBack("arg is error");
			return null;
		}
		
		com.umeng.socom.Log.LOG = isLog;
		SocializeConstants.SHOW_ERROR_CODE = isShowError;
		
		if(appKey != "")
			SocializeConstants.APPKEY = appKey;
		
		UMServiceFactory.getUMSocialService(
				DESCRIPTOR, RequestType.SOCIAL).setGlobalConfig(
						SocializeConfigDemo.getSocialConfig(_context.getActivity()));
		callBack("success");
		//--------------------------------
		
		return result;
	}

	public void callBack(String status){
		Log.d(TAG, "umeng:"+status);
		_context.dispatchStatusEventAsync(TAG,status);
	}

}
