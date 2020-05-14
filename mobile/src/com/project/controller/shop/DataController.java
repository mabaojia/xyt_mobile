package com.project.controller.shop;

import java.io.File;
import java.util.List;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.project.common.BaseController;
import com.project.model.Background;
import com.project.model.Carousel;
import com.project.model.Diy;
import com.project.model.Goods;
import com.project.model.Mask;
import com.project.model.Material;
import com.project.model.OrdersItem;
import com.project.model.OrdersItemPicture;
import com.project.model.Type;
import com.project.model.UserPicture;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/data", viewPath = "/shop/data")
public class DataController extends BaseController{
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		PropKit.use("config.txt");
		List<Background> background_list=Background.dao.find("select * from db_background");
		for(Background item : background_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Carousel> carousel_list=Carousel.dao.find("select * from db_carousel");
		for(Carousel item : carousel_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Diy> diy_list=Diy.dao.find("select * from db_diy");
		for(Diy item : diy_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
				if(item.get("big_img_url")!=null &&StrKit.notBlank(item.get("big_img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("big_img_url").toString().split("/")[item.get("big_img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("big_img_url")));
					ossClient.shutdown();
					item.set("big_img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Goods> goods_list=Goods.dao.find("select * from db_goods");
		for(Goods item : goods_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
				if(item.get("img_urls")!=null &&StrKit.notBlank(item.get("img_urls").toString())){
					String img_urls=",";
					for(String img_url : item.get("img_urls").toString().split(",")){
						String endpoint = PropKit.get("ossEndpoint").toString();
						String accessKeyId = PropKit.get("ossAccessKeyId").toString();
						String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
						OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
						String name=img_url.split("/")[img_url.split("/").length - 1];
						System.out.println(name);
						ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + img_url));
						ossClient.shutdown();
						img_urls+=PropKit.get("ossDomain").toString() + "/" + name + ",";
					}
					item.set("img_urls", img_urls)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Mask> mask_list=Mask.dao.find("select * from db_mask");
		for(Mask item : mask_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Material> material_list=Material.dao.find("select * from db_material");
		for(Material item : material_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<OrdersItem> orders_item_list=OrdersItem.dao.find("select * from db_orders_item");
		for(OrdersItem item : orders_item_list){
			try {
				if(item.get("goods_img_url")!=null &&StrKit.notBlank(item.get("goods_img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("goods_img_url").toString().split("/")[item.get("goods_img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("goods_img_url")));
					ossClient.shutdown();
					item.set("goods_img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<OrdersItemPicture> orders_item_picture_list=OrdersItemPicture.dao.find("select * from db_orders_item_picture");
		for(OrdersItemPicture item : orders_item_picture_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Type> type_list=Type.dao.find("select * from db_type");
		for(Type item : type_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
				if(item.get("top_img_url")!=null &&StrKit.notBlank(item.get("top_img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("top_img_url").toString().split("/")[item.get("top_img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("top_img_url")));
					ossClient.shutdown();
					item.set("top_img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<UserPicture> user_picture_list=UserPicture.dao.find("select * from db_user_picture");
		for(UserPicture item : user_picture_list){
			try {
				if(item.get("img_url")!=null &&StrKit.notBlank(item.get("img_url").toString())){
					String endpoint = PropKit.get("ossEndpoint").toString();
					String accessKeyId = PropKit.get("ossAccessKeyId").toString();
					String accessKeySecret = PropKit.get("ossAccessKeySecret").toString();
					OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
					String name=item.get("img_url").toString().split("/")[item.get("img_url").toString().split("/").length - 1];
					System.out.println(name);
					ossClient.putObject(PropKit.get("ossBucket").toString(), name, new File(getRequest().getSession().getServletContext().getRealPath("/") + item.get("img_url")));
					ossClient.shutdown();
					item.set("img_url", PropKit.get("ossDomain").toString() + "/" + name)
						.update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
