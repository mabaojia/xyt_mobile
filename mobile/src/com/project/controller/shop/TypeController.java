package com.project.controller.shop;

import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Type;

/**
 *
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/type", viewPath = "/shop/type")
public class TypeController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Record> list=Type.getAll(getPara("type"), getLoginShopId());
		setAttr("list", list);
		//分类类型
		setAttr("type", getParaToInt("type"));
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
		
		//分类类型
		setAttr("type", getParaToInt("type"));
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
		String img_url=getPara("img_url");
		String idx=getPara("idx");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(idx)){
			setAttr("success", false);
			setAttr("msg", "排序不能为空");
			renderJson();
			return;
		}
		if(getParaToInt("type")==Type.TYPE_GOODS){
			if(StrKit.isBlank(img_url)){
				setAttr("success", false);
				setAttr("msg", "图片不能为空");
				renderJson();
				return;
			}
		}
		Type type=new Type();
		type.set("title", title)
			.set("img_url", img_url)
			.set("idx", idx)
			.set("shop_id", getLoginShopId())
			.set("status", Type.STATUS_ENABLE)
			.set("type", getPara("type"))
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
		
		Type type=Type.dao.findById(getPara("id"));
		if(!type.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("type", type);
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
		
		Type type=Type.dao.findById(getPara("id"));
		if(!type.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		String idx=getPara("idx");
		String img_url=getPara("img_url");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(idx)){
			setAttr("success", false);
			setAttr("msg", "排序不能为空");
			renderJson();
			return;
		}
		if(type.getInt("type")==Type.TYPE_GOODS){
			if(StrKit.isBlank(img_url)){
				setAttr("success", false);
				setAttr("msg", "图片不能为空");
				renderJson();
				return;
			}
		}
		type.set("title", title)
			.set("idx", idx)
			.set("img_url", img_url)
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

		Type type=Type.dao.findById(getPara("id"));
		if(!type.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		type.set("status", getPara("status"))
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
	public void top() throws Exception{
		
		Type type=Type.dao.findById(getPara("id"));
		if(!type.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("type", type);
		render("top.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void changeTop() throws Exception{

		Type type=Type.dao.findById(getPara("id"));
		if(!type.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(getParaToInt("top")==Type.TOP_YES){
			String top_img_url=getPara("top_img_url");
			if(StrKit.isBlank(top_img_url)){
				setAttr("success", false);
				setAttr("msg", "图片不能为空");
				renderJson();
				return;
			}
			type.set("top_img_url", top_img_url)
				.set("top_date", new Date());
		}
		type.set("top", getPara("top"))
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
