package com.jfinal.weixin.demo;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.msg.in.InImageMsg;
import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
import com.jfinal.weixin.sdk.msg.in.InTextMsg;
import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
import com.jfinal.weixin.sdk.msg.in.event.InTemplateEvent;
import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
import com.jfinal.weixin.sdk.msg.out.OutImageMsg;
import com.jfinal.weixin.sdk.msg.out.OutNewsMsg;
import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
import com.jfinal.weixin.sdk.msg.out.OutVoiceMsg;

@ControllerBind(controllerKey = "/wechat/msg", viewPath = "/wechat/msg")
public class WeixinMsgController extends MsgController {
	
	public ApiConfig getApiConfig() {
		
		ApiConfig ac = new ApiConfig();
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	protected void processInTextMsg(InTextMsg inTextMsg) {
		
	}
	/**
	 * 实现父类抽方法，处理图片消息
	 */
	protected void processInImageMsg(InImageMsg inImageMsg) {
		OutImageMsg outMsg = new OutImageMsg(inImageMsg);
		outMsg.setMediaId(inImageMsg.getMediaId());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理语音消息
	 */
	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
		OutVoiceMsg outMsg = new OutVoiceMsg(inVoiceMsg);
		outMsg.setMediaId(inVoiceMsg.getMediaId());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理视频消息
	 */
	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
		OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
		outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " + inVideoMsg.getMediaId());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理地址位置消息
	 */
	protected void processInLocationMsg(InLocationMsg inLocationMsg) {
		OutTextMsg outMsg = new OutTextMsg(inLocationMsg);
		outMsg.setContent("已收到地理位置消息:" +
							"\nlocation_X = " + inLocationMsg.getLocation_X() +
							"\nlocation_Y = " + inLocationMsg.getLocation_Y() + 
							"\nscale = " + inLocationMsg.getScale() +
							"\nlabel = " + inLocationMsg.getLabel());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理链接消息
	 * 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
	 */
	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
		OutNewsMsg outMsg = new OutNewsMsg(inLinkMsg);
		outMsg.addNews("链接消息已成功接收", "链接使用图文消息的方式发回给你，还可以使用文本方式发回。点击图文消息可跳转到链接地址页面，是不是很好玩 :)" , "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0", inLinkMsg.getUrl());
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理关注/取消关注消息
	 */
	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
		OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
		outMsg.setContent("欢迎关注");
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理扫描带参数二维码事件
	 */
	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
		OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
		outMsg.setContent("processInQrCodeEvent() 方法测试成功");
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理上报地理位置事件
	 */
	protected void processInLocationEvent(InLocationEvent inLocationEvent) {
		OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
		outMsg.setContent("processInLocationEvent() 方法测试成功");
		render(outMsg);
	}
	
	/**
	 * 实现父类抽方法，处理自定义菜单事件
	 */
	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
		renderOutTextMsg("processInMenuEvent() 方法测试成功");
	}
	
	/**
	 * 以接收微信平台发来的发送通知消息 微信服务号模板消息的实现
	 */
	protected void processInTemplateEvent(InTemplateEvent inTemplateEvent)
    {
        renderOutTextMsg(" ");
    }
	
	/**
	 * 实现父类抽方法，处理接收语音识别结果
	 */
	protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {
		renderOutTextMsg("语音识别结果： " + inSpeechRecognitionResults.getRecognition());
	}
}