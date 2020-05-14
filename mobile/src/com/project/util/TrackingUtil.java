package com.project.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;
import com.project.model.Orders;
import com.project.model.TrackingItem;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class TrackingUtil {

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void synTracking(Orders orders, String tracking_code, String tracking_kdn_code) throws Exception{
		
		String host = "https://wdexpress.market.alicloudapi.com";
		String path = "/gxali";
		String method = "GET";
		PropKit.use("config.txt");
		String appcode = PropKit.get("appCode").toString();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("n", tracking_code);
		querys.put("t", tracking_kdn_code);
		try {
			HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
			System.out.println(response.toString());
			String results=EntityUtils.toString(response.getEntity());
			System.out.println(results);
			JSONObject json=JSONObject.parseObject(results);
			System.out.println(json);
			String success=json.getString("Success");//-1：单号或快递公司代码错误, 0：暂无轨迹，1:快递收件，2：在途中,3：签收,4：问题件
			if("true".equals(success)){
				String state=json.getString("State");
				orders.set("tracking_status", Integer.parseInt(state))
						.set("tracking_update_date", new Date())
						.update();
				Db.update("delete from db_tracking_item where orders_id=?", orders.get("id"));
				JSONArray traces=(JSONArray) json.get("Traces");
				if(traces!=null && traces.size()!=0){
					for(Object object : traces){
						JSONObject item=JSONObject.parseObject(object.toString());
						TrackingItem tracking_item=new TrackingItem();
						tracking_item.set("orders_id", orders.get("id"))
										.set("content", item.get("AcceptStation"))
										.set("create_date", DateUtil.stringToDate(item.get("AcceptTime").toString(), "yyyy-MM-dd HH:mm:ss"))
										.save();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
