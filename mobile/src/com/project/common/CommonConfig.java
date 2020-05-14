package com.project.common;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.jfinal.weixin.sdk.jfinal.MsgController;
import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
import com.project.aop.AdminInterceptor;
import com.project.aop.ExceptionInterceptor;
import com.project.aop.ShopInterceptor;
import com.project.aop.WapInterceptor;
import com.project.beetl.tag.HTMLTagSupportWrapper;
import com.project.beetl.tag.TemplteLayoutTag;
import com.project.function.CommonFunction;
import com.project.function.GetVarValue;
/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class CommonConfig extends JFinalConfig {
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void configConstant(Constants me) {
		
		me.setDevMode(true);
		loadPropertyFile("config.txt");
		me.setDevMode(getPropertyToBoolean("devMode", true));
		me.setMainRenderFactory(new BeetlRenderFactory());
		/*配置beetl信息*/
		GroupTemplate gt = BeetlRenderFactory.groupTemplate;
		gt.registerFunctionPackage("_common", new CommonFunction());
		gt.registerTag("htmltag", HTMLTagSupportWrapper.class);
		gt.registerFunction("_value", new GetVarValue());
		gt.registerTag("tlayout", TemplteLayoutTag.class);
		me.setMaxPostSize(999999999);
		me.setError401View("/wap/msg.htm");
		me.setError403View("/wap/msg.htm");
		me.setError404View("/wap/msg.htm");
		me.setError500View("/wap/msg.htm");
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
	public void configRoute(Routes me) {
		
		AutoBindRoutes routes = new AutoBindRoutes();
		routes.addExcludeClasses(BaseController.class);
		routes.addExcludeClasses(ApiController.class);
		routes.addExcludeClasses(MsgController.class);
		routes.addExcludeClasses(MsgControllerAdapter.class);
		me.add(routes);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void configPlugin(Plugins me) {
		
		loadPropertyFile("config.txt");
		DruidPlugin druidPlugin = new DruidPlugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
		me.add(druidPlugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin("xiaodaokeji", druidPlugin);
		arp.setDialect(new MysqlDialect());
		arp.setShowSql(true);
		me.add(arp);
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(druidPlugin);
		atbp.setShowSql(true);
		me.add(atbp);
		/*任务调度*/
		QuartzPlugin quartzPlugin=new QuartzPlugin();
		me.add(quartzPlugin);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void configInterceptor(Interceptors me){
		
		me.add(new AdminInterceptor());
		me.add(new ShopInterceptor());
		me.add(new WapInterceptor());
		me.add(new ExceptionInterceptor());
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void configHandler(Handlers me) {
		
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void main(String[] args) {
		
		JFinal.start("WebRoot", 80, "/", 5);
	}
}
