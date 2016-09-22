$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * 扩展validatebox，添加新的验证功能
 *
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
    trim : {
        validator : function(value) {
            var str = value.charAt(0);
            if (' ' === str)
                return false;
            str = value.charAt(value.length - 1);
            if (' ' === str)
                return false;
            return true;
        },
        message : '内容前后不允许包含空格!'
    },
    chinese : {
        validator : function(value) {
            var strRegex = /^[\u0391-\uFFE5_-]*$/;
            var re = new RegExp(strRegex);
            return re.test(value);
        },
        message : '请勿输入汉字以外的字符!'
    },
    isNotNull : {
        validator : function(value) {
            var flag = true;
            value = $.trim(value);
            if (value.length === 0) {
                flag = false;
            }
            return flag;
        },
        message : '请勿输入空格!'
    },
    eqPwd : {/* 验证两次密码是否一致功能 */
        validator : function(value, param) {
            return value == $(param[0]).val();
        },
        message : '两次密码不一致!'
    },
    number: {
        validator: function (value) {
            return /^\d+$/.test(value);
        },
        message: '请输入数字!'
    },
    pstvInteger: {
        validator: function (value) {
            if(/^[+]?[0-9]\d*$/.test(value)){
                return value > 0;
            }else{
                return false;
            }
        },
        message: '请输入正整数!'
    }
});
//在layout的panle全局配置中,增加一个onCollapse处理title
$.extend($.fn.layout.paneldefaults, {
    onCollapse : function () {
        //获取layout容器
        var layout = $(this).parents("div.layout");
        //获取当前region的配置属性
        var opts = $(this).panel("options");
        //获取key
        var expandKey = "expand" + opts.region.substring(0, 1).toUpperCase() + opts.region.substring(1);
        //从layout的缓存对象中取得对应的收缩对象
        var expandPanel = layout.data("layout").panels[expandKey];
        //针对横向和竖向的不同处理方式
        if (opts.region == "west" || opts.region == "east") {
            //竖向的文字打竖,其实就是切割文字加br
            var split = [];
            for (var i = 0; i < opts.title.length; i++) {
                split.push(opts.title.substring(i, i + 1));
            }
            expandPanel.panel("body").addClass("panel-title").css("text-align", "center").html(split.join("<br>"));
        } else {
            expandPanel.panel("setTitle", opts.title);
        }
    }
});
