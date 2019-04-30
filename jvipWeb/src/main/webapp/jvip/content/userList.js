class UserList extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/userList.html";
    }

    parentSelector(){
        return "#basePanel";
    }

    showView() {
        super.showView();
        loader.getBase().setMode("userList");
    }

    selector(){
        return "#userList";
    }


    init(params){
        super.init(params);
        loader.getBase().setMode("userList");
    }

    userList(params,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/userinfos",
            data:params,
            success:function(result){
                if(result.success){
                    callback.success(result.data);
                }else{
                    callback.fail();
                }
            },
            error:function(){
                callback.error();
            }
        });
    }
}