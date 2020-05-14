package com.project.controller.shop.api;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.project.common.BaseController;
import com.project.model.Background;
import com.project.model.Brand;
import com.project.model.Material;
import com.project.model.Type;
import com.project.model.UploadImageUpyun;
import com.project.util.DateUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/shop/api")
public class CommonController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void header() throws Exception{
		
		getResponse().addHeader("Access-Control-Allow-Origin", "*");
    	getResponse().addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
    	getResponse().addHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
    	getResponse().addHeader("X-Powered-By", "3.2.1");
    	getResponse().addHeader("Content-Type", "application/json;charset=utf-8");
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void uploadImg() throws Exception{
		
		header();
		String save_path="/static/image/" + DateUtil.formatDate(new Date(), "yyyyMMdd")+"/";
		File file=new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path);  
		if(!file.exists()){  
		    file.mkdirs();  
		}  
		UploadFile uploadFile=getFile("image", getRequest().getSession().getServletContext().getRealPath("/") + save_path);
		String type=uploadFile.getContentType().toLowerCase();
	    File rename_file=uploadFile.getFile();
	    String new_name=UUID.randomUUID().toString().replace("-", "") + "." + type.replace("image/", "").replace("+xml", "");
	    rename_file.renameTo(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path + new_name));
	    if(!type.contains("svg")){
	    	ImageInfo imageInfo=Imaging.getImageInfo(new File(getRequest().getSession().getServletContext().getRealPath("/") + save_path + new_name));
	 		System.out.println(imageInfo);
	 		System.out.println("该图片dpi为：" + imageInfo.getPhysicalHeightDpi());
	 		setAttr("dpi", imageInfo.getPhysicalHeightDpi());
	    }
	    String path = save_path + new_name;
	    
	    try {
        	String fwqImgagepath = getRequest().getSession().getServletContext().getRealPath("/") + path;
        	String imagePath = UploadImageUpyun.testWriteFile(fwqImgagepath, type.replace("image/", ""));
        	rename_file.delete();
	        if(imagePath.equals("")){
	        	setAttr("success", false);
				setAttr("msg", "请选择正确图片格式");
				renderJson();
				return;
	        }else{
	        	setAttr("success", true);
				setAttr("img_url", imagePath);
				renderJson();
				return;
	        }
		} catch (Exception e) {	
			setAttr("success", false);
			setAttr("msg", "请选择正确图片格式");
			renderJson();
			return;
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
	public void search() throws Exception{
		
		header();
		//贴图列表
		List<Record> material_list=Db.find("select m.*,t.title type_title from db_material m left join db_type t on m.type_id=t.id where m.status=? and m.title like ? and t.status=? and m.shop_id=? order by m.create_date desc", Material.STATUS_ENABLE,  "%" + getPara("content") + "%", Type.STATUS_ENABLE, getLoginShopId());
		setAttr("material_list", material_list);
		//图库列表
		List<Record> background_list=Db.find("select b.*,t.title type_title from db_background b left join db_type t on b.type_id=t.id where b.status=? and b.title like ? and t.status=? and b.shop_id=? order by b.create_date desc", Background.STATUS_ENABLE, "%" + getPara("content") + "%", Type.STATUS_ENABLE, getLoginShopId());
		setAttr("background_list", background_list);
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void brand() throws Exception{
		
		header();
		//品牌型号
		Brand brand=Brand.dao.findById(getPara("id"));
		Brand parent=Brand.dao.findById(brand.get("parent_id"));
		setAttr("brand", brand);
		setAttr("parent", parent);
		setAttr("success", true);
		setAttr("msg", "操作成功");
		renderJson();
		return;
	}
}