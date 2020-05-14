package com.project.controller.admin;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.common.BaseController;
import com.project.model.KeyValue;
import com.project.model.ShopDetails;
import com.project.model.ShopWithdraw;

/**
 * 商家提现
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/withdraw", viewPath = "/admin/withdraw")
public class WithdrawController extends BaseController{
	
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
		String sSelect="select sw.*,s.title shop_title";
		String sWhere=" from db_shop_withdraw sw left join db_shop s on sw.shop_id=s.id where 1=1";
		if(StrKit.notBlank(getPara("content"))){
			sWhere+=" and s.title like ?";
			params.add("%" + getPara("content")+ "%");
			setAttr("content", getPara("content"));
		}
		if(StrKit.notBlank(getPara("status"))){
			sWhere+=" and sw.status=?";
			params.add(getPara("status"));
			setAttr("status", getParaToInt("status"));
		}
		sWhere+=" order by sw.create_date desc";
		Page<Record> results=Db.paginate(page, 20, sSelect, sWhere, params.toArray());
		for(Record item : results.getList()){
			List<String> img_list=new ArrayList<String>();
			if(item.get("img_urls")!=null && StrKit.notBlank(item.get("img_urls").toString())){
				String [] img_urls=item.get("img_urls").toString().split(",");
				for(String img_url : img_urls){
					if(StrKit.notBlank(img_url)){
						img_list.add(img_url);
					}
				}
			}
			item.set("img_list", img_list);
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
	@Before(Tx.class)
	public void changeStatus() throws Exception{
		
		ShopWithdraw shop_withdraw=ShopWithdraw.dao.findById(getPara("id"));
		shop_withdraw.set("status", getPara("status"))
						.update();
		if(getParaToInt("status")==ShopWithdraw.STATUS_DISABLE){
			Db.update("update db_shop_details set status=? where shop_withdraw_id=?", ShopDetails.STATUS_DISABLE, shop_withdraw.get("id"));
		}else if(getParaToInt("status")==ShopWithdraw.STATUS_ENABLE){
			Db.update("update db_shop_details set status=? where shop_withdraw_id=?", ShopDetails.STATUS_ENABLE, shop_withdraw.get("id"));
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
	public void details() throws Exception{
		
		List<ShopDetails> list=ShopDetails.getByWithdraw(getPara("id"));
		setAttr("list", list);
		render("details.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void remark() throws Exception{
		
		ShopWithdraw shop_withdraw=ShopWithdraw.dao.findById(getPara("id"));
		setAttr("shop_withdraw", shop_withdraw);
		render("remark.htm");
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
	public void doRemark() throws Exception{
		
		ShopWithdraw shop_withdraw=ShopWithdraw.dao.findById(getPara("id"));
		shop_withdraw.set("status", getPara("status"))
						.set("remark", getPara("remark"))
						.set("img_urls", getPara("img_urls"))
						.update();
		if(getParaToInt("status")==ShopWithdraw.STATUS_DISABLE){
			Db.update("update db_shop_details set status=? where shop_withdraw_id=?", ShopDetails.STATUS_DISABLE, shop_withdraw.get("id"));
		}else if(getParaToInt("status")==ShopWithdraw.STATUS_ENABLE){
			Db.update("update db_shop_details set status=? where shop_withdraw_id=?", ShopDetails.STATUS_ENABLE, shop_withdraw.get("id"));
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
	public void min() throws Exception{
		
		KeyValue shop_withdraw=KeyValue.getByKey(KeyValue.SHOP_WITHDRAW);
		setAttr("shop_withdraw", shop_withdraw);
		render("min.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void doMin() throws Exception{
		
		String account=getPara("account");
		if(StrKit.isBlank(account)){
			setAttr("success", false);
			setAttr("msg", "最低提现金额不能为空");
			renderJson();
			return;
		}
		KeyValue shop_withdraw=KeyValue.getByKey(KeyValue.SHOP_WITHDRAW);
		shop_withdraw.set("attr_value", account)
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
	public void fee() throws Exception{
		
		KeyValue shop_fee=KeyValue.getByKey(KeyValue.SHOP_FEE);
		setAttr("shop_fee", shop_fee);
		render("fee.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void doFee() throws Exception{
		
		String account=getPara("account");
		if(StrKit.isBlank(account)){
			setAttr("success", false);
			setAttr("msg", "手续费不能为空");
			renderJson();
			return;
		}
		KeyValue shop_fee=KeyValue.getByKey(KeyValue.SHOP_FEE);
		shop_fee.set("attr_value", account)
				.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}