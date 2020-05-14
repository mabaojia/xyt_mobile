package com.project.controller.admin;

import com.jfinal.ext.route.ControllerBind;
import com.project.common.BaseController;
import com.project.model.Diy;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/diy", viewPath = "/admin/diy")
public class DiyController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void changeStatus() throws Exception{
		
		Diy diy=Diy.dao.findById(getPara("id"));
		diy.set("status", getPara("status"))
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}