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
@TableBind(tableName="db_goods_format")
public class GoodsFormat extends Model<GoodsFormat> {

	private static final long serialVersionUID = 1L;
	public static final GoodsFormat dao = new GoodsFormat();

	public final static int STATUS_DISABLE = 0;//禁用
    public final static int STATUS_ENABLE = 1;//启用
    
    /**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<GoodsFormat> getAll(Object goods_id) {

		return GoodsFormat.dao.find("select * from db_goods_format where goods_id=?", goods_id);
	}
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<GoodsFormat> getList(Object goods_id) {
		
		return GoodsFormat.dao.find("select * from db_goods_format where goods_id=? and status=?", goods_id, GoodsFormat.STATUS_ENABLE);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static GoodsFormat getByTitleGoods(String title, Object goods_id) {

		return GoodsFormat.dao.findFirst("select * from db_goods_format where goods_id=? and title=?", goods_id, title);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static boolean enable(GoodsFormat goods_format) {
		
		if(goods_format==null){
			return false;
		}
		if(goods_format.getInt("status")==GoodsFormat.STATUS_DISABLE){
			return false;
		}
		return true;
	}
}