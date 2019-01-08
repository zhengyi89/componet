<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="64kb"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%-- <link rel="stylesheet" type="text/css" href="${resourcePath}/boss/css/boss-dist.css">
<link rel="stylesheet" type="text/css" href="${resourcePath}/boss/common/component/jquery${jqueryVersion}/css/jquery-common-dist.css">
 --%>
<!-- 兼容jquery1.9 不支持 $.browser，导致组件出错的问题  -->
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery.browser.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/js/boss-dist.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/js/Library-min.js"></script>
<!-- 1.9.x 以上版本的兼容组件 -->
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/lib/jquery-migrate.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/lib/jquery-ui.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/lib/jquery-ui-i18n.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/lib/jquery.bgiframe.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/extend/date/jquery-ui-timepicker-addon.min.js"></script>

<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/extend/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/extend/messagebox/jquery.messagebox.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/extend/select/jquery.suselect.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/extend/area/jquery.areaopt.min.js"></script>
<script type="text/javascript" src="${resourcePath}/boss/common/component/jquery${jqueryVersion}/extend/area/areaopt.data.js"></script>

<link rel="stylesheet" type="text/css" href="${ctxPath}/css/util_boss.css" />
<script type="text/javascript">
/*
 * jQuery.upload v1.0.2
 *
 * Copyright (c) 2010 lagos
 * Dual licensed under the MIT and GPL licenses.
 *
 * http://lagoscript.org
 */
(function($) {

	var uuid = 0;

	$.fn.upload = function(url, data, callback, type) {
		var self = this, inputs, checkbox, checked,
			iframeName = 'jquery_upload' + ++uuid,
			iframe = $('<iframe name="' + iframeName + '" style="position:absolute;top:-9999px" />').appendTo('body'),
			form = '<form target="' + iframeName + '" method="post" enctype="multipart/form-data" />';

		if ($.isFunction(data)) {
			type = callback;
			callback = data;
			data = {};
		}

		checkbox = $('input:checkbox', this);
		checked = $('input:checked', this);
		form = self.wrapAll(form).parent('form').attr('action', url);

		// Make sure radios and checkboxes keep original values
		// (IE resets checkd attributes when appending)
		checkbox.removeAttr('checked');
		checked.attr('checked', true);

		inputs = createInputs(data);
		inputs = inputs ? $(inputs).appendTo(form) : null;

		form.submit(function() {
			iframe.load(function() {
				var data = handleData(this, type),
					checked = $('input:checked', self);

				form.after(self).remove();
				checkbox.removeAttr('checked');
				checked.attr('checked', true);
				if (inputs) {
					inputs.remove();
				}

				setTimeout(function() {
					iframe.remove();
					if (type === 'script') {
						$.globalEval(data);
					}
					if (callback) {
						callback.call(self, data);
					}
				}, 0);
			});
		}).submit();

		return this;
	};

	function createInputs(data) {
		return $.map(param(data), function(param) {
			return '<input type="hidden" name="' + param.name + '" value="' + param.value + '"/>';
		}).join('');
	}

	function param(data) {
		if ($.isArray(data)) {
			return data;
		}
		var params = [];

		function add(name, value) {
			params.push({name:name, value:value});
		}

		if (typeof data === 'object') {
			$.each(data, function(name) {
				if ($.isArray(this)) {
					$.each(this, function() {
						add(name, this);
					});
				} else {
					add(name, $.isFunction(this) ? this() : this);
				}
			});
		} else if (typeof data === 'string') {
			$.each(data.split('&'), function() {
				var param = $.map(this.split('='), function(v) {
					return decodeURIComponent(v.replace(/\+/g, ' '));
				});

				add(param[0], param[1]);
			});
		}

		return params;
	}

	function handleData(iframe, type) {
		var data, contents = $(iframe).contents().get(0);

		if ($.isXMLDoc(contents) || contents.XMLDocument) {
			return contents.XMLDocument || contents;
		}
		data = $(contents).find('body').html();

		switch (type) {
			case 'xml':
				data = parseXml(data);
				break;
			case 'json':
				data = window.eval('(' + data + ')');
				break;
		}
		return data;
	}

	function parseXml(text) {
		if (window.DOMParser) {
			return new DOMParser().parseFromString(text, 'application/xml');
		} else {
			var xml = new ActiveXObject('Microsoft.XMLDOM');
			xml.async = false;
			xml.loadXML(text);
			return xml;
		}
	}

})(jQuery);

</script>