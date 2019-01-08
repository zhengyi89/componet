$(document).ready(function() {
	var type = $("#type").val();
	if(type != "detail") {
		var dates = $("#beginTime, #endTime").datepicker({
	    defaultDate: "+1w",
	    numberOfMonths: 1,
	    changeMonth: true,
	    changeYear: true,
	    showButtonPanel: true,
	    showClearButton: true,
	    closeTextType: 'button',
	    onSelect: function(selectedDate) {
	      var option = this.id == "beginTime" ? "minDate" : "maxDate",
	        instance = $(this).data("datepicker"),
	        date = $.datepicker.parseDate(
	          instance.settings.dateFormat ||
	          $.datepicker._defaults.dateFormat,
	          selectedDate, instance.settings );
	      dates.not(this).datepicker( "option", option, date );
	    }
	  });
	}

	$("#updateButton").bind("click",editJob);
	$("#addButton").bind("click",editJob);
	$("#detailButton").bind("click",jumpToUpdate);

	$("#nameMsg").empty();
	$("#beginTimeMsg").empty();
	$("#endTimeMsg").empty();
	$("#expressionMsg").empty();
	$("#jobObjectMsg").empty();
	$("#jobMethodMsg").empty();
//	$("#warnningEmailMsg").empty();
	$("#noneRuntimeoutMsg").empty();
	$("#runningTimeoutMsg").empty();
	$("#redoNumMsg").empty();
//	$("#groupIDMsg").empty();
	$("#systemMsg").empty();

});

function jumpToUpdate() {
	var id = $("#jobId").val();
	window.location.href = "editJob?type=update&id=" + id;
}

function editJob() {
	if(validateNotNull()) {
		var type = $("#type").val();
		var currentName = $("#name").val();
		if(type == "add") {
			validateNameUnique(currentName);
		} else if(type == "update") {
			var oldName = $("#oldName").val();
			if(oldName == currentName) {
				$("#editJobForm").attr("action","updateJob");
				$("#editJobForm").submit();
			} else {
				validateNameUnique(currentName);
			}
		}
	} else {
		return false;
	}
}

function validateNameUnique(jobName) {

	$.ajax({
		"url" : "validateNameUnique",
		"type" : "POST",
		"data" : {"name" : jobName},
		"dataType" : "json",
		"complete" : function(jqXHR,status) {
		  var length = $.trim($("#name").val()).length;
      if(length > 10) {
        $("#nameMsg").append(
            "<font color='red'>" +
            "任务名称字符不超过10个！" +
            "</font>"
          );
        return;
      }
			if(status == "success") {
				var jobVO = $.parseJSON(jqXHR.responseText);
				var nameUnique = jobVO.nameUnique;
				var type = $("#type").val();
				if(nameUnique) {
					if(type == "add") {
						$("#editJobForm").attr("action","addJob");
						$("#editJobForm").submit();
					} else if(type == "update") {
						$("#editJobForm").attr("action","updateJob");
						$("#editJobForm").submit();
					}
				} else {
					$("#nameMsg").append(
							"<font color='red'>" +
							"任务名称重复，请重新输入！" +
							"</font>"
						);
				}
			} else {
				console.log(jqXHR.responseText);
				$("#systemMsg").append(
						"<font color='red'>" +
						"系统异常，请稍候重试！" +
						"</font>"
					);
			}
		}
	});

}

function validateNotNull() {

	$("#nameMsg").empty();
	$("#beginTimeMsg").empty();
	$("#endTimeMsg").empty();
	$("#expressionMsg").empty();
	$("#jobObjectMsg").empty();
	$("#jobMethodMsg").empty();
//	$("#warnningEmailMsg").empty();
	$("#noneRuntimeoutMsg").empty();
	$("#runningTimeoutMsg").empty();
	$("#redoNumMsg").empty();
//	$("#groupIDMsg").empty();
	$("#systemMsg").empty();

	var name = $("#name").val();

	var beginTime = $("#beginTime").val();
	var endTime = $("#endTime").val();
	var expression = $("#expression").val();
	var jobObject = $("#jobObject").val();
	var jobMethod = $("#jobMethod").val();
//	var warnningEmail = $("#warnningEmail").val();
	var noneRuntimeout = $("#noneRuntimeout").val();
	var runningTimeout = $("#runningTimeout").val();
	var redoNum = $("#redoNum").val();
//	var groupID = $("#groupID").val();
  var regg = /^\d+(\.\d+)?$/;
	var pass = true;

	if(name == "" || name == null) {
		$("#nameMsg").append(
				"<font color='red'>" +
				"请输入任务名称！" +
				"</font>"
			);
		pass = false;
	}

	if(length > 10) {
    $("#nameMsg").append(
        "<font color='red'>" +
        "任务名称字符不超过10个！" +
        "</font>"
      );
    pass = false;
  }

	if(beginTime == "" || beginTime == null) {
		$("#beginTimeMsg").append(
				"<font color='red'>" +
				"请输入开始时间！" +
				"</font>"
			);
		pass = false;
	}

	if(endTime == "" || endTime == null) {
		$("#endTimeMsg").append(
				"<font color='red'>" +
				"请输入结束时间！" +
				"</font>"
			);
		pass = false;
	}

	if(expression == "" || expression == null) {
		$("#expressionMsg").append(
				"<font color='red'>" +
				"请输入定时表达式！" +
				"</font>"
			);
		pass = false;
	}

	if(jobObject == "" || jobObject == null) {
		$("#jobObjectMsg").append(
				"<font color='red'>" +
				"请输入JAVA类路径！" +
				"</font>"
			);
		pass = false;
	}

	if(jobMethod == "" || jobMethod == null) {
		$("#jobMethodMsg").append(
				"<font color='red'>" +
				"请输入JAVA方法名！" +
				"</font>"
			);
		pass = false;
	}

//	if(warnningEmail == "" || warnningEmail == null) {
//		$("#warnningEmailMsg").append(
//				"<font color='red'>" +
//				"请输入报警EMail！" +
//				"</font>"
//			);
//		pass = false;
//	}

	if(noneRuntimeout == "" || noneRuntimeout == null) {
		$("#noneRuntimeoutMsg").append(
				"<font color='red'>" +
				"请输入等待超时时间！" +
				"</font>"
			);
		pass = false;
	}
	if(!regg.test(noneRuntimeout)){
   $("#noneRuntimeoutMsg").append(
        "<font color='red'>" +
        "输入时间必须为数字！" +
        "</font>"
      );
    pass = false;
  }

	if(runningTimeout == "" || runningTimeout == null) {
		$("#runningTimeoutMsg").append(
				"<font color='red'>" +
				"请输入执行超时时间！" +
				"</font>"
			);
		pass = false;
	}
	if(!regg.test(runningTimeout)){
   $("#runningTimeoutMsg").append(
        "<font color='red'>" +
        "输入时间必须为数字！" +
        "</font>"
      );
    pass = false;
  }

	if(redoNum == "" || redoNum == null) {
		$("#redoNumMsg").append(
				"<font color='red'>" +
				"请输入失败重做次数！" +
				"</font>"
			);
		pass = false;
	}
	if(!regg.test(redoNum)){
	  $("#redoNumMsg").append(
        "<font color='red'>" +
        "失败次数必须为数字！" +
        "</font>"
      );
    pass = false;
	}
//	if(groupID == "" || groupID == null) {
//		$("#groupIDMsg").append(
//				"<font color='red'>" +
//				"请输入执行群组ID！" +
//				"</font>"
//			);
//		pass = false;
//	}

	return pass;
}