/**
菜单选中效果
**/
$(document).ready(function(){
	initLeftMenu();
});

function initLeftMenu(){
	$(".list-group a").bind('click',function(){
		$(this).parent().find("a").removeClass('active');
		$(this).addClass('active');		
	});
}
/**
 * 把普通时间转为用户友好的时间
 */
var minute = 1000 * 60;
var hour = minute * 60;
var day = hour * 24;
var halfamonth = day * 15;
var month = day * 30;
function getDateDiff(dateTimeStamp){
var result;
dateTimeStamp = Date.parse(dateTimeStamp.replace(/-/gi,"/"));
var now = new Date().getTime();
var diffValue = now - dateTimeStamp;
if(diffValue < 0){
 //若日期不符则弹出窗口告之
 //alert("结束日期不能小于开始日期！");
}
var monthC =diffValue/month;
var weekC =diffValue/(7*day);
var dayC =diffValue/day;
var hourC =diffValue/hour;
var minC =diffValue/minute;
if(monthC>=1){
	result="发表于" + parseInt(monthC) + "个月前";
}else if(weekC>=1){
	result="发表于" + parseInt(weekC) + "周前";
}else if(dayC>=1){
	result="发表于"+ parseInt(dayC) +"天前";
}else if(hourC>=1){
	result="发表于"+ parseInt(hourC) +"个小时前";
}else if(minC>=1){
	result="发表于"+ parseInt(minC) +"分钟前";
}else
	result="刚刚发表";
return result;
}

/**
 * 根据输入的值获取活动类型
 */
function getActivityType(v){
	if(v==undefined){
		return '其他';
	}
	if(v==0){
		return '自驾游';
	}else if(v==1){
		return '俱乐部活动';
	}else if(v==1){
		return '4S店活动';
	}else{
		return '其他';
	}
}

/**
 * 活动页面公共初始化
 */
function initActivityPage(){
	$("#activity-item").addClass('active');	//高亮活动管理菜单
	//加入滚动条
	$.scrollUp({
		scrollName: 'scrollUp', // Element ID
		topDistance: '300', // Distance from top before showing element (px)
		topSpeed: 300, // Speed back to top (ms)
		animation: 'fade', // Fade, slide, none
		animationInSpeed: 200, // Animation in speed (ms)
		animationOutSpeed: 200, // Animation out speed (ms)
		scrollText: 'Scroll to top', // Text for element
		scrollImg: true,
		activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
	});
	initOperationEvent();
	$('span.time').each(function(){
		$(this).html(getDateDiff($(this).html()));
	});
	$('span.time').removeClass('hidden');
}

/**
 * 初始化活动页面的操作事件
 */
function initOperationEvent(){
	//删除活动
	$('.operation-group button[name="delete"]').bind('click',function(){
		var id = $(this).parents('div.list-group-item').attr('id');
		$.ajax({
			url:'admin/activity/delete',
			data:{
				id:id
			},
			success:function(result){
				if(result.success){
					$('#'+id).attr('deleted','deleted');
					$('#'+id).find('a.title').css('text-decoration','line-through');
					$('#'+id).find('a.title').after('<span class="label label-danger">已删除</span>');
				}else{
					alert('删除失败');
				}
			}	
		});		
	});
	//审核批准
	$('.operation-group button[name="pass"]').bind('click',function(){
		var id = $(this).parents('div.list-group-item').attr('id');
		if($('#'+id).attr('deleted')=='deleted'){ //已删除节点不能进行操作
			alert("已删除节点不能进行操作");
			return ;
		}		
		$.ajax({
			url:'admin/activity/audit',
			data:{
				id:id,
				audit:1
			},
			success:function(result){
				if(result.success){
					$('#'+id).find('a.title').next().remove();
					$('#'+id).find('a.title').after('<span class="label label-success">通过</span>');
					$('#'+id).find('.operation-group button[name="pass"]').addClass('disabled');//设置操作按钮状态
					$('#'+id).find('.operation-group button[name="refuse"]').removeClass('disabled');
				}else{
					alert('审核失败');
				}
			}	
		});		
	});
	//审核不批准
	$('.operation-group button[name="refuse"]').bind('click',function(){
		var id = $(this).parents('div.list-group-item').attr('id');
		if($('#'+id).attr('deleted')=='deleted'){ //已删除节点不能进行操作
			alert("已删除节点不能进行操作");
			return ;
		}		
		$.ajax({
			url:'admin/activity/audit',
			data:{
				id:id,
				audit:2
			},
			success:function(result){
				if(result.success){
					$('#'+id).find('a.title').next().remove();
					$('#'+id).find('a.title').after('<span class="label label-danger">不通过</span>');
					$('#'+id).find('.operation-group button[name="refuse"]').addClass('disabled');//设置操作按钮状态
					$('#'+id).find('.operation-group button[name="pass"]').removeClass('disabled');
				}else{
					alert('审核失败');
				}
			}	
		});		
	});
}

/**
*初始化城市选择级联下拉菜单
*/
function initCityChooser(){
	$('#province-list').bind('change',function(){
		var me = $(this);
		//var provinceName = $(this).val();
		$.ajax({
			url:"city/cityList/"+me.val(),
			success:function(result){
				$('#city-list').empty();
				for(i=0;i<result.data.cities.length;i++){					
					$('#city-list').append('<option value="'+result.data.cities[i].id+'">'+result.data.cities[i].name+'</option>')
				}				
			}			
		});
		
	})
}

//全局初始化校验器
function initGlobalValidation(){
	jQuery.extend(jQuery.validator.messages, {
		  required: "不能为空",
		  remote: "请修正该字段",
		  email: "请输入正确格式的电子邮件",
		  url: "请输入合法的网址",
		  date: "请输入合法的日期",
		  dateISO: "请输入合法的日期 (ISO).",
		  number: "请输入合法的数字",
		  digits: "只能输入整数",
		  creditcard: "请输入合法的信用卡号",
		  equalTo: "请再次输入相同的值",
		  accept: "请输入拥有合法后缀名的字符串",
		  maxlength: jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串"),
		  minlength: jQuery.validator.format("请输入一个 长度最少是 {0} 的字符串"),
		  rangelength: jQuery.validator.format("请输入 一个长度介于 {0} 和 {1} 之间的字符串"),
		  range: jQuery.validator.format("请输入一个介于 {0} 和 {1} 之间的值"),
		  max: jQuery.validator.format("请输入一个最大为{0} 的值"),
		  min: jQuery.validator.format("请输入一个最小为{0} 的值")
	});
}

/**
*后台校验不通过时统一处理函数
*/
function showValidateErrors(result){
	alert("后台校验不通过");
}

/**
 * 文件上传初始化
 * @param url
 */
function initFileUpload(url){
	$('#fileupload').fileupload({
	    url: url,
	    dataType: 'json',
	    done: function (e, data) {
	        $.each(data.result.data.files, function (index, file) {
	            $('#my-file').empty().append('<div><img style="max-width:440px" src="storage/temp/'+file.fileName+'"></img><button class="btn btn-primary btn-xs" name="delete" type="button">删除</button><div>');
	            $('input[name="photoName"]').val(file.fileName);
	        });
	    }    
	}).prop('disabled', !$.support.fileInput)
    .parent().addClass($.support.fileInput ? undefined : 'disabled');
	
	$('button[name="delete"]').live('click',function(){
		$(this).parent().remove();
		$('input[name="photoName"]').val("");
	});
}