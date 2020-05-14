package com.project.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Goods;
import com.project.model.Shop;


/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/admin/goods", viewPath="/admin/goods")
public class GoodsController extends BaseController{

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
		List<Object> params = new ArrayList<Object>();
		String sSelect="select g.*, s.title shop_title ,s.name shop_name, s.mobile shop_mobile";
		String sWhere=" from db_goods g left join db_shop s on g.shop_id=s.id where g.status!=? and s.status!=?";
		params.add(Goods.STATUS_DELETE);
		params.add(Shop.STATUS_DELETE);
		if (StrKit.notBlank(getPara("source"))) {
			sWhere+=" and g.source=?";
			params.add(getPara("source"));
			setAttr("source", getPara("source"));
		}
		if (StrKit.notBlank(getPara("sid"))) {
			sWhere+=" and g.shop_id=?";
			params.add(getPara("sid"));
			setAttr("sid", getPara("sid"));
		}
		if (StrKit.notBlank(getPara("content"))) {
			sWhere+=" and (g.top like ? or s.name like ? or s.title like ?) ";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		sWhere+=" order by g.publish_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
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
		
		Goods goods = Goods.dao.findById(getPara("id"));
		goods.set("status", getPara("status"))
			 .update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
