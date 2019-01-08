<script>
function addHiddenParam(form, pname, pvalue){
	var inputNode=document.createElement("input");
 	inputNode.setAttribute("type",'hidden');
    inputNode.setAttribute("name", pname);
    inputNode.setAttribute("value", pvalue);
    form.appendChild(inputNode);
}

function querysort(formId,queryKey, orderBy, asc){
	var form = document.getElementById(formId);
	if(!form){
  		return;
 	}
 	addHiddenParam(form, "queryKey", queryKey);
	addHiddenParam(form, queryKey+"._orderby", orderBy);
    addHiddenParam(form, queryKey+"._asc", asc);
    for(var query in _default_easyquery_pageinfos){
 		if(query!=queryKey){
 			var pageinfo = _default_easyquery_pageinfos[query];
 			addHiddenParam(form, "queryKey", query);
 			addHiddenParam(form, query+"._cpage", pageinfo.page);
 			if(pageinfo.orderby){
 				addHiddenParam(form, query+"._orderby", pageinfo.orderby);
 				addHiddenParam(form, query+"._asc", pageinfo.asc);
 			}
 		}
 	}
    if(form.onsubmit && !form.onsubmit()){
    	return;
    }
    form.submit();  
	}
	
function gotoPage(formId, queryKey, page, maxPage){
	if(maxPage){
		var pnumber = Number(page);
		if(!pnumber || pnumber>maxPage){
			page = '1';
		}
	}
	var form = document.getElementById(formId);
	if(!form){
  		return;
 	}
	addHiddenParam(form, "queryKey", queryKey);
 	addHiddenParam(form, queryKey+"._cpage", page);   
    for(var query in _default_easyquery_pageinfos){
 		var pageinfo = _default_easyquery_pageinfos[query];
 		if(query!=queryKey){
 			addHiddenParam(form, "queryKey", query);
 			addHiddenParam(form, query+"._cpage", pageinfo.page);
 		}
 		if(pageinfo.orderby){
 			addHiddenParam(form, query+"._orderby", pageinfo.orderby);
 			addHiddenParam(form, query+"._asc", pageinfo.asc);
 		}
 	}
    if(form.onsubmit && !form.onsubmit()){
    	return;
    }
    form.submit();   
	}
	function _easyquerypageinfo(){
		this.page = null; 
 		this.orderby = null;
		this.asc = null;
	}
	var _default_easyquery_pageinfos = new Object();
</script>