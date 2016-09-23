<#assign ctx = rc.getContextPath()>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>管理系统</title>
    <link id="easyuiTheme" rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/default/easyui.css" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/icon.css" charset="utf-8" />
    <link rel="stylesheet" type="text/css" href="${ctx}/static/easyui/themes/color.css" charset="utf-8" />
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
    <div data-options="region:'north',split:true, border:false, href:'${ctx}/currentUser.html'"
         style="height: 50px; overflow: hidden;"></div>
    <div data-options="region:'west',split:true" title="主菜单"
         style="width: 200px; overflow: hidden;">
        <div id="menu_tree" class="easyui-accordion" data-options="border:false" style="height: 100%;">
        <#list menuList as menu>
            <div title="${menu.name}" data-options="iconCls:'${menu.icon!!}'" style="overflow: auto;">
                <ul class="menu-ul">
                    <#list menu.children as child>
                        <li class="module" id="${child.id }" url="${child.link}" title="${child.name}">
                            <div class="icon-menu"><div class="text-menu">${child.name}</div></div>
                        </li>
                    </#list>
                </ul>
            </div>
        </#list>
        </div>
    </div>
    <div data-options="region:'center'"
         style="overflow: hidden;">
        <div id="center_tabs" class="easyui-tabs" fit="true" border="false" plain="true">
            <div title="首页" href="${ctx}/center" style="padding:10px"></div>
        </div>
    </div>
    <div data-options="region:'south',border:false,collapsible:false" title="CopyRight © 2015-">
    </div>
</div>
</body>
</html>
