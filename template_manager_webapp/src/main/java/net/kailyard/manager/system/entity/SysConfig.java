package net.kailyard.manager.system.entity;

import com.google.common.base.MoreObjects;
import net.kailyard.common.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ss_config")
public class SysConfig extends BaseEntity {
    private static final long serialVersionUID = -4586690464819814569L;

    private String configName;

    private String description;

    private String configValue;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("configName", getConfigName()).add("configValue", getConfigValue()).toString();
    }
}
