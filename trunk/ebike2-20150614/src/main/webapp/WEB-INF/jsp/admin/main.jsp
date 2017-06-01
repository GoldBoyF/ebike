<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">   
    <title>后台管理</title>	
    <link href="plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/common.css" rel="stylesheet">
  </head>
  
  <body style="padding-top: 80px">
    <%@ include file="header.jsp"%>
    <div class="container-fluid">   
	  <div class="row">  
	  	<div class="col-sm-3 col-md-2">
	 	  <%@ include file="leftMenu.jsp"%> 
		</div>
		<div class="col-sm-9 col-md-10 main">        
          <p>欢迎来到后台管理中心</p>
        </div>
	  </div>
	</div>
	<%@ include file="footer.jsp"%>
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="plugins/jquery/jquery-1.8.3.js"></script>
    <script src="plugins/bootstrap/js/bootstrap.min.js"></script>
    <script src="javascript/admin/common.js"></script>
  </body>
</html>