package net.kailyard.manager.system.entity;

import com.google.common.base.MoreObjects;
import net.kailyard.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ss_role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = 1946434867048373481L;
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("name", getName()).toString();
    }
}
