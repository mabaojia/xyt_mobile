package com.project.controller.shop;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.util.CodeUtil;

/**
 *
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/brand/store", viewPath = "/shop/brand/store")
public class BrandStoreController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Record> list=Brand.getList(null, null,getParaToInt("type"));
		for(Record item : list){
			item.set("brand_list", Brand.getList(item.get("id"), null,getParaToInt("type")));
		}
		setAttr("list", list);
		setAttr("type", getParaToInt("type"));
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
	public void list_1() throws Exception{
		
		Brand brand=Brand.dao.findById(getPara("id"));
		setAttr("brand", brand);
		//类型
		setAttr("type", brand.getInt("type"));
		List<Record> list=Brand.getAll(getPara("id"), null,getPara("type"));
		for(Record item : list){
			//手机壳
			Diy diy_1=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIKE);
			item.set("diy_1", diy_1);
			if(diy_1!=null){
				item.set("diys_1", 1);
			}else{
				item.set("diys_1", 0);
			}
			//手机膜
			Diy diy_2=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIMO);
			item.set("diy_2", diy_2);
			if(diy_2!=null){
				item.set("diys_2", 1);
			}else{
				item.set("diys_2", 0);
			}
			//定制模板
			Diy diy=Diy.getByBrandType(item.get("id").toString(), brand.getInt("type"));
			item.set("diy", diy);
			if(diy!=null){
				item.set("diys", 1);
			}else{
				item.set("diys", 0);
			}
		}
		setAttr("list", list);
		render("list_1.htm");
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
	public void saveBrand() throws Exception{
	
		Brand parent=Brand.dao.findById(getPara("id"));
		List<Record> brand_list=Brand.getList(parent.get("id"), null,getPara("type"));
		Brand parent_1=Brand.dao.findFirst("select * from db_brand where source_id=? and shop_id=? and type=?  and status!=?", parent.get("id"), getLoginShopId(), getPara("type"), Brand.STATUS_DELETE);
		if(parent_1!=null){
			parent_1.set("title", parent.get("title"))
					.set("idx", 1)
					.set("shop_id", getLoginShopId())
					.set("source_id", parent.get("id"))
					.set("status", Brand.STATUS_ENABLE)
					.set("create_date", new Date())
					.set("type", getPara("type"))
					.update();
		}else{
			parent_1=new Brand();
			parent_1.set("title", parent.get("title"))
					.set("idx", 1)
					.set("shop_id", getLoginShopId())
					.set("source_id", parent.get("id"))
					.set("status", Brand.STATUS_ENABLE)
					.set("type", getPara("type"))
					.set("create_date", new Date())
					.save();
		}
		for(Record item : brand_list){
			Brand brand=Brand.dao.findFirst("select * from db_brand where source_id=? and shop_id=? and type=? and status!=?", item.get("id"), getLoginShopId(), getPara("type"), Brand.STATUS_DELETE);
			if(brand!=null){
				brand.set("title", item.get("title"))
						.set("idx", 1)
						.set("parent_id", parent_1.get("id"))
						.set("shop_id", getLoginShopId())
						.set("source_id", item.get("id"))
						.set("status", Brand.STATUS_ENABLE)
						.set("type", getPara("type"))
						.set("create_date", new Date())
						.update();
			}else{
				brand=new Brand();
				brand.set("title", item.get("title"))
						.set("idx", 1)
						.set("parent_id", parent_1.get("id"))
						.set("shop_id", getLoginShopId())
						.set("source_id", item.get("id"))
						.set("status", Brand.STATUS_ENABLE)
						.set("type", getPara("type"))
						.set("create_date", new Date())
						.save();
			}
			if(Integer.parseInt(brand.get("type").toString())==Brand.TYPE_PINPAI){
				Diy diy_1=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIKE);
				if(diy_1!=null){
					Diy diy_1_1=Diy.getByBrandType(brand.get("id").toString(), Diy.TYPE_SHOUJIKE);
					if(diy_1_1!=null){
						diy_1_1.set("title", diy_1.get("title"))
								.set("img_url", diy_1.get("img_url"))
								.set("width", diy_1.get("width"))
								.set("height", diy_1.get("height"))
								.set("content", diy_1.get("content"))
								.set("brand_id", brand.get("id"))
								.set("shop_id", getLoginShopId())
								.set("status", Diy.STATUS_ENABLE)
								.set("type", Diy.TYPE_SHOUJIKE)
								.set("create_date", new Date())
								.update();
					}else{
						diy_1_1=new Diy();
						diy_1_1.set("code", CodeUtil.getCode())
								.set("title", diy_1.get("title"))
								.set("img_url", diy_1.get("img_url"))
								.set("width", diy_1.get("width"))
								.set("height", diy_1.get("height"))
								.set("content", diy_1.get("content"))
								.set("brand_id", brand.get("id"))
								.set("shop_id", getLoginShopId())
								.set("status", Diy.STATUS_ENABLE)
								.set("type", Diy.TYPE_SHOUJIKE)
								.set("create_date", new Date())
								.save();
					}
				}
				Diy diy_2=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIMO);
				if(diy_2!=null){
					Diy diy_2_1=Diy.getByBrandType(brand.get("id").toString(), Diy.TYPE_SHOUJIMO);
					if(diy_2_1!=null){
						diy_2_1.set("title", diy_2.get("title"))
								.set("img_url", diy_2.get("img_url"))
								.set("width", diy_2.get("width"))
								.set("height", diy_2.get("height"))
								.set("content", diy_2.get("content"))
								.set("brand_id", brand.get("id"))
								.set("shop_id", getLoginShopId())
								.set("status", Diy.STATUS_ENABLE)
								.set("type", Diy.TYPE_SHOUJIMO)
								.set("create_date", new Date())
								.update();
					}else{
						diy_2_1=new Diy();
						diy_2_1.set("code", CodeUtil.getCode())
								.set("title", diy_2.get("title"))
								.set("img_url", diy_2.get("img_url"))
								.set("width", diy_2.get("width"))
								.set("height", diy_2.get("height"))
								.set("content", diy_2.get("content"))
								.set("brand_id", brand.get("id"))
								.set("shop_id", getLoginShopId())
								.set("status", Diy.STATUS_ENABLE)
								.set("type", Diy.TYPE_SHOUJIMO)
								.set("create_date", new Date())
								.save();
					}
				}
			}
			if(Integer.parseInt(brand.get("type").toString())!=Brand.TYPE_PINPAI){
				Diy diy=Diy.getByBrandType(item.get("id").toString(), Integer.parseInt(brand.get("type").toString()) + 1);
				if(diy!=null){
					Diy diy_new=Diy.getByBrandType(brand.get("id").toString(), Integer.parseInt(brand.get("type").toString()) + 1);
					if(diy_new!=null){
						diy_new.set("title", diy.get("title"))
								.set("img_url", diy.get("img_url"))
								.set("width", diy.get("width"))
								.set("height", diy.get("height"))
								.set("content", diy.get("content"))
								.set("brand_id", brand.get("id"))
								.set("shop_id", getLoginShopId())
								.set("status", Diy.STATUS_ENABLE)
								.set("type", Integer.parseInt(brand.get("type").toString()) + 1)
								.set("create_date", new Date())
								.update();
					}else{
						diy_new=new Diy();
						diy_new.set("code", CodeUtil.getCode())
								.set("title", diy.get("title"))
								.set("img_url", diy.get("img_url"))
								.set("width", diy.get("width"))
								.set("height", diy.get("height"))
								.set("content", diy.get("content"))
								.set("brand_id", brand.get("id"))
								.set("shop_id", getLoginShopId())
								.set("status", Diy.STATUS_ENABLE)
								.set("type", Integer.parseInt(brand.get("type").toString()) + 1)
								.set("create_date", new Date())
								.save();
					}
				}
			}
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
	@Before(Tx.class)
	public void saveBrands() throws Exception{
		
		for (String bids : getPara("bids").toString().split(",")) {
			if (StrKit.notBlank(bids)) {
				Brand parent=Brand.dao.findById(bids);
				List<Record> brand_list=Brand.getList(parent.get("id"), null,getPara("type"));
				Brand parent_1=Brand.dao.findFirst("select * from db_brand where source_id=? and shop_id=? and type=? and status!=?", parent.get("id"), getLoginShopId(), getPara("type"), Brand.STATUS_DELETE);
				if(parent_1!=null){
					parent_1.set("title", parent.get("title"))
							.set("idx", 1)
							.set("shop_id", getLoginShopId())
							.set("source_id", parent.get("id"))
							.set("status", Brand.STATUS_ENABLE)
							.set("type", getPara("type"))
							.set("create_date", new Date())
							.update();
				}else{
					parent_1=new Brand();
					parent_1.set("title", parent.get("title"))
							.set("idx", 1)
							.set("shop_id", getLoginShopId())
							.set("source_id", parent.get("id"))
							.set("status", Brand.STATUS_ENABLE)
							.set("type", getPara("type"))
							.set("create_date", new Date())
							.save();
				}
				for(Record item : brand_list){
					Brand brand=Brand.dao.findFirst("select * from db_brand where source_id=? and shop_id=? and type=? and status!=?", item.get("id"), getLoginShopId(), getPara("type"), Brand.STATUS_DELETE);
					if(brand!=null){
						brand.set("title", item.get("title"))
								.set("idx", 1)
								.set("parent_id", parent_1.get("id"))
								.set("shop_id", getLoginShopId())
								.set("source_id", item.get("id"))
								.set("status", Brand.STATUS_ENABLE)
								.set("type", getPara("type"))
								.set("create_date", new Date())
								.update();
					}else{
						brand=new Brand();
						brand.set("title", item.get("title"))
								.set("idx", 1)
								.set("parent_id", parent_1.get("id"))
								.set("shop_id", getLoginShopId())
								.set("source_id", item.get("id"))
								.set("status", Brand.STATUS_ENABLE)
								.set("type", getPara("type"))
								.set("create_date", new Date())
								.save();
					}
					if(Integer.parseInt(brand.get("type").toString())==Brand.TYPE_PINPAI){
						Diy diy_1=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIKE);
						if(diy_1!=null){
							Diy diy_1_1=Diy.getByBrandType(brand.get("id").toString(), Diy.TYPE_SHOUJIKE);
							if(diy_1_1!=null){
								diy_1_1.set("title", diy_1.get("title"))
										.set("img_url", diy_1.get("img_url"))
										.set("width", diy_1.get("width"))
										.set("height", diy_1.get("height"))
										.set("content", diy_1.get("content"))
										.set("brand_id", brand.get("id"))
										.set("shop_id", getLoginShopId())
										.set("status", Diy.STATUS_ENABLE)
										.set("type", Diy.TYPE_SHOUJIKE)
										.set("create_date", new Date())
										.update();
							}else{
								diy_1_1=new Diy();
								diy_1_1.set("code", CodeUtil.getCode())
										.set("title", diy_1.get("title"))
										.set("img_url", diy_1.get("img_url"))
										.set("width", diy_1.get("width"))
										.set("height", diy_1.get("height"))
										.set("content", diy_1.get("content"))
										.set("brand_id", brand.get("id"))
										.set("shop_id", getLoginShopId())
										.set("status", Diy.STATUS_ENABLE)
										.set("type", Diy.TYPE_SHOUJIKE)
										.set("create_date", new Date())
										.save();
							}
						}
						Diy diy_2=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIMO);
						if(diy_2!=null){
							Diy diy_2_1=Diy.getByBrandType(brand.get("id").toString(), Diy.TYPE_SHOUJIMO);
							if(diy_2_1!=null){
								diy_2_1.set("title", diy_2.get("title"))
										.set("img_url", diy_2.get("img_url"))
										.set("width", diy_2.get("width"))
										.set("height", diy_2.get("height"))
										.set("content", diy_2.get("content"))
										.set("brand_id", brand.get("id"))
										.set("shop_id", getLoginShopId())
										.set("status", Diy.STATUS_ENABLE)
										.set("type", Diy.TYPE_SHOUJIMO)
										.set("create_date", new Date())
										.update();
							}else{
								diy_2_1=new Diy();
								diy_2_1.set("code", CodeUtil.getCode())
										.set("title", diy_2.get("title"))
										.set("img_url", diy_2.get("img_url"))
										.set("width", diy_2.get("width"))
										.set("height", diy_2.get("height"))
										.set("content", diy_2.get("content"))
										.set("brand_id", brand.get("id"))
										.set("shop_id", getLoginShopId())
										.set("status", Diy.STATUS_ENABLE)
										.set("type", Diy.TYPE_SHOUJIMO)
										.set("create_date", new Date())
										.save();
							}
						}
					}
					if(Integer.parseInt(brand.get("type").toString())!=Brand.TYPE_PINPAI){
						Diy diy=Diy.getByBrandType(item.get("id").toString(), Integer.parseInt(brand.get("type").toString()) + 1);
						if(diy!=null){
							Diy diy_new=Diy.getByBrandType(brand.get("id").toString(), Integer.parseInt(brand.get("type").toString()) + 1);
							if(diy_new!=null){
								diy_new.set("title", diy.get("title"))
										.set("img_url", diy.get("img_url"))
										.set("width", diy.get("width"))
										.set("height", diy.get("height"))
										.set("content", diy.get("content"))
										.set("brand_id", brand.get("id"))
										.set("shop_id", getLoginShopId())
										.set("status", Diy.STATUS_ENABLE)
										.set("type", Integer.parseInt(brand.get("type").toString()) + 1)
										.set("create_date", new Date())
										.update();
							}else{
								diy_new=new Diy();
								diy_new.set("code", CodeUtil.getCode())
										.set("title", diy.get("title"))
										.set("img_url", diy.get("img_url"))
										.set("width", diy.get("width"))
										.set("height", diy.get("height"))
										.set("content", diy.get("content"))
										.set("brand_id", brand.get("id"))
										.set("shop_id", getLoginShopId())
										.set("status", Diy.STATUS_ENABLE)
										.set("type", Integer.parseInt(brand.get("type").toString()) + 1)
										.set("create_date", new Date())
										.save();
							}
						}
					}
				}
			}
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
	public void addBrand_1() throws Exception{
		
		Brand brand=Brand.dao.findById(getPara("id"));
		setAttr("brand", brand);
		List<Record> brand_list=Brand.getAll(null, getLoginShopId(), getPara("type"));
		setAttr("brand_list", brand_list);
		setAttr("type", getParaToInt("type"));
		render("addBrand_1.htm");
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
	public void saveBrand_1() throws Exception{
		
		Brand parent=Brand.dao.findById(getPara("parent_id"));
		if(!parent.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		Brand item=Brand.dao.findById(getPara("brand_id"));
		Brand brand=new Brand();
		brand.set("title", item.get("title"))
				.set("idx", 1)
				.set("parent_id", parent.get("id"))
				.set("shop_id", getLoginShopId())
				.set("status", Brand.STATUS_ENABLE)
				.set("type", getParaToInt("type"))
				.set("create_date", new Date())
				.save();
		if(Integer.parseInt(brand.get("type").toString())==Brand.TYPE_PINPAI){
			Diy diy_1=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIKE);
			if(diy_1!=null){
				Diy diy_1_1=new Diy();
				diy_1_1.set("code", CodeUtil.getCode())
						.set("title", diy_1.get("title"))
						.set("img_url", diy_1.get("img_url"))
						.set("width", diy_1.get("width"))
						.set("height", diy_1.get("height"))
						.set("content", diy_1.get("content"))
						.set("brand_id", brand.get("id"))
						.set("shop_id", getLoginShopId())
						.set("status", Diy.STATUS_ENABLE)
						.set("type", Diy.TYPE_SHOUJIKE)
						.set("create_date", new Date())
						.save();
			}
			Diy diy_2=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIMO);
			if(diy_2!=null){
				Diy diy_2_1=new Diy();
				diy_2_1.set("code", CodeUtil.getCode())
						.set("title", diy_2.get("title"))
						.set("img_url", diy_2.get("img_url"))
						.set("width", diy_2.get("width"))
						.set("height", diy_2.get("height"))
						.set("content", diy_2.get("content"))
						.set("brand_id", brand.get("id"))
						.set("shop_id", getLoginShopId())
						.set("status", Diy.STATUS_ENABLE)
						.set("type", Diy.TYPE_SHOUJIMO)
						.set("create_date", new Date())
						.save();
			}
		}
		if(Integer.parseInt(brand.get("type").toString())!=Brand.TYPE_PINPAI){
			Diy diy=Diy.getByBrandType(item.get("id").toString(), Integer.parseInt(brand.get("type").toString()) + 1);
			if(diy!=null){
				Diy diy_new=new Diy();
				diy_new.set("code", CodeUtil.getCode())
						.set("title", diy.get("title"))
						.set("img_url", diy.get("img_url"))
						.set("width", diy.get("width"))
						.set("height", diy.get("height"))
						.set("content", diy.get("content"))
						.set("brand_id", brand.get("id"))
						.set("shop_id", getLoginShopId())
						.set("status", Diy.STATUS_ENABLE)
						.set("type", Integer.parseInt(brand.get("type").toString()) + 1)
						.set("create_date", new Date())
						.save();
			}
		}
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
