package com.project.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Material;
import com.project.model.Type;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/material/store", viewPath = "/admin/material/store")
public class MaterialStoreController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		int page=getParaToInt("p",1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select m.*,t.title type_title";
		String sWhere=" from db_material m left join db_type t on m.type_id=t.id where m.status!=? and t.status=? and m.shop_id is null";
		params.add(Material.STATUS_DELETE);
		params.add(Type.STATUS_ENABLE);
		if(StrKit.notBlank(getPara("tid"))){
			sWhere+=" and m.type_id=?";
			params.add(getPara("tid"));
			setAttr("tid", getParaToInt("tid"));
		}
		sWhere+=" order by m.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//贴图分类
		List<Record> list=Type.getList(Type.TYPE_MATERIAL, null);
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
		
		//贴图分类
		List<Record> list=Type.getList(Type.TYPE_MATERIAL, null);
		setAttr("list", list);
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
		String img_urls=getPara("img_urls");
		String type_id=getPara("type_id");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(img_urls)){
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
				Material material=new Material();
				material.set("title", title)
						.set("img_url", img_url)
						.set("type_id", type_id)
						.set("status", Material.STATUS_ENABLE)
						.set("create_date", new Date())
						.save();
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
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void edit() throws Exception{
		
		Material material=Material.dao.findById(getPara("id"));
		setAttr("material", material);
		//贴图分类
		List<Record> list=Type.getList(Type.TYPE_MATERIAL, null);
		setAttr("list", list);
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
		
		Material material=Material.dao.findById(getPara("id"));
		String title=getPara("title");
		String img_url=getPara("img_url");
		String type_id=getPara("type_id");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(img_url)){
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
		material.set("title", title)
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
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void changeStatus() throws Exception{
		
		Material material=Material.dao.findById(getPara("id"));
		material.set("status", getPara("status"))
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
