function submitForm(containerId){
    var xmlhttp;
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    if (window.XMLHttpRequest) {
        //  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
        xmlhttp = new XMLHttpRequest();
    } else {
        // IE6, IE5 浏览器执行代码
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            if (xmlhttp.responseText != "FAIL") {
                // 登录失败返回“FAIL”,成功返回用户ID
                sessionStorage.setItem("sysUserId", xmlhttp.responseText);
                window.location.assign("/system/main/main.html");
            } else {
                Toast("showErrorToast", "用户名或密码错误！");
            }
        }
    }
    xmlhttp.open("POST", "/api/user/login?name=" + username + "&password=" + password, true);
    xmlhttp.send();
}
