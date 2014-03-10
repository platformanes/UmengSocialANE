package com.umeng.func;

import android.util.Log;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.umeng.activity.ANETencentBridge;

/**
 * CP
 * @author Rect
 * @version  Time：2013-5-8 
 */
public class UmengCustomplatfor implements FREFunction {

	private String TAG = "UmengCustomplatfor";
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
		callBack("A");
		ANETencentBridge._context = _context;
		callBack("B");
		ANETencentBridge.getInstance().onSwitch(shareFlag, shareArr);
		callBack("success");
		//--------------------------------
		
		return result;
	}

	public void callBack(String status){
		Log.d(TAG, "umeng:"+status);
		_context.dispatchStatusEventAsync(TAG,status);
	}
}
