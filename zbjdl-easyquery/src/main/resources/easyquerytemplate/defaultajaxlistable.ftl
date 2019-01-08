<script>
	var _ajax_page_info = new  _easyquerypageinfo();
	<#if queryForm.currentPage??>_ajax_page_info.page = ${queryForm.currentPage};</#if>
	<#if queryForm.orderby??>
		_ajax_page_info.orderby = '${queryForm.orderby}';
		 <#if queryForm.asc??>
		  	_ajax_page_info.asc = '${queryForm.asc?string}';
			</#if>
	</#if>
	_ajax_easyquery_pageinfos['${tableBean.formId}'] = _ajax_page_info;
</script>
<div class="table_outer">
 <table ${tableBean.html!""}>
 	<tr ${rowBean.titlehtml!""}>
 	<#list columnBeans as columnBean>
 		<td ${columnBean.titleHtml}>${columnBean.title}<#if columnBean.sortable><a href="javascript:_returnpage()" onclick='_ajax_querysort("${tableBean.formId}","${tableBean.formId}", "${tableBean.ajaxDiv}","${tableBean.queryUrl}", "${columnBean.orderBy!""}",true)'>升序</a>/<a href="javascript:_returnpage()" onclick='_ajax_querysort("${tableBean.formId}","${tableBean.formId}", "${tableBean.ajaxDiv}","${tableBean.queryUrl}", "${columnBean.orderBy!""}",false)'>降序</a></#if></td>
 	</#list>
 	</tr>
  <#list queryForm.queryResult.data as columnData>
   	${stack.push(columnData)}
 		<tr ${stack.buildHtml(rowBean)!""}>
 			<#list columnBeans as columnBean>
  			<td ${stack.buildHtml(columnBean)!""}>${stack.findAllValue(columnBean.value,columnBean.body,false)!""}</td>
  		</#list>
  	</tr>
  	${stack.pop()}
  </#list>
 </table>
</div>
<script>
    //设置 table_outer 的宽度
    $(document).ready(function(){
        $(".table_outer").width($(".content_table").width());
    });
    $(window).resize(function(){
        $(".table_outer").width($(".content_table").width());
    });
</script>