var grid = null;
//在框架初始化函数initComplete()中渲染表格
//改函数一般会调用，但是在对话框里似乎没那么好用了
//该函数永远二次渲染
function initComplete(){
    //经过测试，会自动执行到这里。
    //alert("get this");
    //没有编译检测，容易出错
    grid = $("#mainGrid").quiGrid({
        columns: [
            { display: '用户名', name: 'userLoginName', align: 'left', width: "10%"},
            { display: '姓名', name: 'userName', align: 'left', width: "10%"},
            { display: '单位', name: 'userOrganization', align: 'left', width: "20%"},
            { display: '电话号码', name: 'userTel', align: 'left',  width: "15%"} ,
            { display: '邮箱', name: 'userEmail', align: 'left',  width: "15%"} ,
            { display: '权限等级', name: 'userLevel', align: 'left',  width: "10%"},
            { display: '创建时间', name: 'createTime', align: 'left',  width: "10%"},
            { display: '修改时间', name: 'updateTime', align: 'left',  width: "10%"},
            { display: '操作', isAllowHide: false, align: 'left', width: 180,
                render: function (rowdata, rowindex, value, column){
                    //这时个有趣的测试
                    //alert(rowdata.userId);
                    return '<div class="grid_opp_container">'
                        + '<span class="grid_opp_view" onclick="onViewUserDetail(\'' + rowdata.userId + '\')">查看</span>'
                        + '<span class="grid_opp_edit" onclick="onEditUser(\'' + rowdata.userId + '\')">修改</span>'
                        + '<span class="grid_opp_delete" onclick="onDelete(\'' + rowdata.userId+'\','+rowindex + ')">删除</span>'
                        + '</div>';
                }
            }],
        
        //render用来将该列渲染为自定义的html元素, 
        //dataAction:'local',数据分页方式，默认为server；
        //sortName指定初始时排序的列 
        //rownumbers指定是否显示行号,
        //usePager: true表示分页   
        
        url: '/api/user/getUserInfo2',  
        sortName: 'userId',
        rownumbers:true,
        usePager: true,
        height: '100%', 
        width:"100%",
        heightDiff:-40,
        percentWidthMode:true,
        rowNumberMemory:true
    });
    
    //监听查询框的enter事件
    $("#searchInput").keydown(function(event){
        if(event.keyCode == 13){
            searchHandler();
        }
    })

}
//查询
function searchHandler(){
    //注意此处array包括两个数组元素，第一个为搜索类型，第二个为搜索关键词
   var query = $("#queryForm").formToArray();//采集表单数据到json数组
    //alert(1);
    grid.setOptions({ params : query});////自定义条件查询,增加参数，必须是json
    grid.setNewPage(1);//页号重置为1
    grid.loadData();//加载数据
}

//重置查询
function resetSearch(){
    $("#queryForm")[0].reset();
    $('#search').click();//无条件信息查询，即显示所有数据
}

//刷新表格数据并重置排序和页数
function refresh(Update){
    if(!Update){
        //重置排序
        grid.options.sortName='userId';//表格排序字段名
        grid.options.sortOrder="desc";//排序顺序,降序
        //页号重置为1
        grid.setNewPage(1);
    }

    grid.loadData();
}

////当前登录用户的json数据
//var sysUsers = JSON.parse(sessionStorage.getItem('sysUser'));

//添加用户
function addUser(){
    var diag = new top.Dialog();
    diag.Title = "添加用户信息";
    diag.URL = "/system/user-manage/dialog-add-user.html";
    diag.Width = 800;
    diag.Height = 600;
    diag.OkButtonText = "保存并新建用户";
    //顺序很重要，diag.show()之前添加确定按钮事件，show之后添加新按钮
    diag.OKEvent = function() {
        //参数0 表示不关界面， 1表示关闭界面  调用字界面的submitHandler，
        //这个函数会提交表单，并判断是否关界面
        diag.innerFrame.contentWindow.submitHandler(0);
    };
    diag.show();
    diag.addButton("next", " 保 存 ", function() {
        diag.innerFrame.contentWindow.submitHandler(1);
    });
}

//修改用户信息，需要添加当前登录用户信息!!!!!
function onEditUser(rowId){
    //alert(rowId);
    //验证当前用户的修改权限
    //能改自己和比自己权限低的
    $.post("/api/user/validateAuthority", {"userId": rowId},
        function(result){
            //alert(result);
            if(!result){
                top.Toast("showErrorToast", "您没有该权限！");
            } else{
                //alert(result + "ok");
                
                sessionStorage.setItem("operatedUserId", rowId);
                var diag = new top.Dialog();
                diag.Title = "修改用户信息";
                diag.URL = "/system/user-manage/dialog-revise-user.html";
                diag.Width = 800;
                diag.Height = 600;
                diag.OkButtonText = "保 存";
                //顺序很重要，diag.show()之前添加确定按钮事件，show之后添加新按钮
                diag.OKEvent = function() {
                    diag.innerFrame.contentWindow.submitHandler();
                };
                diag.show();
            }

        },"json");

}

//查看用户信息
function onViewUserDetail(rowId){
    //alert("rowId:"+ rowId);
    //验证当前用户的权限
    $.post("/api/user/validateAuthority",{"userId":rowId},
        function(result){
            if(!result){
                top.Toast("showErrorToast", "您没有该权限！");
            }
            else{
                //jQuery.jCookie('userNameSelected',rowName);
                var diag = new top.Dialog();
                diag.Title = "详细信息";
                //diag.URL = "javascript:void(document.write('点击确定按钮'))";
                sessionStorage.setItem("operatedUserId", rowId);
                diag.URL = "/system/user-manage/dialog-content-user-detail.html";
                diag.Width = 800;
                diag.Height = 600;
                diag.show();
             }

        },"json");

}


function onDelete(rowId, rowidx){
    $.post("/api/user/validateAuthority",{"userId":rowId},
    function(result){
        if(!result){
            top.Toast("showErrorToast", "您没有该权限！");
        } else{
            top.Dialog.confirm("确定要删除该记录吗？",function(){
                //alert("I want to delete somethings"+rowid);
                //alert(rowidx);
                //删除记录
                $.post("/api/user/deleteUser",
                    {"userId":rowId},
                    function(data){
                        handleResult(data);
                    },"json");
                //刷新表格
               
            });
        }
    } ,"json");
}


//删除后的提示
function handleResult(data){
    //alert("I realy want delete something");
    if(data == 1){
        top.Toast("showSuccessToast", "删除成功！");
    }else{
        top.Toast("showErrorToast", "删除失败！");
    }
    grid.loadData();
}