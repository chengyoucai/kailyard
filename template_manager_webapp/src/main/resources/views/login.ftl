<#assign ctx = rc.getContextPath()>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>管理系统登录界面</title>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/bootstrap/css/bootstrap.min.css" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/signin.css" charset="utf-8" />
</head>
<body>
<script type="text/javascript">
    if (top.location.href.indexOf("login") < 0) {
        parent.location.href = "${ctx}/";
    }
</script>
<div class="container">

    <form class="form-signin" action="${ctx}/login" METHOD="post">
        <#if shiroLoginFailure??>
            <div id="alert-box" class="alert alert-danger alert-dismissible" role="alert">
                <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">×</span>
                    <span class="sr-only">Close</span></button>
                <strong>Error: </strong> 登录名或者密码错误，请重试.
            </div>
        </#if>

        <h2 class="form-signin-heading">管理系统</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="text" id="username" name="username" class="form-control" value="${username!!}" placeholder="登录名" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>
        <div class="checkbox">
        <!--    <label>
                <input type="checkbox" value="remember-me">记住我
            </label>-->
        </div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
    </form>

</div>
<script type="text/javascript" src="${ctx}/static/jquery/jquery-3.1.0.min.js" charset="utf-8"></script>
<script src="${ctx}/static/bootstrap/js/bootstrap.js" type="text/javascript"></script>
</body>
</html>
