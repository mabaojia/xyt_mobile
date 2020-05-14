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
@TableBind(tableName="db_shopping_cart")
public class ShoppingCart extends Model<ShoppingCart> {

	private static final long serialVersionUID = 1L;
	public static final ShoppingCart dao = new ShoppingCart();

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static ShoppingCart getByUserGoods(String user_id, Object goods_id, Object diy_id, Object title) {

		if(diy_id==null || StrKit.isBlank(diy_id.toString())){
			return ShoppingCart.dao.findFirst("select sc.* from db_shopping_cart sc where sc.user_id=? and sc.goods_id=? and sc.title=?", user_id, goods_id, title);
		}else{
			return ShoppingCart.dao.findFirst("select sc.* from db_shopping_cart sc where sc.user_id=? and sc.goods_id=? and sc.diy_id=? and sc.title=?", user_id, goods_id, diy_id, title);
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
	public static List<Record> getByUser(String user_id) {

		User user=User.dao.findById(user_id);
		List<Record> list=Db.find("select sc.* from db_shopping_cart sc left join db_goods g on sc.goods_id=g.id where sc.user_id=? and g.shop_id=?", user_id, user.get("shop_id"));
		for(Record item : list){
			if(item.get("diy_id")!=null && StrKit.notBlank(item.get("diy_id").toString())){
				item.set("diy", Diy.dao.findById(item.get("diy_id")));
			}
		}
		return list;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static long getNumberByUser(String user_id) {

		User user=User.dao.findById(user_id);
		return ShoppingCart.dao.findFirst("select count(sc.id) number from db_shopping_cart sc left join db_goods g on sc.goods_id=g.id where sc.user_id=? and g.shop_id=?", user_id, user.get("shop_id")).getLong("number");
	}
}