package net.kailyard.template.system.entity;

import net.kailyard.template.common.entity.RelEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ss_role_permission_rel")
public class RolePermissionRel extends RelEntity {
    private static final long serialVersionUID = 3975810531749667725L;
    private Long roleId;
    private Long permissionId;

    public RolePermissionRel(){}

    public RolePermissionRel(Long roleId, Long permissionId, Long createUserId, Date createTime) {
        this.roleId = roleId;
        this.permissionId = permissionId;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
