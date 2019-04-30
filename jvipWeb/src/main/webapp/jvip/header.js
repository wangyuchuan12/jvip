class Header{
    url(){
        return "header.html";
    }

    doLoginout(){
        loader.getLogin().doLoginout();
    }

    doSetup(){
        loader.getSetup().showDialog();
    }

    doUpdatePassword(){
        loader.getUpdatePassword().showDialog({
            onOpen:function(){

            },
            onClose:function(){

            }
        });
    }


    init(params){
        this.params = params;
        params.render();
    }
}