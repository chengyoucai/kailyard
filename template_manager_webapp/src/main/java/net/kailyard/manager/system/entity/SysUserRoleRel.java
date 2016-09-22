package net.kailyard.manager.system.entity;

import net.kailyard.common.entity.RelEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ss_user_role_rel")
public class SysUserRoleRel extends RelEntity {
    private static final long serialVersionUID = 3151108680388833132L;

    private Long userId;
    private Long roleId;

    public SysUserRoleRel(){}

    public SysUserRoleRel(Long userId, Long roleId, Long createUserId, Date createTime) {
        this.userId = userId;
        this.roleId = roleId;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
