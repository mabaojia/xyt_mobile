package com.project.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.project.model.Orders;
import com.project.util.TrackingUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class TrackingJob implements Job {
	
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
			List<Orders> list=Orders.dao.find("select * from db_orders where status=? and tracking=? and (tracking_status=? or tracking_status=? or tracking_status=?) order by create_date desc",
					Orders.STATUS_TRACKING, Orders.TRACKING_YOUJI, Orders.TRACKING_STATUS_SHOUJIAN, Orders.TRACKING_STATUS_TUZHONG, Orders.TRACKING_STATUS_WUGUIJI);
			for(Orders orders : list){
				TrackingUtil.synTracking(orders, orders.get("tracking_code").toString(), orders.get("tracking_kdn_code").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
