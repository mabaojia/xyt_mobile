package com.project.controller.shop;

import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Area;
import com.project.model.Freight;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/freight", viewPath = "/shop/freight")
public class FreightController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Freight> list=Freight.getAll(getLoginShopId());
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
		
		//地区列表
		List<Area> area_list=Area.getByParent(null);
		setAttr("area_list", area_list);
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
		
		String price=getPara("price");
		if(StrKit.isBlank(price)){
			setAttr("success", false);
			setAttr("msg", "运费不能为空");
			renderJson();
			return;
		}
		String area_id=getPara("area_id");
		Freight freight=Freight.getByAreaShop(area_id, getLoginShopId());
		if(freight!=null){
			setAttr("success", false);
			setAttr("msg", "该地区已存在运费模板");
			renderJson();
			return;
		}
		freight=new Freight();
		freight.set("area_id", area_id)
				.set("price", price)
				.set("shop_id", getLoginShopId())
				.set("status", Freight.STATUS_ENABLE)
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
		
		Freight freight=Freight.dao.findById(getPara("id"));
		if(!freight.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htn");
			return;
		}
		setAttr("freight", freight);
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
		
		Freight freight=Freight.dao.findById(getPara("id"));
		if(!freight.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String price=getPara("price");
		if(StrKit.isBlank(price)){
			setAttr("success", false);
			setAttr("msg", "运费不能为空");
			renderJson();
			return;
		}
		freight.set("price", price)
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
		
		Freight freight=Freight.dao.findById(getPara("id"));
		if(!freight.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		freight.set("status", getPara("status"))
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
