var skinPath = document.getElementById("skin").getAttribute("skinPath");
skinPath = skinPath.replace("../..", "");
//console.log(skinPath);
var indexIcon = skinPath + "index_icon.png";

var sNodes=[
	{ "id":"1", "parentId":"0", "name":"监测系统","url":"/system/monitor/grid-map.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-207px -80px"},
	{ "id":"101", "parentId":"1", "name":"监测系统","icon": indexIcon,"backgroundPosition":"-111px -48px"},
	{ "id":"1101", "parentId":"101", "name":"项目选择","url":"/system/monitor/grid-map.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},
	{ "id":"1102", "parentId":"101", "name":"平面监测","url":"/system/monitor/grid-card.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},
	{ "id":"1103", "parentId":"101", "name":"2D监测","url":"/system/monitor/data-display-2D.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},
	
	
	{ "id":"2", "parentId":"0", "name":"设备管理","url":"/system/device-manage/deviceList.html","icon": indexIcon,"backgroundPosition":"-48px -64px"},
	{ "id":"201", "parentId":"2", "name":"设备管理","icon": indexIcon,"backgroundPosition":"-50px 0px"},
	{ "id":"2101", "parentId":"201", "name":"设备列表","url":"/system/device-manage/deviceList.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},
	
	{ "id":"3", "parentId":"0", "name":"用户管理","url":"/system/user-manage/user-list.html","icon": indexIcon,"backgroundPosition":"-180px 0px"},
	{ "id":"301", "parentId":"3", "name":"用户管理", "isParent": "true","icon": indexIcon,"backgroundPosition":"-178px -64px"},
	{ "id":"3101", "parentId":"301", "name":"用户列表","url":"/system/user-manage/user-list.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},
	
	{ "id":"4", "parentId":"0", "name":"事件管理","url":"/system/event-manage/incidentList.html", "isParent": "true","icon": indexIcon,"backgroundPosition":"-122px -97px"},
	{ "id":"401", "parentId":"4", "name":"事件管理", "isParent": "true","icon": indexIcon,"backgroundPosition":"-178px -64px"},
	{ "id":"2101", "parentId":"401", "name":"事件列表","url":"/system/event-manage/incidentList.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},

	{ "id":"5", "parentId":"0", "name":"项目管理","url":"/system/project-manage/projectList.html", "isParent": "true","icon": indexIcon,"backgroundPosition":"-139px -97px"},
	{ "id":"501", "parentId":"5", "name":"项目管理","icon": indexIcon,"backgroundPosition":"-139px -97px"},
	{ "id":"5101", "parentId":"501", "name":"项目列表","url":"/system/project-manage/projectList.html", "target":"frmright","icon": indexIcon,"backgroundPosition":"-191px -32px"},

];

var setting = {
	data: {
		simpleData: {
			idKey: "id",
			pIdKey: "parentId",
			enable: true
		},
		key: {
			children: "children"
		}
	}
};

var	show_on=0;
function transition(setting, sNodes) {
	var i, l,
		key = setting.data.simpleData.idKey,
		parentKey = setting.data.simpleData.pIdKey,
		childKey = setting.data.key.children;
	if(!key || key == "" || !sNodes) return [];
	var r = [];
	var tmpMap = {};
	for(i = 0, l = sNodes.length; i < l; i++) {
		tmpMap[sNodes[i][key]] = sNodes[i];
	};
	for(i = 0, l = sNodes.length; i < l; i++) {
		if(tmpMap[sNodes[i][parentKey]] && sNodes[i][key] != sNodes[i][parentKey]) {
			if(!tmpMap[sNodes[i][parentKey]][childKey])
				tmpMap[sNodes[i][parentKey]][childKey] = [];
			tmpMap[sNodes[i][parentKey]][childKey].push(sNodes[i]);
		} else {
			r.push(sNodes[i]);
		};
	};
	return r;
};

function switchMenu(switch_on){
	if(!switch_on){
		$("#bs_left").stop().animate({
			width:"42px"
		},200);
		$(".menu_content").find(".menu_content_content").stop().slideUp(50);
		$("#bs_left").removeClass("lbox_middlecenter_content");
		$(".bs_left_shrink i").removeClass("active");
		show_on=1;
	}else{
		$("#bs_left").stop().animate({
			width:"220px"
		},200);
		$(".menu_content").find(".menu_content_content").stop().show();
		$("#bs_left").addClass("lbox_middlecenter_content");
		$(".bs_left_shrink i").addClass("active");
		show_on=0;
	};
}
function switchHead(state){
	if(!state){
		$("#hbox").hide();
		$("#hbox").height(0);
		autoReset();
	}
	else{
		$("#hbox").hide();
		$("#hbox").height(oldBannerHeight);
		autoReset();
	}
}
$(function(){	
	var content = transition(setting, sNodes);
	$(".hbox_content_right_menu").empty();
	var html='';
	for(var i=0;i<content.length;i++){
		if(content[i].parentId=="0"){
			$(".hbox_content_right_menu").append('<li url="'+content[i].url+'"><i style="background-image:url('+content[i].icon+');background-position:'+content[i].backgroundPosition+'"></i>'+content[i].name+'</li>');
		};
		html+='<div class="menu_content">';
		if(content[i].children!=undefined){
			for(var j=0;j<content[i].children.length;j++){
				if(content[i].children[j].children!=undefined){
					html+='<div class="menu_content_win">';
					html+='<div class="menu_content_title clearfloat"><i style="background-image:url('+content[i].children[j].icon+');background-position:'+content[i].children[j].backgroundPosition+'"></i><span>'+content[i].children[j].name+'</span></div>';
					html+='<div class="menu_content_content">';
					for(var k=0;k<content[i].children[j].children.length;k++){
						html+='<a onclick="showProgressBar(\'加载中...\')"  href="'+content[i].children[j].children[k].url+'" target="'+content[i].children[j].children[k].target+'" class="menu_content_list clearfloat "><i style="background-image:url('+content[i].children[j].children[k].icon+');background-position:'+content[i].children[j].children[k].backgroundPosition+'"></i>'+content[i].children[j].children[k].name+'</a>';
					};
					html+='</div>';
					html+='</div>';
				}else{
					html+='<a onclick="showProgressBar(\'加载中...\')"  href="'+content[i].children[j].url+'" target="'+content[i].children[j].target+'" class="menu_content_list clearfloat "><i></i>'+content[i].children[j].name+'</a>';
				};
			};
		}
		html+='</div>';
	};
	$(".lbox_middlecenter_content").append(html);
	//右侧头部js
	$(".right_toggle").hover(function() {
		$(this).find(".right_toggle_content").stop().slideToggle(200);
	});
	//切换左侧导航
	$(".menu_content_list").click(function(){
		if($(this).hasClass()!="active"){
			$(this).parents(".lbox_middlecenter_content").find(".menu_content_list").removeClass("active");
			$(this).addClass("active");
		};
		$(".rbox_topcenter span").text($(this).parent().siblings(".menu_content_title").find("span").text()+' / '+$(this).text());
	});
	//组件导航切换
	$(".hbox_content_right_menu li").click(function(){
		var index=$(this).index();
		$("#bs_left .menu_content").eq(index).show().siblings(".menu_content").hide();//显示对应导航的目录
		if($(this).hasClass()!="active"){
			$(this).addClass("active").siblings("li").removeClass("active");//head导航栏样式变化
		};
		$("#bs_left").stop().animate({scrollTop:0},500);	//动画？
		$("#bs_right iframe").attr("src",$(this).attr("url")); 	//页面跳转
		$(".rbox_topcenter span").text('');	//上面的XX/XX
		
		$(".menu_content").eq(index).show().find("a").removeClass("active");//左侧第一加亮
		$(".menu_content").eq(index).show().find("a").eq(0).addClass("active");  
	});
	//左侧导航收缩
	$(".bs_left_shrink i").click(function(){
		if($(this).hasClass("active")){
			switchMenu(0);
		}else{
			switchMenu(1);
		};
	});
	$(".menu_content_win").hover(function(){
		if(show_on!=0){
			$(this).find(".menu_content_content").addClass("active").stop().show();
			$(this).find(".menu_content_title").addClass("hover");
			var top = $(this).find(".menu_content_content").offset().top; //子集距离浏览器顶部的距离
			var all_height = $(window).height(); //可视窗口的高度
			var self_height = $(this).find(".menu_content_content").height(); //子集的高度
			var end_hei = all_height - top - self_height; //子集距离浏览器底部的距离
			if(end_hei < 0) {
				$(this).find(".menu_content_content").stop().animate({
					"top": -(top-130)
				})
			};
		};
	},function(){
		if(show_on!=0){
			$(this).find(".menu_content_content").removeClass("active").stop().hide();
			$(this).find(".menu_content_title").removeClass("hover");
			$(this).find(".menu_content_content").stop().animate({
				"top": 0
			})
		}
	});
	//按钮控制导航收缩
	$(".right_toggle_fullscreen").click(function(){
		var iconBtn=$(this);
		if(iconBtn.hasClass('right_toggle_screen')){
            switchMenu(1);
            fullScrennHander(false);
            iconBtn.removeClass('right_toggle_screen');
            $(this).find("div").text("全屏");
        }
        else{
          top.switchMenu(0);
          if (typeof window.screenX === "number"){
            top.fullScrennHander(true);
            }
          else{
            top.Toast('showNoticeToast', '您的浏览器不支持全屏，如果需要，请手动按F11键。');
          }
          iconBtn.addClass('right_toggle_screen');
          $(this).find("div").text("还原");
        }
	});
	
	
	//默认第一个组件内容显示
	$(".menu_content").eq(0).show().find("a").eq(0).addClass("active");  //左侧选择加亮
	$("#bs_right iframe").attr("src",$(".menu_content").eq(0).find("a").eq(0).attr("href"));//右侧跳转
	$(".hbox_content_right_menu li").eq(0).addClass("active");
	//$(".menu_content").eq(0).show()
})
