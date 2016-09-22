package net.kailyard.manager.system.entity;

import net.kailyard.common.entity.RelEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "ss_role_menu_rel")
public class RoleMenuRel extends RelEntity {
    private static final long serialVersionUID = 3505763972175345908L;

    private Long roleId;
    private Long menuId;

    public RoleMenuRel(){}

    public RoleMenuRel(Long roleId, Long menuId, Long createUserId, Date createTime){
        this.roleId = roleId;
        this.menuId = menuId;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
