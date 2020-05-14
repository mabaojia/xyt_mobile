package com.project.aop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Shop;
import com.project.model.ShopAdmin;
import com.project.model.ShopAdminMenu;
import com.project.model.ShopMenu;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class ShopInterceptor implements Interceptor{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
    public void intercept(ActionInvocation ai) {

    	PropKit.use("config.txt");
    	BaseController controller=(BaseController) ai.getController();
    	HttpServletRequest request=controller.getRequest();
    	String header=request.getHeader("X-Requested-With");
    	boolean isAjax="XMLHttpRequest".equalsIgnoreCase(header);
    	if (ai.getActionKey().startsWith("/shop")){
    		controller.setAttr("url", ai.getActionKey());
    		controller.setAttr("url_1", ai.getControllerKey());
			if (controller.getLoginShop()==null || controller.getLoginShopAdmin()==null || controller.getSessionAttr("shop_menus")==null) {
				String shop_email=controller.getCookie("shop_email");
				String shop_password=controller.getCookie("shop_password");
				if(StrKit.notBlank(shop_email) && StrKit.notBlank(shop_password)){
					ShopAdmin shop_admin=ShopAdmin.getByEmailMd5Pwd(shop_email, shop_password);
					if(shop_admin==null){
						if(ai.getActionKey().startsWith("/shop/api")){
							controller.setAttr("success", false);
							controller.setAttr("msg", "请重新登录");
							controller.renderJson();
							return;
						}else{
							if(isAjax){
								controller.setAttr("success", false);
								controller.setAttr("msg", "请重新登录");
								controller.renderJson();
								return;
							 }else{
								 controller.redirect("/shop");
								 return;
							 }
						}
					}
					List<ShopAdminMenu> shop_menus=ShopAdminMenu.getMenus(shop_admin.get("id"));
					if(shop_menus==null || shop_menus.size()==0){
						if(ai.getActionKey().startsWith("/shop/api")){
							controller.setAttr("success", false);
							controller.setAttr("msg", "请重新登录");
							controller.renderJson();
							return;
						}else{
							if(isAjax){
								controller.setAttr("success", false);
								controller.setAttr("msg", "请重新登录");
								controller.renderJson();
								return;
							 }else{
								controller.redirect("/shop");
								return;
							 }
						}
					}
					Shop shop=Shop.dao.findById(shop_admin.get("shop_id"));
					if(shop.getInt("status")!=Shop.STATUS_ENABLE){
						if(ai.getActionKey().startsWith("/shop/api")){
							controller.setAttr("success", false);
							controller.setAttr("msg", "没有操作权限");
							controller.renderJson();
							return;
						}else{
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
					}
					controller.setSessionAttr("shop", shop);
					controller.setSessionAttr("shop_admin", shop_admin);
					controller.setSessionAttr("shop_menus", shop_menus);
					controller.setCookie("shop_email", shop_admin.get("email").toString(), 60 * 60 * 24 * 30);
					controller.setCookie("shop_password", shop_admin.get("password").toString(), 60 * 60 * 24 * 30);
				}else{
					if(ai.getActionKey().startsWith("/shop/api")){
						controller.setAttr("success", false);
						controller.setAttr("msg", "请重新登录");
						controller.renderJson();
						return;
					}else{
						if(isAjax){
							controller.setAttr("success", false);
							controller.setAttr("msg", "请重新登录");
							controller.renderJson();
							return;
						 }else{
							controller.redirect("/shop");
							return;
						 }
					}
				}
			}
			ShopMenu shop_menu=ShopMenu.findByUrl(ai.getControllerKey());
			if(shop_menu!=null){
				ShopAdmin shop_admin=ShopAdmin.dao.findById(controller.getLoginShopAdminId());
				ShopAdminMenu shop_admin_menu=ShopAdminMenu.getByAdminMenu(shop_admin.get("id"), shop_menu.get("id"));
				if(shop_admin_menu==null){
					if(ai.getActionKey().startsWith("/shop/api")){
						controller.setAttr("success", false);
						controller.setAttr("msg", "没有操作权限");
						controller.renderJson();
						return;
					}else{
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
				}
			}
			controller.setAttr("shop", Shop.dao.findById(controller.getLoginShopId()));
			controller.setAttr("shop_menus", controller.getSessionAttr("shop_menus"));
			controller.setAttr("shop_admin", controller.getSessionAttr("shop_admin"));
			controller.setAttr("wxUrl", PropKit.get("wxUrl").toString());
    	}
    	ai.invoke();
    }
}
