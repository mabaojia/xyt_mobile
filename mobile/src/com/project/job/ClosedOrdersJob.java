package com.project.job;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.project.model.Orders;
import com.project.model.OrdersLog;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class ClosedOrdersJob implements Job {
	
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
			List<Orders> list=Orders.dao.find("select * from db_orders where status=? and closed=? order by create_date desc", Orders.STATUS_NOT_PAY, Orders.CLOSED_NO);
			for(Orders item : list){
				if((new Date().getTime() - item.getDate("create_date").getTime()) / 1000 / 60 / 60 > 24 * 3){
					OrdersLog.save(item.get("id"), "订单超时待付款系统自动关闭", new Date());
					item.set("closed", Orders.CLOSED_YES)
						.update();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
