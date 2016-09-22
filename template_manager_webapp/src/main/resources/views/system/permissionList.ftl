<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function() {
        $('#dg_permission').datagrid({
            url : '${ctx}/permission/list',
            loadMsg:"数据加载中..",
            columns : [ [
                {
                    field : 'id',
                    order : 'desc',
                    sortable: true,
                    width : 10
                },
                {
                    field : 'name',
                    title : '权限名称',
                    width : 50
                },
                {
                    field : 'permission',
                    title : '权限',
                    width : 50
                },
                {
                    field : 'menuName',
                    title : '所属菜单',
                    width : 30
                },
                {
                    field : 'description',
                    title : '权限描述',
                    width : 120
                },
                {
                    field : 'createTime',
                    title : '创建时间',
                    width : 50
                }
            ] ]
        });

        //捕获回车
        $('#searchForm_permission').keydown(function(event) {
            switch (event.keyCode) {
                case 13 :
                    searchPermission();
                default :
                    break;
            }
        });
    });

    //查询
    function searchPermission() {
        var flag = $('#searchForm_permission').form('validate');
        if (flag) {
            var para = $('#searchForm_permission').serializeObject();
            if(para.search_EQ_menuId=='-1'){
                para.search_EQ_menuId = '';
            }
            $('#dg_permission').datagrid('load', para);
        }
    }

    // 重置查询
    function cancelSearchPermission() {
        $('#searchForm_permission').form('clear');
        $('#dg_permission').datagrid('load', {});
    }

    // 新增
    function addPermission() {
        $('#dlg_permission').dialog('setTitle', '新增权限').dialog('center').dialog('open');
        $('#fm_permission_menuId_combotree').combotree('clear');
        $('#fm_permission').form('clear');
    }

    // 修改
    function editPermission() {
        var row = $('#dg_permission').datagrid('getSelected');
        if (row) {
            $.get("${ctx}/permission/get.json?id=" + row.id, function (data) {
                $('#dlg_permission').dialog('setTitle', '编辑权限').dialog('center').dialog('open');
                $('#fm_permission_menuId_combotree').combotree('clear');
                $('#fm_permission').form('clear').form('load', data);
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要修改的记录!'
            });
            return false;
        }
    }

    // 添加或者修改权限
    function savePermission() {
        $('#fm_permission').form('submit', {
            url : '${ctx}/permission/save.json',
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
                        $('#dlg_permission').dialog('close');        // close the dialog
                        $('#dg_permission').datagrid('reload');    // reload the menu data
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

    // 删除
    function delPermission() {
        var row = $('#dg_permission').treegrid('getSelected');
        if (null == row) {
            $.messager.show({
                title : '提示',
                msg : '请选择需要删除的记录!'
            });
            return false;
        }
        $.messager.confirm('确认', '您是否要删除该权限?', function(r) {
            if (r) {
                $.ajax({
                    type: "GET",
                    url : '${ctx}/permission/del.json',
                    data : 'id=' + row.id,
                    dataType : 'json',
                    success : function(j) {
                        try {
                            $.messager.show({
                                title : '提示',
                                msg : j.msg
                            });
                            $('#fm_menu_parentId_combotree').combotree('reload');
                            $('#dg_permission').datagrid('load');
                            $('#dg_permission').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
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
    <div id="toolbar_permission">
        <table>
            <tr>
            <@shiro.hasPermission name="sys:permission:add">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addPermission();">新增</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:permission:del">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delPermission();">删除</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:permission:edit">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editPermission();">编辑</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
                <td>
                    <form id="searchForm_permission">
                        <label class="ui-label">菜单：</label>
                        <input name="search_EQ_menuId" class="easyui-combotree"  editable="false" style="height: 25px" size="27"
                               data-options="
                                url:'${ctx}/permission/menuList.json',
                                method:'get',
                                panelHeight:'200',
                                customAttr:{
                                    idField: 'id',
                                    textField: 'name',
                                    childrenField: 'childrens',
                                },
                                onBeforeSelect: function (node) {
                                    return $(this).tree('isLeaf', node.target);
                                },
                                value: '-1'
                                "
                        />
                        <label class="ui-label">权限名称：</label> <input type="text" name="search_LIKE_name" data-options="validType:['length[0,30]'],validateOnCreate:false" class="easyui-textbox" size="15"/>
                        <label class="ui-label">权限：</label> <input type="text" name="search_LIKE_permission" data-options="validType:['length[0,30]'],validateOnCreate:false" class="easyui-textbox"  size="15"/>
                        <a href="javascript:searchPermission();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                        <a href="javascript:cancelSearchPermission();" class="easyui-linkbutton" iconCls="icon-cancel">重置</a>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dg_permission" style="height: 100%;" toolbar="#toolbar_permission" sortName="id" sortOrder="desc" fitColumns="true" collapsible="true" border="false" animate="true" pagination="true" singleSelect="true" pageSize="20" pageList="[ 10, 20, 50 ]" checkOnSelect="false" selectOnCheck="false" nowrap="false" fit="true" idField="id">

        </table>
    </div>
</div>

<div id="dlg_permission" class="easyui-dialog" buttons="#dlg_buttons_permission" modal="true" closed="true" style="width: 420px;height: 330px;padding:10px">
    <form id="fm_permission" method="post">
        <input type="hidden" name="version">
        <input type="hidden" name="id">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="width: 80px; height: 30px;" align="right">权限名称：</td>
                <td><input name="name" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="required:true,validType:['isNotNull','length[1,32]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">权限：</td>
                <td><input name="permission" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="required:true,validType:['isNotNull','length[1,32]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">所属菜单：</td>
                <td><input name="menuId" id="fm_permission_menuId_combotree" class="easyui-combotree" editable="false" required="true"
                           size="30" data-options="
                                url:'${ctx}/permission/menuList.json?s=all',
                                method:'get',
                                panelHeight:'200',
                                customAttr:{
                                    idField: 'id',
                                    textField: 'name',
                                    childrenField: 'childrens',
                                },
                                onBeforeSelect: function (node) {
                                    return $(this).tree('isLeaf', node.target);
                                }"
                /></td>
            </tr>
            <tr>
                <td style="height: 30px;" align="right">描述：</td>
                <td><input name="description" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="validType:'length[1,128]'" size="30" /></td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_permission">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="savePermission()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_permission').dialog('close')" style="width:90px">取消</a>
</div>
