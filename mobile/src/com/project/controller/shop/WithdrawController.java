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
import com.project.model.KeyValue;
import com.project.model.Shop;
import com.project.model.ShopDetails;
import com.project.model.ShopWithdraw;
import com.project.util.CodeUtil;

/**
 * 提现管理
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/withdraw", viewPath = "/shop/withdraw")
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
		String sSelect="select sw.*";
		String sWhere=" from db_shop_withdraw sw where sw.shop_id=?";
		params.add(getLoginShopId());
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
		//可提现金额
		Object disable_money=ShopDetails.dao.findFirst("select ifnull(sum(account), 0) money from db_shop_details where shop_id=? and status=?", getLoginShopId(), ShopDetails.STATUS_DISABLE).get("money");
		//已提现金额
		Object enable_money=ShopDetails.dao.findFirst("select ifnull(sum(account), 0) money from db_shop_details where shop_id=? and status=?", getLoginShopId(), ShopDetails.STATUS_ENABLE).get("money");
		//审核中提现金额
		Object audit_money=ShopDetails.dao.findFirst("select ifnull(sum(account), 0) money from db_shop_details where shop_id=? and status=?", getLoginShopId(), ShopDetails.STATUS_AUDIT).get("money");
		//总提现金额
		Object all_money=ShopDetails.dao.findFirst("select ifnull(sum(account), 0) money from db_shop_details where shop_id=?", getLoginShopId()).get("money");
		setAttr("disable_money", CodeUtil.getNumber(Double.parseDouble(disable_money.toString())));
		setAttr("enable_money", CodeUtil.getNumber(Double.parseDouble(enable_money.toString())));
		setAttr("audit_money", CodeUtil.getNumber(Double.parseDouble(audit_money.toString())));
		setAttr("all_money", CodeUtil.getNumber(Double.parseDouble(all_money.toString())));
		//最低提现金额
		KeyValue withdraw_min=KeyValue.getByKey(KeyValue.SHOP_WITHDRAW);
		setAttr("withdraw_min", withdraw_min);
		//手续费
		KeyValue withdraw_fee=KeyValue.getByKey(KeyValue.SHOP_FEE);
		setAttr("withdraw_fee", withdraw_fee);
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
	public void details() throws Exception{
		
		ShopWithdraw shop_withdraw=ShopWithdraw.dao.findById(getPara("id"));
		if(!shop_withdraw.get("shop_id").toString().equals(getLoginShopId())){
			setAttr("msg", "没有操作权限");
			render("/shop/msg.htm");
			return;
		}
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
	@Before(Tx.class)
	public void doAudit() throws Exception{
		
		Shop shop=Shop.dao.findById(getLoginShopId());
		if(shop.get("bank_number")==null || StrKit.isBlank(shop.get("bank_number").toString())){
			setAttr("success", false);
			setAttr("msg", "请先完善财务账号信息");
			renderJson();
			return;
		}
		ShopWithdraw shop_withdraw=ShopWithdraw.dao.findFirst("select * from db_shop_withdraw where shop_id=? and status=?", getLoginShopId(), ShopWithdraw.STATUS_AUDIT);
		if(shop_withdraw!=null){
			setAttr("success", false);
			setAttr("msg", "有一笔提现正在审核中");
			renderJson();
			return;
		}
		Object disable_money=ShopDetails.dao.findFirst("select sum(account) money from db_shop_details where shop_id=? and status=?", getLoginShopId(), ShopDetails.STATUS_DISABLE).get("money");
		if(disable_money==null || Double.parseDouble(disable_money.toString())==0){
			setAttr("success", false);
			setAttr("msg", "没有可提现金额");
			renderJson();
			return;
		}
		KeyValue withdraw_min=KeyValue.getByKey(KeyValue.SHOP_WITHDRAW);
		if(Double.parseDouble(disable_money.toString())<Float.parseFloat(withdraw_min.get("attr_value").toString())){
			setAttr("success", false);
			setAttr("msg", "提现金额达到" + withdraw_min.get("attr_value") + "元才可提现");
			renderJson();
			return;
		}
		//手续费
		KeyValue withdraw_fee=KeyValue.getByKey(KeyValue.SHOP_FEE);
		shop_withdraw=new ShopWithdraw();
		shop_withdraw.set("shop_id", getLoginShopId())
						.set("account", CodeUtil.getNumber(Double.parseDouble(disable_money.toString())))
						.set("percent", withdraw_fee.get("attr_value"))
						.set("fee", CodeUtil.getNumber(Double.parseDouble(disable_money.toString()) * Float.parseFloat(withdraw_fee.get("attr_value").toString()) / 100.0))
						.set("amount", CodeUtil.getNumber(Double.parseDouble(disable_money.toString()) * (100.0 - Float.parseFloat(withdraw_fee.get("attr_value").toString())) / 100.0))
						.set("status", ShopWithdraw.STATUS_AUDIT)
						.set("bank_type", shop.get("bank_type"))
						.set("bank_name", shop.get("bank_name"))
						.set("bank_number", shop.get("bank_number"))
						.set("bank_address", shop.get("bank_address"))
						.set("bank_wx", shop.get("bank_wx"))
						.set("bank_wx_qrcode", shop.get("bank_wx_qrcode"))
						.set("create_date",new Date())
						.save();
		Db.update("update db_shop_details set status=?,shop_withdraw_id=? where shop_id=? and status=?", ShopDetails.STATUS_AUDIT, shop_withdraw.get("id"), getLoginShopId(), ShopDetails.STATUS_DISABLE);
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
	public void finance() throws Exception{
		
		Shop shop=Shop.dao.findById(getLoginShopId());
		setAttr("shop", shop);
		render("finance.htm");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void updateFinance() throws Exception{
		
		String bank_name=getPara("bank_name");
		String bank_number=getPara("bank_number");
		String bank_type=getPara("bank_type");
		String bank_address=getPara("bank_address");
		String bank_wx=getPara("bank_wx");
		String bank_wx_qrcode=getPara("bank_wx_qrcode");
		if(StrKit.isBlank(bank_name)){
			setAttr("success", false);
			setAttr("msg", "账户名不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(bank_number)){
			setAttr("success", false);
			setAttr("msg", "账号不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(bank_type)){
			setAttr("success", false);
			setAttr("msg", "开户行不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(bank_address)){
			setAttr("success", false);
			setAttr("msg", "开户支行不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(bank_wx)){
			setAttr("success", false);
			setAttr("msg", "微信号不能为空");
			renderJson();
			return;
		}
		if(StrKit.isBlank(bank_wx_qrcode)){
			setAttr("success", false);
			setAttr("msg", "微信收款码不能为空");
			renderJson();
			return;
		}
		Shop shop=Shop.dao.findById(getLoginShopId());
		shop.set("bank_name", bank_name)
			.set("bank_number", bank_number)
			.set("bank_type", bank_type)
			.set("bank_address", bank_address)
			.set("bank_wx", bank_wx)
			.set("bank_wx_qrcode", bank_wx_qrcode)
			.update();
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}
