package com.umeng.ane;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;

/**
 * @author Rect
 * @see rectvv@gmail.com
 * @see www.shadowkong.com
 * @see github.com/platformanes
 * @version 2014-3-4
 */
public class UmengExtension implements FREExtension {

	@Override
	public FREContext createContext(String arg0) {
		// TODO Auto-generated method stub
		return new UmengContext();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

}
