package com.project.aop;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.project.common.BaseController;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class ExceptionInterceptor implements Interceptor {  
  
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
    public void intercept(ActionInvocation ai) {  
    	
        BaseController controller=(BaseController)ai.getController();  
        HttpServletRequest request=controller.getRequest();  
        try{  
            ai.invoke();  
        }catch (Exception e){  
        	e.printStackTrace();
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
        }
    }
}