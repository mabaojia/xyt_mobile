package com.project.controller.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.common.BaseController;
import com.project.model.Brand;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.Type;

/**
 *
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/brand", viewPath = "/shop/brand")
public class BrandController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		List<Record> list=Brand.getAll(null, getLoginShopId(), getParaToInt("type"));
		for(Record item : list){
			item.set("brand_list", Brand.getAll(item.get("id"), getLoginShopId(), getParaToInt("type")));
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
		List<Record> list=Brand.getAll(getPara("id"), getLoginShopId(), brand.get("type"));
		for(Record item : list){
			//手机壳
			Diy diy_1=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIKE);
			item.set("diy_1", diy_1);
			if(diy_1!=null){
				Goods goods_1=Goods.dao.findById(diy_1.get("goods_id"));
				item.set("goods_1", goods_1);
				item.set("diys_1", 1);
			}else{
				item.set("diys_1", 0);
			}
			//手机膜
			Diy diy_2=Diy.getByBrandType(item.get("id").toString(), Diy.TYPE_SHOUJIMO);
			item.set("diy_2", diy_2);
			if(diy_2!=null){
				Goods goods_2=Goods.dao.findById(diy_2.get("goods_id"));
				item.set("goods_2", goods_2);
				item.set("diys_2", 1);
			}else{
				item.set("diys_2", 0);
			}
			//定制模板
			Diy diy=Diy.getByBrandType(item.get("id").toString(), brand.getInt("type") + 1);
			item.set("diy", diy);
			if(diy!=null){
				Goods goods=Goods.dao.findById(diy.get("goods_id"));
				item.set("goods", goods);
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
	public void add() throws Exception{
		
		if(StrKit.notBlank(getPara("id"))){
			setAttr("id", getPara("id"));
		}
		setAttr("type", getParaToInt("type"));
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
	public void save() throws Exception{
		
		String title=getPara("title");
		String idx=getPara("idx");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(idx)){
			setAttr("success", false);
			setAttr("msg", "排序不能为空");
			renderJson();
			return;
		}
		Brand brand=new Brand();
		if(StrKit.notBlank(getPara("id"))){
			brand.set("parent_id", getPara("id"));
		}
		brand.set("title", title)
				.set("idx", idx)
				.set("shop_id", getLoginShopId())
				.set("status", Brand.STATUS_ENABLE)
				.set("type", getParaToInt("type"))
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
	public void edit() throws Exception{
		
		Brand brand=Brand.dao.findById(getPara("id"));
		if(!brand.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
		setAttr("brand", brand);
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
		
		Brand brand=Brand.dao.findById(getPara("id"));
		if(!brand.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		String title=getPara("title");
		String idx=getPara("idx");
		if(StrKit.isBlank(title)){
			setAttr("success", false);
			setAttr("msg", "标题不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(idx)){
			setAttr("success", false);
			setAttr("msg", "排序不能为空");
			renderJson();
			return;
		}
		brand.set("title", title)
				.set("idx", idx)
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
	@Before(Tx.class)
	public void changeStatus() throws Exception{

		Brand brand=Brand.dao.findById(getPara("id"));
		if(!brand.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		brand.set("status", getPara("status"))
				.update();
		if(getParaToInt("status")==Brand.STATUS_DELETE){
			if(brand.get("parent_id")==null || StrKit.isBlank(brand.get("parent_id").toString())){
				Db.update("update db_diy set goods_id=null where brand_id in(select id from db_brand where parent_id=?)", brand.get("id"));
			}else{
				Db.update("update db_diy set goods_id=null where brand_id=?", brand.get("id"));
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
	public void json() throws Exception{
		
		setAttr("list", Brand.getAll(getPara("id"), getLoginShopId(), getPara("type")));
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
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	@Before(Tx.class)
	public void changeStatusAll() throws Exception{
		
		for (String bids : getPara("bids").toString().split(",")) {
			if (StrKit.notBlank(bids)) {
				Brand brand = Brand.dao.findById(bids);
				if(!brand.get("shop_id").toString().equals(getLoginShopId())){
					setAttr("success", false);
					setAttr("msg", "没有操作权限");
					renderJson();
					return;
				}
			}
		}
		for (String bids : getPara("bids").toString().split(",")) {
			if (StrKit.notBlank(bids)) {
				Brand brand = Brand.dao.findById(bids);
				brand.set("status", getPara("status"))
				     .update();
				if(getParaToInt("status")==Brand.STATUS_DELETE){
					if(brand.get("parent_id")==null || StrKit.isBlank(brand.get("parent_id").toString())){
						Db.update("update db_diy set goods_id=null where brand_id in(select id from db_brand where parent_id=?)", brand.get("id"));
					}else{
						Db.update("update db_diy set goods_id=null where brand_id=?", brand.get("id"));
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
	public void goodsAll() throws Exception{
		
		setAttr("bids", getPara("bids"));
		setAttr("type", getPara("type"));
		//商品列表
		List<Record> list=new ArrayList<Record>();
		if(getParaToInt("type")==1 || getParaToInt("type")==2){
			list=Db.find("select g.* from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.shop_id=? order by g.publish_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, getParaToInt("type"), Type.STATUS_ENABLE, getLoginShopId());
		}else{
			list=Db.find("select g.* from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.shop_id=? order by g.publish_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, getParaToInt("type") + 1, Type.STATUS_ENABLE, getLoginShopId());
		}
		setAttr("list", list);
		render("goodsAll.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void goodsAlls() throws Exception{
		
		String bids=",";
		for(String pid : getPara("bids").split(",")){
			if(StrKit.notBlank(pid)){
				List<Record> list=new ArrayList<Record>();
				if(getParaToInt("type")==1 || getParaToInt("type")==2){
					list=Brand.getList(pid, getLoginShopId(), Brand.TYPE_PINPAI);
				}else{
					list=Brand.getList(pid, getLoginShopId(), getParaToInt("type") - 1);
				}
				for(Record item : list){
					bids+=item.get("id") + ",";
				}
			}
		}
		System.out.println(bids);
		setAttr("bids", bids);
		setAttr("type", getPara("type"));
		//商品列表
		List<Record> list=new ArrayList<Record>();
		if(getParaToInt("type")==1 || getParaToInt("type")==2){
			list=Db.find("select g.* from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.shop_id=? order by g.publish_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, getParaToInt("type"), Type.STATUS_ENABLE, getLoginShopId());
		}else{
			list=Db.find("select g.* from db_goods g left join db_type t on g.type_id=t.id where g.status=? and g.publish=? and g.source=? and t.status=? and g.shop_id=? order by g.publish_date desc", Goods.STATUS_ENABLE, Goods.PUBLISH_YES, getParaToInt("type") + 1, Type.STATUS_ENABLE, getLoginShopId());
		}
		setAttr("list", list);
		render("goodsAll.htm");
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
	public void doGoodsAll() throws Exception{
		
		String bids=getPara("bids");
		String type=getPara("type");
		if(StrKit.isBlank(bids) || StrKit.isBlank(type)){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		Goods goods=Goods.dao.findById(getPara("goods_id"));
		if(!goods.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("success", false);
			setAttr("msg", "没有操作权限");
			renderJson();
			return;
		}
		for(String bid : bids.split(",")){
			if(StrKit.notBlank(bid)){
				Brand brand=Brand.dao.findById(bid);
				if(brand.get("shop_id").toString().equals(getLoginShopId())){
					Diy diy=Diy.getByBrandType(brand.get("id").toString(), type);
					if(diy!=null){
						diy.set("goods_id", goods.get("id"))
							.update();
					}
				}
			}
		}
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
