package com.project.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.Orders;
import com.project.model.OrdersItem;
import com.project.model.OrdersItemPicture;
import com.project.model.OrdersLog;
import com.project.model.Shop;
import com.project.model.TrackingItem;
import com.project.util.TrackingUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey="/admin/orders", viewPath="/admin/orders")
public class OrdersController extends BaseController{
	
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
		List<Object> params=new ArrayList<Object>();
		String sSelect ="select o.*, s.title shop_title, s.name shop_name, s.mobile shop_mobile";
		String sWhere =" from db_orders o left join db_shop s on o.shop_id=s.id where o.display=? and s.status!=?";
		params.add(Orders.DISPLAY_YES);
		params.add(Shop.STATUS_DELETE);
		if(StrKit.notBlank(getPara("status"))){
			sWhere+=" and o.status=? and o.closed=?";
			params.add(getPara("status"));
			params.add(Orders.CLOSED_NO);
			setAttr("status", getParaToInt("status"));
		}
		if (StrKit.notBlank(getPara("sid"))) {
			sWhere+=" and o.shop_id=?";
			params.add(getPara("sid"));
			setAttr("sid", getPara("sid"));
		}
		if (StrKit.notBlank(getPara("content"))) {
			sWhere+=" and(o.code like ? or o.take_name like ? or o.take_telephone like ? or s.title like ? or s.name like ?)";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		sWhere+=" order by o.create_date desc";
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
	public void msg() throws Exception{
		
		Orders orders = Orders.dao.findById(getPara("id"));
		setAttr("orders", orders);
		//订单商品
		List<Record> goods_list=OrdersItem.getByOrders(orders.get("id"));
		for(Record item : goods_list){
			if(item.get("diy_id")!=null && StrKit.notBlank(item.get("diy_id").toString())){
				Diy diy=Diy.dao.findById(item.get("diy_id"));
				Brand brand=Brand.dao.findById(diy.get("brand_id"));
				Brand parent=Brand.dao.findById(brand.get("parent_id"));
				item.set("diy", diy);
				item.set("brand", brand);
				item.set("parent", parent);
			}
			if(item.getInt("goods_number")!=0){
				List<Record> picture_list=OrdersItemPicture.getByOrdersItem(item.get("id"));
				item.set("picture_list", picture_list);
			}
			if(item.getInt("goods_source")!=Goods.SOURCE_ZHAOPIAN){
				Brand item_brand=Brand.dao.findById(item.get("brand_id"));
				Brand item_parent=Brand.dao.findById(item_brand.get("parent_id"));
				item.set("item_brand", item_brand);
				item.set("item_parent", item_parent);
			}
		}
		setAttr("goods_list", goods_list);
		//订单日志
		List<OrdersLog> log_list=OrdersLog.getList(orders.get("id"));
		setAttr("log_list", log_list);
		render("msg.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void trackingMsg() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		setAttr("orders", orders);
		if((((new Date()).getTime() - orders.getDate("tracking_update_date").getTime()) / 1000 / 60 / 60 > 3) && (orders.getInt("tracking_status")==Orders.TRACKING_STATUS_SHOUJIAN || orders.getInt("tracking_status")==Orders.TRACKING_STATUS_TUZHONG || orders.getInt("tracking_status")==Orders.TRACKING_STATUS_WUGUIJI)){
			TrackingUtil.synTracking(orders, orders.get("tracking_code").toString(), orders.get("tracking_kdn_code").toString());
		}
		List<TrackingItem> list=TrackingItem.getList(orders.get("id").toString());
		setAttr("list", list);
		render("trackingMsg.htm");
	}
}
