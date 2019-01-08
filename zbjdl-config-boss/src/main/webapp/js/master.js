// JavaScript Document
$(document).ready(function () {
//*******************************************常用输入框风格事件*************************************
	$("input.TextInput").hover(
		function(){
			$(this).addClass('Hover')
			},
		function(){
			$(this).removeClass('Hover')
			})
			
	$("input.TextInput").focus(function(){
			$(this).addClass('Focus')
		})
		
	$("input.TextInput").blur(function(){
			$(this).removeClass('Focus')
		})
		
/**************************************按钮状态样式****************************************************************************/	
	$('.bt2').hover(
			function(){
			$(this).addClass('bt2hover')
			},
		function(){
			$(this).removeClass('bt2hover')
	})
//*******************************************头部菜单交互*************************************

		$(".mainbav>dl>dd:contains('首 页')").addClass('current1')

		$(".mainbav>dl>dd").click(function(){
			$(".mainbav>dl>dd").removeClass('current1')
			$(".mainbav>dl>dd").removeClass('current2')
			
			if(this.className=='bav1'){
				$(this).addClass('current1')
			}else if(this.className=='bav2'){
				$(this).addClass('current2')
			}
		})
		
//*******************************************头部搜索交互样式输入框**************************************/

	//输入框
	$(".topSearchTex").hover(
		function(){
			$(this).addClass('Texcurrent')
			$(".topSearchSub").addClass('subCurrent')
			},
		function(){
			$(this).removeClass('Texcurrent')
			$(".topSearchSub").removeClass('subCurrent')
		})
	//按钮
	$(".topSearchSub").hover(
		function(){
			//$("topSearchSub").addClass('current')
			$(this).addClass('subCurrent')
			},
		function(){
			//$("topSearchSub").removeClass('current')
			$(this).removeClass('subCurrent')
		})
		
	//清空搜索框内容
	$(".topSearchTex").focus(function(){
		if($(this).val()=="搜索"){
		$(this).val('');
		$(this).addClass('Texcurrent')
		}
		})
	$(".topSearchTex").blur(function(){
		if($(this).val()==""){
		$(this).val('搜索');
		$(this).removeClass('Texcurrent')
		}
		})
		
/**************************************左侧二级菜单样式****************************************************************************/
/*	var $pageName=$(".mainbav>dl>dd");
	 $pageName.click(function(){
		alert($pageName.html());
	 })*/
	//设置默认状态
	$(".more dd").slideUp(200)
	$(".more:contains('查询会员')").addClass('def')
	$(".def dd").slideDown(200);
	$(".def dt").addClass('default')	
	$(".def dd:first").addClass('subdefault')
	
	
	//点击当前页面样式
	$(".subMenu dl dt").click(function(){	
	$(".more dd").slideUp(200);
		$(".subMenu dl dt").removeClass('default');
		$(this).addClass('default')		
		})
		
	$(".subMenu dl dd").click(function(){	
		$(".subMenu dl dd").removeClass('subdefault');
		$(this).addClass('subdefault')		
		})

	//点击伸展样式设置
	$(".more").click(function(){
		$(".more").removeClass('def');
		$(this).addClass('def');
			$(".def dd").slideDown(200);
		})

	//鼠标移上样式设置
	$(".subMenu dl dt").hover(
		function(){
			$(this).addClass('current')
			},
		function(){
			$(this).removeClass('current')
	})

	$(".subMenu dl dd").hover(
		function(){
			$(this).addClass('subCurrent')
			},
		function(){
			$(this).removeClass('subCurrent')
		})	
/**************************************列表样式****************************************************************************/	
	$(".List tr:even").addClass('even');
	$(".List tr").hover(
		function(){
			$(this).addClass('current')
			},
		function(){
			$(this).removeClass('current')
	})
	$(".List tr:contains('异常')").addClass('error');
	//$(".subMenu dl dt").hover()
	
	
	

		
})


	
/**************************************蒙窗样式****************************************************************************/	
  //$('.miniWindow').addClass('importInfo')
 
  //触发事件
  function showInfo(objId){
	  $('body').prepend('<div class=miniWindow>sssssssssssssssss</div>')
	  $(".miniWindow").css('opacity','0.5' )//透明度
	  $('body').prepend('<div class=containerBox><div class="outBox"><div class=windowTop><span>重置用户密码</span><div class=winClose></div></div><div class=windowDown></div></div></div>')
	   var s=$('#'+objId).html();//获得插入对象的HTML代码
	  // alert(s)
	  $('#objNumber').empty();//清空-否则页面会有两个FORM
	  $('.windowTop').after(s)//把蒙窗的内容插入进去
	   
	   //关闭按钮
	$(".winClose").hover(
			function(){
			$(this).addClass('winCloseHover')
			},
		function(){
			$(this).removeClass('winCloseHover')
	})
	
	
	$(".winClose").click(function(){
	  $('#objNumber').prepend(s);
	  $(".miniWindow").hide(100);
	  $(".containerBox").hide(100)
	})
	////////////////////////////////////////////////////////////////
	
	//*******************************************常用输入框风格事件*************************************
	$("input.TextInput").hover(
		function(){
			$(this).addClass('Hover')
			},
		function(){
			$(this).removeClass('Hover')
			})
			
	$("input.TextInput").focus(function(){
			$(this).addClass('Focus')
		})
		
	$("input.TextInput").blur(function(){
			$(this).removeClass('Focus')
		})
		
/**************************************按钮状态样式****************************************************************************/	
	$('.bt2').hover(
			function(){
			$(this).addClass('bt2hover')
			},
		function(){
			$(this).removeClass('bt2hover')
	})
	
  }


