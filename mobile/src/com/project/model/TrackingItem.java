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
@TableBind(tableName = "db_tracking_item")
public class TrackingItem extends Model<TrackingItem> {

	private static final long serialVersionUID = 1L;
	public static final TrackingItem dao = new TrackingItem();
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<TrackingItem> getList(String orders_id){
	
		return TrackingItem.dao.find("select * from db_tracking_item where orders_id=? order by create_date desc", orders_id);
	}
}
