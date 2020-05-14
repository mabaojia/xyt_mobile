package com.project.controller.wap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.ServletInputStream;
import net.sf.json.xml.XMLSerializer;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.OAuthApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.project.aop.ExceptionInterceptor;
import com.project.common.SynchronizedUtil;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Freight;
import com.project.model.Goods;
import com.project.model.GoodsFormat;
import com.project.model.Orders;
import com.project.model.OrdersItem;
import com.project.model.OrdersLog;
import com.project.model.Shop;
import com.project.model.ShoppingCart;
import com.project.model.User;
import com.project.util.CodeUtil;
import com.project.weixin.pay.GetWxOrderno;
import com.project.weixin.pay.PayUtil;
import com.project.weixin.pay.RequestHandler;
import com.project.weixin.pay.TenpayUtil;
import com.project.weixin.pay.Tools;
import com.project.weixin.pay.WxPayDto;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/wap/shop", viewPath = "/wap/shop")
public class ShoppingController extends ApiController{

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
	public void shopping() throws Exception{
		
		float subtotal=0f;
		List<Record> results=ShoppingCart.getByUser(getLoginUserId());
		List<Record> goods_list=new ArrayList<Record>();
		for(Record shopping_cart : results){
			Goods goods=Goods.dao.findById(shopping_cart.get("goods_id"));
			if(goods.getInt("source")!=Goods.SOURCE_ZHAOPIAN){
				Brand brand=Brand.dao.findById(shopping_cart.get("brand_id"));
				Brand parent=Brand.dao.findById(brand.get("parent_id"));
				shopping_cart.set("brand", brand);
				shopping_cart.set("parent", parent);
			}
			if(Goods.enable(goods)){
				shopping_cart.set("goods", goods);
				GoodsFormat goods_format=GoodsFormat.getByTitleGoods(shopping_cart.get("title").toString(), goods.get("id"));
				if(GoodsFormat.enable(goods_format)){
					shopping_cart.set("price", goods_format.get("price"));
					float item_subtotal=goods_format.getFloat("price") * shopping_cart.getInt("number");
					item_subtotal=CodeUtil.getNumber(item_subtotal);
					shopping_cart.set("item_subtotal", item_subtotal);
					subtotal+=item_subtotal;
					subtotal=CodeUtil.getNumber(subtotal);
					goods_list.add(shopping_cart);
				}else{
					Db.update("delete from db_shopping_cart where id=?", shopping_cart.get("id"));
				}
			}else{
				Db.update("delete from db_shopping_cart where id=?", shopping_cart.get("id"));
			}
		}
		setAttr("subtotal", subtotal);
		setAttr("goods_list", goods_list);
		render("shopping.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void addCart() throws Exception{
		
		Goods goods=Goods.dao.findById(getPara("id"));
		if(!Goods.enable(goods)){
			setAttr("success", false);
			setAttr("msg", "商品已失效");
			renderJson();
			return;
		}
		if(StrKit.notBlank(getPara("did"))){
			Diy diy=Diy.dao.findById(getPara("did"));
			if(!diy.get("user_id").toString().equals(getLoginUserId())){
				setAttr("success", false);
				setAttr("msg", "商品已失效");
				renderJson();
				return;
			}
			if(diy.getInt("status")!=Diy.STATUS_ENABLE){
				setAttr("success", false);
				setAttr("msg", "商品已失效");
				renderJson();
				return;
			}
		}
		String title=getPara("title");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "请选择商品规格");
			renderJson();
			return;
		}
		String brand_id=getPara("brand_id");
		if(goods.getInt("source")!=Goods.SOURCE_ZHAOPIAN){
			if(StrKit.isBlank(brand_id)){
				setAttr("success", false);
				setAttr("msg", "请选择品牌型号");
				renderJson();
				return;
			}
		}
		GoodsFormat goods_format=GoodsFormat.getByTitleGoods(title, goods.get("id"));
		if(goods_format==null || goods_format.getInt("status")!=GoodsFormat.STATUS_ENABLE){
			setAttr("success", false);
			setAttr("msg", "商品已失效");
			renderJson();
			return;
		}
		String number=getPara("number");
		if(StrKit.isBlank(number)){
			setAttr("success", false);
			setAttr("msg", "商品数量不能为空");
			renderJson();
			return;
		}
		ShoppingCart shopping_cart=new ShoppingCart();
		if(StrKit.notBlank(getPara("did"))){
			shopping_cart=ShoppingCart.getByUserGoods(getLoginUserId(), goods.get("id").toString(), getPara("did"), title);
		}else{
			shopping_cart=ShoppingCart.getByUserGoods(getLoginUserId(), goods.get("id").toString(), null, title);
		}
		if(shopping_cart!=null){
			if(goods.getInt("source")!=Goods.SOURCE_ZHAOPIAN){
				shopping_cart.set("brand_id", brand_id);
			}
			shopping_cart.set("number", getParaToInt("number"))
							.update();
		}else{
			shopping_cart=new ShoppingCart();
			if(StrKit.notBlank(getPara("did"))){
				shopping_cart.set("diy_id", getPara("did"));
			}
			if(goods.getInt("source")!=Goods.SOURCE_ZHAOPIAN){
				shopping_cart.set("brand_id", brand_id);
			}
			shopping_cart.set("goods_id", goods.get("id"))
							.set("user_id", getLoginUserId())
							.set("title", title)
							.set("number", getParaToInt("number"))
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
	public void changeNumber() throws Exception{
		
		if(StrKit.isBlank(getPara("number"))){
			setAttr("success", false);
			setAttr("msg", "商品数量不能为空");
			renderJson();
			return;
		}
		ShoppingCart shopping_cart=ShoppingCart.dao.findById(getPara("id"));
		if(shopping_cart!=null){
			if(!shopping_cart.get("user_id").toString().equals(getLoginUserId())){
				setAttr("success", false);
				setAttr("msg", "没有操作权限");
				renderJson();
				return;
			}
			shopping_cart.set("number", getParaToInt("number"))
							.update();
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
	public void deleteItem() throws Exception{
		
		ShoppingCart shopping_cart=ShoppingCart.dao.findById(getPara("id"));
		if(!shopping_cart.get("user_id").toString().equals(getLoginUserId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		shopping_cart.delete();
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
	public void address() throws Exception{
		
		Shop shop=Shop.dao.findById(getLoginUser().get("shop_id"));
		List<Record> goods_list=ShoppingCart.getByUser(getLoginUserId());
		float subtotal=0f;
		for(Record shopping_cart : goods_list){
			Goods goods=Goods.dao.findById(shopping_cart.get("goods_id"));
			if(goods.getInt("source")!=Goods.SOURCE_ZHAOPIAN){
				Brand brand=Brand.dao.findById(shopping_cart.get("brand_id"));
				Brand parent=Brand.dao.findById(brand.get("parent_id"));
				shopping_cart.set("brand", brand);
				shopping_cart.set("parent", parent);
			}
			shopping_cart.set("goods", goods);
			GoodsFormat goods_format=GoodsFormat.getByTitleGoods(shopping_cart.get("title").toString(), goods.get("id"));
			shopping_cart.set("price", goods_format.get("price"));
			subtotal+=com.project.util.CodeUtil.getNumber(goods_format.getFloat("price") * shopping_cart.getInt("number"));
		}
		Freight freight=Freight.getByProvince(null, getLoginUser().get("shop_id").toString());
		int tracking=1;
		if(shop.getInt("tracking")==Shop.TRACKING_ZITI){
			tracking=Orders.TRACKING_ZITI;
		}
		if(StrKit.notBlank(getPara("tracking"))){
			tracking=Integer.parseInt(getPara("tracking"));
		}
		User user=User.dao.findById(getLoginUserId());
		if(user.get("take_name")==null || StrKit.isBlank(user.get("take_name").toString())){
			//会员没有编辑地址
			setAttr("user_take_msg", 0);
		}else{
			//会员已经编辑地址
			setAttr("user_take_msg", 1);
			if(user.get("take_province")!=null && StrKit.notBlank(user.get("take_province").toString())){
				freight=Freight.getByProvince(user.get("take_province").toString(), getLoginUser().get("shop_id").toString());
			}
		}
		setAttr("tracking", tracking);
		setAttr("goods_list", goods_list);
		setAttr("subtotal", com.project.util.CodeUtil.getNumber(subtotal));
		setAttr("freight", com.project.util.CodeUtil.getNumber(freight.getFloat("price")));
		setAttr("grand_total", com.project.util.CodeUtil.getNumber(subtotal + freight.getFloat("price")));
		render("address.htm");
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
	public void createOrders() throws Exception{
		
		List<Record> goods_list=ShoppingCart.getByUser(getLoginUserId());
		if(goods_list==null || goods_list.size()==0){
			setAttr("success", false);
			setAttr("msg", "请选择商品");
			renderJson();
			return;
		}
		String tracking=getPara("tracking");
		if(StrKit.isBlank(tracking)){
			setAttr("success", false);
			setAttr("msg", "请选择发货方式");
			renderJson();
			return;
		}
		Shop shop=Shop.dao.findById(getLoginUser().get("shop_id"));
		if(Integer.parseInt(tracking)==Orders.TRACKING_YOUJI){
			if(shop.getInt("tracking")==Shop.TRACKING_ZITI){
				setAttr("success", false);
				setAttr("msg", "不支持该发货方式");
				renderJson();
				return;
			}
		}
		if(Integer.parseInt(tracking)==Orders.TRACKING_ZITI){
			if(shop.getInt("tracking")==Shop.TRACKING_YOUJI){
				setAttr("success", false);
				setAttr("msg", "不支持该发货方式");
				renderJson();
				return;
			}
		}
		float subtotal=0f;
		for(Record shopping_cart : goods_list){
			Goods goods=Goods.dao.findById(shopping_cart.get("goods_id"));
			GoodsFormat goods_format=GoodsFormat.getByTitleGoods(shopping_cart.get("title").toString(), goods.get("id"));
			if(!Goods.enable(goods)){
				setAttr("success", false);
				setAttr("msg", goods.get("title").toString() + "已失效");
				renderJson();
				return;
			}
			if(!GoodsFormat.enable(goods_format)){
				setAttr("success", false);
				setAttr("msg", goods.get("title").toString() + "已失效");
				renderJson();
				return;
			}
			if(goods_format.getInt("stock") < Integer.parseInt(shopping_cart.get("number").toString())){
				setAttr("success", false);
				setAttr("msg", goods.get("title").toString() + "库存不足");
				renderJson();
				return;
			}
			subtotal+=com.project.util.CodeUtil.getNumber(goods_format.getFloat("price") * shopping_cart.getInt("number"));
		}
		User user=User.dao.findById(getLoginUserId());
		if(Integer.parseInt(tracking)==Orders.TRACKING_YOUJI){
			if(user.get("take_province")==null || StrKit.isBlank(user.get("take_province").toString())){
				setAttr("success", false);
				setAttr("msg", "请输入收件地址");
				renderJson();
				return;
			}
		}else{
			if(user.get("take_name")==null || StrKit.isBlank(user.get("take_name").toString())){
				setAttr("success", false);
				setAttr("msg", "请输入自提人");
				renderJson();
				return;
			}
		}
		Orders orders=new Orders();
		if(Integer.parseInt(tracking)==Orders.TRACKING_YOUJI){
			Freight freight=Freight.getByProvince(user.get("take_province").toString(), getLoginUser().get("shop_id"));
			orders.set("freight_id", freight.get("id"))
					.set("freight_price", com.project.util.CodeUtil.getNumber(freight.getFloat("price")))
					.set("grand_total", com.project.util.CodeUtil.getNumber(subtotal + Float.parseFloat(freight.get("price").toString())));
		}else{
			orders.set("freight_price", 0)
					.set("grand_total", com.project.util.CodeUtil.getNumber(subtotal));
		}
		orders.set("code", com.project.util.CodeUtil.getCode())
				.set("wx_code", "wx" + com.project.util.CodeUtil.getCode())
				.set("subtotal", com.project.util.CodeUtil.getNumber(subtotal))
				.set("user_id", getLoginUserId())
				.set("shop_id", getLoginUser().get("shop_id"))
				.set("status", Orders.STATUS_NOT_PAY)
				.set("display", Orders.DISPLAY_YES)
				.set("take_name", user.get("take_name"))
				.set("take_telephone", user.get("take_telephone"))
				.set("take_province", user.get("take_province"))
				.set("take_area_msg", user.get("take_area_msg"))
				.set("take_address", user.get("take_address"))
				.set("take_post_code", user.get("take_post_code"))
				.set("remark", getPara("remark"))
				.set("tracking", tracking)
				.set("create_date", new Date())
				.save();
		for(Record shopping_cart : goods_list){
			Goods goods=Goods.dao.findById(shopping_cart.get("goods_id"));
			GoodsFormat goods_format=GoodsFormat.getByTitleGoods(shopping_cart.get("title").toString(), goods.get("id"));
			OrdersItem orders_item=new OrdersItem();
			if(shopping_cart.get("diy_id")!=null && StrKit.notBlank(shopping_cart.get("diy_id").toString())){
				Diy diy=Diy.dao.findById(shopping_cart.get("diy_id"));
				orders_item.set("diy_id", shopping_cart.get("diy_id"))
							.set("goods_img_url", diy.get("img_url"));
			}else{
				orders_item.set("goods_img_url", goods.get("img_url"));
			}
			if(goods.getInt("source")==Goods.SOURCE_ZHAOPIAN){
				orders_item.set("goods_width", goods.get("width"))
							.set("goods_height", goods.get("height"))
							.set("goods_number", goods.get("number"));
			}else{
				orders_item.set("brand_id", shopping_cart.get("brand_id"));
			}
			orders_item.set("orders_id", orders.get("id"))
						.set("goods_id", goods.get("id"))
						.set("goods_title", goods.get("title"))
						.set("goods_format_title", goods_format.get("title"))
						.set("goods_source", goods.get("source"))
						.set("item_number", shopping_cart.getInt("number"))
						.set("item_price", goods_format.get("price"))
						.set("item_subtotal", com.project.util.CodeUtil.getNumber(goods_format.getFloat("price") * shopping_cart.getInt("number")))
						.save();
			Db.update("delete from db_shopping_cart where id=?", shopping_cart.get("id"));
		}
		OrdersLog.save(orders.get("id"), "会员创建订单", new Date());
		setAttr("success", true);
		setAttr("code", orders.get("code"));
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
	public void payment() throws Exception{
		
		PropKit.use("config.txt");
		Orders orders=Orders.getByCode(getPara("code"));
		Shop shop=Shop.dao.findById(orders.get("shop_id"));
		if(shop.getInt("payment")==Shop.PAYMENT_XIANXIA){
			setAttr("success", false);
			setAttr("msg", "商家不支持该付款方式");
			renderJson();
			return;
		}
		if(orders==null || orders.getInt("display")==Orders.DISPLAY_NONE){
			setAttr("success", false);
			setAttr("msg", "订单不存在");
			renderJson();
			return;
		}
		if(orders.getInt("status")!=Orders.STATUS_NOT_PAY){
			setAttr("success", false);
			setAttr("msg", "订单已支付");
			renderJson();
			return;
		}
		if(!getLoginUserId().equals(orders.get("user_id").toString())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		List<Record> item_list=OrdersItem.getByOrders(orders.get("id"));
		for(Record item : item_list){
			Goods goods=Goods.dao.findById(item.get("goods_id"));
			GoodsFormat goods_format=GoodsFormat.getByTitleGoods(item.get("goods_format_title").toString(), goods.get("id"));
			if(!Goods.enable(goods) || !GoodsFormat.enable(goods_format)){
				setAttr("success", false);
				setAttr("msg", item.get("goods_title").toString() + "已失效，请重新下单");
				renderJson();
				return;
			}
			if(goods_format.getInt("stock") < item.getInt("item_number")){
				setAttr("success", false);
				setAttr("msg", goods.get("title").toString() + "库存不足");
				renderJson();
				return;
			}
		}
		orders.set("wx_code", CodeUtil.getCode())
				.update();
		User user=User.dao.findById(orders.get("user_id"));
		WxPayDto tpWxPay = new WxPayDto();
		tpWxPay.setAppid(PropKit.get("appid"));
		tpWxPay.setAppsecret(PropKit.get("appsecret"));
		tpWxPay.setPartner(PropKit.get("partner"));
		tpWxPay.setPartnerkey(PropKit.get("partnerkey"));
		tpWxPay.setNotifyurl(PropKit.get("wxUrl").toString() + "/wap/shop/wxPayCallback");
		tpWxPay.setOpenId(user.get("openid").toString());
		tpWxPay.setBody("支付订单" + orders.get("code").toString());
		tpWxPay.setOrderId(orders.get("wx_code").toString());
		tpWxPay.setSpbillCreateIp(PayUtil.getIp(getRequest()));
		tpWxPay.setTotalFee(orders.get("grand_total").toString());
		SortedMap<String, String> pay_msg=PayUtil.getPackage(tpWxPay);
		System.out.println(pay_msg);
		setAttr("pay_msg", pay_msg);
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
	@ClearInterceptor(ClearLayer.ALL)
	@Before({ExceptionInterceptor.class, Tx.class})
	public void wxPayCallback() throws Exception{

		ServletInputStream in = getRequest().getInputStream();  
		String xmlMsg = Tools.inputStream2String(in);  
		System.out.println(xmlMsg);
		String jsonStr=new XMLSerializer().read(xmlMsg).toString();
		JSONObject json=JSONObject.parseObject(jsonStr);
		String code=json.get("out_trade_no").toString();
		Orders orders=Orders.getByCode(code);
		String appid=PropKit.get("appid");
		String appsecret=PropKit.get("appsecret");
		String mch_id=PropKit.get("partner");
		String pkey=PropKit.get("partnerkey");
		String url="https://api.mch.weixin.qq.com/pay/orderquery";
		String currTime = TenpayUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = TenpayUtil.buildRandom(4) + "";
		String nonce_str = strTime + strRandom;
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", code);
		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, pkey);
		String sign = reqHandler.createSign(packageParams);
		String xmlParam = "<xml>" + "<appid>" + appid + "</appid>"
							+ "<mch_id>" + mch_id + "</mch_id>"
							+ "<nonce_str>" + nonce_str + "</nonce_str>"
							+ "<sign><![CDATA[" + sign + "]]></sign>"
							+ "<out_trade_no>" + code + "</out_trade_no>"
							+ "</xml>";
		Map<String, String> map=GetWxOrderno.doXML(url, xmlParam);
		String return_code=map.get("return_code").toString();
		if("SUCCESS".equals(return_code)){
			String result_code=map.get("result_code").toString();
			if("SUCCESS".equals(result_code)){
				String trade_state=map.get("trade_state").toString();
				if("SUCCESS".equals(trade_state)){
					if(orders.getInt("status")==Orders.STATUS_NOT_PAY){
						SynchronizedUtil.wxPaymentCallbackSynchronized(orders);
					}
				}
			}
		}
		String msg="<xml>";
			msg+="<return_code>SUCCESS</return_code>";
			msg+="<return_msg>OK</return_msg>";
		msg+="</xml>";
		renderJson(msg);
		return;
	}
}
