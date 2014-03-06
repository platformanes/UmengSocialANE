package com.umeng.func;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

/**
 * other module
 * @author Rect
 * @version  Time：2013-5-8 
 */
public class UmengOther implements FREFunction {

	private String TAG = "UmengOther";
	private FREContext _context;
	@Override
	public FREObject call(final FREContext context, FREObject[] arg1) {
		// TODO Auto-generated method stub
		_context = context;
		FREObject result = null; 
		// TODO Auto-generated method stub
		//--------------------------------
		callBack("success");
		//--------------------------------
		
		return result;
	}

	public void callBack(String status){
		Log.d(TAG, "umeng:"+status);
		_context.dispatchStatusEventAsync(TAG,status);
	}

}
