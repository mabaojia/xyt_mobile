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
@TableBind(tableName="db_notice")
public class Notice extends Model<Notice> {

	private static final long serialVersionUID = 1L;
	public static final Notice dao = new Notice();
	
	public final static int STATUS_DISABLE = 0;//禁用
    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除
    
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<Notice> getAll(Object shop_id) {

		return Notice.dao.find("select * from db_notice where status!=? and shop_id=? order by create_date desc", Notice.STATUS_DELETE, shop_id);
	}
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<Notice> getList(Object shop_id) {

		return Notice.dao.find("select * from db_notice where status=? and shop_id=? order by create_date desc", Notice.STATUS_ENABLE, shop_id);
	}
}