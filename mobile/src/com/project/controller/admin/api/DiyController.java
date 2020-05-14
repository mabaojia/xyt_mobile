package com.project.controller.admin.api;

import java.util.Date;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Type;
import com.project.util.CodeUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/api/diy")
public class DiyController extends BaseController{

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
	public void save() throws Exception{
		
		header();
		String type=getPara("type");
		String title=getPara("title");
		String img_url=getPara("img_url");
		String width=getPara("width");
		String height=getPara("height");
		String content=getPara("content");
		String brand_id=getPara("brand_id");
		if(StrKit.isBlank(type)){
			setAttr("success", false);
			setAttr("msg", "类型不能为空");
			renderJson();
			return;
		}
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
		if(StrKit.isBlank(width)){
			setAttr("success", false);
			setAttr("msg", "宽度不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(height)){
			setAttr("success", false);
			setAttr("msg", "高度不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(brand_id)){
			setAttr("success", false);
			setAttr("msg", "品牌型号不能为空");
			renderJson();
			return;
		}
		Diy diy=Diy.getByBrandType(brand_id, getPara("type"));
		if(diy!=null){
			setAttr("success", false);
			setAttr("msg", "该品牌型号下面已经存在模板");
			renderJson();
			return;
		}
		diy=new Diy();
		diy.set("code", CodeUtil.getCode())
			.set("title", title)
			.set("img_url", img_url)
			.set("width", width)
			.set("height", height)
			.set("content", content)
			.set("brand_id", brand_id)
			.set("type", type)
			.set("status", Diy.STATUS_ENABLE)
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
	public void update() throws Exception{
		
		header();
		Diy diy=Diy.dao.findById(getPara("id"));
		String title=getPara("title");
		String img_url=getPara("img_url");
		String width=getPara("width");
		String height=getPara("height");
		String content=getPara("content");
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
		if(StrKit.isBlank(width)){
			setAttr("success", false);
			setAttr("msg", "宽度不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(height)){
			setAttr("success", false);
			setAttr("msg", "高度不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		diy.set("title", title)
			.set("img_url", img_url)
			.set("width", width)
			.set("height", height)
			.set("content", content)
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
	public void item() throws Exception{
		
		header();
		Diy diy=Diy.dao.findById(getPara("id"));
		setAttr("diy", diy);
		//模板分类
		Type type=Type.dao.findById(diy.get("type_id"));
		setAttr("type", type);
		//品牌型号
		Brand brand=Brand.dao.findById(diy.get("brand_id"));
		Brand parent=Brand.dao.findById(brand.get("parent_id"));
		setAttr("brand", brand);
		setAttr("parent", parent);
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}