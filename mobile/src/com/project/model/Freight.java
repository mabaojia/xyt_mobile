package com.project.model;

import java.util.List;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@TableBind(tableName="db_freight")
public class Freight extends Model<Freight> {

	private static final long serialVersionUID = 1L;
	public static final Freight dao = new Freight();
	
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
	public static List<Freight> getAll(Object shop_id) {

		return Freight.dao.find("select f.*,a.name area_name from db_freight f left join db_area a on f.area_id=a.id where f.status!=? and f.shop_id=?", Freight.STATUS_DELETE, shop_id);
	}
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
	public static List<Freight> getList(Object shop_id) {

		return Freight.dao.find("select f.*,a.name area_name from db_freight f left join db_area a on f.area_id=a.id where f.status=? and f.shop_id=?", Freight.STATUS_ENABLE, shop_id);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Freight getByProvince(Object take_province, Object shop_id) {

		if(take_province!=null && StrKit.notBlank(take_province.toString())){
			Area area=Area.dao.findFirst("select * from db_area where name like ? and parent_id is null", take_province.toString().substring(0, 2) + "%");
			if(area!=null){
				Freight freight=Freight.dao.findFirst("select * from db_freight where area_id=? and status=? and shop_id=?", area.get("id"), Freight.STATUS_ENABLE, shop_id);
				if(freight!=null){
					return freight;
				}
			}
		}
		return Freight.dao.findFirst("select * from db_freight where area_id is null and shop_id=?", shop_id);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Freight getByAreaShop(String area_id, String shop_id) {
		
		return Freight.dao.findFirst("select * from db_freight where area_id=? and shop_id=? and status!=?", area_id, shop_id, Freight.STATUS_DELETE);
	}
}