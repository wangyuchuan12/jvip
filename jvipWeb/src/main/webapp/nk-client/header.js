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



    init(params){
        this.params = params;
        params.render();
    }
}