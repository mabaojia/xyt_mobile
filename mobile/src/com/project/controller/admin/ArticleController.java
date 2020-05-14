package com.project.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Article;
import com.project.model.Shop;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/admin/article", viewPath="/admin/article")
public class ArticleController extends BaseController{

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
		List<Object> params =new ArrayList<Object>();
		String sSelect ="select a.*, s.title shop_title, s.name shop_name, s.mobile shop_mobile";
		String sWhere =" from db_article a left join db_shop s on a.shop_id=s.id where a.status!=? and (a.shop_id is null or s.status!=?)";
		params.add(Article.STATUS_DELETE);
		params.add(Shop.STATUS_DELETE);
		if (StrKit.notBlank(getPara("sid"))) {
			sWhere+=" and a.shop_id=?";
			params.add(getPara("sid"));
			setAttr("sid", getPara("sid"));
		}
		if (StrKit.notBlank(getPara("content"))) {
			sWhere+=" and (a.title like ? or s.title like ? or s.name like ?)";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		Page<Record> results = Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//商家管理
		List<Shop> shop_list = Shop.getAll();
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
	public void edit() throws Exception{
		
		Article article=Article.dao.findById(getPara("id"));
		setAttr("article", article);
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
		
		Article article=Article.dao.findById(getPara("id"));
		String title=getPara("title");
		String content=getPara("content");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		article.set("title", title)
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
	public void changeStatus() throws Exception{
		
		Article article = Article.dao.findById(getPara("id"));
		article.set("status", getPara("status"))
		       .update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
