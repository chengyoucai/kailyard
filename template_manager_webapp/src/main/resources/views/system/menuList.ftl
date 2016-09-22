<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function() {
        $('#dg_menu').treegrid({
            url : '${ctx}/menu/treeList.json',
            method : 'get',
            loadMsg:"数据加载中..",
            columns : [ [
                {
                    field : 'name',
                    title : '菜单名称',
                    width : 160
                },
                {
                    field : 'sort',
                    title : '排序编号',
                    width : 30
                },
                {
                    field : 'icon',
                    title : '菜单图标',
                    width : 60
                },
                {
                    field : 'createTime',
                    title : '创建时间',
                    width : 70
                },
                {
                    field : 'link',
                    title : '菜单链接',
                    width : 120
                }
            ] ]
        });
    });

    function addMenu() {
        $('#dlg_menu').dialog('setTitle', '新增菜单').dialog('center').dialog('open');
        $('#fm_menu_parentId_combobox').combobox('clear');
        $('#fm_menu').form('clear');
        $('#fm_menu_parentId_combobox').combobox('select','0');
    }

    function editMenu() {
        var row = $('#dg_menu').treegrid('getSelected');
        if (row) {
            $.get("${ctx}/menu/get.json?id=" + row.id, function (data) {
                $('#dlg_menu').dialog('setTitle', '编辑菜单').dialog('center').dialog('open');
                $('#fm_menu_parentId_combobox').combobox('clear');
                $('#fm_menu').form('clear').form('load', data);
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要修改的记录!'
            });
            return false;
        }
    }

    // 添加或者修改菜单
    function saveMenu() {
        $('#fm_menu').form('submit', {
            url : '${ctx}/menu/save.json',
            onSubmit: function () {
                return $(this).form('validate');
            },
            success : function(data) {
                try {
                    var r = $.parseJSON(data);
                    if (r.code==1) {
                        $.messager.show({
                            title : '成功',
                            msg : r.msg
                        });
                        $('#fm_menu_parentId_combobox').combobox('reload');
                        $('#dlg_menu').dialog('close');        // close the dialog
                        $('#dg_menu').treegrid('reload');    // reload the menu data
                    } else {
                        $.messager.show({
                            title : '失败',
                            msg : r.code + r.msg
                        });
                    }
                } catch (e) {
                    $.messager.alert('错误', data);
                }
            }
        });
    }

    // 删除菜单
    function delMenu() {
        var row = $('#dg_menu').treegrid('getSelected');
        if (null == row) {
            $.messager.show({
                title : '提示',
                msg : '请选择需要删除的数据!'
            });
            return false;
        }
        $.messager.confirm('确认', '您是否要删除该菜单及包含的子菜单?', function(r) {
            if (r) {
                $.ajax({
                    type: "GET",
                    url : '${ctx}/menu/del.json',
                    data : 'id=' + row.id,
                    dataType : 'json',
                    success : function(j) {
                        try {
                            $.messager.show({
                                title : '提示',
                                msg : j.msg
                            });
                            $('#fm_menu_parentId_combobox').combobox('reload');
                            $('#dg_menu').treegrid('unselectAll').treegrid('load');
                        } catch (e) {
                            $.messager.alert('提示', j);
                        }
                    }
                });
            }
        });
    }
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
    <div id="toolbar_menu">
        <table>
            <tr>
            <@shiro.hasPermission name="sys:menu:add">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addMenu();">新增</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:menu:del">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delMenu();">删除</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:menu:edit">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editMenu();">编辑</a></td>
            </@shiro.hasPermission>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dg_menu" style="height: 100%;" toolbar="#toolbar_menu" rownumbers="true" fitColumns="true" collapsible="true" border="false" animate="true" idField="id" treeField="name">
        </table>
    </div>
</div>

<div id="dlg_menu" class="easyui-dialog" buttons="#dlg_buttons_menu" modal="true" closed="true" style="width: 420px;height: 320px;padding:10px">
    <form id="fm_menu" method="post">
        <input type="hidden" name="version">
        <input type="hidden" name="id">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="width: 80px; height: 30px;" align="right">菜单名称：</td>
                <td><input name="name" id="name" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="required:true,validType:['isNotNull','length[1,32]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">菜单链接：</td>
                <td><input name="link" type="text" class="easyui-textbox"
                           style="height: 25px" data-options="validType:'length[1,128]'" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">排序号：</td>
                <td><input name="sort" type="number" class="easyui-numberbox" style="height: 25px"
                           data-options="required:true,min:0,precision:0,max:150" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">菜单图标：</td>
                <td><input class="easyui-combobox" name="icon" style="width:100%;" data-options="
                    showItemIcon: true,
                    data: [
                        {value:'icon-cog',text:'icon-cog',iconCls:'icon-cog'},
                        {value:'icon-app',text:'icon-app',iconCls:'icon-app'},
                        {value:'icon-book',text:'icon-book',iconCls:'icon-book'},
                        {value:'icon-clock',text:'icon-clock',iconCls:'icon-clock'},
                        {value:'icon-chart',text:'icon-chart',iconCls:'icon-chart'},
                        {value:'icon-page',text:'icon-page',iconCls:'icon-page'}
                    ],
                    editable: true,
                    panelHeight: 'auto'">
                </td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">所属菜单：</td>
                <td>
                    <input name="parentId" id="fm_menu_parentId_combobox" class="easyui-combobox" style="height: 25px" size="30"
                           data-options="
							url:'${ctx}/menu/getRootMenuList.json',
							method:'get',
							valueField:'id',
							textField:'name',
							panelHeight:'100',
							editable:false"
                    />
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_menu">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveMenu()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_menu').dialog('close')" style="width:90px">取消</a>
</div>
