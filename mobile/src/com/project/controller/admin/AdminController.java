package com.project.controller.admin;

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
import com.jfinal.ext.render.CaptchaRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.project.aop.ExceptionInterceptor;
import com.project.common.BaseController;
import com.project.model.Admin;
import com.project.model.AdminMenu;
import com.project.model.Menu;
import com.project.model.Orders;
import com.project.model.User;
import com.project.util.CodeUtil;
import com.project.util.DateUtil;
import com.project.util.MD5Util;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin", viewPath = "/admin")
public class AdminController extends BaseController{
	
	private static final String RANDOM_CODE_KEY = "1";
	
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
	public void index() throws Exception{
		
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
		if(!CaptchaRender.validate(this, code.toUpperCase(), RANDOM_CODE_KEY)){
			setAttr("success", false);
			setAttr("msg", "验证码不正确");
			renderJson();
			return;
		}
		String account=getPara("account");
		String password=getPara("password");
		if(StrKit.isBlank(account) || StrKit.isBlank(password)){
			setAttr("success", false);
			setAttr("msg", "账号和密码不能为空");
			renderJson();
			return;
		}
		Admin admin=Admin.getByAccountMd5Pwd(account, MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase());
		if(admin==null){
			setAttr("success", false);
			setAttr("msg", "账号或密码不正确");
			renderJson();
			return;
		}
		List<Menu> menus=AdminMenu.getMenus(admin.get("id"));
		if(menus!=null && menus.size()>0){
			setSessionAttr("admin", admin);
			setCookie("admin_account", admin.get("account").toString(), 60 * 60 * 24 * 30);
			setCookie("admin_password", admin.get("password").toString(), 60 * 60 * 24 * 30);
			setSessionAttr("menus", menus);
			setAttr("success", true);
			setAttr("msg", "登陆成功");
			renderJson();
			return;
		}else{
			setAttr("success", false);
			setAttr("msg", "登录失败");
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
			JSONObject object=new JSONObject();
			object.put("error", 0);
			object.put("url", path);
    		renderJson(object.toJSONString());
    		return;
        }else{
        	JSONObject object=new JSONObject();
        	object.put("error", 1);
        	object.put("message", "请选择正确的图片格式");
    		renderJson(object.toJSONString());
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
		
		String save_path="/static/image/" + DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
		File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path);  
		if(!file.exists()){  
		    file.mkdirs();  
		}  
		UploadFile uploadFile=getFile("image", getRequest().getSession().getServletContext().getRealPath("/") + save_path);
		String type=uploadFile.getContentType().toLowerCase();
		if("image/jpg".equals(type) || "image/gif".equals(type) || "image/bmp".equals(type) || "image/png".equals(type) || "image/jpeg".equals(type)){
		    File rename_file=uploadFile.getFile();
		    String new_name=UUID.randomUUID().toString().replace("-", "") + "." + type.replace("image/", "");
		    rename_file.renameTo(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path + new_name));
		    String path=save_path + new_name;
		    //图片压缩至宽1000.0
		    File ys_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + path);
		    int[] with_height=getImgWidth(ys_file);
			System.out.println(with_height[0]);
			if(with_height[1] > 1000.0){
				Thumbnails.of(getRequest().getSession().getServletContext().getRealPath("/") + path).scale(CodeUtil.getNumber(1000.0 / with_height[0])).toFile(getRequest().getSession().getServletContext().getRealPath("/") + path);
			}
		    setAttr("success", true);
			setAttr("img_url", path);
			renderJson();
			return;
		}else{
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
	@Before(ExceptionInterceptor.class)
	public void code() throws Exception{
		
		render(new CaptchaRender(RANDOM_CODE_KEY));  
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void layout() throws Exception{
		
		removeSessionAttr("admin");
		removeCookie("admin_account");
		removeCookie("admin_password");
		redirect("/admin");
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
	public void dashboard() throws Exception{
		
		//会员人数
		Object today_user=User.dao.findFirst("select count(id) number from db_user where create_date>=? and create_date<=? and status!=?" , DateUtil.getStartTimeOfDay(new Date()), DateUtil.getEndTimeOfDay(new Date()), User.STATUS_DELETE).get("number");
		setAttr("today_user", today_user);
		Object yesterday_user=User.dao.findFirst("select count(id) number from db_user where create_date>=? and create_date<=? and status!=?" , DateUtil.getStartTimeOfDay(DateUtil.getYesterday()), DateUtil.getEndTimeOfDay(DateUtil.getYesterday()), User.STATUS_DELETE).get("number");
		setAttr("yesterday_user", yesterday_user);
		Object month_user=User.dao.findFirst("select count(id) number from db_user where create_date>=? and create_date<=? and status!=?" , DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getStartOfMonth(0))), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getEndOfMonth(1))), User.STATUS_DELETE).get("number");
		setAttr("month_user", month_user);
		//订单
		Object today_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(new Date())), DateUtil.formatDate(DateUtil.getEndTimeOfDay(new Date()))).get("number");
		today_money=CodeUtil.getNumber(Double.parseDouble(today_money.toString()));
		setAttr("today_money", today_money);
		Object yesterday_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getYesterday())), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getYesterday()))).get("number");
		yesterday_money=CodeUtil.getNumber(Double.parseDouble(yesterday_money.toString()));
		setAttr("yesterday_money", yesterday_money);
		Object month_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getStartOfMonth(0))), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getEndOfMonth(1)))).get("number");
		month_money=CodeUtil.getNumber(Double.parseDouble(month_money.toString()));
		setAttr("month_money", month_money);
		Object all_money=Orders.dao.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where status!=? and closed=? and display=?" , Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES).get("number");
		all_money=CodeUtil.getNumber(Double.parseDouble(all_money.toString()));
		setAttr("all_money", all_money);
		Object today_orders=Orders.dao.findFirst("select count(id) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=?"
				, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.getStartTimeOfDay(new Date()), DateUtil.getEndTimeOfDay(new Date())).get("number");
		setAttr("today_orders", today_orders);
		Object yesterday_orders=Orders.dao.findFirst("select count(id) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=?"
				, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.getStartTimeOfDay(DateUtil.getYesterday()), DateUtil.getEndTimeOfDay(DateUtil.getYesterday())).get("number");
		setAttr("yesterday_orders", yesterday_orders);
		Object month_orders=Orders.dao.findFirst("select count(id) number from db_orders where status!=? and closed=? and display=? and create_date>=? and create_date<=?"
				, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES, DateUtil.formatDate(DateUtil.getStartTimeOfDay(DateUtil.getStartOfMonth(0))), DateUtil.formatDate(DateUtil.getEndTimeOfDay(DateUtil.getEndOfMonth(1)))).get("number");
		setAttr("month_orders", month_orders);
		//近30天统计图
		List<Record> orders_list=new ArrayList<Record>();
		for(int i=30; i>=0; i--){
			Record orders=new Record();
			Date date=DateUtil.getDayAfter(0 - i);
			orders.set("create_date", DateUtil.formatDate(date, "MM/dd"));
			Date day_start=DateUtil.getStartTimeOfDay(date);
			Date day_end=DateUtil.getEndTimeOfDay(date);
			long zongliang=Db.findFirst("select count(id) number from db_orders where create_date>=? and create_date<=? and status!=? and closed=? and display=?", day_start, day_end, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES).getLong("number");
			orders.set("zongliang", zongliang);
			Object account=Db.findFirst("select ifnull(sum(grand_total), 0) number from db_orders where create_date>=? and create_date<=? and status!=? and closed=? and display=?", day_start, day_end, Orders.STATUS_NOT_PAY, Orders.CLOSED_NO, Orders.DISPLAY_YES).get("number");
			account=CodeUtil.getNumber(Double.parseDouble(account.toString()));
			orders.set("account", account);
			long user=Db.findFirst("select count(id) number from db_user where create_date>=? and create_date<=? and status!=?", day_start, day_end, User.STATUS_DELETE).getLong("number");
			orders.set("user", user);
			orders_list.add(orders);
		}
		setAttr("orders_list", orders_list);
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
