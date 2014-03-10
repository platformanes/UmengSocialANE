UmengSocialANE
==============

友盟社会化分享ANE android/IOS版(v3.3.5)

## UmengSocialANE iOS/Andorid版

* 参照官方DEMO
* 友盟SDK:[传送门](http://dev.umeng.com/social)
* Android版已实现:`分享接口`,`微信/QQ`.(其他模块可根据前两个模块源码自行实现.)
* iOS版已实现:loading

## Building ANE
> A.参照我博客的教程[传送门](http://www.shadowkong.com/archives/1090)

## Android
* 安卓版使用[RDT](https://github.com/recter/Anti-ADT/tree/master/RDT4.0)打包,关于RDT的更多详情:[传送门](https://github.com/recter/Anti-ADT/blob/master/RDT4.0/README.md)
* `BuildAPK/shareModule` 对应 `分享接口` 模块
* `BulidAPK/weiModule` 对应 `微信/QQ` 模块
## android版本回调的解决方案
友盟SDK的注册回调是调用传入的activity的onActivityResult函数,但是对于AIR项目而言AS开发者用普通的方法无法修改主activity的代码.但是并不是不能修改.

* **修改AIR主activity**
 
AIR项目的主activity代码模版分别在ADT.jar中的:

`com.adobe.air.apk.Dex.AppEntry_debug.jar`

`com.adobe.air.apk.Dex.AppEntry_release.jar`

两个jar源码中,onActivityResult函数如下:

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	  {
	    try
	    {
	      if (sRuntimeClassesLoaded)
	      {
	        Method method = sAndroidActivityWrapperClass.getMethod("onActivityResult", new Class[] { Integer.TYPE, Integer.TYPE, Intent.class });
	        InvokeMethod(method, new Object[] { Integer.valueOf(requestCode), Integer.valueOf(resultCode), data });
	      }
	    }
	    catch (Exception e)
	    {
	    }
	  }
这里需要使用[JAVA反射技术](http://hejianjie.iteye.com/blog/136205)
我们尝试把它修改为:

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	  try
	  {
	    if (sRuntimeClassesLoaded)
	    {
	      Method method = sAndroidActivityWrapperClass.getMethod("onActivityResult", new Class[] { Integer.TYPE, Integer.TYPE, Intent.class });
	      InvokeMethod(method, new Object[] { Integer.valueOf(requestCode), Integer.valueOf(resultCode), data });
		     //反射开始 获取ANE中的类com.umeng.ane.UmengJump
		     sAndroidActivityWrapperClass = Class.forName("com.umeng.ane.UmengJump");
		     if(sAndroidActivityWrapperClass != null)
		     {
		   	  method = sAndroidActivityWrapperClass.getMethod("UmengCallBack", new Class[] { Integer.TYPE, Integer.TYPE, Intent.class });
		      if(method != null)
		    	  InvokeMethod(method, new Object[] { Integer.valueOf(requestCode), Integer.valueOf(resultCode), data });
		     }
		      
		     //反射结束 恢复现场
		     sAndroidActivityWrapperClass = Class.forName("com.adobe.air.AndroidActivityWrapper");
		   }
		 }
		 catch (Exception e)
		 {
		 }
	}

然后在ANE中加入此类:`com.umeng.ane.UmengJump`

	package com.umeng.ane;
	/**
	 * @author Rect 
	 * @version 2014-3-10
	 */
	public class UmengJump {
		public static FREContext _context = null;
		public static void UmengCallBack(int requestCode, int resultCode, Intent data)
		{
			if(_context != null)
			{
				//Umeng回调操作 替代原本的onActivityResult
				//参数 requestCode resultCode data
			}
		}
	}

PS:以上方式需要一定的逆向思维.仅仅提供一个可行的方式.

* **修改UmengSDK**

这种方式就很简单了,直接修改UmengSDK 在关键的地方把原本的回调方式改掉.在这里不在赘述,有兴趣的可自行研究.
## iOS
* loading
## About Me

* [platformANEs](https://github.com/platformanes)由 [zrong](http://zengrong.net) 和 [rect](http://www.shadowkong.com/) 共同发起并完成。
