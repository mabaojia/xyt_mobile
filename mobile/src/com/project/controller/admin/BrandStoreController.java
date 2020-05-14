package com.project.controller.admin;

import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;

/**
 *
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/brand/store", viewPath = "/admin/brand/store")
public class BrandStoreController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Record> list=Brand.getAll(null, null, getParaToInt("type"));
		for(Record item : list){
			item.set("brand_list", Brand.getAll(item.get("id"), null, getParaToInt("type")));
		}
		setAttr("list", list);
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
	public void list_1() throws Exception{
		
		Brand brand=Brand.dao.findById(getPara("id"));
		setAttr("brand", brand);
		//类型
		setAttr("type", brand.getInt("type"));
		List<Record> list=Brand.getAll(getPara("id"), null,brand.get("type"));
		for(Record item : list){
			//手机壳
			Diy diy_1=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIKE);
			item.set("diy_1", diy_1);
			if(diy_1!=null){
				item.set("diys_1", 1);
			}else{
				item.set("diys_1", 0);
			}
			//手机膜
			Diy diy_2=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIMO);
			item.set("diy_2", diy_2);
			if(diy_2!=null){
				item.set("diys_2", 1);
			}else{
				item.set("diys_2", 0);
			}
			//定制模板
			Diy diy=Diy.getByBrandType(item.get("id").toString(), brand.getInt("type"));
			item.set("diy", diy);
			if(diy!=null){
				item.set("diys", 1);
			}else{
				item.set("diys", 0);
			}
		}
		setAttr("list", list);
		render("list_1.htm");
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
		
		if(StrKit.notBlank(getPara("id"))){
			setAttr("id", getPara("id"));
		}
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
		Brand brand=new Brand();
		if(StrKit.notBlank(getPara("id"))){
			brand.set("parent_id", getPara("id"));
		}
		brand.set("title", title)
				.set("idx", idx)
				.set("status", Brand.STATUS_ENABLE)
				.set("type", getParaToInt("type"))
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
		
		Brand brand=Brand.dao.findById(getPara("id"));
		setAttr("brand", brand);
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
		
		Brand brand=Brand.dao.findById(getPara("id"));
		String title=getPara("title");
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
		brand.set("title", title)
				.set("idx", idx)
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

		Brand brand=Brand.dao.findById(getPara("id"));
		brand.set("status", getPara("status"))
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
