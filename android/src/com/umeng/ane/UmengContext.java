package com.umeng.ane;

import java.util.HashMap;
import java.util.Map;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.umeng.func.UmengComment;
import com.umeng.func.UmengOther;
import com.umeng.func.UmengInit;
import com.umeng.func.UmengShare;
import com.umeng.func.UmengCustomplatfor;

/**
 * @author Rect
 * @see rectvv@gmail.com
 * @see www.shadowkong.com
 * @see github.com/platformanes
 * @version 2014-3-4
 */
public class UmengContext extends FREContext {
	/**
	 * INIT sdk
	 */
	public static final String UMENG_FUNCTION_INIT = "umeng_function_init";
	/**
	 * 分享接口
	 */
	public static final String UMENG_FUNCTION_SHARE = "umeng_function_share";
	/**
	 * 微信/QQ
	 */
	public static final String UMENG_FUNCTION_CP = "umeng_function_customplatfor";
	/**
	 * 用户接口
	 */
	public static final String UMENG_FUNCTION_OTHER = "umeng_function_other";
	/**
	 * 评论模块
	 */
	public static final String UMENG_FUNCTION_COMM = "umeng_function_comment";
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		// TODO Auto-generated method stub
		Map<String, FREFunction> map = new HashMap<String, FREFunction>();
	       //映射
		   map.put(UMENG_FUNCTION_INIT, new UmengInit());
	       map.put(UMENG_FUNCTION_SHARE, new UmengShare());
	       map.put(UMENG_FUNCTION_CP, new UmengCustomplatfor());
	       map.put(UMENG_FUNCTION_OTHER, new UmengOther());
	       map.put(UMENG_FUNCTION_COMM, new UmengComment());
	       return map;
	}

}
