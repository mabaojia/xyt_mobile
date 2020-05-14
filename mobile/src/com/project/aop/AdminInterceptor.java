package com.project.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Admin;
import com.project.model.AdminMenu;
import com.project.model.Menu;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class AdminInterceptor implements Interceptor{
	
	/**
	 * 管理端拦截器
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
    	if (ai.getActionKey().startsWith("/admin")){
    		controller.setAttr("url", ai.getActionKey());
    		controller.setAttr("url_1", ai.getControllerKey());
			if (controller.getLoginAdmin()==null || controller.getSessionAttr("menus")==null) {
				String admin_account=controller.getCookie("admin_account");
				String admin_password=controller.getCookie("admin_password");
				if(StrKit.notBlank(admin_account) && StrKit.notBlank(admin_password)){
					Admin admin=Admin.getByAccountMd5Pwd(admin_account,admin_password);
					if(admin!=null){
						List<Menu> menus=AdminMenu.getMenus(admin.get("id"));
						if(menus!=null && menus.size()>0){
							controller.setSessionAttr("admin", admin);
							controller.setCookie("admin_account", admin.get("account").toString(), 60 * 60 * 24 * 30);
							controller.setCookie("admin_password", admin.get("password").toString(), 60 * 60 * 24 * 30);
							controller.setSessionAttr("menus", menus);
						}else{
							if(isAjax){
								 controller.setAttr("success", false);
								 controller.setAttr("msg", "请重新登录");
								 controller.renderJson();
								 return;
							 }else{
								 controller.redirect("/admin");
								 return;
							 }
						}
					}else{
						if(isAjax){
							 controller.setAttr("success", false);
							 controller.setAttr("msg", "请重新登录");
							 controller.renderJson();
							 return;
						 }else{
							 controller.redirect("/admin");
							 return;
						 }
					}
				}else{
					if(isAjax){
						 controller.setAttr("success", false);
						 controller.setAttr("msg", "请重新登录");
						 controller.renderJson();
						 return;
					 }else{
						 controller.redirect("/admin");
						 return;
					 }
				}
			}
			Admin admin=Admin.dao.findById(controller.getLoginAdminId());
			if(admin.getInt("status")!=Admin.STATUS_ENABLE){
				if(isAjax){
					 controller.setAttr("success", false);
					 controller.setAttr("msg", "请重新登录");
					 controller.renderJson();
					 return;
				 }else{
					 controller.redirect("/admin");
					 return;
				 }
			}
			controller.setAttr("admin", admin);
		}
    	ai.invoke();
    }
}
