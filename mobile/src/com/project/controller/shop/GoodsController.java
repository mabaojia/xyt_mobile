package com.project.controller.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.common.BaseController;
import com.project.model.Goods;
import com.project.model.GoodsFormat;
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
@ControllerBind(controllerKey = "/shop/goods", viewPath = "/shop/goods")
public class GoodsController extends BaseController{
	
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
		String sSelect="select g.*,t.title type_title";
		String sWhere=" from db_goods g left join db_type t on g.type_id=t.id where g.status!=? and t.status=? and g.shop_id=?";
		params.add(Goods.STATUS_DELETE);
		params.add(Type.STATUS_ENABLE);
		params.add(getLoginShopId());
		if(StrKit.notBlank(getPara("source"))){
			sWhere+=" and g.source=?";
			params.add(getPara("source"));
			setAttr("source", getParaToInt("source"));
		}
		if(StrKit.notBlank(getPara("tid"))){
			sWhere+=" and g.type_id=?";
			params.add(getPara("tid"));
			setAttr("tid", getParaToInt("tid"));
		}
		if(StrKit.notBlank(getPara("top"))){
			sWhere+=" and g.top=?";
			params.add(getPara("top"));
			setAttr("top", getParaToInt("top"));
		}
		if(StrKit.notBlank(getPara("content"))){
			sWhere+=" and g.title like ?";
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		sWhere+=" order by g.publish_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		//商品分类
		List<Record> type_list=Type.getList(Type.TYPE_GOODS, getLoginShopId());
		setAttr("type_list", type_list);
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
		
		//商品分类
		List<Record> type_list=Type.getList(Type.TYPE_GOODS, getLoginShopId());
		setAttr("type_list", type_list);
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
		
		String title=getPara("title");
		String type_id=getPara("type_id");
		String source=getPara("source");
		String width=getPara("width");
		String height=getPara("height");
		String number=getPara("number");
		String img_url=getPara("img_url");
		String img_urls=getPara("img_urls");
		String[] format_array=getParaValues("format");
		String labels=getPara("labels");
		String sale_number=getPara("sale_number");
		String remark=getPara("remark");
		String content=getPara("content");
		Goods goods=new Goods();
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(type_id)){
			setAttr("success", false);
			setAttr("msg", "分类不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(source)){
			setAttr("success", false);
			setAttr("msg", "类型不能为空");
			renderJson();
			return;
		}
		if(Integer.parseInt(source)==Goods.SOURCE_ZHAOPIAN){
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
			if(StrKit.isBlank(number)){
				setAttr("success", false);
				setAttr("msg", "数量不能为空");
				renderJson();
				return;
			}
		}
		if(StrKit.isBlank(img_url)){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(img_urls) || ",".equals(img_urls)){
			setAttr("success", false);
			setAttr("msg", "图集不能为空");
			renderJson();
			return;
		}
		if(format_array==null || format_array.length==0){
			setAttr("success", false);
			setAttr("msg", "规格详情不能为空");
			renderJson();
			return;
		}
		for(String format_value : format_array){
			if(StrKit.isBlank(getPara("format_title_" + format_value))){
				setAttr("success", false);
				setAttr("msg", "标题不能为空");
				renderJson();
				return;
			}
			if(StrKit.isBlank(getPara("format_stock_" + format_value))){
				setAttr("success", false);
				setAttr("msg", "库存不能为空");
				renderJson();
				return;
			}
			if(StrKit.isBlank(getPara("format_price_" + format_value))){
				setAttr("success", false);
				setAttr("msg", "价格不能为空");
				renderJson();
				return;
			}
		}
		if(StrKit.isBlank(sale_number)){
			setAttr("success", false);
			setAttr("msg", "虚拟售量不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(remark)){
			setAttr("success", false);
			setAttr("msg", "简短介绍不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		if(Integer.parseInt(source)==Goods.SOURCE_ZHAOPIAN){
			goods.set("width", width)
					.set("height", height)
					.set("number", number);
		}
		goods.set("code", CodeUtil.getCode())
				.set("title", title)
				.set("img_url", img_url)
				.set("img_urls", img_urls)
				.set("remark", remark)
				.set("content", content)
				.set("type_id", type_id)
				.set("shop_id", getLoginShopId())
				.set("source", source)
				.set("labels", labels)
				.set("sale_number", sale_number)
				.set("publish", Goods.PUBLISH_YES)
				.set("publish_date", new Date())
				.set("status", Goods.STATUS_ENABLE)
				.set("create_date", new Date())
				.save();
		float price=99999999;
		for(String format_value : format_array){
			GoodsFormat goods_format=new GoodsFormat();
			goods_format.set("goods_id", goods.get("id"))
						.set("title", getPara("format_title_" + format_value))
						.set("stock", getPara("format_stock_" + format_value))
						.set("price", getPara("format_price_" + format_value))
						.set("status", GoodsFormat.STATUS_ENABLE)
						.set("create_date", new Date())
						.save();
			if(Float.parseFloat(getPara("format_price_" + format_value))<price){
				price=Float.parseFloat(getPara("format_price_" + format_value));
			}
		}
		goods.set("price", price)
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
		
		Goods goods=Goods.dao.findById(getPara("id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("goods", goods);
		//商品图片
		List<String> img_list=new ArrayList<String>();
		String [] img_urls=goods.get("img_urls").toString().split(",");
		for(String img_url : img_urls){
			if(StrKit.notBlank(img_url)){
				img_list.add(img_url);
			}
		}
		setAttr("img_list", img_list);
		//商品规格
		List<GoodsFormat> format_list=GoodsFormat.getAll(goods.get("id"));
		setAttr("format_list", format_list);
		//商品分类
		List<Record> type_list=Type.getList(Type.TYPE_GOODS, getLoginShopId());
		setAttr("type_list", type_list);
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
		
		Goods goods=Goods.dao.findById(getPara("id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("sucess", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		String type_id=getPara("type_id");
		String source=getPara("source");
		String width=getPara("width");
		String height=getPara("height");
		String number=getPara("number");
		String img_url=getPara("img_url");
		String img_urls=getPara("img_urls");
		String[] format_id_array=getParaValues("format_id");
		String[] format_array=getParaValues("format");
		String labels=getPara("labels");
		String sale_number=getPara("sale_number");
		String remark=getPara("remark");
		String content=getPara("content");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(type_id)){
			setAttr("success", false);
			setAttr("msg", "分类不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(source)){
			setAttr("success", false);
			setAttr("msg", "类型不能为空");
			renderJson();
			return;
		}
		if(Integer.parseInt(source)==Goods.SOURCE_ZHAOPIAN){
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
			if(StrKit.isBlank(number)){
				setAttr("success", false);
				setAttr("msg", "数量不能为空");
				renderJson();
				return;
			}
		}
		if(StrKit.isBlank(img_url)){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(img_urls) || ",".equals(img_urls)){
			setAttr("success", false);
			setAttr("msg", "图集不能为空");
			renderJson();
			return;
		}
		if((format_array==null || format_array.length==0) && (format_id_array==null || format_id_array.length==0)){
			setAttr("success", false);
			setAttr("msg", "规格详情不能为空");
			renderJson();
			return;
		}
		if(format_id_array!=null && format_id_array.length!=0){
			for(String format_value : format_id_array){
				if(StrKit.isBlank(getPara("format_title_" + format_value))){
					setAttr("success", false);
					setAttr("msg", "标题不能为空");
					renderJson();
					return;
				}
				if(StrKit.isBlank(getPara("format_stock_" + format_value))){
					setAttr("success", false);
					setAttr("msg", "库存不能为空");
					renderJson();
					return;
				}
				if(StrKit.isBlank(getPara("format_price_" + format_value))){
					setAttr("success", false);
					setAttr("msg", "价格不能为空");
					renderJson();
					return;
				}
			}
		}
		if(format_array!=null && format_array.length!=0){
			for(String format_value : format_array){
				if(StrKit.isBlank(getPara("format_title_" + format_value))){
					setAttr("success", false);
					setAttr("msg", "标题不能为空");
					renderJson();
					return;
				}
				if(StrKit.isBlank(getPara("format_stock_" + format_value))){
					setAttr("success", false);
					setAttr("msg", "库存不能为空");
					renderJson();
					return;
				}
				if(StrKit.isBlank(getPara("format_price_" + format_value))){
					setAttr("success", false);
					setAttr("msg", "价格不能为空");
					renderJson();
					return;
				}
			}
		}
		if(StrKit.isBlank(sale_number)){
			setAttr("success", false);
			setAttr("msg", "虚拟售量不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(remark)){
			setAttr("success", false);
			setAttr("msg", "简短介绍不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content)){
			setAttr("success", false);
			setAttr("msg", "详细内容不能为空");
			renderJson();
			return;
		}
		float price=99999999;
		List<GoodsFormat> format_list=GoodsFormat.getAll(goods.get("id"));
		Db.update("update db_goods_format set status=? where goods_id=?", GoodsFormat.STATUS_DISABLE, goods.get("id"));
		if(format_id_array!=null && format_id_array.length!=0){
			for(String format_value : format_id_array){
				for(GoodsFormat format : format_list){
					String fid=format_value.replace("id_", "");
					if(fid.equals(format.get("id").toString())){
						format.set("title", getPara("format_title_" + format_value))
								.set("stock", getPara("format_stock_" + format_value))
								.set("price", getPara("format_price_" + format_value))
								.set("status", GoodsFormat.STATUS_ENABLE)
								.update();
						if(Float.parseFloat(getPara("format_price_" + format_value))<price){
							price=Float.parseFloat(getPara("format_price_" + format_value));
						}
					}
				}
			}
		}
		if(format_array!=null && format_array.length!=0){
			for(String format_value : format_array){
				GoodsFormat goods_format=new GoodsFormat();
				goods_format.set("goods_id", goods.get("id"))
							.set("title", getPara("format_title_" + format_value))
							.set("stock", getPara("format_stock_" + format_value))
							.set("price", getPara("format_price_" + format_value))
							.set("status", GoodsFormat.STATUS_ENABLE)
							.set("create_date", new Date())
							.save();
				if(Float.parseFloat(getPara("format_price_" + format_value))<price){
					price=Float.parseFloat(getPara("format_price_" + format_value));
				}
			}
		}
		if(Integer.parseInt(source)==Goods.SOURCE_ZHAOPIAN){
			goods.set("width", width)
					.set("height", height)
					.set("number", number);
		}
		goods.set("title", title)
				.set("price", price)
				.set("img_url", img_url)
				.set("img_urls", img_urls)
				.set("type_id", type_id)
				.set("source", source)
				.set("labels", labels)
				.set("sale_number", sale_number)
				.set("remark", remark)
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
		
		Goods goods=Goods.dao.findById(getPara("id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("sucess", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		goods.set("status", getPara("status"))
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
	public void publishGoods() throws Exception{
		
		Goods goods=Goods.dao.findById(getPara("id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("sucess", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(getParaToInt("publish")==Goods.PUBLISH_YES){
			goods.set("publish_date", new Date());
		}
		goods.set("publish", getPara("publish"))
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
	public void topGoods() throws Exception{
		
		Goods goods=Goods.dao.findById(getPara("id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(getParaToInt("top")==Goods.TOP_YES){
			goods.set("top_date", new Date());
		}
		goods.set("top", getPara("top"))
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
