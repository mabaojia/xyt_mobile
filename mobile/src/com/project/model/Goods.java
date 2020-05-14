package com.project.model;

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
@TableBind(tableName="db_goods")
public class Goods extends Model<Goods> {

	private static final long serialVersionUID = 1L;
	public static final Goods dao = new Goods();
	
    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除
    
	public final static int PUBLISH_NO = 0;//未上架
	public final static int PUBLISH_YES = 1;//已上架
	
	public final static int TOP_NO = 0;
	public final static int TOP_YES = 1;
	
	public final static int SOURCE_SHOUJIKE = 1;
	public final static int SOURCE_SHOUJIMO = 2;
	public final static int SOURCE_ZHAOPIAN = 3;
	public final static int SOURCE_FUZHUANG = 4;
	public final static int SOURCE_DIAOKE = 5;
	public final static int SOURCE_LIPIN = 6;
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Goods getByCode(String code) {

		return Goods.dao.findFirst("select * from db_goods where code=?", code);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static boolean enable(Goods goods) {

		if(goods.getInt("status")==Goods.STATUS_DELETE){
			return false;
		}
		if(goods.getInt("publish")==Goods.PUBLISH_NO){
			return false;
		}
		Type type=Type.dao.findById(goods.get("type_id"));
		if(type.getInt("status")!=Type.STATUS_ENABLE){
			return false;
		}
		return true;
	}
}