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
@TableBind(tableName="db_shop_admin")
public class ShopAdmin extends Model<ShopAdmin> {

	private static final long serialVersionUID = 1L;
	public static final ShopAdmin dao = new ShopAdmin();
	
	public final static int TYPE_ROOT = 2;
	public final static int TYPE_ADMIN = 1;
	
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
	public static ShopAdmin getByEmailMd5Pwd(String email, String password) {

		return ShopAdmin.dao.findFirst("select * from db_shop_admin where email=? and password=? and status=?", email, password, ShopAdmin.STATUS_ENABLE);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static ShopAdmin getByEmail(String email) {

		return ShopAdmin.dao.findFirst("select * from db_shop_admin where email=? and status!=?", email, ShopAdmin.STATUS_DELETE);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<Record> getAll(Object shop_id) {

		return Db.find("select * from db_shop_admin where type=? and shop_id=? and status!=?", ShopAdmin.TYPE_ADMIN, shop_id, ShopAdmin.STATUS_DELETE);
	}
}