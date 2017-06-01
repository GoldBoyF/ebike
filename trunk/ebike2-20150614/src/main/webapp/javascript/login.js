$(document).ready(function(){
	//判断错误信息
	
	
	$('#login-form').validate({
		rules: {
			j_username: {
				required: true,
			},
			j_password: {
				required: true,
			},
			j_code: {
				required: true,
			}
		},
		messages: {
			j_username: {
				required: "请输入用户名",
			},
			j_password: {
				required: "请输入密码",
			},
			j_code: {
				required: "请输入验证码",
			},	   	
		},
		errorPlacement: function(error, element) {  
			element.popover({
				trigger: 'manual',
				'html': true,
				content: '<p class="error" style="width:100px;">'+error.html()+'</p>',				
			}).popover('show');
		},
		showErrors:function(errorMap,errorList) {
			var element = this.currentElements;
			if(element.next().hasClass('popover')){
				element.popover('destroy');
			}
			this.defaultShowErrors();
		},
		submitHandler: function(form) 
    	{      
    		$(form).ajaxSubmit(function(data) { 
    			if(data.success){
    				window.location.href = 'admin/main'
    			}else{
    				var msg = '';
    				var error = data.msg;
    				switch(error){
    					case "1":
    						msg = '用户名不能为空';break;
    					case "2":
    						msg = '密码不能为空';break;
    					case "3":
    						msg ='验证码错误';break;
    					case "4":
    						msg = '用户名或密码错误';break;
    					case "9":
    						msg = '非POST登录请求';break;
    					default:
    						msg = '未知异常';   	break;				
    				}
    				alert(msg);
    			}       
    	    });      
    	} 
	});
});