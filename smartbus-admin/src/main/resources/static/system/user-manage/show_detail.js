// var userId = getQueryString("userId");
// function getQueryString(name) {
    // var result = window.location.search.match(new RegExp("[\?\&]" + name + "=([^\&]+)", "i"));
    // if (result == null || result.length < 1) {
        // return "";
    // }
    // return result[1];
// }

var operatedUserId = sessionStorage.getItem("operatedUserId");
sessionStorage.removeItem("operatedUserId");
$.post("/api/user/getOperatedUserInfo2",{"userId": operatedUserId},
        function(result){
            var loginName = result.userLoginName;
            var userName = result.userName;
            //var userSex;
           // if (result.userSex === 1)
            //    userSex="男";
           // else
            //    userSex="女";
            var userOrg = result.userOrganization;
            var userTel = result.userTel;
            var userEmail = result.userEmail;
            var userLevel;
            switch(result.userLevel){
                case 1:
                    userLevel = "威腾管理人员";
                    break;
                case 2:
                    userLevel ="区域业务员";
                    break;
                case 3:
                    userLevel ="客户公司管理人员";
                    break;
                case 4:
                    userLevel ="客户公司运维人员";
                    break;
                default:
                    userLevel = "未知";
            }
            var alarmWays = result.alarmWay;


           // var userEmployTime = result.userEmployTime;
           // var userEdu;
            // switch(result.userEducation){
                // case 1:
                    // userEdu = "大专";
                    // break;
                // case 2:
                    // userEdu ="本科";
                    // break;
                // case 3:
                    // userEdu ="硕士";
                    // break;
                // case 4:
                    // userEdu ="博士";
                    // break;
                // default:
                    // userEdu = "未知";
            // }
            //var userExtraInfo = result.userExtraInfo;
            var userProject = result.userProject;

            document.getElementById("myTable").rows[0].cells[1].innerHTML = loginName;
            document.getElementById("myTable").rows[1].cells[1].innerHTML = userName;
           // document.getElementById("myTable").rows[2].cells[1].innerHTML = userSex;
            document.getElementById("myTable").rows[2].cells[1].innerHTML = userOrg;
            document.getElementById("myTable").rows[3].cells[1].innerHTML = userTel;
            document.getElementById("myTable").rows[4].cells[1].innerHTML = userEmail;
            document.getElementById("myTable").rows[5].cells[1].innerHTML = userLevel;
            document.getElementById("myTable").rows[6].cells[1].innerHTML = alarmWays;
            document.getElementById("myTable").rows[7].cells[1].innerHTML = userProject;
            //document.getElementById("myTable").rows[8].cells[1].innerHTML = projects;
           // document.getElementById("myTable").rows[9].cells[1].innerHTML = userEdu;
           // document.getElementById("myTable").rows[10].cells[1].innerHTML = userExtraInfo;

        },"json");