package com.project.controller.shop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import net.coobird.thumbnailator.Thumbnails;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.project.aop.ExceptionInterceptor;
import com.project.common.BaseController;
import com.project.model.Article;
import com.project.model.Orders;
import com.project.model.Shop;
import com.project.model.ShopAdmin;
import com.project.model.ShopAdminMenu;
import com.project.model.UploadImageUpyun;
import com.project.util.CodeUtil;
import com.project.util.DateUtil;
import com.project.util.MD5Util;
import com.project.util.QrcodeUtil;
import com.project.util.ValidateCode;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop", viewPath = "/shop")
public class AdminController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@ClearInterceptor(ClearLayer.ALL)
	public void index() throws Exception{
		
		setAttr("article", Article.dao.findById(1));
		render("index.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@ClearInterceptor(ClearLayer.ALL)
	@Before(ExceptionInterceptor.class)
	public void login() throws Exception{
		
		String code=getPara("code");  
		if(!ValidateCode.validate(getRequest().getSession(), code.toUpperCase())){
			setAttr("success", false);
			setAttr("msg", "验证码不正确");
			renderJson();
			return;
		}
		String email=getPara("email");
		String password=getPara("password");
		if(StrKit.isBlank(email) || StrKit.isBlank(password)){
			setAttr("success", false);
			setAttr("msg", "账号和密码不能为空");
			renderJson();
			return;
		}
		ShopAdmin shop_admin=ShopAdmin.getByEmailMd5Pwd(email, MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase());
		if (shop_admin==null) {
			setAttr("success", false);
			setAttr("msg", "账号或密码不正确");
			renderJson();
			return;
		}
		List<ShopAdminMenu> shop_menus=ShopAdminMenu.getMenus(shop_admin.get("id"));
		if(shop_menus==null || shop_menus.size()==0){
			setAttr("success", false);
			setAttr("msg", "没有后台登录权限");
			renderJson();
			return;
		}
		Shop shop=Shop.dao.findById(shop_admin.get("shop_id"));
		if(shop.getInt("status")!=Shop.STATUS_ENABLE){
			setAttr("success", false);
			setAttr("msg", "没有后台登录权限");
			renderJson();
			return;
		}
		setSessionAttr("shop", shop);
		setSessionAttr("shop_menus", shop_menus);
		setSessionAttr("shop_admin", shop_admin);
		setCookie("shop_email", shop_admin.get("email").toString(), 60 * 60 * 24 * 30);
		setCookie("shop_password", shop_admin.get("password").toString(), 60 * 60 * 24 * 30);
		setAttr("success", true);
		setAttr("msg", "登陆成功");
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
	public void blank() throws Exception{
		
		render("blank.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@ClearInterceptor(ClearLayer.ALL)
	public void code() throws Exception{
		
		File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + "/static/qrcode/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/");  
        if(!file.exists()){  
            file.mkdirs();  
        }  
        ValidateCode vCode = new ValidateCode(200, 50, 4, 125, getRequest().getSession());
		String path=getRequest().getSession().getServletContext().getRealPath("/") + "/static/qrcode/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/" + UUID.randomUUID().toString().replace("-", "") + ".png";
		vCode.write(path);
		renderFile(new File(path));
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
	@ClearInterceptor(ClearLayer.ALL)
	public void layout() throws Exception{
		
		removeSessionAttr("shop");
		removeSessionAttr("shop_menus");
		removeSessionAttr("shop_admin");
		removeCookie("shop_email");
		removeCookie("shop_password");
		redirect("/shop");
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
	public void editorUploadImg() throws Exception{
		
		String save_path="/static/image/" + DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";  
        File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path); 
        if(!file.exists()){  
            file.mkdirs();  
        }  
        UploadFile uploadFile=getFile("imgFile", getRequest().getSession().getServletContext().getRealPath("/") + save_path);
        String type=uploadFile.getContentType().toLowerCase();
        if("image/jpg".equals(type) || "image/gif".equals(type) || "image/bmp".equals(type) || "image/png".equals(type) || "image/jpeg".equals(type)){
	        File rename_file=uploadFile.getFile();
	        String new_name=UUID.randomUUID().toString().replace("-", "") + "." + type.replace("image/", "");
	        rename_file.renameTo(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path+new_name));
	        String path=save_path + new_name;
	        //图片压缩至宽1000.0
		    File ys_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + path);
		    int[] with_height=getImgWidth(ys_file);
			System.out.println(with_height[0]);
			if(with_height[1] > 1000.0){
				Thumbnails.of(getRequest().getSession().getServletContext().getRealPath("/") + path).scale(CodeUtil.getNumber(1000.0 / with_height[0])).toFile(getRequest().getSession().getServletContext().getRealPath("/") + path);
			}
			
			try {
	        	String fwqImgagepath = getRequest().getSession().getServletContext().getRealPath("/") + path;
	        	String imagePath = UploadImageUpyun.testWriteFile(fwqImgagepath, type.replace("image/", ""));
	        	ys_file.delete();
		        if(imagePath.equals("")){
		        	JSONObject obj = new JSONObject();
		    		obj.put("error", 1);
		    		obj.put("message", "请选择正确的图片格式");
		    		renderJson(obj.toJSONString());
		    		return;
		        }else{
		        	JSONObject obj=new JSONObject();
		    		obj.put("error", 0);
		    		obj.put("url",imagePath);
		    		renderJson(obj.toJSONString());
		    		return;
		        }
			} catch (Exception e) {	
				JSONObject obj = new JSONObject();
	    		obj.put("error", 1);
	    		obj.put("message", "请选择正确的图片格式");
	    		renderJson(obj.toJSONString());
	    		return;
			}
        }else{
        	JSONObject obj = new JSONObject();
    		obj.put("error", 1);
    		obj.put("message", "请选择正确的图片格式");
    		renderJson(obj.toJSONString());
    		return;
        }
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void uploadImg() throws Exception{
		
		String save_path="/static/image/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/";
		File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path);  
		if(!file.exists()){  
		    file.mkdirs();  
		}  
		UploadFile uploadFile=getFile("image", getRequest().getSession().getServletContext().getRealPath("/") + save_path);
		String type=uploadFile.getContentType().toLowerCase();
	    File rename_file=uploadFile.getFile();
	    String new_name=UUID.randomUUID().toString().replace("-", "") + "." + type.replace("image/", "").replace("+xml", "");
	    rename_file.renameTo(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path + new_name));
	    String path=save_path + new_name;
	    //图片压缩至宽1000.0
	    File ys_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + path);
	    int[] with_height=getImgWidth(ys_file);
		System.out.println(with_height[0]);
		if(with_height[1] > 1000.0){
			Thumbnails.of(getRequest().getSession().getServletContext().getRealPath("/") + path).scale(CodeUtil.getNumber(1000.0 / with_height[0])).toFile(getRequest().getSession().getServletContext().getRealPath("/") + path);
		}
		
		try {
        	String fwqImgagepath = getRequest().getSession().getServletContext().getRealPath("/") + path;
        	String imagePath = UploadImageUpyun.testWriteFile(fwqImgagepath, type.replace("image/", ""));
        	ys_file.delete();
	        if(imagePath.equals("")){
	        	setAttr("success", false);
				setAttr("msg", "请选择正确图片格式");
				renderJson();
				return;
	        }else{
	        	setAttr("success", true);
				setAttr("img_url", imagePath);
				renderJson();
				return;
	        }
		} catch (Exception e) {	
			setAttr("success", false);
			setAttr("msg", "请选择正确图片格式");
			renderJson();
			return;
		}
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void dashboard() throws Exception{
		
		//订单
		Object today_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=? and shop_id=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(new Date())), DateUtil.formatDate(DateUtil.getEndTimeOfDay(new Date())), getLoginShopId()).get("number");
		today_money=CodeUtil.getNumber(Double.parseDouble(today_money.toString()));
		setAttr("today_money", today_money);
		Object yesterday_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=? and shop_id=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getYesterday())), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getYesterday())), getLoginShopId()).get("number");
		yesterday_money=CodeUtil.getNumber(Double.parseDouble(yesterday_money.toString()));
		setAttr("yesterday_money", yesterday_money);
		Object month_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=? and shop_id=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getStartOfMonth(0))), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getEndOfMonth(1))), getLoginShopId()).get("number");
		month_money=CodeUtil.getNumber(Double.parseDouble(month_money.toString()));
		setAttr("month_money", month_money);
		Object all_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and shop_id=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, getLoginShopId()).get("number");
		all_money=CodeUtil.getNumber(Double.parseDouble(all_money.toString()));
		setAttr("all_money", all_money);
		Object today_orders=Orders.dao.findFirst("select count(id) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=? and shop_id=?"
				, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.getStartTimeOfDay(new Date()), DateUtil.getEndTimeOfDay(new Date()), getLoginShopId()).get("number");
		setAttr("today_orders", today_orders);
		Object yesterday_orders=Orders.dao.findFirst("select count(id) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=? and shop_id=?"
				, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.getStartTimeOfDay(DateUtil.getYesterday()), DateUtil.getEndTimeOfDay(DateUtil.getYesterday()), getLoginShopId()).get("number");
		setAttr("yesterday_orders", yesterday_orders);
		Object month_orders=Orders.dao.findFirst("select count(id) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=? and shop_id=?"
				, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getStartOfMonth(0))), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getEndOfMonth(1))), getLoginShopId()).get("number");
		setAttr("month_orders", month_orders);
		//近30天统计图
		List<Record> orders_list=new ArrayList<Record>();
		for(int i=30; i>=0; i--){
			Record orders=new Record();
			Date date=DateUtil.getDayAfter(0 - i);
			orders.set("create_date", DateUtil.formatDate(date, "MM/dd"));
			Date day_start=DateUtil.getStartTimeOfDay(date);
			Date day_end=DateUtil.getEndTimeOfDay(date);
			long zongliang=Db.findFirst("select count(id) number from db_orders where create_date>=? and create_date<=? and status!=? and closed=? and display=? and shop_id=?", day_start, day_end, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, getLoginShopId()).getLong("number");
			orders.set("zongliang", zongliang);
			Object account=Db.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where create_date>=? and create_date<=? and status!=? and closed=? and display=? and shop_id=?", day_start, day_end, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, getLoginShopId()).get("number");
			account=CodeUtil.getNumber(Double.parseDouble(account.toString()));
			orders.set("account", account);
			orders_list.add(orders);
		}
		setAttr("orders_list", orders_list);
		Shop shop=Shop.dao.findById(getLoginShopId());
		if(shop.get("qrcode")==null || StrKit.isBlank(shop.get("qrcode").toString())){
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
		}
		setAttr("shop", shop);
		render("dashboard.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static int[] getImgWidth(File file) { 
		
        InputStream is = null;  
        BufferedImage src = null;  
        int result[] = { 0, 0 };  
        try {  
            is = new FileInputStream(file);  
            src = javax.imageio.ImageIO.read(is);  
            result[0] = src.getWidth(null);//得到源图宽  
            result[1] = src.getHeight(null);//得到源图高  
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }
}
