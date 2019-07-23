var testData2={"id":1,"order":"778","company":"测试公司","create_time":"2019-1-1","location":"南京市","lng":118.800122,"lat":32.060705};
//数据表格使用
var grid = null;
function initComplete(){
    //var sysUsers = JSON.parse(sessionStorage.getItem('sysUser'));
    grid = $("#mainGrid").quiGrid({
        columns: [
            { display: '项目编号', name: 'order', align: 'left',  width:"25%"} ,
            { display: '客户公司', name: 'company', align: 'left',  width:"25%"} ,
            { display: '项目地点', name: 'location', align: 'left',  width:"25%"} ,
            { display: '项目创建时间', name: 'create_time', align: 'left',  width:"25%"},
            //{ display: '设备数量', name: 'device_num', align: 'left',  width:"25%"},
            { display: '操作', isAllowHide: false, align: 'left', width: 180,
                render: function (rowdata, rowindex, value, column){
                    return '<div class="grid_opp_container">'
                        + '<span class="grid_opp_view" onclick="onViewProjectDetail(\'' + rowdata.id + '\')">查看</span>'
                        + '<span class="grid_opp_edit" onclick="onEditProject(\'' + rowdata.id + '\')">修改</span>'
                        + '<span class="grid_opp_delete" onclick="onDeleteProject(\'' + rowdata.id + '\')">删除</span>'
                        + '</div>';
                }
            }
        ],
        url: '/api/project/getProjectInfo',
        sortName: 'order',
        rownumbers: true,
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
    //alert(1);
    //注意此处array包括两个数组元素，第一个为搜索类型，第二个为搜索关键词
    var query = $("#proSearchForm").formToArray();//采集表单数据到json数组
    grid.setOptions({ params : query});////自定义条件查询,增加参数，必须是json
    grid.setNewPage(1);//页号重置为1
    grid.loadData();//加载数据
}

//重置查询
function resetSearch(){
    $("#proSearchForm")[0].reset();
    $('#search').click();//无条件信息查询，即显示所有数据
}

//刷新表格数据并重置排序和页数
function refresh(Update){
    if(!Update){
        //重置排序
        grid.options.sortName='order';//表格排序字段名
        grid.options.sortOrder="desc";//排序顺序,降序
        //页号重置为1
        grid.setNewPage(1);

    }
    grid.loadData();
}

//添加项目
function addProject() {
    //alert("I am here");
    //验证当前用户的权限
    $.get("/api/project/validateUserAuthority",
        function (result) {
            if (!result) {
                top.Toast("showErrorToast", "您没有该权限！");
            } else {
                var diag = new top.Dialog();
                diag.ID = "add_project";
                diag.Title = "添加项目信息";
                diag.URL = "/system/project-manage/project-add.html";
                diag.Width = 800;
                diag.Height = 600;
                diag.OkButtonText = "保存并新建项目";
                //顺序很重要，diag.show()之前添加确定按钮事件，show之后添加新按钮
                diag.OKEvent = function () {
                    diag.innerFrame.contentWindow.submitHandler(0);
                };
                diag.show();
                diag.addButton("next", " 保 存 ", function () {
                    diag.innerFrame.contentWindow.submitHandler(1);
                });
            }
        });



}


//修改项目信息
function onEditProject(rowId){
    var diag = new top.Dialog();
    diag.ID = "edit_project";
    diag.Title = "修改项目信息";
    sessionStorage.setItem("projectId", rowId);
    diag.URL = "/system/project-manage/project-edit.html";
    diag.Width = 800;
    diag.Height = 600;
    diag.OkButtonText = "保 存";
    //顺序很重要，diag.show()之前添加确定按钮事件，show之后添加新按钮
    diag.OKEvent = function() {
        diag.innerFrame.contentWindow.submitHandler();
    };
    diag.show();

}

function onViewProjectDetail(rowId){
    var diag = new top.Dialog();
    diag.Title = "详细信息";
    //diag.URL = "javascript:void(document.write('点击确定按钮'))";
    sessionStorage.setItem("projectId", rowId);
    diag.URL = "/system/project-manage/project-detail.html";
    diag.Width = 800;
    diag.Height = 600;
    diag.show();

}

function onDeleteProject(rowId){
    //验证当前用户的权限
    $.get("/api/project/validateUserAuthority",
        function (result) {
            if (!result) {
                top.Toast("showErrorToast", "您没有该权限！");
            } else {
                top.Dialog.confirm("确定要删除该记录吗？",function(){
                    //删除记录
                    $.post("/api/project/deleteProject",
                        {"projectId":rowId},
                        function(data){
                            handleDeleteResult(data);
                        },"json");
                    //刷新表格
                    grid.loadData();
                });
            }
        });


}

//删除后的提示
function handleDeleteResult(data){
    if(data == 1){
        top.Toast("showSuccessToast", "删除成功！");
    }else{
        top.Toast("showErrorToast", "删除失败！");
    }
}