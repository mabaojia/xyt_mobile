package com.project.model;

import java.util.ArrayList;
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
@TableBind(tableName="db_goods_comment")
public class GoodsComment extends Model<GoodsComment> {

	private static final long serialVersionUID = 1L;
	public static final GoodsComment dao = new GoodsComment();
	
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
	public static List<Record> getList(Object goods_id) {

		List<Record> list=Db.find("select gc.*,u.name user_name,u.img_url user_img_url from db_goods_comment gc left join db_user u on gc.user_id=u.id where gc.goods_id=? and gc.status=? order by gc.create_date desc", goods_id, GoodsComment.STATUS_ENABLE);
		for(Record comment : list){
			List<String> img_list=new ArrayList<String>();
			if(comment.get("img_urls")!=null && StrKit.notBlank(comment.get("img_urls").toString())){
				String [] img_urls=comment.get("img_urls").toString().split(",");
				for(String img_url : img_urls){
					if(StrKit.notBlank(img_url)){
						img_list.add(img_url);
					}
				}
			}
			comment.set("img_list", img_list);
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
	public static GoodsComment getByOrdersGoodsUser(Object orders_id, Object goods_id, String user_id) {

		return GoodsComment.dao.findFirst("select * from db_goods_comment where goods_id=? and orders_id=? and user_id=?", goods_id, orders_id, user_id);
	}
}