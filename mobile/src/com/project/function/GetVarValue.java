package com.project.function;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.jfinal.kit.StrKit;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class GetVarValue implements Function {
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@Override
	public Object call(Object[] paras, Context ctx) {
		
		 if (StrKit.isBlank(paras[0].toString())){
			return null;
		}
		String para = paras[0].toString();
		return ctx.getGlobal(para);
	}
}
