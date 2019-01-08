<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="64kb"%>
<%@ include file="/WEB-INF/views/inc/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
  <%@ include file="/WEB-INF/views/inc/head.jsp"%>
  <title>修改配置</title>
  <script type="text/javascript">
  var configNamespace = "<c:out value='${config.namespace}'/>";
  var configType = "<c:out value='${config.type}'/>";
  var srcConfigKey = "<c:out value='${config.configKey}'/>";
  var valueType = "${config.valueTypeStr}";

    var changeDateType = function(obj, selected) {
      if(valueType == 'VALUE') {
        $("input[name='config.value']").attr("req", "true");
      }else if(valueType == 'MAP') {
        $("input[name='mapValue']").attr("req", "true");
      }else if(valueType == 'LIST') {
        $("input[name='listValue']").attr("req", "true");
      }else {
        obj.attr("req", "true");
      }
      obj.removeAttr('disabled').show().next("select").remove();
      if(selected == 'INTEGER') {
        obj.attr('datatype', 'int');
    }else if(selected == 'BOOLEAN') {
      var $select = $("<select class='select' name='test'><option>true</option><option>false</option></select>");
      //obj.removeAttr('datatype').attr('req', 'false').attr("disabled", "disabled").hide();
      obj.removeAttr('datatype').removeAttr('req').attr("disabled", "disabled").hide();
      obj.each(function(){
        var bool = $(this).attr('bool');
          if(bool == "false") {
            bool == "false";
          }else {
            bool = "true";
          }
        var $selectClone = $select.clone();
        $selectClone.attr('name', $(this).attr('name')).find("option:contains('" + bool + "')").attr('selected', 'selected');
        $(this).after($selectClone);
      });
    }else if(selected == 'DOUBLE') {
      obj.attr('datatype', 'float');
    }else if(selected == 'DATE') {
      obj.attr('datatype', 'date');
    }else if(selected == 'DATETIME') {
      obj.attr('datatype', 'datetime');
    }else {
      obj.removeAttr('datatype');
    }
    };

    var addTr = function($tbl, removeDatatype) {
      var deleteA = $("<a href='#' onclick='deleteTr(this)'>删除</a>");
      var $tr = $tbl.find("tr:first").next().clone();
      $tr.find("td:last").empty().append(deleteA);
      $tr.find("input:text").val('');
      if(removeDatatype) {
        $tr.find("input:text").removeAttr('datatype').removeAttr('bool').removeAttr('disabled').attr("req", "true").show().next("select").remove();;
      }
      $tr.find('option').removeAttr('selected');
      $tbl.append($tr);
    };

    var deleteTr = function(src) {
      var $tr = $(src).parent().parent();
      $tr.remove();
    };

    var itemTypeChange = function(src){
      var val = src.value;
      var $this = $(src);
      var $obj = $this.parent().parent().find("input[name='itemValue']");
    changeDateType($obj, val);
    };

    var keyOnly = function($obj) {
      var key = {};
      var result = true;
      $obj.each(function(){
        var val = $.trim($(this).val()).toLowerCase();
        if(key[val]) {
          result = false;
          return false;
        }
        key[val] = true;
      });
      return result;
    };

  $(function(){
    $('#btn_submit').click(function(){
        $('#btn_submit').attr('disabled', 'disabled');
        if($('#save_form').validateSubmit({})) {
          var key = true;
          if(valueType == 'MAP') {
            key = keyOnly($("input[name='mapKey']"));
            }else if(valueType == 'STRUCTURE') {
              key = keyOnly($("input[name='itemKey']"));
            }
          if(!key) {
            alert("不能存在两个相同的键");
            $('#btn_submit').removeAttr('disabled');
            return;
          }
          var configSystemId = $.trim($("#config_systemId").val());
          if(configSystemId == '000'){
            alert("请选择系统");
            $('#btn_submit').removeAttr('disabled');
            return;
          }
          var configKey = $.trim($("#config_configKey").val());
          if(configKey == srcConfigKey) {
            $('#save_form').submit();
            return;
          }
          $.post('only', {'config.namespace': configNamespace, 'config.type': configType, 'config.configKey': configKey}, function(data) {
            if($.trim(data) == "true") {
              $('#save_form').submit();
            }else {
              alert("命名空间下配置键已经存在！");
            }
          });
        }
        $('#btn_submit').removeAttr('disabled');
      });

      $('#listMapType').change(function(){
        var val = $(this).find("option:selected").val();
        changeDateType($("input[name='config.value'],input[name='mapValue'],input[name='listValue']"), val);
      });

      $("a[name='addTr_a']").click(function(){
        var $tbl = $(this).parent().parent().parent();
        addTr($tbl, $(this).attr('rmdatatype'));
      });
      <c:choose>
        <c:when test="${config.valueTypeStr eq 'STRUCTURE'}">
          $("select[name='itemType']").change();
        </c:when>
        <c:otherwise>
          $('#listMapType').change();
        </c:otherwise>
      </c:choose>
  });

  </script>
  </head>
<body>

    <div id="content_2" class="content_wrapper">
        <div class="content_main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="fw">更新配置</h3>
                </div>
                <div class="panel-body">
                    <form id="postForm" id="save_form" action="save" method="post"
                        class="cmxform form-horizontal">
                        <input name="config.id" type="hidden"  value="${config.id}" />

<!--                         <div class="col-lg-offset-2 col-lg-10">
                            <h2 class="title">任务信息</h2>
                        </div> -->
                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">命名空间</label>
                            <div class="col-lg-6">

                                <select  id="config_namespace" name="config.namespace" class="form-control">
                                    <c:forEach var="n" items="${namespaceList}" varStatus="status">
                                      <option value="${n.code}" <c:if test="${config.namespace eq n.code}"> selected="selected"</c:if>>${n.code}(${n.description})</option>
                                    </c:forEach>
                                </select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">配置类型</label>
                            <div class="col-lg-6">
                                <select id="config_type" name="config.type" class="form-control">
                                    <c:forEach var="t" items="${typeList}" varStatus="status">
                                        <option value="${t.code}"
                                            <c:if test="${config.type eq t.code}"> selected="selected"</c:if>>${t.code}</option>
                                    </c:forEach>
                                </select>


							</div>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">配置键</label>
                            <div class="col-lg-6">
                                <input type="text" class="form-control" id="config_configKey" name="config.configKey"  value="${config.configKey}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">值的类型</label>
                            <div class="col-lg-6">
								<input name="config.valueTypeStr" value="${config.valueTypeStr}"
									type="hidden" /> <label class="control-label"> <c:choose>
										<c:when test="${config.valueTypeStr eq 'VALUE'}">
											<fmt:message key="enum_value_type_VALUE" />
										</c:when>
										<c:when test="${config.valueTypeStr eq 'MAP'}">
											<fmt:message key="enum_value_type_MAP" />
										</c:when>
										<c:when test="${config.valueTypeStr eq 'LIST'}">
											<fmt:message key="enum_value_type_LIST" />
										</c:when>
										<c:when test="${config.valueTypeStr eq 'STRUCTURE'}">
											<fmt:message key="enum_value_type_STRUCTURE" />
										</c:when>
									</c:choose>
								</label>
							</div>
                        </div>
						<c:if test="${config.valueTypeStr != 'STRUCTURE'}">
							<div class="form-group" id="listMapType_tr">
								<label for="inputPassword3" class="col-lg-3 control-label">值数据类型</label>
								<div class="col-lg-6">
									<select class="form-control" id="listMapType"
										name="config.valueDataTypeStr">
										<option value="STRING"
											<c:if test="${config.valueDataTypeStr eq 'STRING'}">selected="selected"</c:if>><fmt:message
												key="enum_value_data_type_STRING" /></option>
										<option value="INTEGER"
											<c:if test="${config.valueDataTypeStr eq 'INTEGER'}">selected="selected"</c:if>><fmt:message
												key="enum_value_data_type_INTEGER" /></option>
										<option value="BOOLEAN"
											<c:if test="${config.valueDataTypeStr eq 'BOOLEAN'}">selected="selected"</c:if>><fmt:message
												key="enum_value_data_type_BOOLEAN" /></option>
										<option value="DOUBLE"
											<c:if test="${config.valueDataTypeStr eq 'DOUBLE'}">selected="selected"</c:if>><fmt:message
												key="enum_value_data_type_DOUBLE" /></option>
										<option value="DATE"
											<c:if test="${config.valueDataTypeStr eq 'DATE'}">selected="selected"</c:if>><fmt:message
												key="enum_value_data_type_DATE" /></option>
										<option value="DATETIME"
											<c:if test="${config.valueDataTypeStr eq 'DATETIME'}">selected="selected"</c:if>><fmt:message
												key="enum_value_data_type_DATETIME" /></option>
									</select>

								</div>
							</div>
						</c:if>


                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">配置值</label>
							<c:if test="${config.valueTypeStr eq 'VALUE'}">
								<div class="col-lg-6" id="primiterType">
									<!-- 修改布尔型每次保存都会增加双引号的问题 -->
									<textarea class="form-control" name="config.value" rows="1"><c:if test="${config.valueDataTypeStr != 'BOOLEAN'}"> <c:out value='${config.value}' /></c:if><c:if test="${config.valueDataTypeStr eq 'BOOLEAN'}">${config.value}</c:if></textarea>
								</div>
							</c:if>

          <c:if test="${config.valueTypeStr eq 'STRUCTURE'}">
        <div id="entityType">
        <div id="addEntityType">
        <table border="1px" style="border-collapse: collapse;">
          <tr>
            <td>键</td>
            <td>值</td>
            <td>类型</td>
            <td>操作</td>
          </tr>
          <c:forEach items="${entityMap}" var="entity" varStatus="status">
            <c:set var="entityDataType" value="${typeMap[entity.key]}" />
            <tr>
            <td><input class="input_text" type="text" name="itemKey" value="<c:out value='${entity.key}'/>" label="键" req="true" len="{max:100}"/></td>
            <td><input class="input_text" type="text" name="itemValue" <c:if test="${entityDataType != 'BOOLEAN'}">value="<c:out value='${entity.value}'/>"</c:if> label="值" req="true" len="{max:10000}" style="width:250px" <c:if test="${entityDataType eq 'BOOLEAN'}">bool="${entity.value}"</c:if>/></td>
            <td>
              <select class="select" name="itemType" onchange="itemTypeChange(this);">
                <option value="STRING" <c:if test="${entityDataType eq 'STRING'}">selected="selected"</c:if>><fmt:message key="enum_value_data_type_STRING"/></option>
                <option value="INTEGER" <c:if test="${entityDataType eq 'INTEGER'}">selected="selected"</c:if>><fmt:message key="enum_value_data_type_INTEGER"/></option>
                <option value="BOOLEAN" <c:if test="${entityDataType eq 'BOOLEAN'}">selected="selected"</c:if>><fmt:message key="enum_value_data_type_BOOLEAN"/></option>
                <option value="DOUBLE" <c:if test="${entityDataType eq 'DOUBLE'}">selected="selected"</c:if>><fmt:message key="enum_value_data_type_DOUBLE"/></option>
                <option value="DATE" <c:if test="${entityDataType eq 'DATE'}">selected="selected"</c:if>><fmt:message key="enum_value_data_type_DATE"/></option>
                <option value="DATETIME" <c:if test="${entityDataType eq 'DATETIME'}">selected="selected"</c:if>><fmt:message key="enum_value_data_type_DATETIME"/></option>
              </select>
            </td>
            <td>
              <c:choose>
                <c:when test="${status.index == 0}"><a name="addTr_a" href="javascript:void(0);" rmdatatype="true">添加</a></c:when>
                <c:otherwise><a href='#' onclick='deleteTr(this)'>删除</a></c:otherwise>
              </c:choose>
            </td>
           </tr>
          </c:forEach>
        </table>
        </div>
        </div>
          </c:if>
          <c:if test="${config.valueTypeStr eq 'MAP'}">
        <div id="mapType">
        <div id="addMapType">
        <table border="1px" style="border-collapse: collapse;">
          <tr>
            <td>键</td>
            <td>值</td>
            <td>操作</td>
          </tr>
          <c:forEach items="${map}" var="entry" varStatus="status">
             <tr>
            <td><input class="input_text" type="text" name="mapKey" value='<c:out value="${entry.key}"/>' label="键" req="true" len="{max:100}"/></td>
            <td><input class="input_text" type="text" name="mapValue" <c:if test="${config.valueDataTypeStr != 'BOOLEAN'}">value="<c:out value='${entry.value}'/>"</c:if> label="值" req="true" len="{max:10000}" style="width:250px" <c:if test="${config.valueDataTypeStr eq 'BOOLEAN'}">bool="${entry.value}"</c:if>/></td>
            <td>
              <c:choose>
                <c:when test="${status.index == 0}"><a name="addTr_a" href="javascript:void(0);">添加</a></c:when>
                <c:otherwise><a href='#' onclick='deleteTr(this)'>删除</a></c:otherwise>
              </c:choose>
            </td>
          </tr>
          </c:forEach>
        </table>
        </div>
        </div>
          </c:if>
          <c:if test="${config.valueTypeStr eq 'LIST'}">
        <div id="listType">
        <div id="addListType">
        <table border="1px" style="border-collapse: collapse;">
          <tr>
            <td>值</td>
            <td>操作</td>
          </tr>
          <c:forEach items="${list}" var="ls" varStatus="status">
           <tr>
            <td><input class="input_text" type="text" name="listValue" <c:if test="${config.valueDataTypeStr != 'BOOLEAN'}">value="<c:out value='${ls}'/>"</c:if> label="值" req="true" len="{max:10000}" style="width:250px" <c:if test="${config.valueDataTypeStr eq 'BOOLEAN'}">bool="${ls}"</c:if>/></td>
            <td>
              <c:choose>
                <c:when test="${status.index == 0}"><a name="addTr_a" href="javascript:void(0);">添加</a></c:when>
                <c:otherwise><a href='#' onclick='deleteTr(this)'>删除</a></c:otherwise>
              </c:choose>
            </td>
          </tr>
          </c:forEach>
        </table>
        </div>
        </div>
          </c:if>

							


						</div>

                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">是否可修改</label>
                            <div class="col-lg-6">

								<select class="form-control" name="config.updatable">
						            <option value="true" <c:if test="${config.updatable}">selected="selected"</c:if>>是</option>
						            <option value="false" <c:if test="${not config.updatable}">selected="selected"</c:if>>否</option>
								</select>

							</div>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">是否可删除</label>
                            <div class="col-lg-6">

								<select class="form-control" name="config.deletable">
						            <option value="true" <c:if test="${config.deletable}">selected="selected"</c:if>>是</option>
						            <option value="false" <c:if test="${not config.deletable}">selected="selected"</c:if>>否</option>
								</select>

							</div>
                        </div>
						<div class="form-group">
							<label for="inputPassword3" class="col-lg-3 control-label">描述</label>
							<div class="col-lg-6" id="primiterType">
								<textarea class="form-control" name="config.description"> ${config.description}</textarea>
							</div>
						</div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-lg-3 control-label">所属系统</label>
                            <div class="col-lg-6">

								<select class="form-control" id="config_systemId"
									name="config.systemId">
									<c:forEach var="n" items="${sysNameMap}">
										<option value="${n.key}"
											<c:if test="${config.systemId eq n.key}"> selected="selected"</c:if>>${n.value}</option>
									</c:forEach>
								</select>

							</div>
                        </div>

                        <div class="form-group">
                            <div class="col-lg-12 align_center">
                                <button type="submit" class="btn btn-primary submit">保存</button>
                                <button type="button" class="btn btn-default button ml_20"
                                    onclick="history.go(-1);">返回</button>
                            </div>
                        </div>	
					</form>

                </div>
            </div>
        </div>
    </div>
<%-- 

<div class="content_wrapper">
      <div class="Content fontText">
<form id="save_form" action="save" method="post" >
<table width="70%" border="1px"  class="confTable">
  <tr>
    <td>命名空间：</td>
    <td>
          <select class="select" label="命名空间" id="config_namespace" name="config.namespace">
            <c:forEach var="n" items="${namespaceList}" varStatus="status">
            <option value="${n.code}" <c:if test="${config_namespace eq n.code}"> selected="selected"</c:if>>${n.code}(${n.description})</option>
            </c:forEach>
          </select>
    </td>
  </tr>
  <tr>
    <td>配置类型：</td>
    <td>
          <select class="select" id="config_type" name="config.type">
            <c:forEach var="t" items="${typeList}" varStatus="status">
            <option value="${t.code}" <c:if test="${config_type eq t.code}"> selected="selected"</c:if>>${t.code}</option>
            </c:forEach>
          </select>
    </td>
  </tr>

  <tr>
    <td>配置键:</td>
    <td><input id="config_configKey" name=config.configKey class="input_text" type="text" label="配置键" req="true" len="{max:100}" style="width:250px"/></td>
  </tr>
  <tr>
    <td>值的类型:</td>
    <td>
          <select class="select" id="valueTypeSelect" name="config.valueTypeStr" onchange="changeInputType(this)">
        <option value="VALUE"><fmt:message key="enum_value_type_VALUE" /></option>
        <option value="MAP"><fmt:message key="enum_value_type_MAP" /></option>
        <option value="LIST"><fmt:message key="enum_value_type_LIST" /></option>
        <option value="STRUCTURE"><fmt:message key="enum_value_type_STRUCTURE" /></option>
        </select>
        </td>
  </tr>
  <tr id="listMapType_tr">
    <td>值数据类型:</td>
    <td>
          <select class="select" id="listMapType" name="config.valueDataTypeStr">
          <option value="STRING"><fmt:message key="enum_value_data_type_STRING"/></option>
          <option value="INTEGER"><fmt:message key="enum_value_data_type_INTEGER"/></option>
          <option value="BOOLEAN"><fmt:message key="enum_value_data_type_BOOLEAN"/></option>
          <option value="DOUBLE"><fmt:message key="enum_value_data_type_DOUBLE"/></option>
          <option value="DATE"><fmt:message key="enum_value_data_type_DATE"/></option>
          <option value="DATETIME"><fmt:message key="enum_value_data_type_DATETIME"/></option>
      </select>
        </td>
  </tr>
  <tr>
    <td>配置值:</td>
    <td>
    <div id="primiterType">
    <textarea class="textfield" name="config.value" cols="60" rows="2" ></textarea></div>
    <div id="entityType">
    <div id="addEntityType">
    <table border="1px" style="border-collapse: collapse;">
      <tr>
        <td>键</td>
        <td>值</td>
        <td>类型</td>
        <td>操作</td>
      </tr>
      <tr>
        <td><input class="input_text" type="text" name="itemKey" label="键" req="true" len="{max:100}" /></td>
        <td><input class="input_text" type="text" name="itemValue" label="值" req="true" len="{max:10000}"/></td>
        <td>
          <select class="select" name="itemType" onchange="itemTypeChange(this);">
            <option value="STRING"><fmt:message key="enum_value_data_type_STRING"/></option>
            <option value="INTEGER"><fmt:message key="enum_value_data_type_INTEGER"/></option>
            <option value="BOOLEAN"><fmt:message key="enum_value_data_type_BOOLEAN"/></option>
            <option value="DOUBLE"><fmt:message key="enum_value_data_type_DOUBLE"/></option>
            <option value="DATE"><fmt:message key="enum_value_data_type_DATE"/></option>
            <option value="DATETIME"><fmt:message key="enum_value_data_type_DATETIME"/></option>
          </select>
        </td>
        <td><a name="addTr_a" href="javascript:void(0);" rmdatatype='true'>添加</a></td>
      </tr>
    </table>
    </div>
    </div>
    <div id="mapType">
    <div id="addMapType">
    <table border="1px" style="border-collapse: collapse;">
      <tr>
        <td>键</td>
        <td>值</td>
        <td>操作</td>
      </tr>
      <tr id="mapInit">
        <td><input class="input_text" type="text" name="mapKey" label="键" req="true" len="{max:100}"/></td>
        <td><input class="input_text" type="text" name="mapValue" label="值" req="true" len="{max:10000}" style="width:250px"/></td>
        <td><a name="addTr_a" href="javascript:void(0);">添加</a></td>
      </tr>
    </table>
    </div>
    </div>
    <div id="listType">
    <div id="addListType">
    <table border="1px" style="border-collapse: collapse;">
      <tr>
        <td>值</td>
        <td>操作</td>
      </tr>
      <tr id="listInit">
        <td><input class="input_text" type="text" name="listValue" label="值" req="true" len="{max:10000}" style="width:240px"/></td>
        <td><a name="addTr_a" href="javascript:void(0);">添加</a></td>
      </tr>
    </table>
    </div>
    </div>
    </td>
  </tr>
  <tr>
    <td>是否可修改：</td>
    <td><select class="select" name="config.updatable">
      <option value="true">是</option>
      <option value="false">否</option>
    </select></td>
  </tr>
  <tr>
    <td>是否可删除:</td>
    <td><select class="select" name="config.deletable">
      <option value="true">是</option>
      <option value="false">否</option>
    </select></td>
  </tr>
  <tr>
    <td>描述 :</td>
    <td><textarea class="textfield" name="config.description" cols="60" rows="10"></textarea></td>
  </tr>
  <tr>
    <td>所属系统:</td>
    <td>
     <select class="select" id="config_systemId" name="config.systemId">
       <c:forEach var="n" items="${sysNameMap}" >
         <option value="${n.key}" <c:if test="${config.systemId eq n.key}"> selected="selected"</c:if>>${n.value}</option>
       </c:forEach>
     </select>
    </td>
  </tr>

  <tr>
    <td>&nbsp;</td>
    <td>
    <div>
      <input id="btn_submit" type="button" value="添加" class="btn_sure fw"/>
      <input type="button" class="btn_cancel fw" value="返回" onclick="history.go(-1);"/>
    </div>
    </td>
  </tr>
</table>

</form>
</div>
</div> --%>
</body>
</html>
