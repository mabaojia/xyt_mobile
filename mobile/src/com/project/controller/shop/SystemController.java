package com.project.controller.shop;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Shop;
import com.project.model.ShopAdmin;
import com.project.util.MD5Util;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/system", viewPath = "/shop/system")
public class SystemController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void editPwd() throws Exception{
		
		render("editPwd.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void updatePwd() throws Exception{
		
		String password=getPara("password");
		String check_password=getPara("check_password");
		if(StrKit.isBlank(password)){
			setAttr("success",false);
			setAttr("msg", "密码不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(check_password)){
			setAttr("success",false);
			setAttr("msg", "确认密码不能为空");
			renderJson();
			return;
		}
		if(!password.equals(check_password)){
			setAttr("success",false);
			setAttr("msg", "两次密码不一致");
			renderJson();
			return;
		}
		ShopAdmin shop_admin=ShopAdmin.dao.findById(getLoginShopAdminId());
		shop_admin.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
					.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void number() throws Exception{
		
		Shop shop=Shop.dao.findById(getLoginShopId());
		shop.set("print_number", shop.getInt("print_number") + 1)
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void editMsg() throws Exception{
		
		render("editMsg.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void updateMsg() throws Exception{
		
		Shop shop = Shop.dao.findById(getLoginShopId());
		String title=getPara("title");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "负责人不能为空");
			renderJson();
			return;
		}
		String mobile=getPara("mobile");
		if(StrKit.isBlank(mobile)){
			setAttr("success", false);
			setAttr("msg", "手机号不能为空");
			renderJson();
			return;
		}
		String wx=getPara("wx");
		if(StrKit.isBlank(wx)){
			setAttr("success", false);
			setAttr("msg", "微信号不能为空");
			renderJson();
			return;
		}
		String wx_qrcode=getPara("wx_qrcode");
		if(StrKit.isBlank(wx_qrcode)){
			setAttr("success", false);
			setAttr("msg", "微信二维码不能为空");
			renderJson();
			return;
		}
		String address=getPara("address");
		if(StrKit.isBlank(address)){
			setAttr("success", false);
			setAttr("msg", "详细地址不能为空");
			renderJson();
			return;
		}
		shop.set("title", title)
			.set("name", name)
			.set("mobile", mobile)
			.set("wx", wx)
			.set("wx_qrcode", wx_qrcode)
			.set("address", address)
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
