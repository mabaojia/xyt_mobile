package com.project.controller.shop;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.jfinal.aop.Before;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.Render;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.GoodsFormat;
import com.project.model.Orders;
import com.project.model.OrdersItem;
import com.project.model.OrdersItemPicture;
import com.project.model.OrdersLog;
import com.project.model.Shop;
import com.project.model.TrackingCompany;
import com.project.model.TrackingItem;
import com.project.util.DateUtil;
import com.project.util.MD5Util;
import com.project.util.TrackingUtil;
import com.project.util.ZipUtils;
import com.project.weixin.pay.GetWxOrderno;
import com.project.weixin.pay.PayUtil;
import com.project.weixin.pay.RequestHandler;
import com.project.weixin.pay.WxPayDto;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/orders", viewPath = "/shop/orders")
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
		
		int page=getParaToInt("p",1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select o.*";
		String sWhere=" from db_orders o where o.display=? and o.shop_id=?";
		params.add(Orders.DISPLAY_YES);
		params.add(getLoginShopId());
		if(StrKit.notBlank(getPara("status"))){
			sWhere+=" and o.status=? and o.closed=?";
			params.add(getPara("status"));
			params.add(Orders.CLOSED_NO);
			setAttr("status", getParaToInt("status"));
		}
		if(StrKit.notBlank(getPara("content"))){
			sWhere+=" and (o.code like ? or o.take_name like ? or o.take_telephone like ?)";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		if(StrKit.notBlank(getPara("gid"))){
			sWhere+=" and o.id in(select goods_id from db_orders_item where goods_id=?)";
			params.add(getPara("gid"));
			setAttr("gid", getPara("gid"));
		}
		if(StrKit.notBlank(getPara("startT"))){
			sWhere+=" and o.create_date>=?";
			params.add(DateUtil.getStartTimeOfDay(DateUtil.stringToDate(getPara("startT"), "yyyy-MM-dd")));
			setAttr("startT", getPara("startT"));
		}
		if(StrKit.notBlank(getPara("endT"))){
			sWhere+=" and o.create_date<=?";
			params.add(DateUtil.getEndTimeOfDay(DateUtil.stringToDate(getPara("endT"), "yyyy-MM-dd")));
			setAttr("endT", getPara("endT"));
		}
		sWhere+=" order by o.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record orders : results.getList()){
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
			orders.set("goods_list", goods_list);
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
	public void export() throws Exception{
		
		List<Object> params = new ArrayList<Object>();
		String sql="select o.*";
		sql+=" from db_orders o where o.display=? and o.shop_id=?";
		params.add(Orders.DISPLAY_YES);
		params.add(getLoginShopId());
		if(StrKit.notBlank(getPara("status"))){
			sql+=" and o.status=? and o.closed=?";
			params.add(getPara("status"));
			params.add(Orders.CLOSED_NO);
		}
		if(StrKit.notBlank(getPara("content"))){
			sql+=" and (o.code like ? or o.take_name like ? or o.take_telephone like ?)";
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
			params.add("%" + getPara("content")+ "%");
		}
		if(StrKit.notBlank(getPara("gid"))){
			sql+=" and o.id in(select goods_id from db_orders_item where goods_id=?)";
			params.add(getPara("gid"));
		}
		if(StrKit.notBlank(getPara("startT"))){
			sql+=" and o.create_date>=?";
			params.add(DateUtil.getStartTimeOfDay(DateUtil.stringToDate(getPara("startT"), "yyyy-MM-dd")));
		}
		if(StrKit.notBlank(getPara("endT"))){
			sql+=" and o.create_date<=?";
			params.add(DateUtil.getEndTimeOfDay(DateUtil.stringToDate(getPara("endT"), "yyyy-MM-dd")));
		}
		sql+=" order by o.create_date desc";
		List<Record> list=Db.find(sql, params.toArray());
		for(Record item : list){
			item.set("create_date", DateUtil.formatDate(item.getDate("create_date")));
			if(item.getInt("tracking")==Orders.TRACKING_YOUJI){
				item.set("take_address", item.get("take_area_msg").toString() + item.get("take_address").toString() + "（" + item.get("take_post_code") + "）");
			}else{
				item.set("take_address", "");
			}
			if(item.get("payment")!=null && StrKit.notBlank(item.get("payment").toString())){
				if(item.getInt("payment")==Orders.PAYMENT_WX){
					item.set("payment", "微信支付");
				}else{
					item.set("payment", "线下支付");
				}
			}else{
				item.set("payment", "");
			}
			if(item.getInt("closed")==Orders.CLOSED_YES){
				item.set("status", "已关闭");
			}else{
				if(item.getInt("status")==0){
					item.set("status", "待付款");
				}else if(item.getInt("status")==1){
					if(item.getInt("tracking")==1){
						item.set("status", "待发货");
					}else{
						item.set("status", "待自提");
					}
				}else if(item.getInt("status")==2){
					item.set("status", "已发货");
				}else if(item.getInt("status")==3){
					item.set("status", "已完成");
				}else if(item.getInt("status")==8){
					item.set("status", "已收货");
				}
			}
			if(item.getInt("tracking")==Orders.TRACKING_YOUJI){
				item.set("tracking", "邮寄");
			}else{
				item.set("tracking", "自提");
			}
		}
		String[] header={"订单号","收件人","联系方式","收件地址","发货方式","总计","付款方式","状态","运费","小计","创建时间"};
	    String[] columns={"code","take_name","take_telephone","take_address","tracking","grand_total","payment","status","freight_price","subtotal","create_date"};
	    Render poirender = PoiRender.me(list).fileName(System.currentTimeMillis() + ".xls").headers(header).sheetName("list").columns(columns);
	    render(poirender);
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
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
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
	@Before(Tx.class)
	public void closed() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_NOT_PAY){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		orders.set("closed", Orders.CLOSED_YES)
				.update();
		OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "关闭订单", new Date());
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
	@Before(Tx.class)
	public void deleted() throws Exception{
	
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("closed")==Orders.CLOSED_NO){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		orders.set("display", Orders.DISPLAY_NONE)
				.update();
		OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "删除订单", new Date());
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
	public void pwd() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_TRACKING && orders.getInt("status")!=Orders.STATUS_WAIT_TRACKING){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("orders", orders);
		render("pwd.htm");
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
	public void refund() throws Exception{
		
		PropKit.use("config.txt");
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_TRACKING && orders.getInt("status")!=Orders.STATUS_WAIT_TRACKING){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String password=getPara("password");
		if(StrKit.isBlank(password)){
			setAttr("success", false);
			setAttr("msg", "支付密码不能为空");
			renderJson();
			return;
		}
		Shop shop=Shop.dao.findById(getLoginShopId());
		if(shop.get("password")==null || StrKit.isBlank(shop.get("password").toString())){
			setAttr("success", false);
			setAttr("msg", "商家未设置支付密码");
			renderJson();
			return;
		}
		if(!MD5Util.getStringMD5("xiaodaokeji" + password + "xiaodaokeji").toLowerCase().equals(shop.get("password").toString())){
			setAttr("success", false);
			setAttr("msg", "支付密码不正确");
			renderJson();
			return;
		}
		if(orders.getInt("payment")==Orders.PAYMENT_WX){
			WxPayDto tpWxPay = new WxPayDto();
			tpWxPay.setAppid(PropKit.get("appid").toString());
			tpWxPay.setAppsecret(PropKit.get("appsecret").toString());
			tpWxPay.setPartner(PropKit.get("partner").toString());
			tpWxPay.setPartnerkey(PropKit.get("partnerkey").toString());
			tpWxPay.setTotalFee(orders.get("grand_total").toString());
			tpWxPay.setOrderId(orders.get("wx_code").toString());
			String results=PayUtil.refundPayment(tpWxPay, PropKit.get("partnerlicense").toString());
			System.out.println(results);
			Map<String, String> map = GetWxOrderno.xmlToMap(results);
			String return_code=map.get("return_code").toString();
			if("SUCCESS".equals(return_code)){
				String result_code=map.get("result_code").toString();
				if("SUCCESS".equals(result_code)){
					String nonce_str=PayUtil.getOrder();
					SortedMap<String, String> packageParams = new TreeMap<String, String>();
					packageParams.put("appid", PropKit.get("appid").toString());
					packageParams.put("mch_id", PropKit.get("partner").toString());
					packageParams.put("nonce_str", nonce_str);
					packageParams.put("out_refund_no", orders.get("wx_code").toString());
					RequestHandler reqHandler = new RequestHandler(null, null);
					reqHandler.init(PropKit.get("appid").toString(), PropKit.get("appsecret").toString(), PropKit.get("partnerkey").toString());
					String sign = reqHandler.createSign(packageParams);
					String xmlParam = "<xml>" + 
								"<appid>" + PropKit.get("appid").toString() + "</appid>" + 
								"<mch_id>" + PropKit.get("partner").toString() + "</mch_id>" + 
								"<nonce_str>" + nonce_str + "</nonce_str>" + 
								"<out_refund_no>" + orders.get("wx_code") + "</out_refund_no>" + 
								"<sign>" + sign + "</sign>"	+ 
								"</xml>";
					map=GetWxOrderno.doXML("https://api.mch.weixin.qq.com/pay/refundquery", xmlParam);
					return_code=map.get("return_code").toString();
					if("SUCCESS".equals(return_code)){
						result_code=map.get("result_code").toString();
						if("SUCCESS".equals(result_code)){
							orders.set("closed", Orders.CLOSED_YES)
									.update();
							OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "订单退款", new Date());
							setAttr("success", true);
							setAttr("msg", "操作成功");
							renderJson();
							return;
						}
					}
				}
			}
		}else{
			orders.set("closed", Orders.CLOSED_YES)
					.update();
			OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "订单退款", new Date());
			setAttr("success", true);
			setAttr("msg", "操作成功");
			renderJson();
			return;
		}
		setAttr("success", false);
		setAttr("msg", "操作失败");
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
	public void editPrice() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("orders", orders);
		render("editPrice.htm");
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
	public void updatePrice() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(StrKit.isBlank(getPara("grand_total"))){
			setAttr("success", false);
			setAttr("msg", "总计不能为空");
			renderJson();
			return;
		}
		OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "订单改价：" + orders.get("grand_total").toString() + "->" + getPara("grand_total"), new Date());
		orders.set("grand_total", getPara("grand_total"))
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
	public void addTracking() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_WAIT_TRACKING){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("orders", orders);
		//运单公司
		setAttr("tracking_company", TrackingCompany.getList());
		render("addTracking.htm");
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
	public void saveTracking() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_WAIT_TRACKING){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String tracking_id=getPara("tracking_id");
		String tracking_code=getPara("tracking_code");
		if(orders.getInt("tracking")==Orders.TRACKING_YOUJI){
			if(StrKit.isBlank(tracking_code)){
				setAttr("success", false);
				setAttr("msg", "运单号不能为空");
				renderJson();
				return;
			}
			TrackingCompany tracking_company=TrackingCompany.dao.findById(tracking_id);
			orders.set("status", Orders.STATUS_TRACKING)
					.set("tracking_code", tracking_code)
					.set("tracking_company", tracking_company.get("title"))
					.set("tracking_kdn_code", tracking_company.get("kdn_code"))
					.set("tracking_status", Orders.TRACKING_STATUS_WUGUIJI)
					.set("tracking_update_date", new Date())
					.set("tracking_date", new Date())
					.update();
			OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "订单发货", new Date());
		}else{
			orders.set("status", Orders.STATUS_TRACKING)
					.set("tracking_date", new Date())
					.update();
			OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "订单已自提", new Date());
		}
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
	public void trackingMsg() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("orders", orders);
		if((((new Date()).getTime() - orders.getDate("tracking_update_date").getTime()) / 1000 / 60 / 60 > 3) && (orders.getInt("tracking_status")==Orders.TRACKING_STATUS_SHOUJIAN || orders.getInt("tracking_status")==Orders.TRACKING_STATUS_TUZHONG || orders.getInt("tracking_status")==Orders.TRACKING_STATUS_WUGUIJI)){
			TrackingUtil.synTracking(orders, orders.get("tracking_code").toString(), orders.get("tracking_kdn_code").toString());
		}
		List<TrackingItem> list=TrackingItem.getList(orders.get("id").toString());
		setAttr("list", list);
		render("trackingMsg.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void doDiy() throws Exception{
		
		OrdersItem orders_item=OrdersItem.dao.findById(getPara("id"));
		Orders orders=Orders.dao.findById(orders_item.get("orders_id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders_item.get("diy_id")==null || StrKit.isBlank(orders_item.get("diy_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		Diy diy=Diy.dao.findById(orders_item.get("diy_id"));
		if(StrKit.isBlank(getPara("big_img_url"))){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		diy.set("big_img_url", getPara("big_img_url"))
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
	public void download() throws Exception{
		
		OrdersItem orders_item=OrdersItem.dao.findById(getPara("id"));
		Orders orders=Orders.dao.findById(orders_item.get("orders_id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		if(orders_item.get("diy_id")==null || StrKit.isBlank(orders_item.get("diy_id").toString())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		Diy diy=Diy.dao.findById(orders_item.get("diy_id"));
		if(diy.get("big_img_url")==null || StrKit.isBlank(diy.get("big_img_url").toString())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		renderFile(new File(getRequest().getSession().getServletContext().getRealPath("/") + diy.get("big_img_url")));
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
	public void downloadPicture() throws Exception{
		
		OrdersItemPicture orders_item_picture=OrdersItemPicture.dao.findById(getPara("id"));
		OrdersItem orders_item=OrdersItem.dao.findById(orders_item_picture.get("orders_item_id"));
		Orders orders=Orders.dao.findById(orders_item.get("orders_id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		renderFile(new File(getRequest().getSession().getServletContext().getRealPath("/") + orders_item_picture.get("img_url")));
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
	public void print() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
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
		}
		setAttr("goods_list", goods_list);
		setAttr("type", getParaToInt("type"));
		render("print.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void printPhoto() throws Exception{
		
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
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
		}
		setAttr("goods_list", goods_list);
		render("printPhoto.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void printAll() throws Exception{
		
		List<Record> goods_list=new ArrayList<Record>();
		String[] oids=getPara("oids").split(",");
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				Orders orders=Orders.dao.findById(oid);
				if(!orders.get("shop_id").toString().equals(getLoginShopId())){
					setAttr("msg", "没有操作权限");
					render("/shop/msg.htm");
					return;
				}
			}
		}
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				//订单商品
				goods_list.addAll(OrdersItem.getByOrders(oid));
			}
		}
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
		}
		setAttr("goods_list", goods_list);
		setAttr("type", getParaToInt("type"));
		render("printAll.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void printAllPhoto() throws Exception{
		
		List<Record> goods_list=new ArrayList<Record>();
		String[] oids=getPara("oids").split(",");
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				Orders orders=Orders.dao.findById(oid);
				if(!orders.get("shop_id").toString().equals(getLoginShopId())){
					setAttr("msg", "没有操作权限");
					render("/shop/msg.htm");
					return;
				}
			}
		}
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				//订单商品
				goods_list.addAll(OrdersItem.getByOrders(oid));
			}
		}
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
		}
		setAttr("goods_list", goods_list);
		render("printAllPhoto.htm");
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
	public void ordersPayment() throws Exception{
	
		Orders orders=Orders.dao.findById(getPara("id"));
		if(!orders.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_NOT_PAY){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		orders.set("status", Orders.STATUS_WAIT_TRACKING)
				.set("payment", Orders.PAYMENT_XIANXIA)
				.set("payment_date", new Date())
				.update();
		List<Record> item_list=OrdersItem.getByOrders(orders.get("id"));
		for(Record item : item_list){
			Goods goods=Goods.dao.findById(item.get("goods_id"));
			goods.set("sale_number", goods.getInt("sale_number") + item.getInt("item_number"))
					.update();
			GoodsFormat goods_format=GoodsFormat.getByTitleGoods(item.get("goods_format_title").toString(), goods.get("id"));
			if(goods_format!=null){
				goods_format.set("stock", goods_format.getInt("stock") - item.getInt("item_number"))
							.set("sale_number", goods_format.getInt("sale_number") + item.getInt("item_number"))
							.update();
			}
		}
		OrdersLog.save(orders.get("id"), "商家" + getLoginShopAdminId() + "|" + getLoginShopAdmin().get("name") + "订单线下收款", new Date());
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
	public void downloadPicturesAll() throws Exception{
		
		keepPara();
		List<Record> goods_list=new ArrayList<Record>();
		String[] oids=getPara("oids").split(",");
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				Orders orders=Orders.dao.findById(oid);
				if(!orders.get("shop_id").toString().equals(getLoginShopId())){
					setAttr("msg", "没有操作权限");
					render("/shop/msg.htm");
					return;
				}
			}
		}
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				//订单商品
				goods_list.addAll(OrdersItem.getByOrders(oid));
			}
		}
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
		}
		setAttr("goods_list", goods_list);
		render("downloadPicturesAll.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void doDownloadPicturesAll() throws Exception{
		
		List<Record> goods_list=new ArrayList<Record>();
		String[] oids=getPara("oids").split(",");
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				Orders orders=Orders.dao.findById(oid);
				if(!orders.get("shop_id").toString().equals(getLoginShopId())){
					setAttr("msg", "没有操作权限");
					render("/shop/msg.htm");
					return;
				}
			}
		}
		for(String oid : oids){
			if(StrKit.notBlank(oid)){
				//订单商品
				goods_list.addAll(OrdersItem.getByOrders(oid));
			}
		}
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
		}
		long download=System.currentTimeMillis();
		String save_path_1="/static/download/1/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/" + download + "/";
		String save_path_2="/static/download/2/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/" + download + "/";
		File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path_2);  
        if(!file.exists()){  
            file.mkdirs();  
        }
		file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path_1);  
        if(!file.exists()){  
            file.mkdirs();  
        }
        for(Record item : goods_list){
        	if(item.get("diy_id")!=null && StrKit.notBlank(item.get("diy_id").toString())){
				Diy diy=Diy.dao.findById(item.get("diy_id"));
				if(diy.get("big_img_url")!=null && StrKit.notBlank(diy.get("big_img_url").toString())){
					File rename_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + diy.get("big_img_url"));
	    			ZipUtils.copyFileUsingFileStreams(rename_file, new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path_1 + diy.get("id") + "~" + "1" + "份（" + diy.get("width") + "cm " + diy.get("height") + "cm）" + "." + diy.get("big_img_url").toString().split("\\.")[diy.get("big_img_url").toString().split("\\.").length - 1]));
				}
			}
        	if(item.getInt("goods_number")!=0){
				List<Record> picture_list=OrdersItemPicture.getByOrdersItem(item.get("id"));
	        	for(Record each : picture_list){
	        		File rename_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + each.get("img_url"));
	    			ZipUtils.copyFileUsingFileStreams(rename_file, new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path_1 + each.get("id") + "~" + each.get("number") + "份（" + item.get("goods_width") + "mm " + item.get("goods_height") + "mm）" + "." + each.get("img_url").toString().split("\\.")[each.get("img_url").toString().split("\\.").length - 1]));
	        	}
        	}
        }
		FileOutputStream fos = new FileOutputStream(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path_2 + download + ".zip"));
        ZipUtils.toZip(getRequest().getSession().getServletContext().getRealPath("/") + save_path_1, fos, true);
        renderFile(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path_2 + download + ".zip"));
        return;
	}
}
