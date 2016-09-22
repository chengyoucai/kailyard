<#assign ctx = rc.getContextPath()>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>管理系统</title>
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/default/easyui.css" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/icon.css" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/extend/themes/icon.css" charset="utf-8" />
    <!-- 自定义控件 -->
    <link rel="stylesheet" type="text/css" href="${ctx}/static/css/style.css" charset="utf-8" />
    <!-- jquery控件 -->
    <script type="text/javascript" src="${ctx}/static/jquery/jquery-3.1.0.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/static/easyui/jquery.easyui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/static/easyui/locale/easyui-lang-zh_CN.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/static/extend/jquery.easyui.extensions.js" charset="utf-8"></script>
    <!-- from表单插件 -->
    <script type="text/javascript" src="${ctx}/static/jquery/jquery.form.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/static/jquery/jquery.cookie.js" charset="utf-8"></script>
    <script type="text/javascript" src="${ctx}/static/js/sys.js" charset="utf-8"></script>
    <script type="text/javascript">
        $(function() {
            $('#center_tabs').tabs();

            $(".module").mouseover(function() {
                $(this).toggleClass("menu-hover");
            }).mouseout(function() {
                $(this).toggleClass("menu-hover");
            }).click(function() {
                $(".module").removeClass("menu-selected");
                $(this).addClass("menu-selected");
                var title = $(this).attr("title");
                var url = $(this).attr("url");
                open1(title, url);
            });
        });

        function open1(plugin, url){
            if ($('#center_tabs').tabs('exists',plugin)){
                $('#center_tabs').tabs('select', plugin);
            } else {
                $('#center_tabs').tabs('add',{
                    title:plugin,
                    href: url,
//                    content: '<iframe src="' + url + '" style="padding:0;margin:0;border:0;width:100%;height:100%;"></iframe>',
                    closable:true,
                    border: false,
                    fit: true
                });
            }
        }
    </script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">
    <div data-options="region:'center'">
        test
    </div>
</div>
</body>
</html>
