package com.project.controller.shop;

import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Notice;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/notice", viewPath = "/shop/notice")
public class NoticeController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Notice> list=Notice.getAll(getLoginShopId());
		setAttr("list", list);
		render("list.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void add() throws Exception{
		
		render("add.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void save() throws Exception{
		
		String title=getPara("title");
		String content=getPara("content");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		Notice notice=new Notice();
		notice.set("title", title)
				.set("content", content)
				.set("shop_id", getLoginShopId())
				.set("status", Notice.STATUS_ENABLE)
				.set("create_date", new Date())
				.save();
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
	public void edit() throws Exception{
		
		Notice notice=Notice.dao.findById(getPara("id"));
		if(!notice.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("notice", notice);
		render("edit.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void update() throws Exception{
		
		Notice notice=Notice.dao.findById(getPara("id"));
		if(!notice.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		String content=getPara("content");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		notice.set("title", title)
				.set("content", content)
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
	public void changeStatus() throws Exception{
		
		Notice notice=Notice.dao.findById(getPara("id"));
		if(!notice.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		notice.set("status", getPara("status"))
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
