<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">   
    <title>登录</title>	
    <link href="plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/common.css" rel="stylesheet">
  </head>
  
  <body>
    <div class="container">
	  <form id="login-form" class="form-signin" role="form" action="user/login" method="post">
	  	<h2 class="form-signin-heading">登录</h2>
	 	<input class="form-control" name="j_username" type="text" autofocus="" required="" placeholder="用户名">
	  	<input class="form-control" name="j_password" type="j_password" required="" placeholder="密码">
	  	<input class="form-control" name="j_code" type="j_code" required="" placeholder="授权码">
	  	<input name="loginType" value="2" hidden="true"/>
	  	<label class="checkbox"><input type="checkbox" name="remember" value="true">记住密码</label>
	  	<button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
	  </form>
	</div>
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="plugins/jquery/jquery-1.8.3.js"></script>
    <script src="plugins/jquery/jquery.validate.js"></script>
    <script src="plugins/jquery/jquery.form.js"></script>
    <script src="plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="javascript/login.js"></script>
  </body>
</html>