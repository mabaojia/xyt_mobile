package com.project.aop;

import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Shop;
import com.project.model.User;
import com.project.util.MD5Util;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class WapInterceptor implements Interceptor{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
    public void intercept(ActionInvocation ai) {

    	BaseController controller=(BaseController) ai.getController();
    	HttpServletRequest request=controller.getRequest();
    	String header=request.getHeader("X-Requested-With");
    	boolean isAjax="XMLHttpRequest".equalsIgnoreCase(header);
    	if (ai.getActionKey().startsWith("/wap")){
    		if (controller.getLoginUser()==null) {
    			String user_account=controller.getCookie("user_account");
    			String user_password=controller.getCookie("user_password");
    			System.out.println(user_account);
    			System.out.println(user_password);
				if(StrKit.notBlank(user_account) && StrKit.notBlank(user_password)){
					User user=User.getByOpenid(user_account);
					if(user==null || user.getInt("status")!=User.STATUS_ENABLE || !MD5Util.getStringMD5("xiaodaokeji" + user.get("openid").toString() + "xiaodaokeji").toLowerCase().equals(user_password)){
						if(isAjax){
		        			controller.setAttr("success", false);
		                	controller.setAttr("msg", "请重新登录");
		                	controller.renderJson();
		                	return;
		        		}
		    			try {
		    				Enumeration<String> params=controller.getParaNames();
		    				String from=ai.getActionKey() + "?s=wx";
		    		    	while(params.hasMoreElements()){
		    		    	    String object=params.nextElement();
		    		    	    if(!"s".equals(object.toString())){
		    		    	    	from+="&" + object + "=" + controller.getPara(object);
		    		    	    }
		    		    	    System.out.println(from);
		    		    	}
		    				controller.redirect("/wap/wx?from=" + URLEncoder.encode(from, "UTF-8"));
		    				return;
		    			} catch (Exception e) {
		    				e.printStackTrace();
		    				controller.redirect("/wap/wx");
		    				return;
		    			}
					}
					controller.setSessionAttr("user", user);
					controller.setCookie("user_account", user.get("openid").toString(), 60 * 60 * 24 * 30);
					controller.setCookie("user_password", MD5Util.getStringMD5("xiaodaokeji" + user.get("openid").toString() + "xiaodaokeji").toLowerCase(), 60 * 60 * 24 * 30);
				}else{
					if(isAjax){
	        			controller.setAttr("success", false);
	                	controller.setAttr("msg", "请重新登录");
	                	controller.renderJson();
	                	return;
	        		}
	    			try {
	    				Enumeration<String> params=controller.getParaNames();
	    				String from=ai.getActionKey() + "?s=wx";
	    		    	while(params.hasMoreElements()){
	    		    	    String object=params.nextElement();
	    		    	    if(!"s".equals(object.toString())){
	    		    	    	from+="&" + object + "=" + controller.getPara(object);
	    		    	    }
	    		    	    System.out.println(from);
	    		    	}
	    				controller.redirect("/wap/wx?from=" + URLEncoder.encode(from, "UTF-8"));
	    				return;
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    				controller.redirect("/wap/wx");
	    				return;
	    			}
				}
    		}
    		User user=User.dao.findById(controller.getLoginUserId());
    		if(user.getInt("status")!=User.STATUS_ENABLE){
    			if(isAjax){
    				controller.setAttr("success", false);
    				controller.setAttr("msg", "会员不可用");
    				controller.renderJson();
        			return;
    			}else{
    				controller.setAttr("msg", "会员不可用");
        			controller.render("/wap/msg.htm");
        			return;
    			}
    		}
    		controller.setAttr("user", user);
    		controller.setAttr("url", ai.getActionKey());
    		controller.setAttr("url_1", ai.getControllerKey());
    		if(StrKit.notBlank(controller.getPara("sid"))){
    			user.set("shop_id", controller.getPara("sid"))
    				.update();
    			controller.setSessionAttr("user", user);
    		}
    		if(user.get("shop_id")==null || StrKit.isBlank(user.get("shop_id").toString())){
    			if(isAjax){
        			controller.setAttr("success", false);
                	controller.setAttr("msg", "请从商家主页进入");
                	controller.renderJson();
                	return;
        		}else{
        			controller.setAttr("msg", "请从商家主页进入");
        			controller.render("/wap/msg.htm");
        			return;
        		}
    		}
    		Shop shop=Shop.dao.findById(user.get("shop_id"));
    		if(shop.getInt("status")!=Shop.STATUS_ENABLE){
    			if(isAjax){
    				controller.setAttr("success", false);
    				controller.setAttr("msg", "商家不可用");
    				controller.renderJson();
        			return;
    			}else{
    				controller.setAttr("msg", "商家不可用");
        			controller.render("/wap/msg.htm");
        			return;
    			}
    		}
    		controller.setAttr("shop", shop);
    	}
    	ai.invoke();
    }
}
