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
@TableBind(tableName="db_admin")
public class Admin extends Model<Admin> {

	private static final long serialVersionUID = 1L;
	public static final Admin dao = new Admin();
	
	public final static int TYPE_1 = 1;//root
	public final static int TYPE_2 = 2;//后台添加
	
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
	public static Admin getByAccountMd5Pwd(String account, String password) {

		return Admin.dao.findFirst("select * from db_admin where account=? and password=? and status!=?", account, password, ShopAdmin.STATUS_DELETE);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Admin getByAccount(String account) {
		
		return Admin.dao.findFirst("select * from db_admin where account=? and status!=?", account, ShopAdmin.STATUS_DELETE);
	}
}