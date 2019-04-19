class Login
{
	constructor() {
	}
	url(){
		return "login.html";
	}
	init(params){
		 loader.getBase().setMode("login");
		 this.params = params;
         params.render();
	}


	winWidth(){
		return 200;
	}

	winHeight(){
		return 200;
	}

	doLoginout(){
		var that = this;
		var base = loader.getBase();
		var params = {
			url:"/usercenter/logout",
			data:{
				
			},
			success:function(result){
				loader.getConfig().setPassword("");
				loader.getBase().reload();
			}
		};

		base.request(params);
	}

	showTray(){
		var errorShowTrayTime = this.errorShowTrayTime;
		if(!errorShowTrayTime){
            errorShowTrayTime = 0;
		}
		var that = this;
        try{
            loader.getBase().initTray();
            errorShowTrayTime = 0;
            this.errorShowTrayTime = errorShowTrayTime;
        }catch (e) {
            errorShowTrayTime++;
            this.errorShowTrayTime = errorShowTrayTime;
            if(errorShowTrayTime<35){
                setTimeout(function(){
                    that.showTray();
                },100);
			}
        }
      /*  setTimeout(function(){
            loader.getBase().initTray();
        },5000);*/
	}

	doLogin(identity,password,callback){
		var that = this;
		var base = loader.getBase();
		var params = {
			url:"/api/login",
			data:{
				username:identity,
				password:password
			},
			timeout:function(){
				callback.timeout();
			},
			error:function(){
				callback.fail();
			},
			success:function(result){
				if(result.success){
					loader.loadMenu();
					loader.loadHeader();
					loader.resizeWindow(1100,800);
					loader.moveWindow(100,100);
					$("#content").css("display","block");
					$("#loginContent").css("display","none");
					var menu = loader.getMenu();
					menu.show();
					loader.loadUserInfo();
					loader.getMenu().receiveReminder("searchProblem");
					//loader.getBase().startRemindTray();
					callback.success();
					loader.getConfig().setToken(result.token);
					loader.getConfig().setLoginName(identity);
					loader.getConfig().setPassword(password);
					loader.getConfig().setUserType(result.user.staffUsertype);
					loader.getBasicInfo().setUsername(result.user.staffName);
					loader.getConfig().setCompanyId(result.user.staffCompanyid);
					loader.getBase().connSocket();
                    that.showTray();
				}else{
					callback.fail();
				}
			}
		};

		base.request(params);
	}
}