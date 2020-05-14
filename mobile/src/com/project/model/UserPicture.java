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
@TableBind(tableName="db_user_picture")
public class UserPicture extends Model<UserPicture> {

	private static final long serialVersionUID = 1L;
	public static final UserPicture dao = new UserPicture();
	
	public final static int USE_YES = 1;
    public final static int USE_NO = 0;
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static List<UserPicture> getByUser(String user_id) {

		return UserPicture.dao.find("select * from db_user_picture where user_id=? order by create_date desc", user_id);
	}
}