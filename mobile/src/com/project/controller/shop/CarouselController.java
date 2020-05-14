package com.project.controller.shop;

import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Carousel;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/carousel", viewPath = "/shop/carousel")
public class CarouselController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Record> list=Carousel.getAll(getLoginShopId());
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
		
		String title=getPara("title");
		String img_url=getPara("img_url");
		String url=getPara("url");
		String idx=getPara("idx");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(img_url)){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(url)){
			setAttr("success", false);
			setAttr("msg", "链接不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(idx)){
			setAttr("success", false);
			setAttr("msg", "排序不能为空");
			renderJson();
			return;
		}
		Carousel carousel=new Carousel();
		carousel.set("title", title)
				.set("img_url", img_url)
				.set("url", url)
				.set("idx", idx)
				.set("shop_id", getLoginShopId())
				.set("status", Carousel.STATUS_ENABLE)
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
		
		Carousel carousel=Carousel.dao.findById(getPara("id"));
		if(!carousel.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("carousel", carousel);
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
		
		Carousel carousel=Carousel.dao.findById(getPara("id"));
		if(!carousel.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		String img_url=getPara("img_url");
		String url=getPara("url");
		String idx=getPara("idx");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(img_url)){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(url)){
			setAttr("success", false);
			setAttr("msg", "链接不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(idx)){
			setAttr("success", false);
			setAttr("msg", "排序不能为空");
			renderJson();
			return;
		}
		carousel.set("title", title)
				.set("img_url", img_url)
				.set("url", url)
				.set("idx", idx)
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
		
		Carousel carousel=Carousel.dao.findById(getPara("id"));
		if(!carousel.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		carousel.set("status", getPara("status"))
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
