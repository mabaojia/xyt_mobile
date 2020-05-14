package com.project.controller.shop.api;

import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
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
@ControllerBind(controllerKey = "/shop/api/material")
public class MaterialController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void header() throws Exception{
		
		getResponse().addHeader("Access-Control-Allow-Origin", "*");
    	getResponse().addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
    	getResponse().addHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
    	getResponse().addHeader("X-Powered-By", "3.2.1");
    	getResponse().addHeader("Content-Type", "application/json;charset=utf-8");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void type() throws Exception{
		
		header();
		//贴图分类
		List<Record> list=Type.getList(Type.TYPE_MATERIAL, getLoginShopId());
		setAttr("list", list);
		List<Record> material_list=Db.find("select m.*,t.title type_title from db_material m left join db_type t on m.type_id=t.id where m.status=? and t.status=? and m.shop_id=? order by m.create_date desc", Material.STATUS_ENABLE, Type.STATUS_ENABLE, getLoginShopId());
		setAttr("material_list", material_list);
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}