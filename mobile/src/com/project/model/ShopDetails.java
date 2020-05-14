package com.project.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * 商家资金流水表
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@TableBind(tableName="db_shop_details")
public class ShopDetails extends Model<ShopDetails> {

	private static final long serialVersionUID = 1L;
	public static final ShopDetails dao = new ShopDetails();
	
	public final static int STATUS_AUDIT = 0;//待审核
    public final static int STATUS_ENABLE = 1;//已提现
    public final static int STATUS_DISABLE = 2;//待提现
	
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<ShopDetails> getByWithdraw(String shop_withdraw_id) {

		return ShopDetails.dao.find("select * from db_shop_details sd where sd.shop_withdraw_id=? order by sd.create_date desc", shop_withdraw_id);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<ShopDetails> getByOrders(Object orders_id) {
		
		return ShopDetails.dao.find("select * from db_shop_details sd where sd.orders_id=? order by sd.create_date desc", orders_id);
	}
}