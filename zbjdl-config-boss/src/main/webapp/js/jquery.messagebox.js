var MessageBox = function(){
  return{
    div: "<div></div>",
    title: "提示", /* 弹出层标题*/
    buttons: {ok: "确定", cancel: "取消"}, /* 按钮取值*/
    popups: {},
    _afterClose: function(){
      /*处理ie8有滚动条问题*/
      ($.browser.msie && $.browser.version == '8.0')? $('body').css('overflow-x','').css('overflow-y','') : ''; 
    },
    _afterOpen: function(){
      /*处理ie8有滚动条问题*/
      if("auto" != $('body').css('overflow-y')){
        ($.browser.msie && $.browser.version == '8.0')? $('body').css('overflow-y','hidden') : '';
      }
      if("auto" != $('body').css('overflow-x')){
        ($.browser.msie && $.browser.version == '8.0')? $('body').css('overflow-x','hidden') : '';
      }
    },
    /*为options附加事件*/
    _addOptionsEvent: function(opt){
      if(opt.close) {
        var tempCloseFun = opt.close;
        opt.close = function(){
          tempCloseFun();
          MessageBox._afterClose();
        };
      }else {
        opt.close = MessageBox._afterClose;
      }

      if(opt.open) {
        var tempOpenFun = opt.open;
        opt.open = function(){
          tempOpenFun();
          MessageBox._afterOpen();
        };
      }else {
        opt.open = MessageBox._afterOpen;
      }

      return opt;
      
    },
    alert: function(msg, options){
      var btn = [{"text": MessageBox.buttons['ok'], "click": function(){$(this).dialog("close");}}];
      options = jQuery.extend({title: MessageBox.title, modal: true, buttons: btn, draggable: false, resizable: false, width: 392}, options || {});
      MessageBox._addOptionsEvent(options);
      $(MessageBox.div).addClass('alert').html(msg).dialog(options);
    },
    
    confirm: function(msg, callback, options){
      var fun = callback;
      var cf = callback[1];
      if(callback instanceof Array) {
        fun = callback[0];
      }
      var okFun = function(){
        $(this).dialog("close");
        fun();
      };
      var calFun = function(){
        $(this).dialog("close");
        if(cf){
          cf();
        }
      };
      var btn = [{"text": MessageBox.buttons['ok'], "click": okFun},
      {"text":  MessageBox.buttons['cancel'], "click": calFun}];
      options = jQuery.extend({title: MessageBox.title, modal: true, buttons: btn, draggable: false, resizable: false, width: 392}, options || {});
      MessageBox._addOptionsEvent(options);
      $(MessageBox.div).addClass('confirm').text(msg).dialog(options);
    },

    close: function(d){
      d.dialog("close");
    },
    
    popup: function(id, options){
      var $p = MessageBox.popups[id];
      if(!$p) {
        $p = $("#" + id);
        MessageBox.popups[id] = $p;
      }
      options = jQuery.extend({title: MessageBox.title, modal: true, draggable: false, resizable: false}, options || {});
      MessageBox._addOptionsEvent(options);
      $p.dialog(options);
    },
    
    unpopup: function(id){
      var $p = MessageBox.popups[id];
      if(!$p) {
        $p = $("#" + id);
      }else {
        delete MessageBox.popups[id];
      }
      $p.dialog("close");
    },
    
    load: function(id, url, options){
      jQuery.get(url, function(data){
        MessageBox.popupContent(id, data, options);
      });
    },
    
    popupContent: function(id, data, options){
      var $p = MessageBox.popups[id];
      if(!$p) {
        $p = $(MessageBox.div).html(data);
        MessageBox.popups[id] = $p;
      }
      options = jQuery.extend({title: MessageBox.title, modal: true, draggable: false, resizable: false}, options || {});
      MessageBox._addOptionsEvent(options);
      return $p.dialog(options);
    }
  };
}();