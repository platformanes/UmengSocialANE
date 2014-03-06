package com.umeng.func;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

/**
 * @author Rect
 * @see rectvv@gmail.com
 * @see www.shadowkong.com
 * @see github.com/platformanes
 * @version 2014-3-4
 */
public class UmengComment implements FREFunction {

	private String TAG = "UmengComment";
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
