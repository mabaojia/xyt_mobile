package com.project.controller.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Mask;
import com.project.model.Type;
/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：176-6401-7800
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/shop/mask", viewPath="/shop/mask")
public class MaskController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		int page=getParaToInt("p",1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select m.*, t.title type_title";
		String sWhere=" from db_mask m left join db_type t on m.type_id=t.id where m.status!=? and t.status=? and m.shop_id =?";
		params.add(Mask.STATUS_DELETE);
		params.add(Type.STATUS_ENABLE);
		params.add(getLoginShopId());
		if(StrKit.notBlank(getPara("tid"))){
			sWhere+=" and m.type_id=?";
			params.add(getPara("tid"));
			setAttr("tid", getParaToInt("tid"));
		}
		sWhere+=" order by m.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//模板分类
		List<Record> type_list=Type.getList(Type.TYPE_MASK,  getLoginShopId());
		setAttr("type_list", type_list);
		render("list.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void add() throws Exception{
		
		List<Record> type_list = Type.getAll(Type.TYPE_MASK, getLoginShopId());
		setAttr("type_list", type_list);
		if (StrKit.notBlank(getPara("id"))) {
			Mask mask = Mask.dao.findById(getPara("id"));
			setAttr("mask", mask);
		}
		render("add.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void save() throws Exception{
		
		String title = getPara("title");
		String img_urls = getPara("img_urls");
		String type_id=getPara("type_id");
		if (StrKit.isBlank(title)) {
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if (StrKit.isBlank(img_urls)) {
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(type_id)){
			setAttr("success", false);
			setAttr("msg", "分类不能为空");
			renderJson();
			return;
		}
		for(String img_url : img_urls.split(",")){
			if(StrKit.notBlank(img_url)){
				Mask mask = Mask.dao.findFirst("select * from db_mask where shop_id=? and img_url=?", getLoginShopId(), img_url);
				if(mask!=null){
					mask.set("title", title)
					    .set("img_url", img_url)
					    .set("type_id", type_id)
					    .set("status", Mask.STATUS_ENABLE)
					    .set("create_date", new Date())
					    .update();
				}else{
					mask = new Mask();
					mask.set("title", title)
					    .set("img_url", img_url)
					    .set("type_id", type_id)
					    .set("shop_id", getLoginShopId())
					    .set("status", Mask.STATUS_ENABLE)
					    .set("create_date", new Date())
					    .save();
				}
			}
		}
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
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void edit() throws Exception{
		
		Mask mask = Mask.dao.findById(getPara("id"));
		if (!mask.get("shop_id").toString().equals(getLoginShopId())) {
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("mask", mask);
		//模板分类
		List<Record> type_list=Type.getList(Type.TYPE_MASK,  getLoginShopId());
		setAttr("type_list", type_list);
		render("edit.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void update() throws Exception{
		
		Mask mask = Mask.dao.findById(getPara("id"));
		if (!mask.get("shop_id").toString().equals(getLoginShopId())) {
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title = getPara("title");
		String img_url = getPara("img_url");
		String type_id=getPara("type_id");
		if (StrKit.isBlank(title)) {
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if (StrKit.isBlank(img_url)) {
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(type_id)){
			setAttr("success", false);
			setAttr("msg", "分类不能为空");
			renderJson();
			return;
		}
		mask.set("title", title)
		    .set("img_url", img_url)
		    .set("type_id", type_id)
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
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void changeStatus() throws Exception{
		
		Mask mask = Mask.dao.findById(getPara("id"));
		if (!mask.get("shop_id").toString().equals(getLoginShopId())) {
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		mask.set("status", getPara("status"))
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
