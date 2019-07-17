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
    alert("operatedUserId is:" + operatedUserId);
    var sysUserId = sessionStorage.getItem("sysUserId");
    alert("He is using this system:" + sysUserId);
    
    $.post("/api/user/getOperatedUserInfo",{"userId": operatedUserId},
    function(result){
        //谜之JSON字符串自动转对象，这也许是$的杰作
        alert("I want to revise this user, his name is:" + result.userLoginName);
        $("#displayUserLoginName").val(result.userLoginName);
        $("#pwdCheck").val(result.userPassword);
        $('#reFormId').ajaxWrite({data:result});
    },"json");
    
    //这里注册表单提交成功后的回调函数
    //执行顺序是这样的： 点击 保存 按钮 ->onEditUser->submitHandler->this(when get submit response, this is in down, have no name)->saveHandler(if success submit)-> close dialog -> over
    $('#myFormId').submit(function(){
        //判断表单的客户端验证是否通过
        var valid = $('#reFormId').validationEngine({returnIsValid: true});
        if(valid){
            $(this).ajaxSubmit({
            //表单提交成功后的回调
                success: function(responseText, statusText, xhr, $form){
                    //responseText应该是字符串，自动转为数字了，难道又是$的杰作吗？
                    if(!responseText){
                        top.Toast("showErrorToast", "修改失败！");
                    }else{
                        top.Toast("showSuccessToast","修改成功！");
                        saveHandler();
                    }
                }
            });
        }
      //阻止表单默认提交事件
      return false;
    });


    
}

// 点击 保存 按钮后，先执行这个函数
function submitHandler(){
    //判断表单的客户端验证是否通过
    var valid = $('#reFormId').validationEngine({returnIsValid: true, showOnMouseOver:false});
    if(valid){
      $('#reFormId').submit()
    }
    //阻止表单默认提交事件
    return false;
}

function saveHandler(){
    //刷新数据
    top.frmright.refresh(true);
    //关闭窗口
    top.Dialog.close();
}