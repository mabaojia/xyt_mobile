package com.project.model;

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
@TableBind(tableName="db_orders")
public class Orders extends Model<Orders> {

	private static final long serialVersionUID = 1L;
	public static final Orders dao = new Orders();
	
	public final static int STATUS_NOT_PAY = 0;//待付款
	public final static int STATUS_WAIT_TRACKING = 1;//待发货
	public final static int STATUS_TRACKING = 2;//已发货
	public final static int STATUS_FINISH = 3;//已完成
	public final static int STATUS_RECEIVED = 8;//已收货
	
	public final static int TRACKING_STATUS_CUOWU = -1;//单号或快递公司代码错误
	public final static int TRACKING_STATUS_WUGUIJI = 0;//暂无轨迹
    public final static int TRACKING_STATUS_SHOUJIAN = 1;//快递收件
    public final static int TRACKING_STATUS_TUZHONG = 2;//在途中
    public final static int TRACKING_STATUS_QIANSHOU = 3;//签收
    public final static int TRACKING_STATUS_WENTIJIAN = 4;//问题件
	
	public final static int CLOSED_YES = 1;
	public final static int CLOSED_NO = 0;
	
	public final static int DISPLAY_NONE = 0;
	public final static int DISPLAY_YES = 1;
	
	public final static int USER_DISPLAY_NONE = 0;
	public final static int USER_DISPLAY_YES = 1;
	
	public final static int PAYMENT_WX = 1;//微信支付
	public final static int PAYMENT_XIANXIA = 2;//线下支付
	
	public final static int TRACKING_YOUJI = 1;//邮寄
	public final static int TRACKING_ZITI = 2;//自提
    
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Orders getByCode(String code) {

		return Orders.dao.findFirst("select * from db_orders where code=? or wx_code=?", code, code);
	}
}