class UserInfo extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/userInfo.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("userInfo");
    }

    selector(){
        return "#userInfo";
    }

    requestUserinfo(callback){
        var base = loader.getBase();
        base.request({
            url:"/api/userInfo",
            data:{},
            success:function(result){
                if(result.success){
                    callback.success(result.data);
                }
            }
        });
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("userInfo");
    }
}