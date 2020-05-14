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
@TableBind(tableName="db_area")
public class Area extends Model<Area> {

	private static final long serialVersionUID = 1L;
	public static final Area dao = new Area();
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<Area> getByParent(Object parent_id) {

		if(parent_id==null){
			return Area.dao.find("select * from db_area where parent_id is null");
		}
		return Area.dao.find("select * from db_area where parent_id=?", parent_id);
	}
}