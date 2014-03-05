package com.umeng.ane 
{ 
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	
	/**
	 * 
	 * @author Rect  2013-5-6 
	 * 
	 */
	public class UmengExtension extends EventDispatcher 
	{ 
		private static const UMENG_FUNCTION_INIT:String = "umeng_function_init";//与java端中Map里的key一致
		private static const UMENG_FUNCTION_SHARE:String = "umeng_function_share";//与java端中Map里的key一致
		private static const UMENG_FUNCTION_CP:String = "umeng_function_customplatfor";//与java端中Map里的key一致
		private static const UMENG_FUNCTION_OTHER:String = "umeng_function_other";//与java端中Map里的key一致
		private static const UMENG_FUNCTION_COMM:String = "umeng_function_comment";
		
		public static const EXTENSION_ID:String = "com.umeng.ane.UmengANE";//与extension.xml中的id标签一致
		private var extContext:ExtensionContext;
		
		/**单例的实例*/
		private static var _instance:UmengExtension; 
		public function UmengExtension(target:IEventDispatcher=null)
		{
			super(target);
			if(extContext == null) {
				extContext = ExtensionContext.createExtensionContext(EXTENSION_ID, "");
				extContext.addEventListener(StatusEvent.STATUS, statusHandler);
			}
			
		} 
		
		//第二个为参数，会传入java代码中的FREExtension的createContext方法
		/**
		 * 获取实例
		 * @return DLExtension 单例
		 */
		public static function getInstance():UmengExtension
		{
			if(_instance == null) 
				_instance = new UmengExtension();
			return _instance;
		}
		
		/**
		 * 转抛事件
		 * @param event 事件
		 */
		private function statusHandler(event:StatusEvent):void
		{
			dispatchEvent(event);
		}
		/**
		 * 
		 * @param isLog
		 * @param isShowError
		 * @param appKey
		 * @param descriptor
		 * @return 
		 * 
		 */					
		public function UmengInit(isLog:Boolean,isShowError:Boolean,appKey:String,descriptor:String):String{
			if(extContext ){
				return extContext.call(UMENG_FUNCTION_INIT,isLog,isShowError,appKey,descriptor) as String;
			}
			return "call UmengInit failed";
		} 
		
			
		public function UmengShare(key:int,data:Vector.<String>):String{
			if(extContext ){
				return extContext.call(UMENG_FUNCTION_SHARE,key,data) as String;
			}
			return "call UmengShare failed";
		} 
		
		
		public function UmengCustomplatfor(key:int):String{
			if(extContext){ 
				return extContext.call(UMENG_FUNCTION_CP,key)as String;
			}
			return "call UmengCustomplatfor failed";
		}
		
			
		public function UmengOther(key:int):String{
			if(extContext){ 
				return extContext.call(UMENG_FUNCTION_OTHER,key) as String;
			}
			return "call UmengOther failed";
		}
		
		public function UmengComment(key:int):String{
			if(extContext){ 
				return extContext.call(UMENG_FUNCTION_COMM,key) as String;
			}
			return "call UmengComment failed";
		}
	} 
}