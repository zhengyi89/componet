<#setting number_format="0">

<div class="form-group">
  <div class="col-lg-4">
    第${queryForm.getCurrentPage()}页 共${queryForm.totalPage}页 ${queryForm.totalCount}条  
  </div>
  <div class="col-lg-8">
    <ul class="pagination pull-right">

    <#if queryForm.hasPrevious()>
    <li><a ${pagingBean.paginglinkhtml!""} href='javascript:gotoPage("${pagingBean.formId}","${queryForm.queryKey}",${queryForm.getFirstPage()})' >首页</a></li>
    <li><a ${pagingBean.paginglinkhtml!""} href='javascript:gotoPage("${pagingBean.formId}","${queryForm.queryKey}",${queryForm.getPreviousPage()})' >上一页</a></li>
    <#else>
        <li>首页</li>
        <li>上一页</li>
    </#if>

    <#if queryForm.hasNext()>
        <li><a ${pagingBean.paginglinkhtml!""} href='javascript:gotoPage("${pagingBean.formId}","${queryForm.queryKey}",${queryForm.getNextPage()})' >下一页</a></li>
        <li><a ${pagingBean.paginglinkhtml!""} href='javascript:gotoPage("${pagingBean.formId}","${queryForm.queryKey}",${queryForm.getLastPage()})' >尾页</a></li>
    <#else>
        <li>下一页</li>
        <li>尾页</li>
    </#if>
    <#if queryForm.totalPage gt 1>
        <li>
        <a href="javascript:void(0)">前往第<input size="5"  style="margin-bottom:0;margin-top:0;width:30px;height:15px;direction:rtl;font-size:10px;" id="_pageinput_${pagingBean.formId}" type="text" onkeydown='if(event.keyCode==13) gotoPage("${pagingBean.formId}","${queryForm.queryKey}",this.value, ${queryForm.getLastPage()});'/>页</a>
        </li>
        <li><a href="#" onclick='gotoPage("${pagingBean.formId}","${queryForm.queryKey}",document.getElementById("_pageinput_${pagingBean.formId}").value, ${queryForm.getLastPage()});return false;'>转到</a></li>
    </#if>

    </ul>
  </div>
</div>

