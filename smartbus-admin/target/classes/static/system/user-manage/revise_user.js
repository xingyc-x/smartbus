// var userId = getQueryString("userId");
//alert(userId);
// function getQueryString(name) {
    // var result = window.location.search.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    // if (result == null || result.length < 1) {
        // return "";
    // }
    // return result[1];
// }

// 该函数用于二次渲染
function initComplete(){
    //var sysUsers = JSON.parse(sessionStorage.getItem('sysUserId'));
    //$('#sysUserId').val(sysUsers.userId);//sysUsers.userId
    //$('#userId').val(userId);
    //表单提交
    var operatedUserId = sessionStorage.getItem("operatedUserId");
    sessionStorage.removeItem("operatedUserId");
    //$('#userId').val(operatedUserId);
    //alert("operatedUserId is:" + operatedUserId);
    //sysUserId是在登陆时存储的
    var sysUserId = sessionStorage.getItem("sysUserId");
    //alert("He is using this system:" + sysUserId);
   
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
    
    $.post("/api/user/getOperatedUserInfo",{"userId": operatedUserId},
    function(result){
        //谜之JSON字符串自动转对象，这也许是$的杰作
        //alert("I want to revise this user, his name is:" + result.userLoginName);
        //$("#displayUserLoginName").val(result.userLoginName);
        $("#pwdCheck").val(result.userPassword);
        $('#reFormId').ajaxWrite({data:result});
    },"json");
    
    //这里注册表单提交成功后的回调函数
    //执行顺序是这样的： 点击 保存 按钮 ->onEditUser->submitHandler->this(when get submit response, this is in down, have no name)->saveHandler(if success submit)-> close dialog -> over
    $('#reFormId').submit(function(){
        //判断表单的客户端验证是否通过
        //var valid = $('#reFormId').validationEngine({returnIsValid: true});
        $(this).ajaxSubmit({
        //表单提交成功后的回调
            success: function(responseText, statusText, xhr, $form){
                //responseText应该是字符串，自动转为数字了，难道又是$的杰作吗？
                //alert(responseText);
                if(!responseText){
                    top.Toast("showErrorToast", "修改失败！");
                }else{
                    top.Toast("showSuccessToast","修改成功！");
                    saveHandler();
                }
            }
        });
      //阻止表单默认提交事件
      return false;
    });


    
}

// 点击 保存 按钮后，先执行这个函数
function submitHandler(){
    //判断表单的客户端验证是否通过
    var valid = $('#reFormId').validationEngine({returnIsValid: true, showOnMouseOver:false});
                    
    if(valid){
        //威腾公司管理员但不是威腾公司的人，不允许注册,这里要求威腾公司的id必须为1
        //selectTree2 就是 userOrganization 的那个
        if ($('#userLevel').val() < 3 && $("#userOrganization").attr("relValue") != 1) {
            //alert($("#selectTree2").attr("relValue"));
            top.Toast("showErrorToast", "非威腾公司人员不能注册为威腾公司管理员与业务员！");
        } else {
            $('#reFormId').submit();
        }
    }
    //阻止表单默认提交事件
    return false;
}

function saveHandler(){
    //刷新数据
    //如果在主界面点修改用户信息，是没有这个函数的，就报错，
    //但是如果在用户管理界面，这个函数就是重要的
    try {
        top.frmright.refresh(true);
    }catch(err){}
    //关闭窗口
    top.Dialog.close();
}