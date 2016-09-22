<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function() {
        $('#dg_sysconfig').datagrid({
            url : '${ctx}/sysConfig/list',
            loadMsg:"数据加载中..",
            columns : [ [
                {
                    field : 'configName',
                    title : '系统参数名称',
                    width : 140
                },
                {
                    field : 'configValue',
                    title : '系统参数值',
                    width : 140
                },
                {
                    field : 'description',
                    title : '参数描述',
                    width : 110
                }
            ] ]
        });

        //捕获回车
        $('#searchForm_sysconfig').keydown(function(event) {
            switch (event.keyCode) {
                case 13 :
                    searchSysconfig();
                default :
                    break;
            }
        });
    });

    //查询
    function searchSysConfig() {
        var flag = $('#searchForm_sysconfig').form('validate');
        if (flag) {
            $('#dg_sysconfig').datagrid('load', $('#searchForm_sysconfig').serializeObject());
        }
    }

    // 重置查询
    function cancelSearchSysConfig() {
        $('#searchForm_sysconfig').form('clear');
        $('#dg_sysconfig').datagrid('load', {});
    }

    // 修改
    function editSysConfig() {
        var row = $('#dg_sysconfig').datagrid('getSelected');
        if (row) {
            $.get("${ctx}/sysConfig/get.json?id=" + row.id, function (data) {
                $('#dlg_sysconfig').dialog('setTitle', '编辑参数').dialog('center').dialog('open');
                $('#fm_sysconfig').form('clear').form('load', data);
            });
        } else {
            $.messager.show({
                title : '提示',
                msg : '请选择需要修改的记录!'
            });
            return false;
        }
    }

    //  修改系统参数
    function saveSysConfig() {
        $('#fm_sysconfig').form('submit', {
            url : '${ctx}/sysConfig/save.json',
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
                        $('#dlg_sysconfig').dialog('close');        // close the dialog
                        $('#dg_sysconfig').datagrid('reload');    // reload the menu data
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
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
    <div id="toolbar_sysconfig">
        <table>
            <tr>
            <@shiro.hasPermission name="sys:config:edit">
                <td><a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editSysConfig();">编辑</a></td>
                <td><span class="toolbar-item dialog-tool-separator"></span></td>
            </@shiro.hasPermission>
                <td>
                    <form id="searchForm_sysconfig">
                        <label class="ui-label">系统参数名称：</label> <input type="text" name="search_LIKE_configName" data-options="validType:['length[0,30]'],validateOnCreate:false" class="easyui-textbox" />
                        <a href="javascript:searchSysConfig();" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                        <a href="javascript:cancelSearchSysConfig();" class="easyui-linkbutton" iconCls="icon-cancel">重置</a>
                    </form>
                </td>
            </tr>
        </table>
    </div>
    <div data-options="region:'center',border:false">
        <table id="dg_sysconfig" style="height: 100%;" toolbar="#toolbar_sysconfig" rownumbers="true" fitColumns="true" collapsible="true" border="false" animate="true" pagination="true" singleSelect="true" pageSize="20" pageList="[ 10, 20, 50 ]" checkOnSelect="false" selectOnCheck="false" nowrap="false" fit="true" idField="id">
        </table>
    </div>
</div>

<div id="dlg_sysconfig" class="easyui-dialog" buttons="#dlg_buttons_sysconfig" modal="true" closed="true" style="width: 420px;height: 350px;padding:10px">
    <form id="fm_sysconfig" method="post">
        <input type="hidden" name="version">
        <input type="hidden" name="id">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="width: 80px; height: 30px;" align="right">参数名称：</td>
                <td><input name="configName" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="editable:false,readonly:true,disable:true" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px" align="right">参数值：</td>
                <td><input name="configValue" type="text" class="easyui-textbox" style="height:165px"
                           data-options="validType:['length[0,1000]'],multiline:true" size="30" /></td>
            </tr>
            <tr>
                <td style="height: 30px" align="right">描述：</td>
                <td><input name="description" type="text" class="easyui-textbox" style="height: 25px"
                           data-options="validType:'length[0,512]'" size="30" /></td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_sysconfig">
    <a href="javascript:void(0)" class="easyui-linkbutton c6" iconCls="icon-ok" onclick="saveSysConfig()" style="width:90px">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg_sysconfig').dialog('close')" style="width:90px">取消</a>
</div>
