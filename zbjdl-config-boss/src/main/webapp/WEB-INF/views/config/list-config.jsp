<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" buffer="64kb"%>
<%@ page isELIgnored="false"%>
<%@ include file="/WEB-INF/views/inc/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/taglibs/queryview-tags.tld" prefix="q"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/views/inc/head.jsp"%>
<title>配置管理</title>
<style type="text/css">
.search_con {
	position: relative;
}

.search_con a {
	position: absolute;
	top: 25px;
}

.boss_select {
	border: 1px solid #8f8f8f;
	color: #666;
	font-size: 13px;
	line-height: 22px;
	padding: 3px;
	width: 156px;
}
</style>
<link rel="stylesheet" type="text/css" href="${resourcePath}/boss/common/component/jquery${jqueryVersion}/css/jquery-common-dist.css">


<script type="text/javascript">
    var errorAlert = {
        "error_codeNull": "编码为必填项",
        "error_codeHas": "编号已经存在",
        "error_used": "已被配置使用，不能删除！"};
    var updateDiv = function(u){
      var listURL = "list" + u;
      var saveURL = "save" + u;
      $.get(listURL, function(data){
        $("#dataDiv").html(data);
      });
      $("#divForm").attr("action", saveURL);
      $("#divSubmitBtn").click(function(){
        if($("#divForm").validateSubmit({alert:false})) {
          var c = $("#divCode").val();
          var d = $("#divDescription").val();
          $.post($("#divForm").attr("action"), {code: c, description: d}, function(data){
            if(data.indexOf("error_") >= 0) {
              $("#error_divInput").text(errorAlert[data]);
            }else {
              $("#divCode").val('');
              $("#divDescription").val('');
              $("#error_divInput").text('');
              $("#dataDiv").html(data);
            }
          });
        }
      });
      MessageBox.popup("showDiv", {width:"550px", position:'top'});
      return false;
    };

    var delDiv = function(url){
      MessageBox.confirm("确认删除吗？删除后将不能恢复！", function(){
        $.get(url, function(data){
            if(data.indexOf("error_") >= 0) {
            $("#error_divInput").text(errorAlert[data]);
          }else {
            $("#error_divInput").text('');
            $("#dataDiv").html(data);
          }
          });
      });
      return false;
    };

    var closeDiv = function(){
      MessageBox.unpopup("showDiv");
    };

    var delConfig = function(ci){
      MessageBox.confirm("你确定删除该配置信息吗？删除后不能恢复", function(){
        location.href = "delete?configId=" + ci;
      });
    };
    var addPara = function(){
      var namespace = $("#config_namespace").val();
      var type = $("#config_type").val();
      window.location='add?config_namespace='+namespace+'&config_type='+type;
    };
    
    var downLoadFile = function(){
    	var fileName = $("#fileName").val();
    	if((fileName=='')||(fileName=='nofilename')){
    		alert("请先查询出结果");
    		return;
    	}  	
    	window.location='downloadFile?fileName='+fileName;
    }
    

  </script>
</head>
<body>

	<div id="content_2" class="content_wrapper">
		<div class="content_main">
			<div class="panel panel-default">
				<div class="panel-heading">内容筛选</div>
				<div class="panel-body">
					<form action="${pageContext.request.contextPath}/config/list"
						method="get" id="godownForma" name="godownForma"
						class="cmxform form-horizontal">
						<div class="form-group">
							<label for="customerType" class="control-label col-lg-2">命名空间</label>
							<div class="col-lg-4">
								<select class="form-control" id="config_namespace"
									name="namespace">
									<c:forEach var="n" items="${namespaceList}" varStatus="status">
										<option value="${n.code}"
											<c:if test="${namespace eq n.code}"> selected="selected"</c:if>>${n.code}(${n.description})</option>
									</c:forEach>

								</select>
							</div>
							<label class="col-lg-2 control-label">配置类型</label>
							<div class="col-lg-4">
								<select class="form-control" id="config_type" name="type">
									<c:forEach var="t" items="${typeList}" varStatus="status">
										<option value="${t.code}"
											<c:if test="${type eq t.code}"> selected="selected"</c:if>>${t.code}(${t.description})</option>
									</c:forEach>

								</select>
							</div>
						</div>
						<div class="form-group selectDepart">
							<label for="customerType" class="col-lg-2 control-label">值类型</label>
							<div class="col-lg-4">
								<select class="form-control" name="valueTypeStr">
									<option value="">请选择</option>
									<c:choose>
										<c:when test="${valueTypeStr eq 'MAP'}">
											<option value="MAP" selected="selected"><fmt:message
													key="enum_value_type_MAP" /></option>
										</c:when>
										<c:otherwise>
											<option value="MAP"><fmt:message
													key="enum_value_type_MAP" /></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${valueTypeStr eq 'LIST'}">
											<option value="LIST" selected="selected"><fmt:message
													key="enum_value_type_LIST" /></option>
										</c:when>
										<c:otherwise>
											<option value="LIST"><fmt:message
													key="enum_value_type_LIST" /></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${valueTypeStr eq 'VALUE'}">
											<option value="VALUE" selected="selected"><fmt:message
													key="enum_value_type_VALUE" /></option>
										</c:when>
										<c:otherwise>
											<option value="VALUE"><fmt:message
													key="enum_value_type_VALUE" /></option>
										</c:otherwise>
									</c:choose>
									<c:choose>
										<c:when test="${valueTypeStr eq 'STRUCTURE'}">
											<option value="STRUCTURE" selected="selected"><fmt:message
													key="enum_value_type_STRUCTURE" /></option>
										</c:when>
										<c:otherwise>
											<option value="STRUCTURE"><fmt:message
													key="enum_value_type_STRUCTURE" /></option>
										</c:otherwise>
									</c:choose>
								</select>

							</div>
							<label for="customerType" class="col-lg-2 control-label">配置键</label>
							<div class="col-lg-4">
								<input type="text" class="form-control" id="configKey"
									name="configKey">
							</div>

						</div>

						<div class="form-group">
							<div class="col-lg-12 align_center">
								<button class="btn btn-default button" type="button">清
									除</button>
								<button class="btn btn-primary submit ml_20" type="submit">查
									询</button>
							</div>
						</div>
					</form>
				</div>
			</div>

			<div class="panel panel-default">
				<div class="panel-heading">查询结果</div>
				<div class="panel-body">
					<div class="form-group">
						<div class="col-lg-4">
							<a href="${pageContext.request.contextPath}/config/add">
								<button class="btn btn-primary submit" type="button">
									新增<span class="content_blank_10"></span><span
										class="glyphicon glyphicon-plus"></span>
								</button>
							</a>
						</div>
					</div>
					<!-- 结果列表 -->
					<div class="col-lg-12">
						<q:table queryService="queryService" queryKey="queryConfig"
							formId="godownForma" class="table table-striped table-bordered"
							pageSize="20">
							<q:nodata>无符合条件的记录</q:nodata>
							<q:column title="序号" value="${_rowstatus.globalIndex}" />
<%-- 							<q:column title="系统">
								<c:forEach var="n" items="${sysNameMap}">
									<c:if test="${system_id eq n.key}">
										<c:out value="${n.value}" />
									</c:if>
								</c:forEach>

							</q:column>
 --%>							<q:column title="配置键" escapeHtml="false">
								<a href="detail?configId=${id}"><c:out value="${config_key}" />&nbsp;</a>
							</q:column>
							<q:column title="值类型" escapeHtml="false" width="80px;">
								<fmt:message key="enum_value_type_${value_type}" />
                            </q:column>
							<q:column title="值数据类型" width="80px">
							   <fmt:message key="enum_value_data_type_${value_data_type}" />
							
							</q:column>
							<q:column title="配置值">
								<c:choose>
									<c:when test="${value_type eq 'MAP'}">
										<fmt:message key="enum_value_type_MAP" />
                                    </c:when>
									<c:when test="${value_type eq 'LIST'}">
										<fmt:message key="enum_value_type_LIST" />
                                    </c:when>

									<c:when test="${value_type eq 'STRUCTURE'}">
										<fmt:message key="enum_value_type_STRUCTURE" />
                                    </c:when>
									<c:otherwise>
                                         ${value}          
                                    </c:otherwise>
								</c:choose>


							</q:column>
							<q:column title="描述" value="${description}" />
							<q:column title="最后修改时间">
								<fmt:formatDate value="${update_date_time}"
									pattern="yyyy-MM-dd HH:mm:ss" />
							</q:column>
							<q:column title="操作" escapeHtml="false" width="100px">
								<c:if test="${updatable>0}">
									<a class="margr4" href="edit?configId=${id}">修改</a>
								</c:if>
								<c:if test="${deletable>0}">
									<a class="margr4" href="javascript:void(0);"
										onclick="delConfig('${id}');">删除</a>
								</c:if>
								<a class="margr4" href="copyEdit?configId=${id}" onclick="">复制</a>
            &nbsp;
                        </q:column>

						</q:table>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 提示 -->
	<div id="showDiv" style="display: none;">
		<div id="error_divInput"></div>
		<form id="divForm" action="#">
			<div class="input_cont" style="border: none;">
				<ul>
					<li><label class="text_tit" for="titd">编号：</label> <input
						class="input_text" type="text" id="divCode" style="width: 185px"
						class="input_text" label="编号" req="true" len="{min:1, max:50}"
						name="divInput" /></li>
					<li><label class="text_tit" for="titd">描述：</label> <textarea
							class="textfield" id="divDescription" class="textfield"></textarea>
					</li>
				</ul>
			</div>
			<div class="submitForInfo" style="padding: 0 0 0 0">
				<div class="btn">
					<input id="divSubmitBtn" type="button" class="btn_sure fw"
						value="提交"> <input onclick="closeDiv();" type="button"
						class="btn_cancel fw" value="关闭" />
				</div>
			</div>
		</form>
		<div id="dataDiv"></div>
	</div>
	<!-- 提示结束 -->
</body>
</html>
