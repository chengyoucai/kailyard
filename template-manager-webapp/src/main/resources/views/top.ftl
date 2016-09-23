<#assign ctx = rc.getContextPath()>
<script type="text/javascript">
    $(function(){
//        var easyuiThemeValue = $.cookie('easyuiTheme');
//        if(easyuiThemeValue==null){
//            easyuiThemeValue = 'default';
//        }
        $('#themes').combobox({
            editable: false,
            onLoadSuccess : function() {
                $(this).combobox('setValue', 'default');
//                changeTheme(easyuiThemeValue);
            },
            onSelect : function(rec) {
                changeTheme(rec.value);
            }
        });
    });

    function changeTheme(themeName){
        var href = '${ctx}/static/easyui/themes/' + themeName + '/easyui.css';
        $('#easyuiTheme').attr('href', href);
        var iframe = $('iframe');
        if(iframe.length > 0){
            for(var i = 0; i < iframe.length; i++){
                var ifr = iframe[i];
                $(ifr).contents().find('#easyuiTheme').attr('href', href);
            }
        }
    }

    // 修改
    function editSysUserPassword() {
        $('#fm_sysuser_password').form('clear');
        $('#dlg_sysuser_password').dialog('setTitle', '修改密码').dialog('center').dialog('open');
    }

    // 提交修改
    function saveSysUserPassword() {
        $('#fm_sysuser_password').form('submit', {
            url : '${ctx}/sysUser/savePa.json',
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
                        $('#fm_sysuser_password').form('clear');
                        $('#dlg_sysuser_password').dialog('close');        // close the dialog
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
<div id="header" style="background-image: url('${ctx }/static/images/hd.jpg'); height: 60px;">
    <div>
        <span style="text-align: left; font-size: 32px; color: white;">&nbsp;管理系统</span>
    </div>
    <ul class="nav">
        <li> 您好, <a href="javascript:editSysUserPassword();">${user.name }</a></li>
        <li><a href="${ctx}/logout">退出</a></li>
    </ul>

    <ul class="themeList">
        更换皮肤风格：
        <select id="themes">
            <option value="default">天空蓝(默认)</option>
            <option value="bootstrap">灰霾</option>
            <option value="gray">银色</option>
            <option value="metro">磁贴</option>
            <option value="black">黑色</option>
        </select>
    </ul>
</div>
<div id="dlg_sysuser_password" class="easyui-dialog" buttons="#dlg_buttons_sysuser_password" modal="true" closed="true" style="width: 420px;height: 330px;padding:10px">
    <form id="fm_sysuser_password" method="post">
        <table class="tableForm" style="padding: 10px 0px 0px 20px;">
            <tr>
                <td style="height: 40px;" align="right">旧密码：</td>
                <td><input name="oldPassword" type="password" class="easyui-textbox" style="height: 25px"
                           data-options="required:true" size="30" /></td>
                </td>
            </tr>
            <tr>
                <td style="height: 40px;" align="right">密码：</td>
                <td><input name="plainPassword" type="password" class="easyui-textbox" style="height: 25px"
                           data-options="required:true,validType:['isNotNull','length[5,20]']" size="30" /></td>
                </td>
            </tr>
            <tr>
                <td align="right">确认密码：</td>
                <td><input name="confirmPassword" type="password" class="easyui-textbox" style="height: 25px"
                           data-options="required:true,validType:['length[5,20]','eqPwd[\'#fm_sysuser_password input[name=plainPassword]\']']" size="30" /></td>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg_buttons_sysuser_password">
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="saveSysUserPassword()">保存</a>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true"
       onclick="javascript:$('#dlg_sysuser_password').dialog('close')">取消</a>
</div>
