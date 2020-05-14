package com.project.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
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
@TableBind(tableName="db_type")
public class Type extends Model<Type> {

	private static final long serialVersionUID = 1L;
	public static final Type dao = new Type();
	
	public final static int STATUS_DISABLE = 0;//禁用
    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除

    public final static int TYPE_MATERIAL = 1;
    public final static int TYPE_BACKGROUND = 2;
    public final static int TYPE_GOODS = 3;
    public final static int TYPE_DIY = 4;
    public final static int TYPE_MASK = 5;
    
    public final static int TOP_NO = 0;
    public final static int TOP_YES = 1;
   
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<Record> getAll(Object type, Object shop_id) {

		if(shop_id!=null && StrKit.notBlank(shop_id.toString())){
			return Db.find("select * from db_type where status!=? and type=? and shop_id=? order by idx asc", Type.STATUS_DELETE, type, shop_id);
		}else{
			return Db.find("select * from db_type where status!=? and type=? and shop_id is null order by idx asc", Type.STATUS_DELETE, type);
		}
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<Record> getList(Object type, Object shop_id) {
		
		if(shop_id!=null && StrKit.notBlank(shop_id.toString())){
			return Db.find("select * from db_type where status=? and type=? and shop_id=? order by idx asc", Type.STATUS_ENABLE, type, shop_id);
		}else{
			return Db.find("select * from db_type where status=? and type=? and shop_id is null order by idx asc", Type.STATUS_ENABLE, type);
		}
	}
}