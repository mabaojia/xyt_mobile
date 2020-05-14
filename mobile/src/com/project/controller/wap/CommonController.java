package com.project.controller.wap;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

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
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.OAuthApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.project.aop.ExceptionInterceptor;
import com.project.common.SynchronizedUtil;
import com.project.model.Article;
import com.project.model.Brand;
import com.project.model.Carousel;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.GoodsComment;
import com.project.model.GoodsFormat;
import com.project.model.Notice;
import com.project.model.Orders;
import com.project.model.ShoppingCart;
import com.project.model.Type;
import com.project.util.DateUtil;
import com.project.weixin.pay.GetWxOrderno;
import com.project.weixin.pay.RequestHandler;
import com.project.weixin.pay.TenpayUtil;
import com.project.weixin.pay.Tools;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/wap", viewPath = "/wap")
public class CommonController extends ApiController{

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
		
		//轮播图
		List<Record> carousel_list=Carousel.getList(getLoginUser().get("shop_id"));
		setAttr("carousel_list", carousel_list);
		//商品分类
		List<Record> type_list=Type.getList(Type.TYPE_GOODS, getLoginUser().get("shop_id"));
		setAttr("type_list", type_list);
		//手机壳
		List<Record> goods_list_1=Db.find("select g.*,t.title type_title from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.top=? and g.shop_id=? order by g.top_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, Goods.SOURCE_SHOUJIKE, Type.STATUS_ENABLE, Goods.TOP_YES, getLoginUser().get("shop_id"));
		setAttr("goods_list_1", goods_list_1);
		//手机膜
		List<Record> goods_list_2=Db.find("select g.*,t.title type_title from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.top=? and g.shop_id=? order by g.top_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, Goods.SOURCE_SHOUJIMO, Type.STATUS_ENABLE, Goods.TOP_YES, getLoginUser().get("shop_id"));
		setAttr("goods_list_2", goods_list_2);
		//照片
		List<Record> goods_list_3=Db.find("select g.*,t.title type_title from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.top=? and g.shop_id=? order by g.top_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, Goods.SOURCE_ZHAOPIAN, Type.STATUS_ENABLE, Goods.TOP_YES, getLoginUser().get("shop_id"));
		setAttr("goods_list_3", goods_list_3);
		//置顶分类
		List<Record> top_list=Db.find("select * from db_type where status=? and type=? and top=? and shop_id=? order by top_date desc", Type.STATUS_ENABLE, Type.TYPE_GOODS, Type.TOP_YES, getLoginUser().get("shop_id"));
		for(Record item : top_list){
			List<Record> goods_list=Db.find("select g.*,t.title type_title from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.type_id=? and g.top=? and g.shop_id=? order by g.top_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, item.get("id"), Goods.TOP_YES, getLoginUser().get("shop_id"));
			item.set("goods_list", goods_list);
		}
		setAttr("top_list", top_list);
		//公告
		List<Notice> notice_list = Notice.getList(getLoginUser().get("shop_id"));
		setAttr("notice_list", notice_list);
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
	public void list() throws Exception{
	
		//商品列表
		int page=getParaToInt("p",1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select g.*,t.title type_title";
		String sWhere=" from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and t.status=? and g.shop_id=?";
		params.add(Goods.STATUS_ENABLE);
		params.add(Goods.PUBLISH_YES);
		params.add(Type.STATUS_ENABLE);
		params.add(getLoginUser().get("shop_id"));
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
		if(StrKit.notBlank(getPara("content"))){
			sWhere+=" and g.title like ?";
			params.add("%" + getPara("content") + "%");
			setAttr("content", getPara("content"));
		}
		if(StrKit.notBlank(getPara("oby"))){
			if(getParaToInt("oby")==1){
				sWhere+=" order by g.sale_number desc";
			}else if(getParaToInt("oby")==2){
				sWhere+=" order by g.price asc";
			}else{
				sWhere+=" order by g.publish_date desc";
			}
			setAttr("oby", getParaToInt("oby"));
		}else{
			sWhere+=" order by g.publish_date desc";
		}
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record item : results.getList()){
			List<String> labels_list=new ArrayList<String>();
			if(item.get("labels")!=null && StrKit.notBlank(item.get("labels").toString())){
				String [] labels=item.get("labels").toString().split(",");
				for(String label : labels){
					if(StrKit.notBlank(label)){
						labels_list.add(label);
					}
				}
			}
			item.set("labels_list", labels_list);
			Diy diy=Diy.dao.findFirst("select d.* from db_diy d left join db_brand b on d.brand_id=b.id left join db_brand p on b.parent_id=p.id left join db_goods g on d.goods_id=g.id where d.status=? and b.status=? and p.status=? and d.goods_id=? and d.user_id is null and (g.source=? or g.source=? or g.source=? or g.source=? or g.source=?) order by d.create_date desc", Diy.STATUS_ENABLE, Brand.STATUS_ENABLE, Brand.STATUS_ENABLE, item.get("id"), Goods.SOURCE_SHOUJIKE, Goods.SOURCE_SHOUJIMO, Goods.SOURCE_FUZHUANG, Goods.SOURCE_DIAOKE, Goods.SOURCE_LIPIN);
			item.set("diy", diy);
			if(diy!=null){
				item.set("exist_diy", 1);
			}else{
				item.set("exist_diy", 0);
			}
		}
		setAttr("results", results);
		setAttr("totalPage", results.getTotalPage());
		setAttr("totalRow", results.getTotalRow());
		//商品分类
		List<Record> type_list=Type.getList(Type.TYPE_GOODS, getLoginUser().get("shop_id"));
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
	public void photo() throws Exception{
	
		//商品列表
		int page=getParaToInt("p",1);
		List<Object> params = new ArrayList<Object>();
		String sSelect="select g.*,t.title type_title";
		String sWhere=" from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and t.status=? and g.shop_id=?";
		params.add(Goods.STATUS_ENABLE);
		params.add(Goods.PUBLISH_YES);
		params.add(Type.STATUS_ENABLE);
		params.add(getLoginUser().get("shop_id"));
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
		if(StrKit.notBlank(getPara("content"))){
			sWhere+=" and g.title like ?";
			params.add("%" + getPara("content") + "%");
			setAttr("content", getPara("content"));
		}
		if(StrKit.notBlank(getPara("oby"))){
			if(getParaToInt("oby")==1){
				sWhere+=" order by g.sale_number desc";
			}else if(getParaToInt("oby")==2){
				sWhere+=" order by g.price asc";
			}else{
				sWhere+=" order by g.publish_date desc";
			}
			setAttr("oby", getParaToInt("oby"));
		}else{
			sWhere+=" order by g.publish_date desc";
		}
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record item : results.getList()){
			List<String> labels_list=new ArrayList<String>();
			if(item.get("labels")!=null && StrKit.notBlank(item.get("labels").toString())){
				String [] labels=item.get("labels").toString().split(",");
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
		setAttr("totalRow", results.getTotalRow());
		//商品分类
		List<Record> type_list=Type.getList(Type.TYPE_GOODS, getLoginUser().get("shop_id"));
		setAttr("type_list", type_list);
		render("photo.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void brand() throws Exception{
		
		List<Record> list=new ArrayList<Record>();
		if(getParaToInt("type")==1 || getParaToInt("type")==2){
			list=Brand.getList(null, getLoginUser().get("shop_id"), Brand.TYPE_PINPAI);
		}else{
			list=Brand.getList(null, getLoginUser().get("shop_id"), getParaToInt("type") - 1);
		}
		for(Record item : list){
			List<Record> brand_list=new ArrayList<Record>();
			List<Record> results=new ArrayList<Record>();
			if(getParaToInt("type")==1 || getParaToInt("type")==2){
				results=Brand.getList(item.get("id"), getLoginUser().get("shop_id"), Brand.TYPE_PINPAI);
			}else{
				results=Brand.getList(item.get("id"), getLoginUser().get("shop_id"), getParaToInt("type") - 1);
			}
			for(Record each : results){
				Diy diy=Diy.getByBrandType(each.get("id").toString(), getParaToInt("type"));
				if(diy!=null && diy.get("goods_id")!=null && StrKit.notBlank(diy.get("goods_id").toString())){
					each.set("diy", diy);
					brand_list.add(each);
				}
			}
			item.set("brand_list", brand_list);
		}
		setAttr("list", list);
		//定制类型
		setAttr("type", getParaToInt("type"));
		render("brand.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void goods() throws Exception{
	
		Goods goods=Goods.getByCode(getPara("code"));
		if(StrKit.notBlank(getPara("dcode"))){
			Diy diy=Diy.getByCode(getPara("dcode"));
			if(!diy.get("user_id").toString().equals(getLoginUserId())){
				setAttr("msg", "没有操作权限");
				render("/wap/msg.htm");
				return;
			}
			if(diy.getInt("status")!=Diy.STATUS_ENABLE){
				setAttr("msg", "没有操作权限");
				render("/wap/msg.htm");
				return;
			}
			goods.set("img_url", diy.get("img_url"))
					.set("img_urls", "," + diy.get("img_url") + ",");
			setAttr("diy", diy);
			Brand brand=Brand.dao.findById(diy.get("brand_id"));
			setAttr("brand", brand);
			setAttr("parent", Brand.dao.findById(brand.get("parent_id")));
		}
		if(!Goods.enable(goods)){
			goods.set("publish", Goods.PUBLISH_NO);
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
		setAttr("labels_list", labels_list);
		//商品规格
		List<GoodsFormat> format_list=GoodsFormat.getList(goods.get("id"));
		setAttr("format_list", format_list);
		//商品分类
		Type type=Type.dao.findById(goods.get("type_id"));
		setAttr("type", type);
		//商品评价
		List<Record> comment_list=GoodsComment.getList(goods.get("id").toString());
		setAttr("comment_list", comment_list);
		//购物车数量
		setAttr("cart_number", ShoppingCart.getNumberByUser(getLoginUserId()));
		//私人定制
		Diy diy_1=Diy.dao.findFirst("select d.* from db_diy d left join db_brand b on d.brand_id=b.id left join db_brand p on b.parent_id=p.id left join db_goods g on d.goods_id=g.id where d.status=? and b.status=? and p.status=? and d.goods_id=? and d.user_id is null and (g.source=? or g.source=? or g.source=? or g.source=? or g.source=?) order by d.create_date desc", Diy.STATUS_ENABLE, Brand.STATUS_ENABLE, Brand.STATUS_ENABLE, goods.get("id"), Goods.SOURCE_SHOUJIKE, Goods.SOURCE_SHOUJIMO, Goods.SOURCE_FUZHUANG, Goods.SOURCE_DIAOKE, Goods.SOURCE_LIPIN);
		setAttr("diy_1", diy_1);
		//品牌型号
		List<Record> brand_list=new ArrayList<Record>();
		if(goods.getInt("source")==1 || goods.getInt("source")==2){
			brand_list=Brand.getList(null, getLoginUser().get("shop_id"), Brand.TYPE_PINPAI);
		}else{
			brand_list=Brand.getList(null, getLoginUser().get("shop_id"), goods.getInt("source") - 2);
		}
		for(Record item : brand_list){
			List<Record> child_list=new ArrayList<Record>();
			if(goods.getInt("source")==1 || goods.getInt("source")==2){
				child_list=Brand.getList(item.get("id"), getLoginUser().get("shop_id"), Brand.TYPE_PINPAI);
			}else{
				child_list=Brand.getList(item.get("id"), getLoginUser().get("shop_id"), goods.getInt("source") - 2);
			}
			item.set("child_list", child_list);
		}
		setAttr("brand_list", brand_list);
		render("goods.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void article() throws Exception{
		
		Article article=Article.dao.findById(getPara("id"));
		setAttr("article", article);
		render("article.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void notice() throws Exception{
		
		Notice notice=Notice.dao.findById(getPara("id"));
		setAttr("notice", notice);
		render("notice.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	public void noticeList() throws Exception{
		
		List<Notice> list = Notice.getList(getLoginUser().get("shop_id"));
		setAttr("list", list);
		render("noticeList.htm");
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
	public void wxPaymentCallback() throws Exception{

		PropKit.use("config.txt");
		ServletInputStream in = getRequest().getInputStream();  
		String xmlMsg = Tools.inputStream2String(in);  
		System.out.println(xmlMsg);
		String jsonStr=new XMLSerializer().read(xmlMsg).toString();
		JSONObject json=JSONObject.parseObject(jsonStr);
		String code=json.get("out_trade_no").toString();
		String appid = PropKit.get("appid").toString();
		String appsecret = PropKit.get("appsecret").toString();
		String mch_id = PropKit.get("partner").toString();
		String pkey = PropKit.get("partnerkey").toString();
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
		String xmlParam = "<xml>"
							+ "<appid>" + appid + "</appid>"
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
					Orders orders=Orders.getByCode(code);
					SynchronizedUtil.wxPaymentCallbackSynchronized(orders);
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
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void uploadImg() throws Exception{
		
		String save_path="/static/image/" + DateUtil.formatDate(new Date(), "yyyyMMdd") + "/";
		File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path);  
		if(!file.exists()){  
		    file.mkdirs();  
		}  
		UploadFile uploadFile=getFile("image", getRequest().getSession().getServletContext().getRealPath("/") + save_path);
		String type=uploadFile.getContentType().toLowerCase();
	    File rename_file=uploadFile.getFile();
	    String new_name=UUID.randomUUID().toString().replace("-", "") + "." + type.replace("image/", "").replace("+xml", "");
	    rename_file.renameTo(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path + new_name));
	    String path=save_path + new_name;
	    setAttr("success", true);
		setAttr("img_url", path);
		renderJson();
		return;
	}
}