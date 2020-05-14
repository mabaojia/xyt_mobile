package com.project.model;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;
/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：176-6401-7800
 * 技术QQ：2511251392
 */
@TableBind(tableName="db_mask")
public class Mask extends Model<Mask>{

	/**
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：176-6401-7800
	 * 技术QQ：2511251392
	 */
	private static final long serialVersionUID = 1L;
	public static final Mask dao = new Mask();
	
	public final static int STATUS_DISABLE = 0;//禁用
    public final static int STATUS_ENABLE = 1;//启用
    public final static int STATUS_DELETE = 9;//删除 

}
