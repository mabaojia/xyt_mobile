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
@TableBind(tableName="db_key_value")
public class KeyValue extends Model<KeyValue> {

	private static final long serialVersionUID = 1L;
	public static final KeyValue dao = new KeyValue();
	
	public final static String SHOP_WITHDRAW = "shop_withdraw";
	public final static String SHOP_FEE = "shop_fee";
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static KeyValue getByKey(String attr_key) {

		return KeyValue.dao.findFirst("select * from db_key_value where attr_key=?", attr_key);
	}
}