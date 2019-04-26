class BasePageParser{
    constructor(){
    }

    parseValue(){
        var value = this.getRawValue();
        var elements = new Array();
        var regexp = /{{[a-z A-Z .]{1,20}}}/;
        while(regexp.test(value)){
            var prefixIndex = value.search(regexp);
            var suffixIndex = value.indexOf("}}",prefixIndex)
            var prefix = value.substr(0,prefixIndex);
            var name = value.substring(prefixIndex+2,suffixIndex);
            var suffix = value.substring(suffixIndex+2);
            value = value.substring(suffixIndex+2);
            var element = {
                prefix:prefix,
                name:name,
                suffix:suffix
            }
            elements.push(element);
        }
        this.elements = elements;
        
    }

    getValue(data){
        var str = "";
        for(var i=0;i<this.elements.length;i++){
            var element = this.elements[i];
            var splitNames = element.name.split(".");
            for(var i = 0;i<splitNames.length;i++){
                data = data[splitNames[i]];
                if(!data){
                    data = "";
                    break;
                }
            }
            str = str+element.prefix+data+element.suffix;
        }
        return str;
    }

    updateData(data){
        var elements = this.elements;
        if(elements&&elements.length>0){
            var value = this.getValue(data);
            this.doUpdateData(value);
        } 
    }
}