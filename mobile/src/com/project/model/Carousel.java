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
@TableBind(tableName="db_carousel")
public class Carousel extends Model<Carousel> {

	private static final long serialVersionUID = 1L;
	public static final Carousel dao = new Carousel();

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
	public static List<Record> getAll(Object shop_id) {
		
		return Db.find("select c.* from db_carousel c where c.status!=? and c.shop_id=? order by c.idx asc, c.create_date desc", Carousel.STATUS_DELETE, shop_id);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<Record> getList(Object shop_id) {
		
		return Db.find("select c.* from db_carousel c where c.status=? and c.shop_id=? order by c.idx asc, c.create_date desc", Carousel.STATUS_ENABLE, shop_id);
	}
}