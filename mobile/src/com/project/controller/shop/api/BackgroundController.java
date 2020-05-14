package com.project.controller.shop.api;

import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Background;
import com.project.model.Type;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/api/background")
public class BackgroundController extends BaseController{

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
		//图库分类
		List<Record> list=Type.getList(Type.TYPE_BACKGROUND, getLoginShopId());
		setAttr("list", list);
		List<Record> background_list=Db.find("select b.*,t.title type_title from db_background b left join db_type t on b.type_id=t.id where b.status=? and t.status=? and b.shop_id=? order by b.create_date desc", Background.STATUS_ENABLE, Type.STATUS_ENABLE, getLoginShopId());
		setAttr("background_list", background_list);
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}