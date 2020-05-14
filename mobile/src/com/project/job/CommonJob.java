package com.project.job;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.project.model.Orders;
import com.project.model.OrdersLog;
import com.project.model.ShopDetails;
import com.project.util.DateUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class CommonJob implements Job {
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		try {
			List<Orders> list=Orders.dao.find("select * from db_orders where status=? order by create_date asc", Orders.STATUS_TRACKING);
			for(Orders orders : list){
				if(DateUtil.dayNum(orders.getDate("tracking_date"), new Date()) > 6){
					OrdersLog.save(orders.get("id"), "订单超时自动确认收货", new Date());
					orders.set("status", Orders.STATUS_RECEIVED)
							.set("received_date", new Date())
							.update();
					//商家流水
					if(orders.getInt("payment")==Orders.PAYMENT_WX){
						ShopDetails shop_details=new ShopDetails();
						shop_details.set("shop_id", orders.get("shop_id"))
									.set("orders_id", orders.get("id"))
									.set("account", orders.getFloat("grand_total"))
									.set("content", orders.get("code") + "订单入账")
									.set("status", ShopDetails.STATUS_DISABLE)
									.set("create_date", new Date())
									.save();
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			List<Orders> list=Orders.dao.find("select * from db_orders where status=? order by create_date asc", Orders.STATUS_RECEIVED);
			for(Orders orders : list){
				if(DateUtil.dayNum(orders.getDate("received_date"), new Date()) > 7){
					OrdersLog.save(orders.get("id"), "订单超时自动确认完成", new Date());
					orders.set("status", Orders.STATUS_FINISH)
							.set("finished_date", new Date())
							.update();
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
