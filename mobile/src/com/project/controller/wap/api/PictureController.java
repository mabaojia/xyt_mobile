package com.project.controller.wap.api;

import com.jfinal.ext.route.ControllerBind;
import com.project.common.BaseController;
import com.project.model.UserPicture;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/wap/api/picture")
public class PictureController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void header() throws Exception{
		
		getResponse().addHeader("Access-Control-Allow-Origin", "*");
    	getResponse().addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
    	getResponse().addHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
    	getResponse().addHeader("X-Powered-By", "3.2.1");
    	getResponse().addHeader("Content-Type", "application/json;charset=utf-8");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
	
		header();
		setAttr("list", UserPicture.getByUser(getLoginUserId()));
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
	public void delete() throws Exception{

		header();
		UserPicture user_picture=UserPicture.dao.findById(getPara("upid"));
		if(!user_picture.get("user_id").toString().equals(getLoginUserId())){
	    	setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
	    }
		user_picture.delete();
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
	public void use() throws Exception{
		
		header();
		UserPicture user_picture=UserPicture.dao.findById(getPara("upid"));
		if(!user_picture.get("user_id").toString().equals(getLoginUserId())){
	    	setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
	    }
		user_picture.set("use", getPara("use"))
					.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}