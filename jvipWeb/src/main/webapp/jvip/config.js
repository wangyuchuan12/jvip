class Config
{
	constructor() {
		this.init();
	}
	init(){
		try{
			var fs = require("fs");
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.trayMode = "0";
			this.writeConfig(this.configPath()+this.configName(),obj);
			this.config = obj;
			this.getLocalToken();
			return;
		}catch(ex){
			this.config={
				"baseUrl":"http://localhost:8085",
				"waitIconUrl":"etc/wait.png",
				"socketPath":"ws://www.chengxihome.com:9999/ws",
				"localSocketPath":"/ws",
				"localSocketHost":"localhost",
				"localSocketPort": 8087
			}
			return;
		}
	}

	configName(){
		return "config.json";
	}

	configPath(){
		var fileName = "nk-client/etc/";
		return fileName;
	}


	socketPath(){
		return this.config.socketPath;
	}

	localSocketPath(){
		return this.config.localSocketPath;
	}

	localSocketHost(){
		return this.config.localSocketHost;
	}

	localSocketPort(){
		var obj = this.readConfig(this.configPath()+this.configName());
		return obj.localSocketPort;
	}

	setLocalSocketPort(localSocketPort){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.localSocketPort = localSocketPort;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
		if(callback){
			callback.success();
			return;
		}
	}



	readConfig(fileName){
		try{
			var fs = require("fs");
			var content = fs.readFileSync(fileName);
			var obj = eval("("+content+")");
			return obj;
		}catch(ex){
			return this.config;
		}
		
	}


	writeConfig(fileName,config){
		var fs = require("fs");
		fs.writeFileSync(fileName,JSON.stringify(config));
	}
	



	setToken(token,callback){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.token = token;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
		if(callback){
			callback.success();
			return;
		}
	}

	

	getToken(){
		return this.config.token;
	}

	getLocalToken(){

		var token = this.config.localToken;
		if(!token){
			token = new Util().guid();
			this.config.localToken = token;
			this.writeConfig(this.configPath()+this.configName(),this.config);
		}
		return token;
		
	}

	baseUrl(){
		return this.config.baseUrl;
	}

	trayIconUrl(){
		return this.config.trayIconUrl;
	}

	remindIconUrl(){
		return this.config.remindIconUrl;
	}

	setTrayMode(trayMode){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.trayMode = trayMode;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
	}

	getTrayMode(){
		return this.config.trayMode;
	}

	setRole(role){
		this.config.role = role;
	}

	getRole(){
		return this.config.role;
	}

	getWaitIconUrl(){
		return this.config.waitIconUrl;
	}

	setGoods(goods){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.goods = goods;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
	}

	setCompanyId(companyId){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.companyId = companyId;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
	}

	getCompanyId(){
		return this.config.companyId;
	}

	getGoods(){
		return this.config.goods;
	}

	setLoginName(loginName){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.loginName = loginName;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
		if(callback){
			callback.success();
			return;
		}
	}

	setUserType(userType){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.userType = userType;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
		if(callback){
			callback.success();
			return;
		}
	}

	setServicePort(port){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.servicePort = port;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
		if(callback){
			callback.success();
			return;
		}
	}

	getServicePort(){
		var obj = this.readConfig(this.configPath()+this.configName());
		return obj.servicePort;
	}

	getServiceHost(){
		return this.config.serviceHost;
	}

	getServiceToken(){
		return this.config.serviceToken;
	}

	getUserType(){
		return this.config.userType;
	}

	getLoginName(){
		return this.config.loginName;
	}

	setPassword(password){
		try{
			var obj = this.readConfig(this.configPath()+this.configName());
			obj.password = password;
			this.config = obj;
			this.writeConfig(this.configPath()+this.configName(),obj);
			return;
		}catch(ex){
			return;
		}
		if(callback){
			callback.success();
			return;
		}
	}

	getPassword(password){
		return this.config.password;
	}
}