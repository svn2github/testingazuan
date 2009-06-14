
Sbi.sdk.namespace('Sbi.sdk.ajax');


Sbi.sdk.apply(Sbi.sdk.ajax, {

    activeX: [
        'MSXML2.XMLHTTP.3.0',
        'MSXML2.XMLHTTP',
        'Microsoft.XMLHTTP'
    ]
    
    ,  transactionId: 0
    
    , createXhrObject:function(transactionId) {
        var obj,http;
        try {
            http = new XMLHttpRequest();
            obj = { conn:http, tId:transactionId };
        } catch(e) {
            for (var i = 0; i < this.activeX.length; ++i) {
                try {
                    http = new ActiveXObject(this.activeX[i]);
                    obj = { conn:http, tId:transactionId };
                    break;
                } catch(e) { }
            }
        } finally {
            return obj;
        }
    },

    getConnectionObject:function() {
        var o;
        var tId = this.transactionId;

        try {
            o = this.createXhrObject(tId);
            if (o) {
                this.transactionId++;
            }
        } catch(e) { }
        finally {
            return o;
        }
    },
    
    asyncRequest:function(method, uri, callback, postData) {
        var o = this.getConnectionObject();

        if (!o) {
            return null;
        } else {
            o.conn.open(method, uri, false);
            
            /*
            if (this.useDefaultXhrHeader) {
                if (!this.defaultHeaders['X-Requested-With']) {
                    this.initHeader('X-Requested-With', this.defaultXhrHeader, true);
                }
            }

            if(postData && this.useDefaultHeader){
                this.initHeader('Content-Type', this.defaultPostHeader);
            }

            if (this.hasDefaultHeaders || this.hasHeaders) {
                this.setHeader(o);
            }
            */

            //this.handleReadyState(o, callback);
            o.conn.send(postData || null);

            return o;
        }
    }

});