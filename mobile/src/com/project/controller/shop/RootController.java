package com.project.controller.shop;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.aop.ShopRootInterceptor;
import com.project.common.BaseController;
import com.project.model.Shop;
import com.project.model.ShopAdmin;
import com.project.model.ShopAdminMenu;
import com.project.util.MD5Util;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@Before(ShopRootInterceptor.class)
@ControllerBind(controllerKey = "/shop/root", viewPath = "/shop/root")
public class RootController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Record> list=ShopAdmin.getAll(getLoginShopId());
		for(Record item : list){
			item.set("menu_list", ShopAdminMenu.getMenus(item.get("id")));
		}
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
		
		List<Record> menu_list=Db.find("select * from db_shop_menu order by idx asc");
		setAttr("menu_list", menu_list);
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
	@Before(Tx.class)
	public void save() throws Exception{
		
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "姓名不能为空");
			renderJson();
			return;
		}
		String email=getPara("email");
		if(StrKit.isBlank(email)){
			setAttr("success", false);
			setAttr("msg", "登录账号不能为空");
			renderJson();
			return;
		}
		ShopAdmin shop_admin=ShopAdmin.getByEmail(email);
		if(shop_admin!=null){
			setAttr("success", false);
			setAttr("msg", "登录账号已经使用");
			renderJson();
			return;
		}
		String password=getPara("password");
		if(StrKit.isBlank(password)){
			setAttr("success", false);
			setAttr("msg", "登录密码不能为空");
			renderJson();
			return;
		}
		String[] mids=getParaValues("mids");
		if(mids==null || mids.length==0){
			setAttr("success", false);
			setAttr("msg", "菜单权限不能为空");
			renderJson();
			return;
		}
		shop_admin=new ShopAdmin();
		shop_admin.set("name", name)
					.set("email", email)
					.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
					.set("shop_id", getLoginShopId())
					.set("type", ShopAdmin.TYPE_ADMIN)
					.set("create_date", new Date())
					.save();
		for(String mid : mids){
			ShopAdminMenu menu=new ShopAdminMenu();
			menu.set("shop_admin_id", shop_admin.get("id"))
				.set("shop_menu_id", mid)
				.set("create_date", new Date())
				.save();
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
		
		ShopAdmin shop_admin=ShopAdmin.dao.findById(getPara("id"));
		if(!shop_admin.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("shop_admin", shop_admin);
		List<Record> menu_list=Db.find("select * from db_shop_menu order by idx asc");
		for(Record item : menu_list){
			ShopAdminMenu menu=ShopAdminMenu.getByAdminMenu(shop_admin.get("id"), item.get("id"));
			if(menu==null){
				item.set("checked", 0);
			}else{
				item.set("checked", 1);
			}
		}
		setAttr("menu_list", menu_list);
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
	@Before(Tx.class)
	public void update() throws Exception{
		
		ShopAdmin shop_admin=ShopAdmin.dao.findById(getPara("id"));
		if(!shop_admin.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "姓名不能为空");
			renderJson();
			return;
		}
		String email=getPara("email");
		if(StrKit.isBlank(email)){
			setAttr("success", false);
			setAttr("msg", "登录账号不能为空");
			renderJson();
			return;
		}
		if(!email.equals(shop_admin.get("email").toString())){
			shop_admin=ShopAdmin.getByEmail(email);
			if(shop_admin!=null){
				setAttr("success", false);
				setAttr("msg", "登录账号已经使用");
				renderJson();
				return;
			}
		}
		String[] mids=getParaValues("mids");
		if(mids==null || mids.length==0){
			setAttr("success", false);
			setAttr("msg", "菜单权限不能为空");
			renderJson();
			return;
		}
		shop_admin=ShopAdmin.dao.findById(getPara("id"));
		shop_admin.set("name", name)
					.set("email", email)
					.update();
		Db.update("delete from db_shop_admin_menu where shop_admin_id=?", shop_admin.get("id"));
		for(String mid : mids){
			ShopAdminMenu menu=new ShopAdminMenu();
			menu.set("shop_admin_id", shop_admin.get("id"))
				.set("shop_menu_id", mid)
				.set("create_date", new Date())
				.save();
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
	public void changeStatus() throws Exception{
		
		ShopAdmin shop_admin=ShopAdmin.dao.findById(getPara("id"));
		if(!shop_admin.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		shop_admin.set("status", getPara("status"))
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
	public void pwd() throws Exception{
		
		render("pwd.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void updatePwd() throws Exception{
		
		String password=getPara("password");
		String check_password=getPara("check_password");
		if(StrKit.isBlank(password)){
			setAttr("success",false);
			setAttr("msg", "密码不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(check_password)){
			setAttr("success",false);
			setAttr("msg", "确认密码不能为空");
			renderJson();
			return;
		}
		if(!password.equals(check_password)){
			setAttr("success",false);
			setAttr("msg", "两次密码不一致");
			renderJson();
			return;
		}
		Shop shop=Shop.dao.findById(getLoginShopId());
		shop.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}