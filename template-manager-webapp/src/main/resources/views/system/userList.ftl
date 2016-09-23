<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function() {
        $('#dg_sysuser').datagrid({
            url : '${ctx}/sysUser/list.json',
            loadMsg:"数据加载中..",
            columns : [ [
                {
                    field : 'id',
                    checkbox : true,
                    width : 15
                },
                {
                    field : 'loginName',
                    title : '登录名',
                    width : 80
                },
                {
                    field : 'name',
                    title : '用户名',
                    width : 80
                },
                {
                    field : 'email',
                    title : '邮箱',
                    width : 120
                },
                {
                    field : 'createTime',
                    title : '创建时间',
                    width : 70
                }
            ] ]
        });
        //捕获回车
        $('#searchForm_sysuser').keydown(function(event) {
            switch (event.keyCode) {
                case 13 :
                    searchSysUser();
                default :
                    break;
            }
        });
        $('#fm_dg_sysuser_role').datagrid({columns : [ [
            {
                field : 'id',
                checkbox : true
            },
            {
                field : 'text',
                title : '角色',
                width : 260
            }
        ] ]});
    });

    //查询
    function searchSysUser() {
        var flag = $('#searchForm_sysuser').form('validate');
        if (flag) {
            $('#dg_sysuser').datagrid('load', $('#searchForm_sysuser').serializeObject());
        }
    }

    // 重置查询
    function cancelSearchSysUser() {
        $('#searchForm_sysuser').form('clear');
        $('#dg_sysuser').datagrid('load', {});
    }

    // 新增操作员
    function addSysUser() {
        $('#fm_sysuser').form('clear');
        $('#fm_sysuser_loginName').textbox({editable:true,disabled:false});
        $('#fm_sysuser_plainPassword').validatebox({required:true});
        $('#fm_sysuser_confirmPassword').validatebox({required:true});
        $('#fm_sysuser_plainPassword_warn').hide();
        $('#dlg_sysuser').dialog('setTitle', '新增操作员').dialog('center').dialog('open');
    }

    // 修改操作员
    function editSysUser() {
        var row = $('#dg_sysuser').datagrid('getSelected');
        if (row) {
            $.get("${ctx}/sysUser/get.json?id=" + row.id, function (data) {
                $('#fm_sysuser').form('clear').form('load', data);
                $('#fm_sysuser_loginName').textbox({editable:false,disabled:true});
                $('#fm_sysuser_plainPassword').validatebox({required:false});
                $('#fm_sysuser_confirmPassword').validatebox({required:false});
                $('#fm_sysuser_plainPassword_warn').show();
                $('#dlg_sysuser').dialog('setTitle', '编辑操作员').dialog('center').dialog('open');
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要修改的记录!'
            });
            return false;
        }
    }

    // 添加或者修改操作员
    function saveSysUser() {
        $('#fm_sysuser').form('submit', {
            url : '${ctx}/sysUser/save.json',
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
                        $('#dlg_sysuser').dialog('close');        // close the dialog
                        $('#dg_sysuser').datagrid('reload');    // reload the menu data
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
    // 删除操作员
    function delSysUser() {
        var rows = $('#dg_sysuser').datagrid('getChecked');
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
        $.messager.confirm('确认', '您是否要删除选中操作员?', function(r) {
            if (r) {
                $.ajax({
                    type: "GET",
                    url : '${ctx}/sysUser/del.json',
                    data : 'ids=' + ids,
                    dataType : 'json',
                    success : function(j) {
                        try {
                            $.messager.show({
                                title : '提示',
                                msg : j.msg
                            });
                            $('#dg_sysuser').datagrid('load');
                            $('#dg_sysuser').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
                        } catch (e) {
                            $.messager.alert('提示', j);
                        }
                    }
                });
            }
        });
    }

    // 配置操作员角色
    function editSysUserRoles(){
        var row = $('#dg_sysuser').datagrid('getSelected');
        if (row) {
            $.messager.progress({text:'加载中...'});
            $('#fm_dg_sysuser_role').datagrid('clearSelections');
            $('#fm_dg_sysuser_role').datagrid({
                url:'${ctx}/sysUser/getSysUserRoleTree.json?userId=' + row.id,
                method : 'get',
                loadMsg:"数据加载中..",
                onLoadSuccess: function(data){
                    var rowData = data.rows;
                    $.each(rowData,function(index,row){
                        if(row.checked){
                            $("#fm_dg_sysuser_role").datagrid("selectRecord", row.id);
                        }
                    });

                    $.messager.progress('close');
                    $('#fm_sysuser_role_userId').val(row.id);
                    $('#dlg_sysuser_role').dialog('center').dialog('open');
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
                msg : '请选择需要配置角色的记录!'
            });
            return false;
        }
    }

    //提交修改操作员角色
    function saveSysUserRoles(){
        $('#fm_sysuser_role').form('submit', {
            url: '${ctx}/sysUser/saveSysUserRoles.json',
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
                $('#dlg_sysuser_role').dialog('close');
            }
        });
    }
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div id="toolbar_sysuser">
        <table>
            <tr>
            <@shiro.hasPermission name="sys:user:add">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addSysUser();">新增</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:user:del">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delSysUser();">删除</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:user:edit">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editSysUser();">修改</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:user:role:auth">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-up" plain="true" onclick="editSysUserRoles();">角色授权</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
                <td>
                    <form id="searchForm_sysuser">
                        <label class="ui-label">用户名：</label> <input type="text" name="search_LIKE_name" data-options="validType:['length[0,50]'],validateOnCreate:false" class="easyui-textbox" size="18"/>
                        <label class="ui-label">登录名：</label> <input type="text" name="search_LIKE_loginName" data-options="validType:['length[0,50]'],validateOnCreate:false" class="easyui-textbox" size="18"/>
                        <label class="ui-label">邮箱：</label> <input type="text" name="search_LIKE_email" data-options="validType:['length[0,50]'],validateOnCreate:false" class="easyui-textbox" size="18"/>
                        <a href="javascript:searchSysUser();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                        <a href="javascript:cancelSearchSysUser();" class="easyui-linkbutton" iconCls="icon-cancel">重置</a>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dg_sysuser" style="height: 100%;" toolbar="#toolbar_sysuser" rownumbers="true" fitColumns="true" collapsible="true" border="false" animate="true" pagination="true" singleSelect="true" pageSize="20" pageList="[ 10, 20, 50 ]" checkOnSelect="false" selectOnCheck="false" nowrap="false" fit="true">
        </table>
    </div>
</div>

<div id="dlg_sysuser" class="easyui-dialog" buttons="#dlg_buttons_sysuser" modal="true" closed="true" style="width: 420px;height: 330px;padding:10px">
    <form id="fm_sysuser" method="post">
        <input type="hidden" name="version">
        <input type="hidden" name="id" id="dlg_sysuser_userId">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="width: 70px;height: 40px;" align="right">登录名：</td>
                <td><input name="loginName" type="text" id="fm_sysuser_loginName" class="easyui-textbox"
                           data-options="required:true, validType:'length[5,20]'" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 40px" align="right">用户名：</td>
                <td><input name="name" type="text" class="easyui-textbox" data-options="required:true, validType:'length[2,20]'" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 40px" align="right">密码：</td>
                <td><input id="fm_sysuser_plainPassword" name="plainPassword" type="password"  class="easyui-validatebox textbox" style="height:23px;padding-left:4px;" data-options="validType:'length[5,20]'" size="30"/>
                    <div id="fm_sysuser_plainPassword_warn"><font color="gray">不改变则留空</font></div>
                </td>
            </tr>
            <tr>
                <td style="height: 40px" align="right">确认密码：</td>
                <td><input id="fm_sysuser_confirmPassword" name="confirmPassword" type="password" class="easyui-validatebox textbox" style="height:23px;padding-left:4px;" data-options="validType:['length[5,20]','eqPwd[\'#fm_sysuser input[name=plainPassword]\']']" size="30"/></td>
            </tr>
            <tr>
                <td style="height: 40px" align="right">邮箱：</td>
                <td><input name="email" type="text" class="easyui-textbox"
                           data-options="validType:'email'" size="30" /></td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_sysuser">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveSysUser()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_sysuser').dialog('close')" style="width:90px">取消</a>
</div>

<div id="dlg_sysuser_role"  class="easyui-dialog" buttons="#dlg_buttons_sysuser_role" modal="true" closed="true" title="配置角色" style="width: 420px;height: 500px">
    <form id="fm_sysuser_role" method="post">
        <input type="hidden" name="userId" id="fm_sysuser_role_userId"/>
        <div class="fitem">
            <table id="fm_dg_sysuser_role" style="height: 400px;" rownumbers="true" fitColumns="true" collapsible="true" border="false" animate="true" idField="id" selectOnCheck="true" singleSelect="false">
            </table>
        </div>
    </form>
</div>

<div id="dlg_buttons_sysuser_role">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveSysUserRoles()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_sysuser_role').dialog('close')" style="width:90px">取消</a>
</div>



