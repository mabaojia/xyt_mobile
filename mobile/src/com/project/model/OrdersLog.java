package com.project.model;

import java.util.Date;
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
@TableBind(tableName="db_orders_log")
public class OrdersLog extends Model<OrdersLog> {

	private static final long serialVersionUID = 1L;
	public static final OrdersLog dao = new OrdersLog();
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void save(Object orders_id, String content, Date create_date) {
		
		OrdersLog orders_log=new OrdersLog();
		orders_log.set("orders_id", orders_id)
					.set("content", content)
					.set("create_date", create_date)
					.save();
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<OrdersLog> getList(Object orders_id) {

		return OrdersLog.dao.find("select * from db_orders_log where orders_id=? order by create_date desc", orders_id);
	}
}