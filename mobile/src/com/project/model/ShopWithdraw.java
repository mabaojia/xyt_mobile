package com.project.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * 商家提现表
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@TableBind(tableName="db_shop_withdraw")
public class ShopWithdraw extends Model<ShopWithdraw> {

	private static final long serialVersionUID = 1L;
	public static final ShopWithdraw dao = new ShopWithdraw();
	
	public final static int STATUS_DISABLE = 0;//未通过
    public final static int STATUS_ENABLE = 1;//已提现
    public final static int STATUS_AUDIT = 2;//审核中
}