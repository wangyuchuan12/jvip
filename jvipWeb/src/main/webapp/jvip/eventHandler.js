class EventHandler{
    localSocketOnmessage(msg){
        var outThis = this;
        var obj = eval("("+msg+")");
        var messageType = obj.messageType;
        if(messageType=="win"){
            var data = obj.data;
            if(data.type=="close"){
                loader.getBase().close();
            }else if(data.type="show"){
                loader.getBase().show();
                loader.getBase().stopRemindTray();
            }
        }else if(messageType=="sourceUpdate"){
            var data = obj.data;
            loader.getBase().confirmUpdate(data);
        }else if(messageType=="updateProcess"){
            var data = obj.data;
            loader.getBase().progressSchedule(data.process,{
                complete:function(){
                    var cmd = require('node-cmd');
                    cmd.run('updateSource.bat');
                    
                },
                displayComplete:function(){
                    setTimeout(() => {
                        alert("更新成功，重新启动");
                        loader.getBase().restart();
                    }, 5000);
                }
            });
        }
    }

    localSocketOnopen(){
        loader.getSocketSender().doLogin();
        setTimeout(function(){
            loader.getSocketSender().doCheckupdate();
        },500);
    }

    localSocketOnclose(){
        loader.getBase().connLocalSocket();
        var cmd = require('node-cmd');
        cmd.run('startService.bat');
    }

    socketOnmessage(msg){
        msg = eval(("(")+msg+")");
        if(msg.messageType=="messageList"){
            var data = msg.data;
            loader.getMessage().receiveMessages(data);
        }else if(msg.messageType=="loginResult"){
            var data = msg.data;
            if(data.success){
               loader.getMessage().requestMessageToSocket();
            }
        }
        else{
            var array = new Array();
            array.push(msg);
            loader.getMessage().receiveMessages(array);
        }
        
    }

    socketOnopen(){
        var base = loader.getBase();
        var obj = new Object();
        obj.messageType = "login";
        var data = new Object();
        data.token = loader.getConfig().getToken();
        obj.data = data;
        base.sendMessage(JSON.stringify(obj));
    }

    socketOnclose(){
        var base = loader.getBase();
        base.connSocket();
    }
}