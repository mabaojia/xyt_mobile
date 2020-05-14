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
@TableBind(tableName="db_diy")
public class Diy extends Model<Diy> {

	private static final long serialVersionUID = 1L;
	public static final Diy dao = new Diy();

    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除
    
    public final static int TYPE_SHOUJIKE = 1;//手机壳
    public final static int TYPE_SHOUJIMO = 2;//手机膜
    public final static int TYPE_FUZHUANG = 3;//服装
    public final static int TYPE_DIAOKE = 4;//雕刻
    public final static int TYPE_LIPIN = 5;//礼品
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Diy getByCode(String code) {

		return Diy.dao.findFirst("select * from db_diy where code=? and status=?", code, Diy.STATUS_ENABLE);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Diy getByBrandType(String brand_id, Object type) {

		if(Integer.parseInt(type.toString())==Diy.TYPE_SHOUJIKE){
			return Diy.dao.findFirst("select * from db_diy where brand_id=? and type=? and status=? and user_id is null", brand_id, type, Diy.STATUS_ENABLE);
		}
		if(Integer.parseInt(type.toString())==Diy.TYPE_SHOUJIMO){
			return Diy.dao.findFirst("select * from db_diy where brand_id=? and type=? and status=? and user_id is null", brand_id, type, Diy.STATUS_ENABLE);
		}
		return Diy.dao.findFirst("select * from db_diy where brand_id=? and status=? and user_id is null", brand_id, Diy.STATUS_ENABLE);
	}
}