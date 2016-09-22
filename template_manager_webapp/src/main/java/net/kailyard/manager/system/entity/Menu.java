package net.kailyard.manager.system.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import net.kailyard.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ss_menu")
public class Menu extends BaseEntity {
    private static final long serialVersionUID = -5454308507629139L;

    private String name;

    private Long parentId;

    private String link;

    private Integer sort;

    private String icon;

    private String description;

    private List<Menu> children;

    private List<Permission> childPermission;

    public Menu(){}

    public Menu(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    @JSONField(serialize = false)
    @OneToMany(mappedBy = "menuId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Permission> getChildPermission() {
        return childPermission;
    }

    public void setChildPermission(List<Permission> childPermission) {
        this.childPermission = childPermission;
    }

    /**
     * 添加二级菜单
     *
     * @param child
     */
    public void addChildren(Menu child) {
        if (null == children) {
            children = Lists.newArrayList();
        }
        children.add(child);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues().add("id", getId()).add("name", getName()).add("link", getLink()).add("parentId", getParentId()).toString();
    }
}
