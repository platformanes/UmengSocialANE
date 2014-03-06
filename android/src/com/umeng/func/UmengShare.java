package com.umeng.func;


import android.content.Intent;
import android.util.Log;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.umeng.activity.ANEShareActivity;

/**
 * share modules
 * @author Rect
 * @version  Time：2013-5-8 
 */
public class UmengShare implements FREFunction {

	private String TAG = "UmengShare";
	private FREContext _context;
	@Override
	public FREObject call(final FREContext context, FREObject[] arg1) {
		// TODO Auto-generated method stub
		_context = context;
		FREObject result = null; 
		int shareFlag = 0;
		String[] shareArr = null;
		// TODO Auto-generated method stub
		//--------------------------------
		try
		{
			shareFlag = arg1[0].getAsInt();
			FREArray __array = (FREArray)arg1[1];
			callBack("shareFlag = " + shareFlag);
			
			int __len = (int)__array.getLength();
			if (__len != 0)
			{
				shareArr = new String[__len];
				for(int t = 0;t<__len;t++)
				{
					shareArr[t] = __array.getObjectAt(t).getAsString();
					callBack("shareArr = " + shareArr[t]);
				}
			}
			
		}
		catch (Exception e) {
			// TODO: handle exception
			callBack("argv is error");
			return null;
		}
		
		ANEShareActivity.shareArr = shareArr;
		ANEShareActivity.shareFlag = shareFlag;
		ANEShareActivity._context = _context;
		
		Intent intent = new Intent(ANEShareActivity.MYACTIVITY_ACTION);
		_context.getActivity().startActivityForResult(intent, 0);
		
		callBack("success");
		//--------------------------------

		return result;
	}

	public void callBack(String status){
		Log.d(TAG, "umeng:"+status);
		_context.dispatchStatusEventAsync(TAG,status);
	}
}
