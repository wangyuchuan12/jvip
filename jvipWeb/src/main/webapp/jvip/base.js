class Base
{
	constructor() {
		this.initFrame();
	}

	init(params){
		try{
			var that = this;
			var gui = require('nw.gui');
			var win = gui.Window.get();
			win.on("focus",function(){
				that.status = "show";
				that.stopRemindTray();
			});
		}catch(e){

		}
		
		params.render();
	}

	confirmUpdate(obj){
		var that = this;
		$.messager.alert('更新提示','检测到您所在的版本不是最新版本,立刻更新',"info",function(r){
			that.startUpdate(obj.lastestVersion);
		});
	}

	url(){
		return "base.html";
	}

	startUpdate(version){
		$("#updateProgressBar").css("display","block");
		loader.getSocketSender().downloadFile(version);
	}

	stopUpdate(){
		$("#updateProgressBar").css("display","none");
	}

	progressSchedule(schedule,callback){
		var flag = false;
		var that = this;
		var interval = setInterval(function(){
			var nowSchedule = $("#updateProgressBarContent").progressbar("getValue");
			if(nowSchedule<schedule){
				nowSchedule++;
				$("#updateProgressBarContent").progressbar("setValue",nowSchedule);
			}else{
				clearInterval(interval);
				that.stopUpdate();
				callback.displayComplete();
			}
			if(schedule>=100){
				if(callback&&!flag){
					callback.complete();
					flag = true;
				}

			}
		},100);
	}

	connSocket(){
		return;
		var eventHandler = loader.getEventHandler();
		var path = loader.getConfig().socketPath();
		var socket = new WebSocket(path);
		this.socket = socket;
 
        //收到消息
        socket.onmessage = function (msg) {
			eventHandler.socketOnmessage(msg.data);
        }
 
        //连接打开
        socket.onopen = function (event) {
			eventHandler.socketOnopen();
        }
 
        //连接断开
        socket.onclose = function (event) {
            eventHandler.socketOnclose();
        }
	}

	connLocalSocket(){
		return;
		var eventHandler = loader.getEventHandler();
		var path = "ws://"+loader.getConfig().localSocketHost()+":"+loader.getConfig().localSocketPort()+loader.getConfig().localSocketPath();
		var localSocket = new WebSocket(path);
		this.localSocket = localSocket;
 
        //收到消息
        localSocket.onmessage = function (msg) {
			eventHandler.localSocketOnmessage(msg.data);
        }
 
        //连接打开
        localSocket.onopen = function (event) {
			eventHandler.localSocketOnopen();
        }
 
        //连接断开
        localSocket.onclose = function (event) {
            eventHandler.localSocketOnclose();
        }
	}

	sendMessage(msg){
		var socket = this.socket;
		socket.send(msg);
	}

	sendLocalMessage(msg){
		var socket = this.localSocket;
		socket.send(msg);
	}

	showMessageAlert(msg){
		$("#messageAlert").dialog("open");
	}

	doStartService(callback){
		var that = this;
		setTimeout(() => {
			that.startService(callback);
		}, 5000);
		
		/*
		var isStartService = that.isStartService;
		if(!isStartService){
			that.isStartService = true;
			var localSocketPort = loader.getConfig().localSocketPort();
			new Util().getFreePort(localSocketPort,{
				success:function(port){
					loader.getConfig().setLocalSocketPort(port);
					var cmd = require('node-cmd');
					var pid = cmd.run('startService.bat '+loader.getConfig().getServicePort()+" "+localSocketPort)._handle.pid;
					var fs = require("fs");
					fs.writeFileSync("pid",pid);
					that.startService(callback);
				}
			});
		}*/
	}

	startService(callback){
		this.connLocalSocket();
	}

	requestToLocal(type,params){
		var obj = {
			type:type
		};
		localSocket.send(obj);

	}

	proxyRequest(params,callback){
		params = new Util().clone(params);
		var that = this;
		var requestUrl = params.url;
		var data = params.data;
		if(!data){
			data = {};
		}
		
		var requestUrl = loader.getConfig().baseUrl()+requestUrl;
		data.url = requestUrl;
		var localUrl = "http://localhost:8081/in/requestServer";
		params.url = localUrl;
		params.beforeSend = function(request){
			var token = loader.getConfig().getToken();
			request.setRequestHeader("x-token",token);
		}

		var istimeout = true;
		setTimeout(function(){
			if(istimeout){
				if(callback.timeout){
					callback.timeout();
				}
			}
		},10000);

		params.error = function(resp){
			istimeout = false;
			if(resp.status==401){
				callback.loadLogin();
			}else if(resp.status==0){
				callback.error(resp);
			}else{
				callback.error(resp);
			}
		}

		params.success = function(resp){
			istimeout = false;
			if(resp.status=="415"){
				callback.error();
			}else{
				callback.success(resp);
			}
		}
		// params.crossDomain=true;
		// params.dataType = "jsonp";
		// params.jsonpCallback="success";

		$.ajax(params);
	}

	redirectRequest(params,callback){
		params = new Util().clone(params);
		var that = this;
		var requestUrl = params.url;
		var data = params.data;
		if(!data){
			data = {};
		}
		
		var requestUrl = loader.getConfig().baseUrl()+requestUrl;
		alert("requestUrl:"+requestUrl+",data:"+JSON.stringify(data));
		data.url = requestUrl;
		params.url = requestUrl;
		params.beforeSend = function(request){
			var token = loader.getConfig().getToken();
			request.setRequestHeader("x-token",token);
		}

		var istimeout = true;
		setTimeout(function(){
			if(istimeout){
				if(callback.timeout){
					callback.timeout();
				}
			}
		},10000);


		var errorFun = params.error;
		params.error = function(resp){
			istimeout = false;
			if(resp.status==401){
				callback.loadLogin();
			}else if(resp.status==0){
				/*if(callback.timeout){
					callback.timeout();
				}*/
				callback.error();
			}else{
				callback.error(null,resp);
			}
		}

		params.success = function(resp){
			istimeout = false;
			callback.success(resp);
		}
		// params.crossDomain=true;
		// params.dataType = "jsonp";
		// params.jsonpCallback="success";

		$.ajax(params);
	}

	request(params){
		var that = this;
		var url = params.url;
		url = loader.getConfig().baseUrl()+url;
		params.url = url;
		params.beforeSend = function(request){
			var token = loader.getConfig().getToken();
			request.setRequestHeader("x-token",token);
		}

		var istimeout = true;
		setTimeout(function(){
			if(istimeout){
				if(params.timeout){
					params.timeout();
				}
			}
		},10000);


		var errorFun = params.error;
		params.error = function(resp){
			istimeout = false;
			if(resp.status==401){
				loader.loadLogin();
			}else if(resp.status==0){
				if(params.timeout){
					params.timeout();
				}
			}else{
				errorFun.call(null,resp);
			}
		}

		var successFun = params.success;

		params.success = function(resp){
			istimeout = false;
			successFun.call(null,resp);
		}
		// params.crossDomain=true;
		// params.dataType = "jsonp";
		// params.jsonpCallback="success";

		$.ajax(params);

	}

	uploadFile(callback){
		this.request({
			url: '/api/uploadFile',　　　　　　　　　　//上传地址
			type: 'POST',
			cache: false,
			data: new FormData($('#uploadForm')[0]),　　　　　　　　　　　　　//表单数据
			processData: false,
			contentType: false,
			success:function(resp){
				if(resp.success){
					callback.success(resp.data)
				}else{
					callback.fail();
				}
			},
			error:function(error){
				alert("error:"+JSON.stringify(error));
			}
		});
	}

	openUploadFile(callback){
		var that = this;
		$("#file_upload").val("");
		$("#file_upload").off("change").on("change",function(){
            that.uploadFile(callback);

		});

        $("#file_upload").click();

	}

	startWaiting(){
		var that = this;
		if(!this.waiting){
			this.waiting = true;
			var index = 0;
			
			var interval = setInterval(function(){
				if(that.waiting){
					index++;
					$(".baseContentLoading").css("display","block");
					$(".baseContentLoadingImg").css("transform", "rotate(" + (index*20) + "deg)");
				}else{
					clearInterval(interval);
				}
				
			},100);
		}
	}

	stopWaiting(){
		this.waiting = false;
		$(".baseContentLoading").css("display","none");
	}

	fullScreen(){
		try{
			var gui = require('nw.gui');

			var win = gui.Window.get();
			win.enterFullscreen();
		}catch(ex){

		}
		
    }

    exitFullScreen(){

		try{
			var gui = require('nw.gui');

			var win = gui.Window.get();
			win.leaveFullscreen();
		}catch(ex){

		}
		
	}

	refreshWin(){
		try{
			var gui = require('nw.gui');
			var win = gui.Window.get();
			if(this.status=="close"){
				win.close(true);
			}else if(this.status=="hide"){
				win.hide();
			}else if(this.status=="show"){
				win.show();
			}
		}catch(ex){

		}
		
	}

	reload(){
		this.removeTray();
		location.reload();
		
	}

	restart(){
		loader.getSocketSender().showWin();
		this.close();
	}

	close(){
		this.status="close";
		this.refreshWin();
	}

	hide(){
		this.status="hide";
		this.refreshWin();
	}

	show(){
		this.status="show";
		this.refreshWin();
	}

	setMode(mode){
		this.mode = mode;
	}

	getMode(){
		return this.mode;
	}

	getStatus(){
		return this.status;
	}

	
	initFrame(){
		var that = this;
		try{
			
			var gui = require('nw.gui');
			var win = gui.Window.get();
			win.on('close',function(){
				if(that.mode=="login"){
					that.close();
				}else{
					that.hide();
				}
			});

		}catch(e){
			//alert("初始化发生错误");
		}
		
	}

	flushTray(){
		/*var that = this;
		try{
			var trayMode = this.trayMode;
			var gui = require('nw.gui');
			if(trayMode=="normal"){
				var tray = new gui.Tray({ title: '诺客智能客服', icon: loader.getConfig().trayIconUrl()});
			}else if(trayMode=="remind"){
				var iconUrl = loader.getConfig().remindIconUrl();
				var tray = new gui.Tray({ title: '诺客智能客服',icon:iconUrl});
			}
		}catch(ex){

		}*/
		
	}

	startRemindTray(){
		/*this.trayMode="remind";
		this.flushTray();*/

		loader.getSocketSender().startTray();
		
	}

	stopRemindTray(){
		/*
		this.trayMode="normal";
		this.flushTray();*/
		loader.getSocketSender().stopTray();
	}

	removeTray(){
		/*try{
			var that = this;
			if(this.tray){
				this.stopRemindTray();
				this.tray.removeAllListeners();
				this.tray.remove();
			}
		}catch(ex){

		}*/

		loader.getSocketSender().hideTray();
		
	}

	initTray(mode){
		/*if(this.tray){
			return;
		}
		var that = this;
		try{
			this.removeTray();
			var that = this;
			var gui = require('nw.gui');
			var win = gui.Window.get();
			var tray = new gui.Tray({ title: '诺客智能客服', icon: loader.getConfig().trayIconUrl()});
			this.tray = tray;
			tray.tooltip = '点此打开';
			var menu = new gui.Menu();
			menu.append(new gui.MenuItem({label: '退出',click:function(){
				that.close();
			}}));
	
			tray.menu = menu;
	
			tray.on('click',
	
				function()
	
				{
					that.stopRemindTray();
					that.show();
				}
			);
			

			return tray;
			
			
		}catch(ex){

		}*/

		if(mode==0){

		}else if(mode==1){

		}

		loader.getSocketSender().showTray();
		
	}

}
