package net.kailyard.manager.system.entity;

import com.google.common.base.MoreObjects;
import net.kailyard.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ss_dict")
public class Dict extends BaseEntity {
    private static final long serialVersionUID = 3957581910405484444L;

    private String name;
    private String dictValue;
    private String dictType;
    private int sort;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("dictType", getDictType()).add("dictValue", getDictValue()).add("name", getName()).toString();
    }
}
