<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/inc/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>错误</title>
<%@ include file="/WEB-INF/views/inc/head.jsp"%>
<script type="text/javascript" >
   $(function(){
     setTimeout(function(){
       history.go(-1);
     },5000);
   });
</script>
</head>

<body>
  <div class="content_wrapper">
    <!------------ 错误页面 ------------>
    <div class="weberror">
      <div class="web_tit">
        <div class="web_l"></div>
        <h2>温馨提示</h2>
        <div class="web_r"></div>
      </div>
      <div class="web_con">
        <div class="web_left"><img src="${resourcPath}/boss/images/u129_original.png" width="96" height="96" /></div>
        <div class="web_right">
          <h3>系统运行异常,请与管理员联系。</h3>
          <p>异常码为：<span style="color:red"><s:property value="exception.id"/></span> </p>
          <p>异常信息：<span style="color:red"><s:property value="exception.message"/><s:property value="msg"/> </span> </p>
          <p>系统将在5秒钟后  返回 上一页</p>
          <p class="kongbai"></p>
          <p>您现在可以返回：<span><a href="javascript:history.go(-1);">上一页</a></span></p>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
