package com.project.common;

import java.util.Date;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.project.model.Goods;
import com.project.model.GoodsFormat;
import com.project.model.Orders;
import com.project.model.OrdersItem;
import com.project.model.OrdersLog;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class SynchronizedUtil {

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	synchronized
	@Before(Tx.class)
	public static void wxPaymentCallbackSynchronized(Orders orders) throws Exception{
		
		orders=Orders.dao.findById(orders.get("id"));
		if(orders.getInt("status")==Orders.STATUS_NOT_PAY){
			orders.set("status", Orders.STATUS_WAIT_TRACKING)
					.set("payment", Orders.PAYMENT_WX)
					.set("payment_date", new Date())
					.update();
			List<Record> item_list=OrdersItem.getByOrders(orders.get("id"));
			for(Record item : item_list){
				Goods goods=Goods.dao.findById(item.get("goods_id"));
				goods.set("sale_number", goods.getInt("sale_number") + item.getInt("item_number"))
						.update();
				GoodsFormat goods_format=GoodsFormat.getByTitleGoods(item.get("goods_format_title").toString(), goods.get("id"));
				if(goods_format!=null){
					goods_format.set("stock", goods_format.getInt("stock") - item.getInt("item_number"))
								.set("sale_number", goods_format.getInt("sale_number") + item.getInt("item_number"))
								.update();
				}
			}
			OrdersLog.save(orders.get("id"), "会员微信支付订单", new Date());
		}
	}
}
