package com.pcis.smartbus.project.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.utils.RedisUtil;

/*
 * author:王琪善
 * data：2019-7-16
 * info:2.5D监测图片上传下载，数据点上传下载
 */
@Controller
public class ProjectPicController {
	private final String point_3d = "3DPoints:";
	
	@Autowired
	RedisUtil redisUtil;
	
	/*
	 * 图片上传，前端应传:项目id+区域+图片
	 * 1.创建目录
	 * 2.保存文件
	 * 3.设置redis
	 * 	3.1新建pic:proid表
	 * 	3.2新建3DPoints:proid:area表
	 */
	@PostMapping(path = "/project/uploadpic")
	@ResponseBody
	public String UploadPic(@RequestParam("img") MultipartFile img,@RequestParam("addr") String addr,
			@RequestParam("proid") String proid) throws IllegalStateException, IOException{
		if(!redisUtil.Has("project:"+proid))
			return "此项目不存在";
		String fileName = img.getOriginalFilename();
		//文件路径分隔符，在windows和linux下面可能不一样，部署以后需要注意
		String separator = "//";
		StringBuffer path = new StringBuffer();
		//"C:\\pic\\pro1\\A区域\\1.png
		path.append("C://pic").append(separator).append(proid).append(separator).append(addr);
		if(new File(path.toString()).exists())
			return "error1:该区域相应图片已经存在，请删除后重新上传";
		
		//将文件保存在文件系统中
		//首先要创建所有的目录
		Files.createDirectories(Paths.get(path.toString()));
		
		//接着创建一个新得空的文件
		path.append(separator).append(fileName);
		Files.createFile(Paths.get(path.toString()));
		//最后将图片保存至空的文件中
		img.transferTo(new File(path.toString()));
		
		/*在redis里面存储相关信息
	    	表名字pic，主键：proid
	    	类型Hashmap
	    	内容  区域:图片名
	    	例：
	    		pic:123ABC
	    		-----------
	    		A区:1.png
	    		B区:2.png
		*/
		redisUtil.HashSet("pic:"+proid,addr, fileName);
		//在redis中新建一个普通表，表名为3DPoints:proid:addr,内容为存储点信息的arraylist<map<String,object>>
		//首次上传图片无数据点，设置为空数组
		List<Map<String, Object>>  l1= new ArrayList<>();
		redisUtil.ComSet(point_3d+proid+":"+addr, l1);
		return "上传成功";
	}
	
	/*
	 * 删除图片，前端应传项目id+区域
	 */
	@PostMapping(path = "/projct/deletePic")
	@ResponseBody
	public String deletePic(@RequestParam("proid") String proid,
			@RequestParam("addr") String addr) {
		String seperator = "//";
		String abslutePath = "C://pic" + seperator + proid + seperator + addr;
		Path path = Paths.get(abslutePath);
		File file = path.toFile();
		//删除区域文件夹下面的所有文件
		for(File f : file.listFiles()) {
			f.delete();
		}
		//删除区域文件夹
		file.delete();
		//删除redis中对应的条目
		redisUtil.HashDel("pic:"+proid, addr);
		//删除2Dpoints:proid:addr表
		redisUtil.Delete(point_3d+proid+":"+addr);
		return "删除成功";
	}
	
	/*
	 * 返回对应项目的所有图片的3D检测的点的信息
	 */
	@GetMapping(path="project/get3DPoints")
	@ResponseBody
	public Object get3DPionts(@PathParam("proid") String proid)
	{
		JSONObject jb = new JSONObject();
		jb.put("proid", proid);
		String key = "pic:"+proid;
		//获取文件的路径前缀
		String picPrefix = "/pics/";
		if(!redisUtil.Has(key))
			return "error";
		Map<Object, Object> urlMap = redisUtil.HashGetAll(key);
		List<Map<String,Object>> outerList = new  ArrayList<>();
		urlMap.forEach((k,v)->{
			Map<String,Object> innerMap = new HashMap<>();
			innerMap.put("address", k);
			innerMap.put("url", picPrefix+proid+"/"+k+"/"+(String)v);
			Object innerList = redisUtil.ComGet(point_3d+proid+":"+(String)k);//此处为ArrayList<Map<String,Object>>
			innerMap.put("points", innerList);
			outerList.add(innerMap);
		});
		jb.put("pictures", outerList);
		return jb;
	}
	
	/*
	 * 更新某个项目的所有图片对应数据点的信息，和前端约定的是每一次修改都提交所有
	 * 这里将更新所有的数据点的表
	 */
	@PostMapping(path = "project/update3DPoints")
	@ResponseBody
	public String update3DPoints(@RequestBody JSONObject jb){
		String proid = (String) jb.get("proid");
		if(!redisUtil.Has(proid)) {
			return "error";
		}
		List<Map<String,Object>> list1 = (List<Map<String, Object>>) jb.get("pictures");
		list1.forEach(value->{
			String addr = (String) value.get("address");
			List<Map<String,Object>> list2 = (List<Map<String, Object>>) value.get("points");
			String key = point_3d + proid + ":" + addr;
			redisUtil.ComSet(key, list2);
		});
		return "更新成功";
	}
//	@ExceptionHandler(Exception.class)
//	public Map<String,String> ExceptionReturn(Exception e){
//		Map<String,String> map = new HashMap<>();
//		map.put("info", e.getMessage());
//		return map;
//	
//	}
}
