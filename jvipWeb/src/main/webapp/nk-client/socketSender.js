class SocketSender{
    // showTray(){
    //     var obj = {
    //         type:"showTray",
    //         noCheck:"1"
    //     };
    //     var msg = JSON.stringify(obj);
    //     loader.getBase().sendLocalMessage(msg);
    // }

    downloadFile(version){
        var obj = {
            type:"downloadFile",
            version:version,
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    exitService(){
        var obj = {
            type:"exitService",
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    hideTray(){
        var obj = {
            type:"hideTray",
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    showTray(){
        var obj = {
            type:"showTray",
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    showWin(){
        var obj = {
            type:"showWin",
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    startTray(){
        var obj = {
            type:"startTray",
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    stopTray(){
        var obj = {
            type:"stopTray",
            noCheck:"1"
        };
        var msg = JSON.stringify(obj);
        loader.getBase().sendLocalMessage(msg);
    }

    doLogin(){
        var base = loader.getBase();
        var obj = new Object();
        obj.messageType = "login";
        var data = new Object();
        data.noCheck = "1";
        data.token = "4289912wang";
        obj.data = data;
        base.sendLocalMessage(JSON.stringify(obj));
    }

    doCheckupdate(){
        var base = loader.getBase();
        var checkUpdate = new Object();
        checkUpdate.noCheck = "1";
        checkUpdate.type="checkUpdate";
        base.sendLocalMessage(JSON.stringify(checkUpdate));
    }
}