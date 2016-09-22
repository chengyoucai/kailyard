package net.kailyard.common.security;

import java.io.Serializable;
import java.util.Objects;

public class ShiroUser implements Serializable {
    private static final long serialVersionUID = 2032810002282279171L;

    public String loginName;
    public String name;
    public Long id;

    public ShiroUser(Long id, String loginName, String name) {
        this.id = id;
        this.loginName = loginName;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLoginName() {
        return loginName;
    }

    @Override
    public String toString() {
        return loginName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(loginName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ShiroUser)) {
            return false;
        }

        ShiroUser other = (ShiroUser) obj;
        if (loginName == null) {
            if (other.loginName != null) {
                return false;
            }
        } else if (!loginName.equals(other.loginName)) {
            return false;
        }
        return true;
    }
}
