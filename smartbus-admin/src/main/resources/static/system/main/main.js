var data = [
    {"news": "IBC0013 IBC3434过温，50℃, 2019-05-15 12:36"},
    {"news": "IBC0013 IBC7777, 59℃, 2019-05-14 05:33"},
    {"news": "IBC3457 IBC3434, 49℃, 2019-05-15 19:34"},
    {"news": "IBC3457 , 51℃, 2019-05-15 19:00"}
];
tip_construction(data, ".right_tip", 10000); //数据，div的class或者id，定时器时间
//给小铃铛动态添加信息用函数top.addTip     top.addTip("123456");
//var timer;
//定时器 10秒轮询

//timer = window.setInterval(queryInfo,10000);//10秒一次

function queryInfo() {
    //根据当前的用户信息查询过温记录
    $.ajax({
        type: "POST",
        contentType: JSON,
        url: "",
        data:data,
        //请求成功
        success: function (data) {
            var info;
            top.addTip(info);
        },
        //请求失败，包含具体的错误信息
        error: function (e) {
            console.log(e.status);
            console.log(e.responseText);
        },
    });
}

function exitHandler() {
    top.Dialog.confirm("确定要退出系统吗", function () {
        window.location.href = "/login/login.html";
        $.get("/api/user/loginOut", function(){});
    }, function () {
       //如果取消，就执行这里
    });
}

function editMyInfo() {
    var diag = new top.Dialog();
    diag.Title = "修改用户信息";
    sessionStorage.setItem("operatedUserId", sessionStorage.getItem("sysUserId"));
    diag.URL = "/system/user-manage/dialog-revise-user.html";
    diag.Width = 800;
    diag.Height = 500;
    diag.OkButtonText = "保 存";
    //顺序很重要，diag.show()之前添加确定按钮事件，show之后添加新按钮
    diag.OKEvent = function() {
        diag.innerFrame.contentWindow.submitHandler();
    };
    diag.show();
}