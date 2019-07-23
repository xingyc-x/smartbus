var closeWinStatus = 0;
var WEITEN_ID = 1;
function initComplete(){
    //这个函数还是会自动执行的
    //先干掉，之后再说
    //var sysUsers = JSON.parse(sessionStorage.getItem('sysUser'));
    //$('#sysUserId').val(sysUsers.userId);

    //获取远程数据
    $.get("/api/project/getAvailableProject", function(result){
        //赋给data属性
        $("#userProject").data("data",result);
        //刷新树形下拉框
        $("#userProject").render();
    },"json");
    
    $.get("/api/user/getAvailableCompany",function(result){
        //赋给data属性
        $("#userOrganization").data("data",result);
        //刷新树形下拉框
        $("#userOrganization").render();
    },"json");

    //赋给data属性
    $("#alarmWay").data("data",{"treeNodes":[
        {"id":"1","name":"邮件","parentId":"0"},
        {"id":"2","name":"短信","parentId":"0"}]});
    //刷新树形下拉框
    $("#alarmWay").render();

    //注册表单提交完成后的回调函数
    $('#addFormId').submit(function(){
        //判断表单的客户端验证是否通过
        var valid = $('#addFormId').validationEngine({returnIsValid: true});
        if(valid){
            $(this).ajaxSubmit({
                //表单提交成功后的回调
                success: function(responseText, statusText, xhr, $form){
                    //成功返回字符串“1”，好像可以自动转成整数型，不知道其他浏览器是否会有兼容性问题
                    //alert(responseText);
                    //alert(responseText.status);
                    var response = eval("(" + responseText + ")");
                   // alert(responseText.status);
                    //alert(responseText);
                    if(!response.status){
                        top.Toast("showErrorToast", response.info);
                    }
                    else{
                        top.Toast("showSuccessToast",response.info);
                        //0 不关界面， 1 关界面
                        if(closeWinStatus == 1){
                            saveCloseWinHandler();
                        } else{
                            saveHandler();
                        }
                    }
                }
            });
        }
        else{
            top.Toast("showErrorToast","表单填写不正确！");
        }

        //阻止表单默认提交事件
        return false;
    });

}

// 点击窗口上的添加用户的两个按钮时（保存并新建用户，和保存），先执行这个函数
function submitHandler(closeWin){
    //closeWin 0 不关界面， 1 关界面
    closeWinStatus = closeWin;
    //判断表单的客户端验证是否通过
    var valid = $('#addFormId').validationEngine({returnIsValid: true, showOnMouseOver:false});
    if(valid){
        //威腾公司管理员但不是威腾公司的人，不允许注册,这里要求威腾公司的id必须为1
        //selectTree2 就是 userOrganization 的那个
        if ($('#userLevel').val() < 3 && $("#userOrganization").attr("relValue") != WEITEN_ID) {
            //alert($("#selectTree2").attr("relValue"));
            top.Toast("showErrorToast", "非威腾公司人员不能注册为威腾公司管理员与业务员！");
        } else {
            $('#addFormId').submit()
        }
    }
    //阻止表单默认提交事件
    return false;
}

//保存并关闭
function saveCloseWinHandler(){
    //刷新数据,重排序，重置页码为1
    top.frmright.refresh(false);
    //关闭窗口
    top.Dialog.close();
}
//保存不关闭
function saveHandler(){
    //刷新数据,重排序，重置页码为1
    top.frmright.refresh(false);
    $("#userName").val("");
    $("#trueName").val("");
    
}