class Loader
{
	
	constructor() {
			
	}

	load(el,url,callback){
		$(el).load(url,function(){
			if(callback){
				callback.success();
			}
		});
	}


	getWidth(){
		return this.width;
	}

	getHiehgt(){
		return this.height;
	}

	resizeWindow(width,height){
		this.width = width;
		this.height = height;
		try {
			var gui = require('nw.gui');
			gui.Window.get().resizeTo(width,height);
		}
		catch(err){
		
		}
	}

	moveWindow(left,top){
		try {
			var gui = require('nw.gui');
			gui.Window.get().moveTo(left,top);
		}
		catch(err){
		
		}
	}

	getMenu(){
		if(!this.menu){
			var menu = new Menu();
			this.menu = menu;
		}
		return this.menu;
	}

	getLogin(){
		if(!this.login){
			var login = new Login();
			this.login = login;
		}
		return this.login;
	}

	getConfig(){
		if(!this.config){
			var config = new Config();
			this.config = config;
		}
		return this.config;
	}

	getHeader(){
		if(!this.header){
			var header = new Header();
			this.header = header;
		}
		return this.header;
	}



	getEventHandler(){
		if(!this.eventHandler){
			var eventHandler = new EventHandler();
			this.eventHandler = eventHandler;
		}
		return this.eventHandler;
	}

	getProblemInfo(){
		if(!this.problemInfo){
			var problemInfo = new ProblemInfo();
			this.problemInfo = problemInfo;
		}
		return this.problemInfo;
	}


	getBase(){
		if(!this.base){
			var base = new Base();
			this.base = base;
		}

		return this.base;
	}

	getBasicInfo(){
		if(!this.basicInfo){
			var basicInfo = new BasicInfo();
			this.basicInfo = basicInfo;
		}
		return this.basicInfo;
	}


	getSocketSender(){
		if(!this.socketSender){
			var socketSender = new SocketSender();
			this.socketSender = socketSender;
		}
		return this.socketSender;
	}


	loadLogin(){
		$("#content").css("display","none");
		$("#loginContent").css("display","block");
		this.load("head","resource.html");
		this.load("#loginContent",this.getLogin().url());

		this.resizeWindow(500,450);
	}

	loadMenu(){
		this.load("#baseMenuContent",this.getMenu().url());
	}

	loadBase(){
		this.load("#base",this.getBase().url())
	}

	loadHeader(){
		this.load("#header",this.getHeader().url())
	}

	getOrderList(){
		if(!this.orderList){
			var orderList = new OrderList();
			this.orderList = orderList;
		}
		return this.orderList;
	}

	getTaskList(){
		if(!this.taskList){
			var taskList = new TaskList();
			this.taskList = taskList;
		}
		return this.taskList;
	}

	getUserinfo(){
		if(!this.userInfo){
			var userInfo = new UserInfo();
			this.userInfo = userInfo;
		}
		return this.userInfo;
	}

	getUserList(){
		if(!this.userList){
			var userList = new UserList();
			this.userList = userList;
		}
		return this.userList;
	}

	getTaskUpload(){
		if(!this.taskUpload){
			var taskUpload = new TaskUpload();
			this.taskUpload = taskUpload;
		}
		return this.taskUpload;
	}

	getTaskUserinfoDetail(){
		if(!this.taskUserinfoDetail){
			var taskUserinfoDetail = new TaskUserinfoDetail();
			this.taskUserinfoDetail = taskUserinfoDetail;
		}
		return this.taskUserinfoDetail;
	}

	getSetup(){
		if(!this.setup){
			var setup = new Setup();
			this.setup = setup;
		}
		return this.setup;
	}

	getUpdatePassword(){
		if(!this.updatePassword){
			var updatePassword = new UpdatePassword();
			this.updatePassword = updatePassword;
		}
		return this.updatePassword;
	}

	loadTaskUpload(){
		this.getTaskUpload().showView();
	}

	loadOrderList(){
		this.getOrderList().showView();
	}

	loadTaskList(){
		this.getTaskList().showView();
	}

	loadUserInfo(){
		this.getUserinfo().showView();
	}

	loadUserList(){
		this.getUserList().showView();
	}
}

var loader = new Loader();