class UpdatePassword extends BasePage{
    constructor() {
        super();
    }
    url(){
        return "content/updatePassword.html";
    }

    parentSelector(){
        return "#dialogContent";
    }

    dialogParentSelector(){
        return "#dialogContent";
    }

    dialogWidth(){
        return 500;
    }

    dialogHeight(){
        return 300;
    }

    onOpen(){

    }

    onClose(){

    }

    updatePassword(oldPassword,newPassword,callback){
        var base = loader.getBase();
        base.request({
            url:"/api/updatePassword",
            data:{
                new_password:newPassword,
                old_password:oldPassword
            },
            success:function(result){
                if(result.success){
                    callback.success();
                }else{
                    callback.fail();
                }
            },
            error:function(){
                callback.error();
            }
        });
    }


    showDialog(callback){
        this.title = "修改密码";
        super.showDialog();
        this.callback = callback;

    }

    getCallback(){
        return this.callback;
    }

    selector(){
        return "#updatePassword";
    }
}