package net.kailyard.template.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import net.kailyard.template.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name = "ss_user")
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 2180691141367231227L;

    private String loginName;

    private String name;

    private String email;

    private String password;

    private String plainPassword;

    private String salt;

    private String roles;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 不持久化到数据库.
    @JSONField(serialize = false)
    @Transient
    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    @JSONField(serialize = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JSONField(serialize = false)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @JSONField(serialize = false)
    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @JSONField(serialize = false)
    @Transient
    public List<String> getRoleList() {
        return ImmutableList.copyOf(Splitter.on(",").omitEmptyStrings().split(roles));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("name", getName()).add("loginName", getLoginName()).toString();
    }
}
