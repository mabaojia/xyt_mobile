package com.project.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@TableBind(tableName="db_shop")
public class Shop extends Model<Shop> {

	private static final long serialVersionUID = 1L;
	public static final Shop dao = new Shop();

	public final static int STATUS_DISABLE = 0;//禁用
    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除
    
    public final static int SHOP_DISABLE = 0;
    public final static int SHOP_ENABLE = 1;
    
    public final static int TRACKING_YOUJI = 1;//邮寄
	public final static int TRACKING_ZITI = 2;//自提
	public final static int TRACKING_ALL = 3;//邮寄+自提
	
	public final static int PAYMENT_WX = 1;//微信支付
	public final static int PAYMENT_XIANXIA = 2;//线下支付
	public final static int PAYMENT_WX_XIANXIA = 3;//微信支付+线下支付
	
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<Shop> getAll() {
		return Shop.dao.find("select * from db_shop where status!=?", Shop.STATUS_DELETE);
	}
}