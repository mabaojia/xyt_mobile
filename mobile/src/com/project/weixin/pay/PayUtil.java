package com.project.weixin.pay;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.PropKit;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class PayUtil{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@SuppressWarnings("static-access")
	public static SortedMap<String, String> getPackage(WxPayDto tpWxPayDto) throws Exception{
		
		String openId = tpWxPayDto.getOpenId();
		String orderId = tpWxPayDto.getOrderId();
		String attach = "";
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		String notify_url = tpWxPayDto.getNotifyurl();
		String trade_type = "JSAPI";
		String mch_id = tpWxPayDto.getPartner();
		String nonce_str = getOrder();
		String body = tpWxPayDto.getBody();
		String out_trade_no = orderId;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", tpWxPayDto.getAppid());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(tpWxPayDto.getAppid(), tpWxPayDto.getAppsecret(), tpWxPayDto.getPartnerkey());
		System.out.println("appid=" + tpWxPayDto.getAppid());
		System.out.println("appsecret=" + tpWxPayDto.getAppsecret());
		System.out.println("partner=" + tpWxPayDto.getPartner());
		System.out.println("partnerkey=" + tpWxPayDto.getPartnerkey());
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + tpWxPayDto.getAppid() + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		String prepay_id = "";
		System.out.println(xml);
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		prepay_id = new GetWxOrderno().getPayNo(createOrderURL, xml);
		System.out.println("获取到的预支付ID：" + prepay_id);
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", tpWxPayDto.getAppid());  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		String finalsign = reqHandler.createSign(finalpackage);
		finalpackage.put("paySign",finalsign);
		finalpackage.put("_package",packages);
		System.out.println("finalpackage:" + finalpackage);
		return finalpackage;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String getOrder() throws Exception{
		
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = TenpayUtil.buildRandom(4) + "";
		return strTime + strRandom;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String getMoney(String amount) throws Exception{
		
		if(amount==null){
			return "";
		}
		String currency =  amount.replaceAll("\\$|\\￥|\\,", "");
        int index = currency.indexOf(".");  
        int length = currency.length();  
        Long amLong = 0l;  
        if(index == -1){  
            amLong = Long.valueOf(currency+"00");  
        }else if(length - index >= 3){  
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));  
        }else if(length - index == 2){  
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);  
        }else{  
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");  
        }  
        return amLong.toString(); 
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String getIp(HttpServletRequest request) throws Exception{
		
		String ip = request.getHeader("X-Forwarded-For");
		if(ip!=null && !"unKnown".equalsIgnoreCase(ip)){
	        int index = ip.indexOf(",");
	        if(index != -1){
	            return ip.substring(0,index);
	        }else{
	            return ip;
	        }
	    }
	    ip = request.getHeader("X-Real-IP");
	    if(ip!=null && !"unKnown".equalsIgnoreCase(ip)){
	        return ip;
	    }
	    return request.getRemoteAddr();
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static boolean cardPayment(WxPayDto tpWxPayDto, String auth_code) throws Exception{
		
		String attach = "";
		String time_expire = "";
		String nonce_str = getOrder();
		String orderId = tpWxPayDto.getOrderId();
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		String mch_id = tpWxPayDto.getPartner();
		String body = tpWxPayDto.getBody();
		String out_trade_no = orderId;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", tpWxPayDto.getAppid());
		packageParams.put("attach", attach);
		packageParams.put("auth_code", auth_code);
		packageParams.put("body", body);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("total_fee", totalFee);
		packageParams.put("time_expire", time_expire);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(tpWxPayDto.getAppid(), tpWxPayDto.getAppsecret(), tpWxPayDto.getPartnerkey());
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>"
					+ "<appid>" + tpWxPayDto.getAppid() + "</appid>"
					+ "<attach>" + attach + "</attach>"
					+ "<auth_code>" + auth_code + "</auth_code>"
					+ "<body><![CDATA[" + body + "]]></body>" 
					+ "<mch_id>" + mch_id + "</mch_id>"
					+ "<nonce_str>" + nonce_str + "</nonce_str>"
					+ "<out_trade_no>" + out_trade_no + "</out_trade_no>"
					+ "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
					+ "<total_fee>" + totalFee + "</total_fee>"
					+ "<sign>" + sign + "</sign>"
					+ "<time_expire>" + time_expire + "</time_expire>"
					+ "</xml>";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/micropay";
		boolean results = new GetWxOrderno().getResults(createOrderURL, xml);
		return results;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String getCodeurl(WxPayDto tpWxPayDto) throws Exception{
		
		String orderId = tpWxPayDto.getOrderId();
		String attach = "";
		String totalFee = getMoney(tpWxPayDto.getTotalFee());
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		String notify_url = tpWxPayDto.getNotifyurl();
		String trade_type = "NATIVE";
		String mch_id = tpWxPayDto.getPartner();
		String nonce_str = getOrder();
		String body = tpWxPayDto.getBody();
		String out_trade_no = orderId;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", tpWxPayDto.getAppid());
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);
		packageParams.put("trade_type", trade_type);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(tpWxPayDto.getAppid(), tpWxPayDto.getAppsecret(), tpWxPayDto.getPartnerkey());
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + tpWxPayDto.getAppid() + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		code_url = new GetWxOrderno().getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------" + code_url);
		return code_url;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static boolean closeorder(String code) throws Exception{
		
		PropKit.use("config.txt");
		String appid = PropKit.get("appId").toString();
		String appsecret = PropKit.get("appSecret").toString();
		String mch_id = PropKit.get("partner").toString();
		String pkey = PropKit.get("partnerkey").toString();
		String url="https://api.mch.weixin.qq.com/pay/closeorder";
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = TenpayUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", code);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, pkey);
		String sign = reqHandler.createSign(packageParams);
		String xmlParam = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>"
				+ "<out_trade_no>" + code + "</out_trade_no>"
				+ "</xml>";
		Map<String, String> map=GetWxOrderno.doXML(url, xmlParam);
		String return_code=map.get("return_code").toString();
		if("SUCCESS".equals(return_code)){
			String result_code=map.get("result_code").toString();
			if("SUCCESS".equals(result_code)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String refundPayment(WxPayDto tpWxPayDto, String license) throws Exception{
		
		String nonce_str = getOrder();
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", tpWxPayDto.getAppid());
		packageParams.put("mch_id", tpWxPayDto.getPartner());
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", tpWxPayDto.getOrderId());
		packageParams.put("out_refund_no", tpWxPayDto.getOrderId());
		packageParams.put("total_fee", getMoney(tpWxPayDto.getTotalFee()));
		packageParams.put("refund_fee", getMoney(tpWxPayDto.getTotalFee()));
		packageParams.put("op_user_id", tpWxPayDto.getPartner());
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(tpWxPayDto.getAppid(), tpWxPayDto.getAppsecret(), tpWxPayDto.getPartnerkey());
		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + 
				"<appid>" + tpWxPayDto.getAppid() + "</appid>" + 
				"<mch_id>" + tpWxPayDto.getPartner() + "</mch_id>" + 
				"<op_user_id>" + tpWxPayDto.getPartner() + "</op_user_id>" + 
				"<nonce_str>" + nonce_str + "</nonce_str>" + 
				"<out_refund_no>" + tpWxPayDto.getOrderId() + "</out_refund_no>" + 
				"<out_trade_no>" + tpWxPayDto.getOrderId() + "</out_trade_no>"	+ 
				"<total_fee>" + getMoney(tpWxPayDto.getTotalFee()) + "</total_fee>" + 
				"<refund_fee>" + getMoney(tpWxPayDto.getTotalFee()) + "</refund_fee>" + 
				"<sign>" + sign + "</sign>"	+ 
				"</xml>";
		String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
		String refundResult = ClientCustomSSL.doRefund(createOrderURL, xml, tpWxPayDto.getPartner(), license);
		System.out.println(refundResult);
		return refundResult;
	}
}
