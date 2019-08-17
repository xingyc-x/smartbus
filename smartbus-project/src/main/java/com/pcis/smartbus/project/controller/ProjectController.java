package com.pcis.smartbus.project.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.project.bean.Project;
import com.pcis.smartbus.utils.RedisUtil;


/* 
 * @author 王琪善
 * @data 2019-7-5
 * @info 项目新建，绑定删除等等
 */
@Controller
public class ProjectController {
	
	@Autowired
	RedisUtil redisUtil;
	
	/*
	 * 添加新项目
	 */
	@PostMapping(path="/project/add")
	@ResponseBody
	public Project AddProject(Project pro)
	{
//		if(pro==null)
//			return null;
//		if(redisUtil.Has("project:"+pro.getNumber()))
//			return null;
		redisUtil.ComSet("project:"+pro.getNumber(), pro);
		
		return (Project)redisUtil.ComGet("project:"+pro.getNumber());
	}
	
	/*
	 * 绑定项目1:根据设备MAC返回包含的数据点
	 * 返回数据示例：p1;p2;p3
	 */
	@GetMapping(path="/project/bind/listPoint")
	@ResponseBody
	public String getPointsByMac(@PathParam("mac") String mac)
	{
		String key= "machine:"+mac;
		if(!redisUtil.Has(key))
			//设备不存在
			return "error";
		if(!((String)redisUtil.ListGetOne(key, 0)).equals("p1"))
			//设备已经绑定过
			return "error2";
		List<Object> ls = redisUtil.ListGet(key,0,-1);
		int pointNum = ls.size();
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<pointNum;i++) 
		{
			sb.append((String)ls.get(i));
			if(i != (pointNum-1))
				sb.append(";");
		}
		return sb.toString();
	}
	
	/*
	 * 绑定项目2：根据返回的项目编号，mac地址,包含的设备点的数量，和对应的点设备编号映射进行设置
	 */
	@PostMapping(path = "/project/bind/bindPoint")
	@ResponseBody
	public String BindPoint(HttpServletRequest request)
	{
		String proNummber = request.getParameter("proNummber");
		if(!redisUtil.Has("project:"+proNummber))
			return "error";
		String mac = request.getParameter("mac");
		String key = "machine:"+mac;
		if(!redisUtil.Has(key))
			return "error2";
		//此处判断mac是否已经绑定过了
		if(!((String)redisUtil.ListGetOne(key, 0)).equals("p1"))
			return "error3";
		redisUtil.Delete(key);
		int pointNum = Integer.parseInt(request.getParameter("total"));
		Map<String,Object> map2 = new HashMap<>(pointNum);
		for(int i=1;i<=pointNum;i++)
		{
			String pointNummber = request.getParameter("p"+Integer.toString(i));
			redisUtil.ListSet(key, pointNummber);
			map2.put(pointNummber, null);
		}
		//生成设备表，其中包含所有设备点的编号，为List类型
		redisUtil.ListSetL(key,  proNummber);
		//生成项目表，其中存储着此项目包含的所有的设备点数据映射
		redisUtil.HashSetAll(proNummber, map2);
		return "ok";
	}
	
	/*
	 * 根据项目编号获取相对应的数据（分页）
	 */
	@GetMapping(path="/project/dataGet")
	@ResponseBody
	//注意此处函数声明中，@PathParam注解属性名和变量名需要一致，否则匹配失败
	public String GetPointDataByProID(HttpServletRequest request,@PathParam("proid") String proid)
	{
		String pageNo = request.getParameter("pageNo");
		String totalRaws = request.getParameter("pageSize");
		JSONObject jsobj = new JSONObject();
		jsobj.put("pager.pageNo", pageNo);
		Map<Object, Object> map = redisUtil.HashGetAll(proid);
		List<Object> list1 = new ArrayList<>(map.keySet());
		List<Object> list2 = new ArrayList<>(map.values());
		int page = Integer.parseInt(pageNo);
		int rawnum = Integer.parseInt(totalRaws);
		int size = map.size();
		int remain = size-(page-1)*rawnum;
		int num = (remain<rawnum)?remain:rawnum;
		if(num<=0)
			jsobj.put("pager.totalRows", 0);
		else
			jsobj.put("pager.totalRows", num);
		List<JSONObject> jb = new ArrayList<>();
		int i = (page-1)*rawnum;
		int limit = i+num;
		final String  order = "device_id";
		final String  data = "data";
		for(;i<limit;i++)
		{
			JSONObject json = new JSONObject();
			json.put(order, list1.get(i));
			json.put(data, list2.get(i));
			jb.add(json);
		}
		jsobj.put("rows", jb);
		return jsobj.toString();
		
	}	
}
