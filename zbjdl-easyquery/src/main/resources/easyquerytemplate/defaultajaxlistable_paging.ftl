<#setting number_format="0">
<div ${pagingBean.pagingdivhtml!""}>
        <dl ${pagingBean.pagingdlhtml!""}>
        	<dd>共 ${queryForm.totalPage}页 ${queryForm.totalCount}条</dd>
        	<dd>
            	<select ${pagingBean.pagingselecthtml!""} onchange='_ajax_gotoPage("${tableBean.formId}","${tableBean.formId}","${tableBean.ajaxDiv}","${tableBean.queryUrl}", this.options[this.selectedIndex].value)'>
                	<#assign x=queryForm.totalPage>
									<#list 1..x as i>
										<option value="${i}"  <#if queryForm.currentPage==i>selected</#if>>${i}</option>
									</#list>
                </select>
            </dd>
            <#if queryForm.hasPrevious()>
            <dd><a ${pagingBean.paginglinkhtml!""} href='javascript:_returnpage()' onclick='_ajax_gotoPage("${tableBean.formId}","${tableBean.formId}","${tableBean.ajaxDiv}","${tableBean.queryUrl}",${queryForm.getFirstPage()})' >首页</a></dd>
            <dd><a ${pagingBean.paginglinkhtml!""} href='javascript:_returnpage()' onclick='_ajax_gotoPage("${tableBean.formId}","${tableBean.formId}","${tableBean.ajaxDiv}","${tableBean.queryUrl}",${queryForm.getPreviousPage()})' >上一页</a></dd>
           	<#else>
           		<dd>首页</dd>
           		<dd>上一页</dd>
           	</#if>
           	<#if queryForm.hasNext()>
            <dd><a ${pagingBean.paginglinkhtml!""} href='javascript:_returnpage()' onclick='_ajax_gotoPage("${tableBean.formId}","${tableBean.formId}","${tableBean.ajaxDiv}","${tableBean.queryUrl}",${queryForm.getNextPage()})' >下一页</a></dd>
            <dd><a ${pagingBean.paginglinkhtml!""} href='javascript:_returnpage()' onclick='_ajax_gotoPage("${tableBean.formId}","${tableBean.formId}","${tableBean.ajaxDiv}","${tableBean.queryUrl}",${queryForm.getLastPage()})' >尾页</a></dd>
            <#else>
           		<dd>下一页</dd>
           		<dd>尾页</dd>
            </#if>
        </dl>
</div>