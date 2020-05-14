package com.project.controller.admin;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.common.BaseController;
import com.project.model.Admin;
import com.project.model.AdminMenu;
import com.project.util.MD5Util;

/**
 * 超级管理
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/root", viewPath = "/admin/root")
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
		
		List<Record> admin_list=Db.find("select * from db_admin where type=? and status!=?", Admin.TYPE_2, Admin.STATUS_DELETE);
		for(Record item : admin_list){
			item.set("menu_list", AdminMenu.getMenus(item.get("id")));
		}
		setAttr("admin_list", admin_list);
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
		
		List<Record> menu_list=Db.find("select * from db_menu order by idx asc");
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
		
		if(getLoginAdmin().getInt("type")!=Admin.TYPE_1){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String account=getPara("account");
		if(StrKit.isBlank(account)){
			setAttr("success", false);
			setAttr("msg", "登录账号不能为空");
			renderJson();
			return;
		}
		Admin admin=Admin.getByAccount(account);
		if(admin!=null){
			setAttr("success", false);
			setAttr("msg", "登录账号已经注册");
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
		admin=new Admin();
		admin.set("account", account)
				.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
				.set("type", Admin.TYPE_2)
				.set("create_date", new Date())
				.save();
		for(String mid : mids){
			AdminMenu menu=new AdminMenu();
			menu.set("admin_id", admin.get("id"))
				.set("menu_id", mid)
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
		
		Admin admin=Admin.dao.findById(getPara("id"));
		if(getLoginAdmin().getInt("type")!=Admin.TYPE_1){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		admin.set("status", getPara("status"))
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
	public void edit() throws Exception{
		
		Admin admin=Admin.dao.findById(getPara("id"));
		setAttr("admin", admin);
		List<Record> menu_list=Db.find("select * from db_menu order by idx asc");
		for(Record item : menu_list){
			AdminMenu menu=AdminMenu.getByAdminMenu(admin.get("id"), item.get("id"));
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
		
		if(getLoginAdmin().getInt("type")!=Admin.TYPE_1){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String account=getPara("account");
		if(StrKit.isBlank(account)){
			setAttr("success", false);
			setAttr("msg", "登录账号不能为空");
			renderJson();
			return;
		}
		Admin admin=Admin.dao.findById(getPara("id"));
		if(!account.equals(admin.get("account").toString())){
			admin=Admin.getByAccount(account);
			if(admin!=null){
				setAttr("success", false);
				setAttr("msg", "登录账号已经注册");
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
		admin.set("account", account)
				.update();
		Db.update("delete from db_admin_menu where admin_id=?", admin.get("id"));
		for(String mid : mids){
			AdminMenu menu=new AdminMenu();
			menu.set("admin_id", admin.get("id"))
				.set("menu_id", mid)
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
	public void editPwd() throws Exception{
		
		Admin admin = Admin.dao.findById(getPara("id"));
		setAttr("admin", admin);
		render("editPwd.htm");
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
		
		if(getLoginAdmin().getInt("type")!=Admin.TYPE_1){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		Admin admin = Admin.dao.findById(getPara("id"));
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
		admin.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}