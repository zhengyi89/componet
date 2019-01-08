<script type = "text/javascript" >
// 关闭请求参数自动回填
// var query_autoFillParams = false;
// 关闭自动设置时间参数
// var query_autoFillDate = false;
// 起始时间定义
// var query_startFillDay = -30;
$(function () {
    // 请求参数自动回填
    if (typeof query_autoFillParams === 'undefined' || query_autoFillParams) {
        revertRequestParams();
    }
    $(function () {
        $(function () {
            var url = decodeURIComponent(location.search);
            if (typeof query_autoFillDate === 'undefined' || query_autoFillDate) {
                var needCheck = (url.length === 0);
                if (!needCheck) {
                    var queryString = url.substring(url.indexOf("?") + 1);
                    var re = /(\._cpage)/i;
                    needCheck = !re.test(queryString);
                }
                if (needCheck) {
                    $("input.hasDatepicker").each(function (index, item) {
                        if ("" === $(item).val()) {
                            var inputName = $(item).attr("name").toLowerCase();
                            if (inputName.indexOf("start") >= 0 || inputName.indexOf("from") >= 0) {
                                if (typeof query_startFillDay === 'undefined') {
                                    $(item).datepicker("setDate", "-7d");
                                } else {
                                    $(item).datepicker("setDate", query_startFillDay+"d");
                                }
                            } else {
                                $(item).datepicker("setDate", 0);
                            }
                        }
                    });
                }
            }
        });
    });
});
function revertRequestParams() {
    // 获取url中"?"符后的字串
    var url = decodeURIComponent(location.search);
    // 如果链接没有参数，或者链接中不存在我们要获取的参数，直接返回空
    if (url.length === 0) {
        return;
    }

    var queryString = url.substring(url.indexOf("?") + 1);

    // 分离参数对 ?key=value&key2=value2
    var parameters = queryString.split("&");

    var pos, paraName, paraValue;
    for (var i = 0; i < parameters.length; i++) {
        // 获取等号位置
        pos = parameters[i].indexOf('=');
        if (pos === -1) {
            continue;
        }

        // 获取name 和 value
        paraName = parameters[i].substring(0, pos);
        if (paraName === 'CSRFToken'
            || paraName === 'boss_sso_token'
            || paraName === 'belongSystem'
            || paraName === '_menuId'
            || paraName === '_firstMenuId') {
            continue;
        }
        paraValue = parameters[i].substring(pos + 1);
        paraValue = unescape(paraValue.replace(/\+/g, " "));
        if (paraValue === '') {
            continue;
        }

        // 还原选中项
        if ("" != paraValue) {
            if ($("input:radio[name='" + paraName + "'][value='" + paraValue + "']").size() > 0) {
                $("input:radio[name='" + paraName + "'][value='" + paraValue + "']").attr({
                    checked: true
                });
            } else if ($("select[name='" + paraName + "'] option[value='" + paraValue + "']").size() > 0) {
                $("select[name='" + paraName + "'] option[value='" + paraValue + "']").attr("selected", "selected");
            } else if ($("input[name='" + paraName + "']").size() > 0) {
                $("input[name='" + paraName + "']").attr("value", paraValue);
            }
        }
    }
}
function addHiddenParam(form, pname, pvalue) {
    var inputEle = form.elements[pname];
    if (inputEle) {
        inputEle.value = pvalue;
        return;
    }
    var inputNode = document.createElement("input");
    inputNode.setAttribute("type", 'hidden');
    inputNode.setAttribute("name", pname);
    inputNode.setAttribute("value", pvalue);
    form.appendChild(inputNode);
}
function querysort(formId, queryId, orderBy, asc) {
    var form = document.getElementById(formId);
    if (!form) {
        return;
    }
    addHiddenParam(form, "queryId", queryId);
    addHiddenParam(form, queryId + "._orderby", orderBy);
    addHiddenParam(form, queryId + "._asc", asc);
    for (var query in _default_easyquery_pageinfos) {
        if (query != queryId) {
            var pageinfo = _default_easyquery_pageinfos[query];
            addHiddenParam(form, "queryId", query);
            addHiddenParam(form, query + "._cpage", pageinfo.page);
            if (pageinfo.orderby) {
                addHiddenParam(form, query + "._orderby", pageinfo.orderby);
                addHiddenParam(form, query + "._asc", pageinfo.asc);
            }
        }
    }
    if (form.onsubmit && !form.onsubmit()) {
        return;
    }
    form.submit();
}
function gotoPage(formId, queryId, page, maxPage) {
    if (maxPage) {
        var pnumber = Number(page);
        if (!pnumber || pnumber > maxPage) {
            page = '1';
        }
    }
    var form = document.getElementById(formId);
    if (!form) {
        return;
    }
    addHiddenParam(form, "queryId", queryId);
    addHiddenParam(form, queryId + "._cpage", page);
    for (var query in _default_easyquery_pageinfos) {
        var pageinfo = _default_easyquery_pageinfos[query];
        if (query != queryId) {
            addHiddenParam(form, "queryId", query);
            addHiddenParam(form, query + "._cpage", pageinfo.page);
        }
        if (pageinfo.orderby) {
            addHiddenParam(form, query + "._orderby", pageinfo.orderby);
            addHiddenParam(form, query + "._asc", pageinfo.asc);
        }
    }
    if (form.onsubmit && !form.onsubmit()) {
        return;
    }
    form.submit();
}
function _easyquerypageinfo() {
    this.page = null;
    this.orderby = null;
    this.asc = null;
}
var _default_easyquery_pageinfos = new Object();
function sendRequest(formId){
	document.getElementById(formId).submit();
}
</script>