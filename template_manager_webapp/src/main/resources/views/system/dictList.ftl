<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function() {
        $('#dg_dict').datagrid({
            url : '${ctx}/dict/list',
            loadMsg:"数据加载中..",
            columns : [ [
                {
                    field : 'dictType',
                    title : '数据字典分类',
                    width : 50
                },
                {
                    field : 'dictValue',
                    title : '数据字典值',
                    width : 50
                },
                {
                    field : 'name',
                    title : '名字',
                    width : 50
                },
                {
                    field : 'sort',
                    title : '序号',
                    width : 50
                },
                {
                    field : 'description',
                    title : '描述',
                    width : 110
                }
            ] ]
        });

        //捕获回车
        $('#searchForm_dict').keydown(function(event) {
            switch (event.keyCode) {
                case 13 :
                    searchDict();
                default :
                    break;
            }
        });
    });

    //查询
    function searchDict() {
        var flag = $('#searchForm_dict').form('validate');
        if (flag) {
            $('#dg_dict').datagrid('load', $('#searchForm_dict').serializeObject());
        }
    }

    // 重置查询
    function cancelSearchDict() {
        $('#searchForm_dict').form('clear');
        $('#dg_dict').datagrid('load', {});
    }

    // 新增
    function addDict() {
        $('#dlg_dict').dialog('setTitle', '新增数据').dialog('center').dialog('open');
        $('#fm_dict').form('clear');
    }

    // 修改
    function editDict() {
        var row = $('#dg_dict').datagrid('getSelected');
        if (row) {
            $.get("${ctx}/dict/get.json?id=" + row.id, function (data) {
                $('#dlg_dict').dialog('setTitle', '编辑数据').dialog('center').dialog('open');
                $('#fm_dict').form('clear').form('load', data);
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要修改的记录!'
            });
            return false;
        }
    }

    //  保存修改
    function saveDict() {
        $('#fm_dict').form('submit', {
            url : '${ctx}/dict/save.json',
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
                        $('#dlg_dict').dialog('close');        // close the dialog
                        $('#dg_dict').datagrid('reload');    // reload the menu data
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


    function delDict() {
        var row = $('#dg_dict').treegrid('getSelected');
        if (null == row) {
            $.messager.show({
                title : '提示',
                msg : '请选择需要删除的数据!'
            });
            return false;
        }
        $.messager.confirm('确认', '您是否要删除该数据?', function(r) {
            if (r) {
                $.ajax({
                    type: "GET",
                    url : '${ctx}/dict/del.json',
                    data : 'id=' + row.id,
                    dataType : 'json',
                    success : function(j) {
                        try {
                            $.messager.show({
                                title : '提示',
                                msg : j.msg
                            });
                            $('#dg_dict').datagrid('load');
                            $('#dg_dict').datagrid('uncheckAll').datagrid('unselectAll').datagrid('clearSelections');
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
    <div id="toolbar_dict">
        <table>
            <tr>
            <@shiro.hasPermission name="sys:dict:add">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addDict();">新增</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:dict:del">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="delDict();">删除</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
            <@shiro.hasPermission name="sys:dict:edit">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editDict();">编辑</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
                <td>
                    <form id="searchForm_dict">
                        <label class="ui-label">字典分类：</label> <input type="text" name="search_LIKE_dictType" data-options="validType:['length[0,50]'],validateOnCreate:false" class="easyui-textbox" />
                        <a href="javascript:searchDict();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                        <a href="javascript:cancelSearchDict();" class="easyui-linkbutton" iconCls="icon-cancel">重置</a>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dg_dict" style="height: 100%;" toolbar="#toolbar_dict" rownumbers="true" fitColumns="true" collapsible="true" border="false" animate="true" pagination="true" singleSelect="true" pageSize="20" pageList="[ 10, 20, 50 ]" checkOnSelect="false" selectOnCheck="false" nowrap="false" fit="true" idField="id">
        </table>
    </div>
</div>

<div id="dlg_dict" class="easyui-dialog" buttons="#dlg_buttons_dict" modal="true" closed="true" style="width: 420px;height: 350px;padding:10px">
    <form id="fm_dict" method="post">
        <input type="hidden" name="version">
        <input type="hidden" name="id">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="width: 80px; height: 30px;" align="right">字典分类：</td>
                <td><input name="dictType" type="text" class="easyui-textbox" style="height:25px"
                           data-options="required:true,validType:['length[0,255]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px" align="right">名称：</td>
                <td><input name="name" type="text" class="easyui-textbox" style="height:25px"
                           data-options="required:true,validType:['length[0,255]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px" align="right">字典值：</td>
                <td><input name="dictValue" type="text" class="easyui-textbox" style="height:25px"
                           data-options="required:true,validType:['length[0,255]']" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px" align="right">描述：</td>
                <td><input name="description" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="validType:'length[0,255]'" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px" align="right">序号：</td>
                <td><input name="sort" type="number" class="easyui-numberbox" style="height: 25px"
                           data-options="required:true,min:0,precision:0,max:150" size="30" /></td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_dict">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveDict()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_dict').dialog('close')" style="width:90px">取消</a>
</div>
