package com.jfinal.weixin.sdk.jfinal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.project.common.BaseController;

/**
 * APIController 为 APIController 绑定 ApiConfig 对象到当前线程，
 * 以便在后续的操作中可以使用 ApiConfigKit.getApiConfig() 获取到该对象
 */
public class ApiInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		if (controller instanceof ApiController == false)
			throw new RuntimeException("控制器需要继承 APIController");
		try {
			ApiConfigKit.setThreadLocalApiConfig(((ApiController)controller).getApiConfig());
			BaseController bc = (BaseController)controller;
			String url = bc.getDomain() + bc.getRequest().getRequestURI() + bc.getQueryUrl();
			Map<String,String>  map = ((ApiController)controller).getSignature(url);
			controller.setAttr("wxconfig", map);
			ai.invoke();
		} catch (Exception e) {
			e.printStackTrace();
			HttpServletRequest request=controller.getRequest();
			String header=request.getHeader("X-Requested-With");
        	boolean isAjax="XMLHttpRequest".equalsIgnoreCase(header);  
        	if(ai.getActionKey().startsWith("/wap/api")){
        		controller.setAttr("success", false);
    			controller.setAttr("msg", "操作失败");
    			controller.renderJson();
        		return;
        	}
        	if(ai.getActionKey().startsWith("/shop/api")){
        		controller.setAttr("success", false);
    			controller.setAttr("msg", "操作失败");
    			controller.renderJson();
        		return;
        	}
        	if(ai.getActionKey().startsWith("/admin/api")){
        		controller.setAttr("success", false);
    			controller.setAttr("msg", "操作失败");
    			controller.renderJson();
        		return;
        	}
            if(isAjax){
            	System.out.println("----------ajax请求");
            	controller.setAttr("success", false);
            	controller.setAttr("msg", "操作失败");
            	controller.renderJson();
            	return;
            }else{
            	System.out.println("----------非ajax请求");
            	if(ai.getActionKey().startsWith("/admin")){
            		controller.setAttr("msg", "操作失败");
                	controller.render("/admin/msg.htm");
                	return;
            	}else if(ai.getActionKey().startsWith("/shop")){
            		controller.setAttr("msg", "操作失败");
                	controller.render("/shop/msg.htm");
                	return;
	            }else{
            		controller.setAttr("msg", "操作失败");
                	controller.render("/wap/msg.htm");
                	return;
            	}
            }
		} finally {
			ApiConfigKit.removeThreadLocalApiConfig();
		}
	}
}

