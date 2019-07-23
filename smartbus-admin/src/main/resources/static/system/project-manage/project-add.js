var closeWinStatus = 0;
function initComplete(){
    //var sysUsers = JSON.parse(sessionStorage.getItem('sysUser'));
    //$('#sysUserId').val(sysUsers.userId);
    $("#address").on("click", function() {
        var diag = new top.Dialog();
        diag.ID = "map";
        diag.Title = "选择详细地址";
        sessionStorage.setItem("whoCallMap", "addProject");
        diag.URL = "/system/project-manage/dialog-map.html";
        diag.Width = 800;
        diag.Height = 600;
        diag.show();
    });

    $.get("/api/user/getAvailableCompany",function(result){
        //赋给data属性
        $("#company").data("data",result);
        //刷新树形下拉框
        $("#company").render();
    },"json");

    //表单提交
    $('#form').submit(function(){
        //判断表单的客户端验证是否通过
        var valid = $('#form').validationEngine({returnIsValid: true});
        if(valid){
            $(this).ajaxSubmit({
                //表单提交成功后的回调
                success: function(responseText, statusText, xhr, $form){
                    var response = eval("(" + responseText + ")");
                    if(!response.status){
                        top.Toast("showErrorToast", response.info);
                    }
                    else{
                        top.Toast("showSuccessToast", response.info);
                        if(closeWinStatus == 1){
                            saveCloseWinHandler();
                        }
                        else{
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
function submitHandler(closeWin){
    closeWinStatus=closeWin;
    //判断表单的客户端验证是否通过
    var valid = $('#form').validationEngine({returnIsValid: true, showOnMouseOver:false});
    if(valid){
        $('#form').submit()
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
    $("#projectOrder").val("");
    $("#companyName").val("");
    $("#address").val("");
    $("#info").val("");
}

