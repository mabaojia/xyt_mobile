package com.project.controller.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.coobird.thumbnailator.Thumbnails;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.project.common.BaseController;
import com.project.model.Diy;
import com.project.util.CodeUtil;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/admin/data", viewPath = "/admin/data")
public class DataController extends BaseController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void picture() throws Exception{
	
		PropKit.use("config.txt");
		try {
			List<String> list=new ArrayList<String>();
			searchFiles(getRequest().getSession().getServletContext().getRealPath("/") + "/static/", list);
			for(String img_url : list){
				img_url="/static" + img_url.split("static")[img_url.split("static").length - 1].replace("\\", "/");
				System.out.println(img_url);
				String path=img_url;
	        	//图片压缩至宽1000.0
			    File ys_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + path);
			    int[] with_height=getImgWidth(ys_file);
				System.out.println(with_height[0]);
				if(with_height[1] > 1000.0){
					Thumbnails.of(getRequest().getSession().getServletContext().getRealPath("/") + path).scale(CodeUtil.getNumber(1000.0 / with_height[0])).toFile(getRequest().getSession().getServletContext().getRealPath("/") + path);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
	public void searchFiles(String path, List<String> list) {

		File file = new File(path);        
		if (file.exists()) {
			File[] files = file.listFiles();            
			if (null == files || files.length == 0) {
				return;
			} else {         
				for (File file2 : files) {                    
					if (file2.isDirectory()) {
						searchFiles(file2.getAbsolutePath(), list);
					} else {
						list.add(file2.getAbsolutePath());
					}
				}
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
	public void diy() throws Exception{
		 
		List<Diy> list=Diy.dao.find("select * from db_diy");
		for(Diy diy : list){
			try {
				JSONObject content=JSONObject.parseObject(diy.get("content").toString());
				JSONArray objects=JSONArray.parseArray(content.get("objects").toString());
				JSONArray objects_new=new JSONArray();
				for(Object object : objects){
					String each=object.toString();
					JSONObject item=JSONObject.parseObject(object.toString());
					if("camera".equals(item.get("mediaType").toString())){
						String scaleX=item.get("scaleX").toString();
						String scaleY=item.get("scaleY").toString();
						String width=item.get("width").toString();
						String height=item.get("height").toString();
						String src=item.get("src").toString();
						System.out.println(scaleX);
						System.out.println(scaleY);
						System.out.println(width);
						System.out.println(height);
						System.out.println(src);
						File ys_file=new File(getRequest().getSession().getServletContext().getRealPath("/") + src);
					    int[] with_height=getImgWidth(ys_file);
						System.out.println(with_height[0]);
						each=each.replace("\"width\":" + width, "\"width\":" + with_height[0])
									.replace("\"height\":" + height, "\"height\":" + with_height[0] * Float.parseFloat(height) / Float.parseFloat(width))
									.replace("\"scaleX\":" + scaleX, "\"scaleX\":" + Float.parseFloat(width) * Double.parseDouble(scaleX) / with_height[0])
									.replace("\"scaleY\":" + scaleY, "\"scaleY\":" + Float.parseFloat(height) * Double.parseDouble(scaleY) / (with_height[0] * Float.parseFloat(height) / Float.parseFloat(width)));
					}
					objects_new.add(JSONObject.parseObject(each));
				}
				content.put("objects", objects_new);
				System.out.println(content.toJSONString());
				diy.set("content", content.toJSONString())
					.update();
			} catch (Exception e) {
				e.printStackTrace();
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
	public static int[] getImgWidth(File file) { 
		
        InputStream is = null;  
        BufferedImage src = null;  
        int result[] = { 0, 0 };  
        try {  
            is = new FileInputStream(file);  
            src = javax.imageio.ImageIO.read(is);  
            result[0] = src.getWidth(null);//得到源图宽  
            result[1] = src.getHeight(null);//得到源图高  
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }
}