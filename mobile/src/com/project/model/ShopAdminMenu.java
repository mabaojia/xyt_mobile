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
@TableBind(tableName="db_shop_admin_menu")
public class ShopAdminMenu extends Model<ShopAdminMenu> {

	private static final long serialVersionUID = 1L;
	public static final ShopAdminMenu dao = new ShopAdminMenu();
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<ShopAdminMenu> getMenus(Object shop_admin_id) {

		return ShopAdminMenu.dao.find("select sm.* from db_shop_admin_menu sam left join db_shop_menu sm on sam.shop_menu_id=sm.id where sam.shop_admin_id=? order by sm.idx asc", shop_admin_id);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static ShopAdminMenu getByAdminMenu(Object shop_admin_id, Object shop_menu_id) {

		return ShopAdminMenu.dao.findFirst("select * from db_shop_admin_menu where shop_admin_id=? and shop_menu_id=?", shop_admin_id, shop_menu_id);
	}
}