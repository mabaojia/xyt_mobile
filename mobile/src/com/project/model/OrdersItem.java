package com.project.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@TableBind(tableName="db_orders_item")
public class OrdersItem extends Model<OrdersItem> {

	private static final long serialVersionUID = 1L;
	public static final OrdersItem dao = new OrdersItem();
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<Record> getByOrders(Object orders_id) {

		return Db.find("select oi.*,g.code goods_code,d.code diy_code from db_orders_item oi left join db_goods g on oi.goods_id=g.id left join db_diy d on oi.diy_id=d.id where oi.orders_id=?", orders_id);
	}
}