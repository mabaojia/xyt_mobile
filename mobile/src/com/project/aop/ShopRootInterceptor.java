package com.project.aop;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.project.common.BaseController;
import com.project.model.ShopAdmin;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class ShopRootInterceptor implements Interceptor{
	
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
    	if(controller.getLoginShopAdmin().getInt("type")!=ShopAdmin.TYPE_ROOT){
    		if(isAjax){
				 controller.setAttr("success", false);
				 controller.setAttr("msg", "没有操作权限");
				 controller.renderJson();
				 return;
			 }else{
				 controller.setAttr("msg", "没有操作权限");
				 controller.render("/shop/msg.htm");
				 return;
			 }
    	}
    	ai.invoke();
    }
}
