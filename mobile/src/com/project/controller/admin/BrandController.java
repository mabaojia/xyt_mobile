package com.project.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Shop;

/**
 *
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/brand", viewPath = "/admin/brand")
public class BrandController extends BaseController{
	
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
		String sWhere=" from db_brand b left join db_shop s on b.shop_id=s.id where b.status!=? and b.parent_id is null and s.status!=?";
		params.add(Brand.STATUS_DELETE);
		params.add(Shop.STATUS_DELETE);
		if (StrKit.notBlank(getPara("type"))) {
			sWhere+=" and b.type=?";
			params.add(getParaToInt("type"));
			setAttr("type", getParaToInt("type"));
		}
		if (StrKit.notBlank(getPara("sid"))) {
			sWhere+=" and b.shop_id=?";
			params.add(getPara("sid"));
			setAttr("sid", getPara("sid"));
		}
		sWhere+=" order by b.create_date desc";
		Page<Record> results = Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record item : results.getList()){
			item.set("brand_list", Brand.getAll(item.get("id"), item.get("shop_id"), getPara("type")));
		}
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
	public void list_1() throws Exception{
		
		Brand brand=Brand.dao.findById(getPara("id"));
		setAttr("brand", brand);
		//类型
		setAttr("type", brand.getInt("type"));
		List<Record> list=Brand.getAll(getPara("id"), brand.get("shop_id"), brand.get("type"));
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
	public void changeStatus() throws Exception{

		Brand brand=Brand.dao.findById(getPara("id"));
		brand.set("status", getPara("status"))
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
	public void changeStatusAll() throws Exception{
		
		for (String bids : getPara("bids").toString().split(",")) {
			if (StrKit.notBlank(bids)) {
				Brand brand = Brand.dao.findById(bids);
				brand.set("status", getPara("status"))
				     .update();
			}
		}
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
