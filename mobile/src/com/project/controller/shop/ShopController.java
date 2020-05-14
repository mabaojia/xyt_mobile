package com.project.controller.shop;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.common.BaseController;
import com.project.model.Freight;
import com.project.model.Shop;
import com.project.model.ShopAdmin;
import com.project.model.ShopAdminMenu;
import com.project.model.ShopMenu;
import com.project.util.DateUtil;
import com.project.util.MD5Util;
import com.project.util.QrcodeUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/shop/shop", viewPath="/shop/shop")
public class ShopController extends BaseController{
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
		String sSelect ="select s.*";
		String sWhere =" from db_shop s where s.status!=? and s.parent_id=?";
		params.add(Shop.STATUS_DELETE);
		params.add(getLoginShopId());
		if (StrKit.notBlank(getPara("content"))) {
			sWhere+=" and s.title like ? or s.name like ?";
			params.add("%" + getPara("content") + "%");
			params.add("%" + getPara("content") + "%");
			setAttr("content", getPara("content"));
		}
		Page<Record> results = Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record item : results.getList()){
			ShopAdmin shop_admin = ShopAdmin.dao.findFirst("select * from db_shop_admin where shop_id=? and type=?", item.get("id"), ShopAdmin.TYPE_ROOT);
			item.set("shop_admin", shop_admin);
		}
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
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
		
		Shop shop_1=Shop.dao.findById(getLoginShopId());
		if(shop_1.getInt("number")==0){
			setAttr("success", false);
			setAttr("msg", "没有授权权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "负责人不能为空");
			renderJson();
			return;
		}
		String mobile=getPara("mobile");
		if(StrKit.isBlank(mobile)){
			setAttr("success", false);
			setAttr("msg", "手机号不能为空");
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
		String password=getPara("password");
		if(StrKit.isBlank(password)){
			setAttr("success", false);
			setAttr("msg", "登录密码不能为空");
			renderJson();
			return;
		}
		String wx=getPara("wx");
		if(StrKit.isBlank(wx)){
			setAttr("success", false);
			setAttr("msg", "微信号不能为空");
			renderJson();
			return;
		}
		String wx_qrcode=getPara("wx_qrcode");
		if(StrKit.isBlank(wx_qrcode)){
			setAttr("success", false);
			setAttr("msg", "微信二维码不能为空");
			renderJson();
			return;
		}
		String address=getPara("address");
		if(StrKit.isBlank(address)){
			setAttr("success", false);
			setAttr("msg", "详细地址不能为空");
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
		Shop shop=new Shop();
		shop.set("title", title)
			.set("name", name)
			.set("mobile", mobile)
			.set("wx", wx)
			.set("wx_qrcode", wx_qrcode)
			.set("address", address)
			.set("tracking", getPara("tracking"))
			.set("payment", getPara("payment"))
			.set("parent_id", getLoginShopId())
			.set("status", Shop.STATUS_ENABLE)
			.set("create_date", new Date())
			.save();
		//生成二维码
		String save_path="/static/qrcode/";
        String qrcode=UUID.randomUUID().toString().replace("-", "") + ".png";
		File file=new File(getSession().getServletContext().getRealPath("/") + save_path);
		if(!file.exists()){  
			file.mkdir();
		}
		save_path="/static/qrcode/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/";
		file=new File(getSession().getServletContext().getRealPath("/") + save_path);
		if(!file.exists()){  
			file.mkdir();
		}
		file=new File(getSession().getServletContext().getRealPath("/") + save_path + qrcode);
		if(!file.exists()){  
			file.createNewFile();
		}
		QrcodeUtil.create(PropKit.get("wxUrl") + "/wap?sid=" + shop.get("id"), file);
		shop.set("qrcode", save_path + qrcode)
			.update();
		shop_admin=new ShopAdmin();
		shop_admin.set("name", "超级管理员")
					.set("email", email)
					.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
					.set("shop_id", shop.get("id"))
					.set("type", ShopAdmin.TYPE_ROOT)
					.set("create_date", new Date())
					.save();
		List<ShopMenu> menu_list=ShopMenu.dao.find("select * from db_shop_menu");
		for(ShopMenu menu : menu_list){
			ShopAdminMenu item_menu=new ShopAdminMenu();
			item_menu.set("shop_admin_id", shop_admin.get("id"))
					.set("shop_menu_id", menu.get("id"))
					.set("create_date", new Date())
					.save();
		}
		Freight freight=new Freight();
		freight.set("price", 0)
				.set("status", Freight.STATUS_ENABLE)
				.set("shop_id", shop.get("id"))
				.set("create_date", new Date())
				.save();
		shop_1.set("number", shop_1.getInt("number") - 1)
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
		
		Shop shop = Shop.dao.findById(getPara("id"));
		if (!shop.get("parent_id").toString().equals(getLoginShopId())) {
			setAttr("msg", "没有权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("shop", shop);
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
		
		Shop shop = Shop.dao.findById(getPara("id"));
		if (!shop.get("parent_id").toString().equals(getLoginShopId())) {
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "负责人不能为空");
			renderJson();
			return;
		}
		String mobile=getPara("mobile");
		if(StrKit.isBlank(mobile)){
			setAttr("success", false);
			setAttr("msg", "手机号不能为空");
			renderJson();
			return;
		}
		String wx=getPara("wx");
		if(StrKit.isBlank(wx)){
			setAttr("success", false);
			setAttr("msg", "微信号不能为空");
			renderJson();
			return;
		}
		String wx_qrcode=getPara("wx_qrcode");
		if(StrKit.isBlank(wx_qrcode)){
			setAttr("success", false);
			setAttr("msg", "微信二维码不能为空");
			renderJson();
			return;
		}
		String address=getPara("address");
		if(StrKit.isBlank(address)){
			setAttr("success", false);
			setAttr("msg", "详细地址不能为空");
			renderJson();
			return;
		}
		shop.set("title", title)
			.set("name", name)
			.set("mobile", mobile)
			.set("wx", wx)
			.set("wx_qrcode", wx_qrcode)
			.set("address", address)
			.set("tracking", getPara("tracking"))
			.set("payment", getPara("payment"))
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
		
		Shop shop = Shop.dao.findById(getPara("id"));
		if (!shop.get("parent_id").toString().equals(getLoginShopId())) {
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		shop.set("status", getPara("status"))
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
	public void editPwd() throws Exception{
		
		Shop shop = Shop.dao.findById(getPara("id"));
		if (!shop.get("parent_id").toString().equals(getLoginShopId())) {
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("shop", shop);
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
		
		Shop shop = Shop.dao.findById(getPara("id"));
		if (!shop.get("parent_id").toString().equals(getLoginShopId())) {
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
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
		ShopAdmin shop_admin = ShopAdmin.dao.findFirst("select * from db_shop_admin where shop_id=? and type=?", shop.get("id"), ShopAdmin.TYPE_ROOT);
		shop_admin.set("password", MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase())
					.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
