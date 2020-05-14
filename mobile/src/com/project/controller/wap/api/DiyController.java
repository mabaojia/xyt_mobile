package com.project.controller.wap.api;

import java.util.Date;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Goods;
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
@ControllerBind(controllerKey = "/wap/api/diy")
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
		Diy diy=Diy.dao.findById(getPara("id"));
		if(!diy.get("shop_id").toString().equals(getLoginUser().get("shop_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String img_url=getPara("img_url");
		String content=getPara("content");
		if(StrKit.isBlank(img_url)){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		Diy user_diy=new Diy();
		user_diy.set("code", CodeUtil.getCode())
				.set("title", diy.get("title"))
				.set("img_url", img_url)
				.set("width", diy.get("width"))
				.set("height", diy.get("height"))
				.set("content", content)
				.set("type_id", diy.get("type_id"))
				.set("user_id", getLoginUserId())
				.set("brand_id", diy.get("brand_id"))
				.set("goods_id", diy.get("goods_id"))
				.set("shop_id", diy.get("shop_id"))
				.set("type", diy.get("type"))
				.set("status", Diy.STATUS_ENABLE)
				.set("create_date", new Date())
				.save();
		setAttr("diy", user_diy);
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
		if(!diy.get("shop_id").toString().equals(getLoginUser().get("shop_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		setAttr("diy", diy);
		//模板分类
		Type type=Type.dao.findById(diy.get("type_id"));
		setAttr("type", type);
		//品牌型号
		Brand brand=Brand.dao.findById(diy.get("brand_id"));
		Brand parent=Brand.dao.findById(brand.get("parent_id"));
		setAttr("brand", brand);
		setAttr("parent", parent);
		//关联商品
		Goods goods=Goods.dao.findById(diy.get("goods_id"));
		setAttr("goods", goods);
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}