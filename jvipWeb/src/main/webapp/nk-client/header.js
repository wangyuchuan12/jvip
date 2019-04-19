class Header{
    url(){
        return "header.html";
    }

    fullScreen(){
        loader.getBase().fullScreen();
    }

    exitFullScreen(){
       loader.getBase().exitFullScreen();
    }

    close(){
        loader.getBase().close();
        loader.getBase().stopRemindTray();
    }

    doLoginout(){
        loader.getLogin().doLoginout();
        loader.getBase().stopRemindTray();
    }

    init(params){
        this.params = params;
        params.render();
    }
}