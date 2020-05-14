package com.project.beetl.tag;

import java.util.Map;
import java.util.Map.Entry;
import org.beetl.core.ByteWriter;
import org.beetl.core.Tag;
import org.beetl.core.Template;
import org.beetl.core.misc.BeetlUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class TemplteLayoutTag extends Tag {
	
	ByteWriter tempWriter = null;
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public boolean requriedInput() {
		
		return true;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@SuppressWarnings("unchecked")
	public void render() {
		
		if (args.length==0||args.length > 2) {
			throw new RuntimeException("参数错误");
		}
		String tpl_dir = "/";
		String child = tpl_dir + args[0];
		if (BeetlUtil.isOutsideOfRoot(child)) {
			throw new RuntimeException("layout 文件非法,不在根目录里:" + child);
		}
		Template t = gt.getTemplate(child);
		t.binding(ctx.globalVar);
		String varName = "layoutContent";
		t.binding(varName, getBodyContent());
		if (args.length == 2) {
			Map<String, Object> map = (Map<String, Object>) args[1];
			for (Entry<String, Object> entry : map.entrySet()) {
				t.binding(entry.getKey(), entry.getValue());
			}
		}
		t.renderTo(ctx.byteWriter);
	}
}
