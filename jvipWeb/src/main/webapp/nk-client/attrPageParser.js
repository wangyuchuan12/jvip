class AttrPageParser extends BasePageParser{
    constructor(el,attrname){
        super();
        this.el = el;
        this.attrname = attrname;
    }

    getRawValue(){
        var rawValue = this.el.attr(this.attrname);
        return rawValue;
    }


    doUpdateData(value){
        this.el.attr(this.attrname,value);
    }

}