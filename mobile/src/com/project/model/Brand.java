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
@TableBind(tableName="db_brand")
public class Brand extends Model<Brand> {

	private static final long serialVersionUID = 1L;
	public static final Brand dao = new Brand();
	
	public final static int STATUS_DISABLE = 0;//禁用
    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除
    
    public final static int TYPE_PINPAI = 1;//品牌型号
    public final static int TYPE_FUZHUANG = 2;//服装管理
    public final static int TYPE_DIAOKE = 3;//激光雕刻
    public final static int TYPE_LIPIN = 4;//广告礼品
    
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static List<Record> getAll(Object parent_id, Object shop_id, Object type) throws Exception{
    	
    	if(parent_id==null || StrKit.isBlank(parent_id.toString())){
    		if(shop_id==null || StrKit.isBlank(shop_id.toString())){
    			return Db.find("select * from db_brand where shop_id is null and status!=? and parent_id is null and type=? order by idx asc", Brand.STATUS_DELETE, type);
    		}else{
    			return Db.find("select * from db_brand where shop_id=? and status!=? and parent_id is null and type=? order by idx asc", shop_id, Brand.STATUS_DELETE, type);
    		}
    	}else{
    		if(shop_id==null || StrKit.isBlank(shop_id.toString())){
    			return Db.find("select * from db_brand where shop_id is null and status!=? and parent_id=? and type=? order by idx asc", Brand.STATUS_DELETE, parent_id, type);
    		}else{
    			return Db.find("select * from db_brand where shop_id=? and status!=? and parent_id=? and type=? order by idx asc", shop_id, Brand.STATUS_DELETE, parent_id, type);
    		}
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
    public static List<Record> getList(Object parent_id, Object shop_id, Object type) throws Exception{
    	
    	if(parent_id==null || StrKit.isBlank(parent_id.toString())){
    		if(shop_id==null || StrKit.isBlank(shop_id.toString())){
    			return Db.find("select * from db_brand where shop_id is null and status=? and parent_id is null and type=? order by idx asc", Brand.STATUS_ENABLE, type);
    		}else{
    			return Db.find("select * from db_brand where shop_id=? and status=? and parent_id is null and type=? order by idx asc", shop_id, Brand.STATUS_ENABLE, type);
    		}
    	}else{
    		if(shop_id==null || StrKit.isBlank(shop_id.toString())){
    			return Db.find("select * from db_brand where shop_id is null and status=? and parent_id=? and type=? order by idx asc", Brand.STATUS_ENABLE, parent_id, type);
    		}else{
    			return Db.find("select * from db_brand where shop_id=? and status=? and parent_id=? and type=? order by idx asc", shop_id, Brand.STATUS_ENABLE, parent_id, type);
    		}
    	}
    }
}