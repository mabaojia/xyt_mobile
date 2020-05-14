package com.project.controller.wap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.OAuthApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.GoodsComment;
import com.project.model.Orders;
import com.project.model.OrdersItem;
import com.project.model.OrdersItemPicture;
import com.project.model.OrdersLog;
import com.project.model.ShopDetails;
import com.project.model.TrackingItem;
import com.project.model.User;
import com.project.util.CodeUtil;
import com.project.util.TrackingUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/wap/user", viewPath = "/wap/user")
public class UserController extends ApiController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@Override
	public ApiConfig getApiConfig() {
		
		ApiConfig ac = new ApiConfig();
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appid"));
		ac.setAppSecret(PropKit.get("appsecret"));
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "xiaodaokeji"));
		return ac;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@Override
	public Map<String, String> getSignature(String url) {

		return OAuthApi.getSignature(url);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		long not_pay_number=Db.findFirst("select count(o.id) number from db_orders o where o.display=? and o.user_display=? and o.closed=? and o.user_id=? and o.shop_id=? and o.status=?", Orders.DISPLAY_YES, Orders.USER_DISPLAY_YES, Orders.CLOSED_NO, getLoginUserId(), getLoginUser().get("shop_id"), Orders.STATUS_NOT_PAY).getLong("number");
		setAttr("not_pay_number", not_pay_number);
		long wait_tracking_number=Db.findFirst("select count(o.id) number from db_orders o where o.display=? and o.user_display=? and o.closed=? and o.user_id=? and o.shop_id=? and o.status=?", Orders.DISPLAY_YES, Orders.USER_DISPLAY_YES, Orders.CLOSED_NO, getLoginUserId(), getLoginUser().get("shop_id"), Orders.STATUS_WAIT_TRACKING).getLong("number");
		setAttr("wait_tracking_number", wait_tracking_number);
		long tracking_number=Db.findFirst("select count(o.id) number from db_orders o where o.display=? and o.user_display=? and o.closed=? and o.user_id=? and o.shop_id=? and o.status=?", Orders.DISPLAY_YES, Orders.USER_DISPLAY_YES, Orders.CLOSED_NO, getLoginUserId(), getLoginUser().get("shop_id"), Orders.STATUS_TRACKING).getLong("number");
		setAttr("tracking_number", tracking_number);
		long received_number=Db.findFirst("select count(o.id) number from db_orders o where o.display=? and o.user_display=? and o.closed=? and o.user_id=? and o.shop_id=? and o.status=?", Orders.DISPLAY_YES, Orders.USER_DISPLAY_YES, Orders.CLOSED_NO, getLoginUserId(), getLoginUser().get("shop_id"), Orders.STATUS_RECEIVED).getLong("number");
		setAttr("received_number", received_number);
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
	public void updateAddress() throws Exception{
		
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "收件人不能为空");
			renderJson();
			return;
		}
		String telephone=getPara("telephone");
		if(StrKit.isBlank(telephone)){
			setAttr("success", false);
			setAttr("msg", "手机号不能为空");
			renderJson();
			return;
		}
		if(!CodeUtil.isMobile(telephone)){
			setAttr("success", false);
			setAttr("msg", "手机号格式不正确");
			renderJson();
			return;
		}
		String area_msg=getPara("area_msg");
		if(StrKit.isBlank(area_msg)){
			setAttr("success", false);
			setAttr("msg", "地区不能为空");
			renderJson();
			return;
		}
		String address=getPara("address");
		if(StrKit.isBlank(address)){
			setAttr("success", false);
			setAttr("msg", "详细地址不能为空");
			renderJson();
			return;
		}
		String post_code=getPara("post_code");
		if(StrKit.isBlank(post_code)){
			setAttr("success", false);
			setAttr("msg", "邮编不能为空");
			renderJson();
			return;
		}
		User user=User.dao.findById(getLoginUserId());
		user.set("take_name", name)
			.set("take_telephone", telephone)
			.set("take_province", area_msg.split(" ")[0])
			.set("take_area_msg", area_msg)
			.set("take_address", address)
			.set("take_post_code", post_code)
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
	public void updateAddressZiti() throws Exception{
		
		String name=getPara("name");
		if(StrKit.isBlank(name)){
			setAttr("success", false);
			setAttr("msg", "自提人不能为空");
			renderJson();
			return;
		}
		String telephone=getPara("telephone");
		if(StrKit.isBlank(telephone)){
			setAttr("success", false);
			setAttr("msg", "手机号不能为空");
			renderJson();
			return;
		}
		if(!CodeUtil.isMobile(telephone)){
			setAttr("success", false);
			setAttr("msg", "手机号格式不正确");
			renderJson();
			return;
		}
		User user=User.dao.findById(getLoginUserId());
		user.set("take_name", name)
			.set("take_telephone", telephone)
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
	public void orders() throws Exception{
		
		int page=getParaToInt("p", 1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select o.*";
		String sWhere=" from db_orders o where o.display=? and o.user_display=? and o.user_id=? and o.shop_id=?";
		params.add(Orders.DISPLAY_YES);
		params.add(Orders.USER_DISPLAY_YES);
		params.add(getLoginUserId());
		params.add(getLoginUser().get("shop_id"));
		if(StrKit.notBlank(getPara("status"))){
			sWhere+=" and o.status=? and o.closed=?";
			params.add(getPara("status"));
			params.add(Orders.CLOSED_NO);
			setAttr("status", getParaToInt("status"));
		}
		sWhere+=" order by o.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record orders : results.getList()){
			List<Record> goods_list=OrdersItem.getByOrders(orders.get("id"));
			for(Record item : goods_list){
				if(item.getInt("goods_source")==Goods.SOURCE_SHOUJIKE || item.getInt("goods_source")==Goods.SOURCE_SHOUJIMO){
					Brand brand=Brand.dao.findById(item.get("brand_id"));
					Brand parent=Brand.dao.findById(brand.get("parent_id"));
					item.set("brand", brand);
					item.set("parent", parent);
				}
			}
			orders.set("goods_list", goods_list);
		}
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		render("orders.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void ordersMsg() throws Exception{
		
		Orders orders=Orders.getByCode(getPara("code"));
		if(orders.getInt("display")!=Orders.DISPLAY_YES || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("msg", "订单不存在");
			render("/wap/msg.htm");
			return;
		}
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("msg", "没有操作权限");
			render("/wap/msg.htm");
			return;
		}
		setAttr("orders", orders);
		//商品列表
		List<Record> goods_list=OrdersItem.getByOrders(orders.get("id"));
		for(Record goods : goods_list){
			if(goods.getInt("goods_source")==Goods.SOURCE_SHOUJIKE || goods.getInt("goods_source")==Goods.SOURCE_SHOUJIMO){
				Brand brand=Brand.dao.findById(goods.get("brand_id"));
				Brand parent=Brand.dao.findById(brand.get("parent_id"));
				goods.set("brand", brand);
				goods.set("parent", parent);
			}
			if(orders.getInt("status")!=Orders.STATUS_RECEIVED){
				goods.set("comment", 0);
				continue;	
			}
			GoodsComment comment=GoodsComment.getByOrdersGoodsUser(orders.get("id"), goods.get("goods_id"), getLoginUserId());
			if(comment==null){
				goods.set("comment", 1);
				continue;
			}else{
				goods.set("comment", 0);
				continue;
			}
		}
		setAttr("goods_list", goods_list);
		render("ordersMsg.htm");
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
	public void colseOrders() throws Exception{
		
		Orders orders=Orders.getByCode(getPara("code"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_NOT_PAY){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许确认关闭");
			renderJson();
			return;
		}
		orders.set("closed", Orders.CLOSED_YES)
				.update();
		OrdersLog.save(orders.get("id"), "会员关闭订单", new Date());
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
	public void deleteOrders() throws Exception{
		
		Orders orders=Orders.getByCode(getPara("code"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("closed")!=Orders.CLOSED_YES){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许删除");
			renderJson();
			return;
		}
		orders.set("user_display", Orders.USER_DISPLAY_NONE)
				.update();
		OrdersLog.save(orders.get("id"), "会员删除订单", new Date());
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
	public void reciveTracking() throws Exception{
	
		Orders orders=Orders.getByCode(getPara("code"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("closed")==Orders.CLOSED_YES){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许确认收货");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_TRACKING){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许确认收货");
			renderJson();
			return;
		}
		orders.set("status", Orders.STATUS_RECEIVED)
				.set("received_date", new Date())
				.update();
		OrdersLog.save(orders.get("id"), "会员确认收货", new Date());
		//商家流水
		if(orders.getInt("payment")==Orders.PAYMENT_WX){
			ShopDetails shop_details=new ShopDetails();
			shop_details.set("shop_id", orders.get("shop_id"))
						.set("orders_id", orders.get("id"))
						.set("account", orders.getFloat("grand_total"))
						.set("content", orders.get("code") + "订单入账")
						.set("status", ShopDetails.STATUS_DISABLE)
						.set("create_date", new Date())
						.save();
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
	public void tracking() throws Exception{
		
		Orders orders=Orders.getByCode(getPara("code"));
		if(!orders.get("user_id").toString().equals(getLoginUserId())){
			setAttr("msg", "没有操作权限");
			render("/wap/msg.htm");
			return;
		}
		setAttr("orders", orders);
		if((((new Date()).getTime() - orders.getDate("tracking_update_date").getTime()) / 1000 / 60 / 60 > 3) && (orders.getInt("tracking_status")==Orders.TRACKING_STATUS_SHOUJIAN || orders.getInt("tracking_status")==Orders.TRACKING_STATUS_TUZHONG || orders.getInt("tracking_status")==Orders.TRACKING_STATUS_WUGUIJI)){
			TrackingUtil.synTracking(orders, orders.get("tracking_code").toString(), orders.get("tracking_kdn_code").toString());
		}
		List<TrackingItem> list=TrackingItem.getList(orders.get("id").toString());
		setAttr("list", list);
		render("tracking.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void comment() throws Exception{
		
		Orders orders=Orders.getByCode(getPara("code"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("closed")==Orders.CLOSED_YES){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许评价");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_RECEIVED){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许评价");
			renderJson();
			return;
		}
		setAttr("gid", getPara("gid"));
		setAttr("code", getPara("code"));
		render("comment.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void saveComment() throws Exception{
		
		String content=getPara("content");
		String img_urls=getPara("img_urls");
		Goods goods=Goods.dao.findById(getPara("gid"));
		Orders orders=Orders.getByCode(getPara("code"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("closed")==Orders.CLOSED_YES){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许评价");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_RECEIVED){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许评价");
			renderJson();
			return;
		}
		if(StrKit.isBlank(content) && StrKit.isBlank(img_urls)){
			setAttr("success", false);
			setAttr("msg", "评论内容不能为空");
			renderJson();
			return;
		}
		GoodsComment goods_comment=new GoodsComment();
		goods_comment.set("goods_id", goods.get("id"))
						.set("orders_id", orders.get("id"))
						.set("user_id", getLoginUserId())
						.set("img_urls", img_urls)
						.set("content", content)
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
	public void diy() throws Exception{
		
		int page=getParaToInt("p", 1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select d.*";
		String sWhere=" from db_diy d where d.user_id=? and d.shop_id=? and d.status=?";
		params.add(getLoginUserId());
		params.add(getLoginUser().get("shop_id"));
		params.add(Diy.STATUS_ENABLE);
		if(StrKit.notBlank(getPara("type"))){
			sWhere+=" and d.type=?";
			params.add(getPara("type"));
			setAttr("type", getParaToInt("type"));
		}
		sWhere+=" order by d.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record item : results.getList()){
			Goods goods=Goods.dao.findById(item.get("goods_id"));
			item.set("goods", goods);
			//商品标签
			List<String> labels_list=new ArrayList<String>();
			if(goods.get("labels")!=null && StrKit.notBlank(goods.get("labels").toString())){
				String [] labels=goods.get("labels").toString().split(",");
				for(String label : labels){
					if(StrKit.notBlank(label)){
						labels_list.add(label);
					}
				}
			}
			item.set("labels_list", labels_list);
		}
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		render("diy.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void deleteDiy() throws Exception{
		
		Diy diy=Diy.dao.findById(getPara("id"));
		if(!diy.get("user_id").toString().equals(getLoginUserId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		diy.set("status", Diy.STATUS_DELETE)
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
	public void picture() throws Exception{
		
		OrdersItem orders_item=OrdersItem.dao.findById(getPara("oiid"));
		Orders orders=Orders.dao.findById(orders_item.get("orders_id"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("msg", "没有操作权限");
			render("/wap/msg.htm");
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("msg", "订单不存在");
			render("/wap/msg.htm");
			return;
		}
		if(orders.getInt("closed")==Orders.CLOSED_YES){
			setAttr("msg", "订单状态不允许上传图片");
			render("/wap/msg.htm");
			return;
		}
		if(orders.getInt("status")==Orders.STATUS_NOT_PAY){
			setAttr("msg", "订单状态不允许上传图片");
			render("/wap/msg.htm");
			return;
		}
		setAttr("orders", orders);
		setAttr("orders_item", orders_item);
		//图片列表
		List<Record> list=OrdersItemPicture.getByOrdersItem(orders_item.get("id"));
		setAttr("list", list);
		render("picture.htm");
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
	public void savePicture() throws Exception{
	
		OrdersItem orders_item=OrdersItem.dao.findById(getPara("oiid"));
		Orders orders=Orders.dao.findById(orders_item.get("orders_id"));
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		if(orders.getInt("display")==Orders.DISPLAY_NONE || orders.getInt("user_display")!=Orders.USER_DISPLAY_YES){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("closed")==Orders.CLOSED_YES){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许上传图片");
			renderJson();
			return;
		}
		if(orders.getInt("status")==Orders.STATUS_NOT_PAY){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许上传图片");
			renderJson();
			return;
		}
		List<Record> list=OrdersItemPicture.getByOrdersItem(orders_item.get("id"));
		if(list!=null && list.size()!=0){
			setAttr("success", false);
			setAttr("msg", "订单状态不允许上传图片");
			renderJson();
			return;
		}
		String img_urls=getPara("img_urls").substring(1, getPara("img_urls").length() - 1);
		String numbers=getPara("numbers").substring(1, getPara("numbers").length() - 1);
		if(StrKit.isBlank(img_urls)){
			setAttr("success", false);
			setAttr("msg", "图片不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(numbers)){
			setAttr("success", false);
			setAttr("msg", "数量不能为空");
			renderJson();
			return;
		}
		String[] img_url_list=img_urls.split(",");
		String[] number_list=numbers.split(",");
		if(img_url_list.length!=number_list.length){
			setAttr("success", false);
			setAttr("msg", "图片和数量不一致");
			renderJson();
			return;
		}
		for(String img_url : img_url_list){
			if(StrKit.isBlank(img_url)){
				setAttr("success", false);
				setAttr("msg", "图片不能为空");
				renderJson();
				return;
			}
		}
		int i=0;
		for(String number : number_list){
			if(StrKit.isBlank(number)){
				setAttr("success", false);
				setAttr("msg", "数量不能为空");
				renderJson();
				return;
			}
			i+=Integer.parseInt(number);
		}
		if(i>orders_item.getInt("goods_number")){
			setAttr("success", false);
			setAttr("msg", "图片总数不能多于" + orders_item.getInt("goods_number"));
			renderJson();
			return;
		}
		int idx=0;
		for(String img_url : img_url_list){
			OrdersItemPicture orders_item_picture=new OrdersItemPicture();
			orders_item_picture.set("orders_item_id", orders_item.get("id"))
								.set("img_url", img_url)
								.set("number", number_list[idx])
								.set("create_date", new Date())
								.save();
			idx++;
		}
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
