function initComplete(){

    $.get("/api/user/getAvailableCompany",function(result){
        //赋给data属性
        $("#company").data("data",result);
        //刷新树形下拉框
        $("#company").render();
    },"json");

    var projectId = sessionStorage.getItem("projectId");
    sessionStorage.removeItem("projectId");

    $.post("/api/project/getAProjectInfoForEdit",{"projectId":projectId},
        function(result){
            //alert(result.id);
            //当projectId 隐藏后，val就不能把值传递进去了，不知是为什么
            //$("#projectId:hidden").val(result.id);
            $("#form").ajaxWrite({data:result});
        },"json");

    $("#address").on("click", function() {
        var diag = new top.Dialog();
        diag.ID = "map";
        diag.Title = "选择详细地址";
        sessionStorage.setItem("whoCallMap", "editProject");
        diag.URL = "/system/project-manage/dialog-map.html";
        diag.Width = 800;
        diag.Height = 600;
        diag.show();
    });

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
                        saveHandler();
                    }
                }
            });
        }

        //阻止表单默认提交事件
        return false;
    });



}


function submitHandler(){
    //判断表单的客户端验证是否通过
    var valid = $('#form').validationEngine({returnIsValid: true, showOnMouseOver:false});
    if(valid){
        $('#form').submit()
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

