<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function() {
        $('#dg_role').datagrid({
            url : '${ctx}/role/list',
            loadMsg:"数据加载中..",
            columns : [ [
                {
                    field : 'id',
                    checkbox : true,
                    width : 15
                },
                {
                    field : 'name',
                    title : '角色名称',
                    width : 80
                },
                {
                    field : 'description',
                    title : '角色描述',
                    width : 160
                },
                {
                    field : 'createTime',
                    title : '创建时间',
                    width : 70
                }
            ] ]
        });

        //捕获回车
        $('#searchForm_role').keydown(function(event) {
            switch (event.keyCode) {
                case 13 :
                    searchRole();
                default :
                    break;
            }
        });
    });

    //查询
    function searchRole() {
        var flag = $('#searchForm_role').form('validate');
        if (flag) {
            $('#dg_role').datagrid('load', $('#searchForm_role').serializeObject());
        }
    }

    // 重置查询
    function cancelSearchRole() {
        $('#searchForm_role').form('clear');
        $('#dg_role').datagrid('load', {});
    }

    // 新增角色
    function addRole() {
        $('#dlg_role').dialog('setTitle', '新增角色').dialog('center').dialog('open');
        $('#fm_role').form('clear');
    }

    // 修改角色
    function editRole() {
        var row = $('#dg_role').datagrid('getSelected');
        if (row) {
            $.get("${ctx}/role/get.json?id=" + row.id, function (data) {
                $('#dlg_role').dialog('setTitle', '编辑角色').dialog('center').dialog('open');
                $('#fm_role').form('clear').form('load', data);
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要修改的记录!'
            });
            return false;
        }
    }

    // 添加或者修改角色
    function saveRole() {
        $('#fm_role').form('submit', {
            url : '${ctx}/role/save.json',
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
                        $('#dlg_role').dialog('close');        // close the dialog
                        $('#dg_role').datagrid('reload');    // reload the menu data
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

    // 删除角色
    function delRole() {
        var rows = $('#dg_role').datagrid('getChecked');
        if (rows.length == 0) {
            $.messager.show({
                title : '提示',
                msg : '请勾选需要删除的记录!'
            });
            return false;
        }
        var ids = '';
        for (var i = 0, len = rows.length; i < len; i++) {
            ids += rows[i].id + ',';
        }
        ids = ids.substring(0, ids.length - 1);
        $.messager.confirm('确认', '您是否要删除选中角色?', function(r) {
            if (r) {
                $.ajax({
                    type: "GET",
                    url : '${ctx}/role/del.json',
                    data : 'ids=' + ids,
                    dataType : 'json',
                    success : function(j) {
                        try {
                            $.messager.show({
                                title : '提示',
                                msg : j.msg
                            });
                            $('#dg_role').datagrid('load');
                            $('#dg_role').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
                        } catch (e) {
                            $.messager.alert('提示', j);
                        }
                    }
                });
            }
        });
    }

    //修改角色和菜单
    function editRolePermission(){
        var row = $('#dg_role').datagrid('getSelected');
        if (row) {
            $.messager.progress({text:'加载中...'});
            $('#fm_tree_role_permission').tree({
                url:'${ctx}/role/getRolePermissionTree.json?roleId=' + row.id,
                checkbox:true,
                onLoadSuccess: function(){
                    $.messager.progress('close');
                    $('#fm_role_permission_roleId').val(row.id);
                    $('#dlg_role_permission').dialog('center').dialog('open');
                },
                onLoadError: function(){
                    $.messager.progress('close');
                    $.messager.show({
                        title : '提示',
                        msg : '获取数据失败!'
                    });
                    return false;
                }
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要配置权限的记录!'
            });
            return false;
        }
    }

    //提交修改角色和菜单
    function saveRolePermissions(){
        var nodes = $('#fm_tree_role_permission').tree('getChecked');
        var permissionIds = [];
        for ( var e in nodes) {
            permissionIds.push(nodes[e].id);
            permissionIds.push(nodes[e].parentId);
        }
        $('#fm_role_permission_permissionIds').val(permissionIds.join(","));
        $('#fm_role_permission').form('submit', {
            url: '${ctx}/role/saveRolePermissions.json',
            onSubmit: function () {
                return $(this).form('validate');
            },
            success: function (data) {
                var r = $.parseJSON(data);
                if (r.code==1) {
                    $.messager.show({
                        title : '成功',
                        msg : r.msg
                    });
                } else {
                    $.messager.show({
                        title: '失败',
                        msg : r.code + r.msg
                    });
                }
                $('#dlg_role_permission').dialog('close');
            }
        });
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div id="toolbar_role">
        <table>
            <tr>
            <@shiro.hasPermission name="sys:role:add">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addRole();">新增</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:role:del">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delRole();">删除</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:role:edit">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editRole();">编辑</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:role:authority">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-up" plain="true" onclick="editRolePermission();">权限分配</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
                <td>
                    <form id="searchForm_role">
                        <label class="ui-label">角色名：</label> <input type="text" name="search_LIKE_name" data-options="validType:['length[0,30]'],validateOnCreate:false" class="easyui-textbox" />
                        <a href="javascript:searchRole();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                        <a href="javascript:cancelSearchRole();" class="easyui-linkbutton" iconCls="icon-cancel">重置</a>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dg_role" style="height: 100%;" toolbar="#toolbar_role" rownumbers="true" fitColumns="true" collapsible="true" border="false" animate="true" pagination="true" singleSelect="true" pageSize="20" pageList="[ 10, 20, 50 ]" checkOnSelect="false" selectOnCheck="false" nowrap="false" fit="true" idField="id">

        </table>
    </div>
</div>

<div id="dlg_role" class="easyui-dialog" buttons="#dlg_buttons_role" modal="true" closed="true" style="width: 420px;height: 220px;padding:10px">
    <form id="fm_role" method="post">
        <input type="hidden" name="version">
        <input type="hidden" name="id">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="width: 80px; height: 30px;" align="right">角色名称：</td>
                <td><input name="name" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="required:true,validType:['isNotNull','length[1,32]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">描述：</td>
                <td><input name="description" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="validType:'length[1,128]'" size="30" /></td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_role">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveRole()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_role').dialog('close')" style="width:90px">取消</a>
</div>

<div id="dlg_role_permission"  class="easyui-dialog" buttons="#dlg_buttons_role_permission" modal="true" closed="true" title="配置菜单和权限" style="width: 420px;height: 500px;padding:10px 10px">
    <form id="fm_role_permission" method="post">
        <input type="hidden" name="roleId" id="fm_role_permission_roleId"/>
        <input type="hidden" name="menuIds" id="fm_role_permission_permissionIds"/>
        <div class="fitem">
            <ul id="fm_tree_role_permission"></ul>
        </div>
    </form>
</div>

<div id="dlg_buttons_role_permission">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveRolePermissions()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_role_permission').dialog('close')" style="width:90px">取消</a>
</div>
