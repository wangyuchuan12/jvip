class Menu
{
	hide(){
		var baseMenu = $("#baseMenu");
		baseMenu.css("display","none");
	}

	show(){
		var baseMenu = $("#baseMenu");
		baseMenu.css("display","inline-block");
	}

	url(){
		return "menu.html";
	}

	load(type){
		if(type=="orderList"){
			loader.loadOrderList();
		}else if(type=="taskList"){
			loader.loadTaskList();
		}else if(type=="userInfo"){
			loader.loadUserInfo();
		}else if(type=="userList"){
			loader.loadUserList();
		}
	}

	receiveReminder(type,num,flag){
		for(var i=0;i<this.getMenus().length;i++){
			var menu = this.getMenus()[i];
			if(menu.loadTarget==type){
				menu.num = num;
				break;
			}
		}
        if(this.params){
        	if(flag){
				this.params.receiveReminder(loader.getBase().getMode(),num);
			}else{
				this.params.receiveReminder(type,num);
			}

        }
	}

    onClickMenu(type){
		/*
		for(var i=0;i<this.getMenus().length;i++){
			var menu = this.getMenus()[i];
			if(menu.loadTarget==type){
				menu.num = 0;
				break;
			}
		}*/

        if(this.params){
            this.params.onClickMenu(type);
            if(type=="message"){
            	loader.getBase().stopRemindTray();
			}
        }
	}
	
	init(params){
		this.params = params;
		params.render();
	}

	getMenus(){
		var menus = this.menus;
		if(!menus){
			var menus = new Array();
			menus.push({
				name:"账号信息",
				url:"",
				loadTarget:"userInfo",
				iconUrl:"image/upload.png",
				imgStyle:"left:5px;"
			});
	
			menus.push({
				name:"我的用户",
				url:"",
				loadTarget:"userList",
				iconUrl:"image/search.png"
			});
	
			menus.push({
				name:"订单",
				url:"",
				loadTarget:"orderList",
				iconUrl:"image/message.png",
				imgStyle:"left:3px;"
			});

			var role = loader.getConfig().getRole();

			if(role==1){
				menus.push({
					name:"任务",
					url:"",
					loadTarget:"taskList",
					iconUrl:"image/task.png",
					imgStyle:"left:3px;"
				});
			}


			this.menus = menus;

			
		}

		return this.menus;
		
	}
}