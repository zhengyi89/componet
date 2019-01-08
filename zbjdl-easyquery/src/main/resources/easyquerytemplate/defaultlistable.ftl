<script>
	var _query_page_info = new _easyquerypageinfo();
	<#if queryForm.currentPage??>_query_page_info.page = ${queryForm.currentPage};</#if>
	<#if queryForm.orderby??>
		_query_page_info.orderby = '${queryForm.orderby}';
		<#if queryForm.asc??>_query_page_info.asc = '${queryForm.asc?string}';</#if>
	</#if>
	_default_easyquery_pageinfos['${queryForm.queryKey}'] = _query_page_info;
</script>
<div class="table_outer">
	<table ${tableBean.html!""}>
	 	<tr ${rowBean.titlehtml!""}>
	 	<#list columnBeans as columnBean>
	 		<td ${columnBean.titleHtml}>${columnBean.title} <#if columnBean.sortable><a href='javascript:querysort("${tableBean.formId}","${queryForm.queryKey}","${columnBean.orderBy!""}",true)'>升序</a>/<a href='javascript:querysort("${tableBean.formId}","${queryForm.queryKey}","${columnBean.orderBy!""}",false)'>降序</a></#if></td>
	 	</#list>
	 	</tr>
	  <#if queryForm.queryResult??>
	  <#list queryForm.queryResult.data as listable>
	   	${stack.push(listable)}
	 	<tr ${stack.buildHtml(rowBean)!""}>
	 		<#list columnBeans as columnBean>
	  			<td ${stack.buildHtml(columnBean)!""}><#if columnBean.value??>${stack.findValue(columnBean.value,columnBean.escape)!""}<#elseif columnBean.body??>${stack.parseFtlStr(columnBean.body,columnBean.escape)!""}</#if></td>
	  		</#list>
	  	</tr>
	  	${stack.pop()}
	  </#list>
	  </#if>
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