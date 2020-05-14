package com.project.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Admin;
import com.project.util.MD5Util;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/system", viewPath = "/admin/system")
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
		Admin admin=Admin.dao.findById(getLoginAdminId());
		admin.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
