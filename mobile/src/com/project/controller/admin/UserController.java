package com.project.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Shop;
import com.project.model.User;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/admin/user", viewPath="/admin/user")
public class UserController extends BaseController{
	
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
		String sSelect ="select u.*, s.title shop_title, s.name shop_name, s.mobile shop_mobile";
		String sWhere =" from db_user u left join db_shop s on u.shop_id=s.id where u.status!=? and s.status!=?";
		params.add(User.STATUS_DELETE);
		params.add(Shop.STATUS_DELETE);
		if (StrKit.notBlank(getPara("sid"))) {
			sWhere+=" and u.shop_id=?";
			params.add(getPara("sid"));
			setAttr("sid", getPara("sid"));
		}
		if (StrKit.notBlank(getPara("content"))) {
			sWhere+=" and (u.name like ? or s.title like ? or s.name like ?)";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		Page<Record> results = Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//商家管理
		List<Shop> shop_list=Shop.getAll();
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
		
		User user = User.dao.findById(getPara("id"));
		user.set("status", getPara("status"))
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
