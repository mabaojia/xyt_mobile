package com.project.controller.shop;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Background;
import com.project.model.Type;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/background/store", viewPath = "/shop/background/store")
public class BackgroundStoreController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		int page=getParaToInt("p",1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select b.*,t.title type_title";
		String sWhere=" from db_background b left join db_type t on b.type_id=t.id where b.status=? and t.status=? and b.shop_id is null";
		params.add(Background.STATUS_ENABLE);
		params.add(Type.STATUS_ENABLE);
		if(StrKit.notBlank(getPara("tid"))){
			sWhere+=" and b.type_id=?";
			params.add(getPara("tid"));
			setAttr("tid", getParaToInt("tid"));
		}
		sWhere+=" order by b.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//图库分类
		List<Record> list=Type.getList(Type.TYPE_BACKGROUND, null);
		setAttr("list", list);
		render("list.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void addAll() throws Exception{
		
		String img_urls = ",";
		List<String> img_list = new ArrayList<String>();
		for (String bid : getPara("bids").toString().split(",")) {
			if (StrKit.notBlank(bid)) {
				Background background = Background.dao.findById(bid);
				img_list.add(background.get("img_url").toString());
				img_urls+= background.get("img_url").toString() + ",";
			}
		}
		setAttr("img_urls", img_urls);
		setAttr("img_list", img_list);
		//图库分类
		List<Record> type_list = Type.getList(Type.TYPE_BACKGROUND, getLoginShopId());
		setAttr("type_list", type_list);
		render("addAll.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void exportAll() throws Exception{
		
		String img_urls=",";
		List<String> img_list=new ArrayList<String>();
		List<Object> params = new ArrayList<Object>();
		String sSelect="select b.*,t.title type_title";
		String sWhere=" from db_background b left join db_type t on b.type_id=t.id where b.status=? and t.status=? and b.shop_id is null";
		params.add(Background.STATUS_ENABLE);
		params.add(Type.STATUS_ENABLE);
		if(StrKit.notBlank(getPara("tid"))){
			sWhere+=" and b.type_id=?";
			params.add(getPara("tid"));
			setAttr("tid", getParaToInt("tid"));
		}
		sWhere+=" order by b.create_date desc";
		List<Record> list=Db.find(sSelect + sWhere, params.toArray());
		for (Record background : list){
			img_list.add(background.get("img_url").toString());
			img_urls+=background.get("img_url").toString() + ",";
		}
		setAttr("img_list", img_list);
		setAttr("img_urls", img_urls);
		//图库分类
		List<Record> type_list = Type.getList(Type.TYPE_BACKGROUND, getLoginShopId());
		setAttr("type_list", type_list);
		render("addAll.htm");
	}
}
