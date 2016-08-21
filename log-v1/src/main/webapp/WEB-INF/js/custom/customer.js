/**
 * Created by wk on 2016/7/28.
 */
initFromDate('yyyy', 4);
initToDate('yyyy', 4);
initAnalysisTime();

function timeRangeChange(range) {
	clearDateTime();
	if (range == "month") {
		reInitFromDate('yyyy', 4);
		reInitToDate('yyyy', 4);
	} else if (range == "day") {
		reInitFromDate('yyyy-mm', 3);
		reInitToDate('yyyy-mm', 3);
	} else if (range == "hour") {
		reInitFromDate('yyyy-mm-dd', 2);
		reInitToDate('yyyy-mm-dd', 2);
	}
	reInitAnalysisTime(range);
}

function initFromDate(formate, range) {
	$('#fromDate1').datetimepicker({
		language : 'zh-CN',
		format : formate,
		todayHighlight : 1,
		todayBtn : 1,
		startView : range,
		minView : range, // 选择day期后，不会再跳转去选择时分秒
		autoclose : 1
	// 选择day期后自动关闭
	}).on('changeDate', function(ev) {
		var fromdate = $('#fromDate1').val();
		var todate = $('#toDate1').val();
		if (todate == null || todate == "") {
			$('#fromDate1').datetimepicker('hide');
			return;
		}
		var mfromdate = new Date(fromdate).getTime(); // 毫秒
		var mtodate = new Date(todate).getTime(); // 毫秒
		if (mtodate <= mfromdate) {
			$('#fromDate1').val("")
			alert("结束时间要大于开始时间!");
			return;
		}
	});
}

function reInitFromDate(formate, range) {
	$('#fromDate1').datetimepicker({
		language : 'zh-CN',
		format : formate,
		todayHighlight : 1,
		todayBtn : 1,
		startView : range,
		minView : range, // 选择day期后，不会再跳转去选择时分秒
		autoclose : 1
	// 选择day期后自动关闭
	})
}

function initToDate(formate, range) {
	$('#toDate1').datetimepicker({
		language : 'zh-CN',
		format : formate,
		todayHighlight : 1,
		todayBtn : 1,
		startView : range,
		minView : range, // 选择day期后，不会再跳转去选择时分秒
		autoclose : 1
	// 选择day期后自动关闭
	}).on('changeDate', function(ev) {
		var fromdate = $('#fromDate1').val();
		var todate = $('#toDate1').val();
		if (fromdate == null || fromdate == "") {
			$('#toDate1').val("");
			alert("请填写开始时间!");
			return;
		}
		var mfromdate = new Date(fromdate).getTime(); // 毫秒
		var mtodate = new Date(todate).getTime(); // 毫秒
		if (mtodate <= mfromdate) {
			$('#toDate1').val("");
			alert("结束时间要大于开始时间!");
			return;
		}
	});
}
function reInitToDate(formate, range) {
	$('#toDate1').datetimepicker({
		language : 'zh-CN',
		format : formate,
		todayHighlight : 1,
		todayBtn : 1,
		startView : range,
		minView : range, // 选择day期后，不会再跳转去选择时分秒
		autoclose : 1
	// 选择day期后自动关闭
	})
}

function clearDateTime() {
	$('#fromDate1').datetimepicker('remove');
	$('#toDate1').datetimepicker('remove');
	$('#fromDate1').val("");
	$('#toDate1').val("");
}

function initAnalysisTime() {
	var obj = document.getElementById('analysisTime');
	$("#analysisTime").find("option").remove();
	for (var i = 1; i <= 12; i++) {
		var text = i + "月";
		var val;
		if (i < 10) {
			val = "0" + i;
		} else {
			val = i;
		}
		obj.options.add(new Option(text, val)); // 这个兼容IE与firefox
	}
}

function reInitAnalysisTime(range) {
	var obj = document.getElementById('analysisTime');
	if (range == "month") {
		$("#analysisTime").find("option").remove();
		for (var i = 1; i <= 12; i++) {
			var text = i + "月";
			var val;
			if (i < 10) {
				val = "0" + i;
			} else {
				val = i;
			}
			obj.options.add(new Option(text, val)); // 这个兼容IE与firefox
		}
	} else if (range == "day") {
		$("#analysisTime").find("option").remove();
		for (var i = 1; i <= 31; i++) {
			var text = i + "日";
			var val;
			if (i < 10) {
				val = "0" + i;
			} else {
				val = i;
			}
			obj.options.add(new Option(text, val)); // 这个兼容IE与firefox
		}
	} else if (range == "hour") {
		$("#analysisTime").find("option").remove();
		for (var i = 0; i <= 23; i++) {
			var text = i + "点";
			var val;
			if (i < 10) {
				val = "0" + i;
			} else {
				val = i;
			}
			obj.options.add(new Option(text, val)); // 这个兼容IE与firefox
		}
	}
}