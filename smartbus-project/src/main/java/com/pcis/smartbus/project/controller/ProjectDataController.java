package com.pcis.smartbus.project.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.pcis.smartbus.utils.RedisUtil;
import com.pcis.smartbus.common.Constant;
import com.pcis.smartbus.db.dao.ProjectManualMapper;
import com.pcis.smartbus.db.dao.ProjectMapper;
import com.pcis.smartbus.db.dao.ProjectUserRelationManualMapper;
import com.pcis.smartbus.db.dao.SmartbusUserMapper;
import com.pcis.smartbus.db.domain.Project;
import com.pcis.smartbus.db.domain.SmartbusUser;
import com.pcis.smartbus.event.EventModel;
import com.pcis.smartbus.event.EventProducer;
import com.pcis.smartbus.event.EventType;

/* 
 * @author 王琪善
 * @data 2019-7-5
 * @info 项目新建，绑定删除等等
 */
@Controller
public class ProjectDataController {
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	ProjectManualMapper projectManualMapper;
	
	@Autowired
	ProjectUserRelationManualMapper relationMapper;
	
	@Autowired
	ProjectMapper projectMapper;
	
	@Autowired
	SmartbusUserMapper userMapper;
	
	@Autowired
	EventProducer eventProducer;
//	@Autowired
//	ProjectMapper projectMapper;
	/*
	 * 添加新项目
	 */
//	@PostMapping(path="/project/add")
//	@ResponseBody
	public Project AddProject(Project pro)
	{
//		if(pro==null)
//			return null;
//		if(redisUtil.Has("project:"+pro.getNumber()))
//			return null;
		redisUtil.ComSet("project:"+pro.getId(), pro);
		
		return (Project)redisUtil.ComGet("project:"+pro.getId());
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
		String first = (String)redisUtil.ListGetOne(key, 0);
		if(!first.equals("p1"))
		{
			
			String proID = first;
			//设备已经绑定过,有两种情况，1绑定的项目已经过期（被删除，在redis中查不到），2绑定的项目没有过期
			//1绑定的项目没过期
			if(redisUtil.Has("project:"+proID))
				return "error2";//设备已绑定上线
			//绑定的项目已经过期，假装设备首次上线
			else
			{
				int len = redisUtil.ListGet(key,1,-1).size();
				redisUtil.Delete(key);
				for(int i=1;i<=len;i++)
	        	{
	        		redisUtil.ListSet(key, "p"+Integer.toString(i));
	        	}
			}
		}
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
		if(!redisUtil.Has("project:"+proid))
			return "error";//项目已经被删除不存在了
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
	
	@GetMapping(path="/project/getavaliableproject")
	@ResponseBody
	//注意此处函数声明中，@PathParam注解属性名和变量名需要一致，否则匹配失败
	public String GetProjectByUserID(HttpServletRequest request)
	{
		int pageNo = Integer.valueOf(request.getParameter("pageNo"));
		int totalRaws = Integer.valueOf(request.getParameter("pageSize"));
		JSONObject jsobj = new JSONObject();
		jsobj.put("form.paginate.pageNo", pageNo);
		int capacity = (int) request.getSession().getAttribute(Constant.CAPACITY);
		int userID = (int) request.getSession().getAttribute(Constant.USER_ID);
		 List<Project> projects = new ArrayList<>();
		if(capacity == Constant.WEITEN_ADMIN){
			//查看所有的项目
			projects = projectManualMapper.getAPageProject((pageNo-1)*totalRaws, totalRaws, "id", "asc");
		}
		else{
			//查看与userid相匹配的项目
			 projects  = projectManualMapper.getProjectsByUserIDSearch(userID);
		}
			
			 if(projects == null)
			 {
				 jsobj.put("form.paginate.totalRows", 0);
				 jsobj.put("rows", new ArrayList<>());
			 }
			 else
			 {
				 jsobj.put("form.paginate.totalRows", projects.size());
				 List<JSONObject> rows = new ArrayList<>(projects.size());
				 projects.forEach(project->{
					 JSONObject map = new JSONObject();
					 map.put("id", project.getId());
					 map.put("location", project.getLocation());
					 map.put("lng", project.getLongitude());
					 map.put("lat", project.getLatitude());
					 map.put("remarks", project.getIntroduction());
					 rows.add(map);
			 });
				 jsobj.put("rows", rows);
			 }
		
		
		return jsobj.toString();
		
	}
	
	//用于设备绑定请求项目下拉列表，仅限管理员权限才可以操作
	@GetMapping("/project/getallproject")
	@ResponseBody
	public String getAllProject(HttpSession session)
	{
		int capacity = (int)session.getAttribute(Constant.CAPACITY);
		if(capacity != Constant.WEITEN_ADMIN)
			return "error";
		List<Project> projects = projectManualMapper.getAllProject();
		JSONObject jsobj = new JSONObject();
		List<JSONObject>  jsonList = new ArrayList<>(projects.size());
		for(Project project : projects){
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("key", project.getOrder());
			jsonobj.put("value", project.getId());
			jsonList.add(jsonobj);
		}
		jsobj.put("list", jsonList);
		return jsobj.toString();	
	}
	
	@PostMapping("/project/alarm")
	@ResponseBody
	public String sendEmailAlarm(@RequestParam("proid")String proid,
								@RequestParam("device_id")String device_id,
								@RequestParam("message") String message,
								@RequestParam("data")String data) {
		int proID = Integer.valueOf(proid);
		List<SmartbusUser> users = userMapper.getServicerByProjectID(proID);
		if(users == null)
			return "error";
		Project project = projectMapper.selectByPrimaryKey(proID);
		for(SmartbusUser user : users) {
			EventModel model = new EventModel();
			model.setEventType(EventType.EMAIL);
			Map<String ,Object> eventMsg = new HashMap<>();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			eventMsg.put("date", dateFormat.format(new Date()));
			eventMsg.put("user_name", user.getUserName());
			eventMsg.put("user_realName", user.getRealName());
			eventMsg.put("project_order", project.getOrder());
			eventMsg.put("project_location", project.getLocation());
			eventMsg.put("email", user.getEmail());
			eventMsg.put("device", device_id);
			eventMsg.put("message", message);
			eventMsg.put("data", data);
			model.setEventMsg(eventMsg);
			eventProducer.produce(model);
		}	
		return "ok";
		}
	
	
}
