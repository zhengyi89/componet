<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="64kb"%>
<%@ include file="/WEB-INF/views/inc/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/WEB-INF/views/inc/head.jsp"%>
<script type="text/javascript">
  $(document).ready(function () {
    document.getElementById("primiterType").style.display = "none";
    document.getElementById("mapType").style.display = "none";
    document.getElementById("listType").style.display = "none";
    document.getElementById("entityType").style.display = "none";
    var valueTypeObj = document.getElementById("valueTypeStr");
      var valueType = valueTypeObj.value;
      if (valueType == 'VALUE') {
      document.getElementById("primiterType").style.display = "block";
    } else if (valueType == 'MAP') {
      document.getElementById("primiterType").style.display = "none";
      document.getElementById("mapType").style.display = "block";
    } else if (valueType == 'LIST') {
      document.getElementById("listType").style.display = "block";
      document.getElementById("primiterType").style.display = "none";
    } else if (valueType == 'STRUCTURE') {
      document.getElementById("primiterType").style.display = "none";
      document.getElementById("entityType").style.display = "block";
    }
  });
</script>
<title>配置详细信息</title>
  <%@ include file="/WEB-INF/views/inc/head.jsp"%>
</head>
<body onload="depaly()">

    <div id="content_2" class="content_wrapper">
      <div class="content_main">
        <div class="panel panel-default">
          <div class="panel-heading">配置详情</div>
          <div class="panel-body">
            <form class="cmxform form-horizontal">
              <div class="col-lg-offset-2 col-lg-10"><h2 class="title">配置详情</h2></div>
             
              <div class="form-group">
                <label class="col-lg-3 control-label">命名空间：</label>
                <div class="col-lg-6 pt_7">
                  ${config.namespace}
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">配置类型：</label>
                <div class="col-lg-6 pt_7">
                  ${config.type}
                  
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">配置键：</label>
                <div class="col-lg-6 pt_7">
                  ${config.configKey}
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">值类型：</label>
                <div class="col-lg-6 pt_7">
                 <fmt:message key="enum_value_type_${config.valueTypeStr}" />
                  <input id="valueTypeStr" type="hidden"" value="<c:out value="${config.valueTypeStr}"></c:out>"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">值数据类型：</label>
                <div class="col-lg-6 pt_7">
                 <c:if test="${config.valueTypeStr != 'STRUCTURE' }">
			      <fmt:message key="enum_value_data_type_${config.valueDataTypeStr}" />
			     </c:if>
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">配置值：</label>
                <div class="col-lg-6 pt_7">

								<div id="primiterType">
									<c:out value="${config.value}"></c:out>
								</div>
								<div id="entityType">
									<div id="addEntityType">
										<table border="1px" style="border-collapse: collapse;">
											<tr>
												<td>键</td>
												<td>值</td>
											</tr>
											<c:forEach items="${entityMap}" var="entity">
												<tr>
													<td><c:out value="${entity.key}"></c:out></td>
													<td><c:out value="${entity.value}"></c:out></td>
												</tr>
											</c:forEach>
										</table>
									</div>
								</div>
								<div id="mapType">
									<div id="addMapType">
										<table border="1px" style="border-collapse: collapse;">
											<tr>
												<td>键</td>
												<td>值</td>
											</tr>
											<c:forEach items="${map}" var="entry">
												<tr>
													<td><c:out value="${entry.key}" /></td>
													<td><c:out value="${entry.value}" /></td>
												</tr>
											</c:forEach>
										</table>
									</div>
								</div>
								<div id="listType">
									<div id="addListType">
										<table border="1px" style="border-collapse: collapse;">
											<tr>
												<td>序号</td>
												<td>值</td>
											</tr>
											<c:forEach items="${list}" var="ls" varStatus="index">
												<tr>
													<td>${index.count}</td>
													<td><c:out value="${ls}"></c:out></td>
												</tr>
											</c:forEach>
										</table>
									</div>
								</div>
							</div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">是否可修改：</label>
                <div class="col-lg-6 pt_7">
                 ${config.updatable}
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">是否可修改：</label>
                <div class="col-lg-6 pt_7">
                ${config.deletable}
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">创建时间：</label>
                <div class="col-lg-6 pt_7">
                <fmt:formatDate value="${config.createDate}"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">更新时间：</label>
                <div class="col-lg-6 pt_7">
                <fmt:formatDate value="${config.updateDate}"/>
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">配置描述：</label>
                <div class="col-lg-6 pt_7">
                ${config.description}
                </div>
              </div>
              <div class="form-group">
                <label class="col-lg-3 control-label">所属系统：</label>
                <div class="col-lg-6 pt_7">
               
	                <c:forEach var="n" items="${sysNameMap}" >
	                     <c:if test="${config.systemId eq n.key}">
	                     <c:out value="${n.value}" />
	                     </c:if>
	                </c:forEach>
                </div>
              </div>

              <div class="form-group">
                <div class="col-lg-12 align_center">
                  <button class="btn btn-primary submit" type="button" onclick="history.go(-1);">返回</button>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

<%--     <table width="70%"  border="1px" class="confTable" >
      <tr >
      <td width="20%"> <strong>命名空间:</strong></td>
      <td> <label><c:out value="${config.namespace}"></c:out></label></td>
      </tr>
      <tr>
      <td><strong> 配置类型: </strong>  </td>
      <td> <label><c:out value="${config.type}"></c:out></label></td>
      </tr>
       <tr>
      <td><strong>配置键:</strong></td>
      <td> <label ><c:out value="${config.configKey}"></c:out></label></td>
      </tr>
       <tr>
      <td><strong> 值类型 :</strong></td>
      <td><fmt:message key="enum_value_type_${config.valueTypeStr}" /> &nbsp;
      <input id="valueTypeStr" type="hidden"" value="<c:out value="${config.valueTypeStr}"></c:out>"/>
      </td>
      </tr>
       <tr>
      <td> <strong>值数据类型: </strong></td>
      <td><c:if test="${config.valueTypeStr != 'STRUCTURE' }">
      <fmt:message key="enum_value_data_type_${config.valueDataTypeStr}" />
      </c:if>&nbsp;</td>
      </tr>
         <tr>
      <td> <strong>配置值: </strong></td>
      <td>
        <div id="primiterType"><c:out value="${config.value}"></c:out></div>
    <div id="entityType">
    <div id="addEntityType">
    <table border="1px" style="border-collapse: collapse;">
      <tr>
        <td>键</td>
        <td>值</td>
      </tr>
      <c:forEach items="${entityMap}" var="entity">
      <tr>
        <td><c:out value="${entity.key}"></c:out></td>
        <td><c:out value="${entity.value}"></c:out></td>
      </tr>
      </c:forEach>
    </table>
    </div>
    </div>
    <div id="mapType">
    <div id="addMapType">
    <table border="1px" style="border-collapse: collapse;">
      <tr>
        <td>键</td>
        <td>值</td>
      </tr>
      <c:forEach items="${map}" var ="entry">
         <tr>
        <td><c:out value="${entry.key}"/></td>
        <td><c:out value="${entry.value}"/></td>
      </tr>
      </c:forEach>
    </table>
    </div>
    </div>
    <div id="listType">
    <div id="addListType">
    <table border="1px" style="border-collapse: collapse;">
      <tr>
        <td>序号</td>
        <td>值</td>
      </tr>
      <c:forEach items="${list}" var="ls" varStatus="index">
       <tr>
        <td>${index.count}</td>
        <td><c:out value="${ls}"></c:out></td>
      </tr>
      </c:forEach>
    </table>
    </div>
    </div>
      </td>
      </tr>
       <tr>
      <td> <strong>是否可修改 :</strong></td>
      <td> <label> <c:out value="${config.updatable}"></c:out></label> </td>
      </tr>
       <tr>
      <td> <strong>是否可刪除:</strong></td>
      <td> <label> <c:out value="${config.deletable}"></c:out></label> </td>
      </tr>
      <tr>
      <td> <strong>创建时间:</strong></td>
      <td> <label><fmt:formatDate value="${config.createDate}"/>&nbsp;</label> </td>
      </tr>
      <tr>
      <td> <strong>更新时间:</strong></td>
      <td> <label><fmt:formatDate value="${config.updateDate}"/>&nbsp;</label></td>
      </tr>
      <tr>
      <td> <strong>配置描述:</strong></td>
      <td> <label> <c:out value="${config.description}"></c:out></label></td>
      </tr>
      <tr>
      <td> <strong>所属系统:</strong></td>
      <td> <label>
      <c:forEach var="n" items="${sysNameMap}" >
                     <c:if test="${config.systemId eq n.key}">
                     <c:out value="${n.value}" />
                     </c:if>
                    </c:forEach>
      </label></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>
          <input type="button" class="btn_cancel fw" value="返回" onclick="history.go(-1);"/>
        </td>
      </tr>
    </table> --%>
</body>
</html>
