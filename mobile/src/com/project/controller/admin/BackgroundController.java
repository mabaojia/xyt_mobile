package com.project.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Background;
import com.project.model.Shop;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/admin/background", viewPath="/admin/background")
public class BackgroundController extends BaseController{
	
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		int page=getParaToInt("p", 1);
		List<Object> params= new ArrayList<Object>();
		String sSelect="select b.*, s.title shop_title, s.name shop_name, s.mobile shop_mobile";
		String sWhere=" from db_background b left join db_shop s on b.shop_id=s.id where b.status!=? and s.status!=?";
		params.add(Background.STATUS_DELETE);
		params.add(Shop.STATUS_DELETE);
		if (StrKit.notBlank(getPara("sid"))) {
			sWhere+=" and b.shop_id=?";
			params.add(getPara("sid"));
			setAttr("sid", getPara("sid"));
		}
		if (StrKit.notBlank(getPara("content"))) {
			sWhere+=" and (b.title like ? or s.title like ? or s.name like ?)";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		sWhere+=" order by b.create_date desc";
		Page<Record> results = Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//商家管理
		List<Shop> shop_list = Shop.getAll();
		setAttr("shop_list", shop_list);
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
	public void changeStatus() throws Exception{
		
		Background background = Background.dao.findById(getPara("id"));
		background.set("status", getPara("status"))
			      .update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
