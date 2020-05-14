package com.project.controller.shop;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.Type;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/diy", viewPath = "/shop/diy")
public class DiyController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void goods() throws Exception{
		
		Diy diy=Diy.dao.findById(getPara("id"));
		if(!diy.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("diy", diy);
		//商品列表
		List<Record> list=new ArrayList<Record>();
		if(diy.getInt("type")==1 || diy.getInt("type")==2){
			list=Db.find("select g.* from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.shop_id=? order by g.publish_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, diy.getInt("type"), Type.STATUS_ENABLE, getLoginShopId());
		}else{
			list=Db.find("select g.* from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.shop_id=? order by g.publish_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, diy.getInt("type") + 1, Type.STATUS_ENABLE, getLoginShopId());
		}
		setAttr("list", list);
		render("goods.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void doGoods() throws Exception{
		
		Diy diy=Diy.dao.findById(getPara("id"));
		if(!diy.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		Goods goods=Goods.dao.findById(getPara("goods_id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		diy.set("goods_id", goods.get("id"))
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
		
		Diy diy=Diy.dao.findById(getPara("id"));
		if(!diy.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		diy.set("status", getPara("status"))
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}