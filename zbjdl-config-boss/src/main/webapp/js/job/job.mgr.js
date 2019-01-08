function doJobOnce(jobId) {
	blocking("任务执行中...");
	var timestamp = (new Date()).valueOf();
	$.ajax({
		"url" : "doJobOnce?timestamp=" + timestamp,
		"type" : "POST",
		"data" : {"jobId" : jobId},
		"dataType" : "json",
		"complete" : function(jqXHR,status) {
			if(status == "success") {
				unblocking();
				
				var jobExecutedInfo = $.parseJSON(jqXHR.responseText);
				if(jobExecutedInfo.success) {
					MessageBox.alert("任务执行成功");
				} else {
					MessageBox.alert("任务执行失败:" + jobExecutedInfo.exceptionMessage);
				}
			} else {
				unblocking();
				MessageBox.alert("系统异常，请稍候重试");
			}
		}
	});
}

function blocking(content) {
	$.blockUI({
		message : content,
		css : {
			border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff'
		}
	});
}

function unblocking() {
	$.unblockUI();
}