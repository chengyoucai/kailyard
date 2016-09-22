package net.kailyard.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tree implements Serializable {
    private static final long serialVersionUID = -935859537873941180L;

    private String id;

    private String text;

    private String parentId;

    private boolean checked = false;

    private String state;

    private List<Tree> children;

    public Tree() {}

    public Tree(String id, String text, String parentId) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
    }

    public Tree(String id, String text, String parentId, boolean checked) {
        this.id = id;
        this.text = text;
        this.parentId = parentId;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public void addChild(Tree child){
        if(children==null){
            children = new ArrayList<Tree>();
        }
        children.add(child);
    }
}
