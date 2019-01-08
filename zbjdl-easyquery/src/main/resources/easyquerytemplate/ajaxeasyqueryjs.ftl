
<#if _firstquery>
<script>
if (window.ActiveXObject && !window.XMLHttpRequest){
	window.XMLHttpRequest = function() {
		var MSXML = ['Msxml2.XMLHTTP.5.0','Msxml2.XMLHTTP.4.0','Msxml2.XMLHTTP.3.0','Msxml2.XMLHTTP','Microsoft.XMLHTTP'];
		for (var i = 0; i < msxmls.length; i++) {
			try {
				return new ActiveXObject(msxmls[i]);
			}catch (e) {}
		}
		return null;
	};
}
function _XMLHttpRequest(){
	this.xhr=new XMLHttpRequest();
}
_XMLHttpRequest.prototype.request=function(method,url,content,callback){
	this.xhr.open(method,url);
	this.xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	var xmlreq = this.xhr; 
	this.xhr.onreadystatechange = function(){
		if(xmlreq.readyState==4 && xmlreq.status==200){callback(xmlreq)}
  };
	this.xhr.send(content);
}
function _easyquery_ajaxcall(formId, queryDivId, queryUrl, page, orderby, isAsc){
	var params = _easyquery_serialform(formId);
	if(page){
		params['_cpage'] = page ;
	}
	if(orderby){
		params['_orderby'] = orderby;
		if(isAsc!=null && isAsc!=undefined){
			params['_asc'] = isAsc ;
		}
	}
	var content = _easyquery_toquerystring(params);
	new _XMLHttpRequest().request('POST', queryUrl,content, function(xmlreq){
		var query_div = document.getElementById(queryDivId);
		if(query_div){
				query_div.innerHTML = xmlreq.responseText;
		} 
		_eval_ajaxresponsejs(xmlreq.responseText);
	});
}
function _easyquery_serialform(formid){
	var elem = document.getElementById(formid);
	var params = new Object();
  for(var i = 0; i < elem.elements.length; i++){
   	params[elem.elements[i].name] = elem.elements[i].value;
  }
  return params;
}
function _easyquery_toquerystring(obj){
 var params = ''; 
 for(var p in obj){
  	if(p != ''){
  		params  +=  p + "=" + encodeURIComponent(obj[p]) + "&";
  	}
 }
 if(params.length>0){
   	params=params.substring(0,params.length-1);
 }
 return params;
}

function _easyquery_registajaxform(formid, queryDivId, queryUrl, autoQuery){
	var oldonload = window.onload;
	window.onload = function(){
	if(oldonload){oldonload()};
	var elem = document.getElementById(formid); 
	elem.action="";
	var ori_onsubmit = elem.onsubmit;
		elem.onsubmit = function(){
		 if(ori_onsubmit){
		  	ori_onsubmit();
		 }
		 _easyquery_ajaxcall(formid, queryDivId, queryUrl);
		 return false;
		}
		if(autoQuery){
			elem.onsubmit();
		}
  }
}

function _eval_ajaxresponsejs(ajaxLoadedData){
var regDetectJs = /<script(.|\n)*?>(.|\n|\r\n)*?<\/script>/ig;
var jsContained = ajaxLoadedData.match(regDetectJs);
if(jsContained) {
	var regGetJS = /<script(.|\n)*?>((.|\n|\r\n)*)?<\/script>/im;
	var jsNums = jsContained.length;
	for (var i=0; i<jsNums; i++) {
		var jsSection = jsContained[i].match(regGetJS);
		if(jsSection[2]) {
			if(window.execScript) {
				window.execScript(jsSection[2]);
			} else {
				window.eval(jsSection[2]);
			}
		}
	}
}
}
function _ajax_querysort(formId, queryKey, queryDivId, queryUrl, orderBy, asc){
	var page = 1;
	if(_ajax_easyquery_pageinfos && _ajax_easyquery_pageinfos[queryKey]){
			page =  _ajax_easyquery_pageinfos[queryKey].page;
  }
  _easyquery_ajaxcall(formId, queryDivId, queryUrl, page, orderBy, asc);
}

function _ajax_gotoPage(formId, queryKey, queryDivId, queryUrl, page){
	var orderBy;
  var asc;
 	if(_ajax_easyquery_pageinfos && _ajax_easyquery_pageinfos[queryKey]){
		orderBy =  _ajax_easyquery_pageinfos[queryKey].orderby;
		if(orderBy){
			 asc  = _ajax_easyquery_pageinfos[queryKey].asc;
		}
  }
  _easyquery_ajaxcall(formId, queryDivId, queryUrl, page, orderBy, asc);
}
function _returnpage(){
}

function _easyquerypageinfo(){
	this.page = null; 
 	this.orderby = null;
	this.asc = null;
}
var _ajax_easyquery_pageinfos = new Object();
</script>
</#if>
<script>
 _easyquery_registajaxform('${formId}','${ajaxDiv}','${queryUrl}',${autoQuery?string});
</script>
