package com.project.seg2105.choremanager;

/**
 * Created by jalil on 30/11/17.
 */

public class Tool {
    private int id;
    private String name;
    private String icon;

    public Tool() {}

    public Tool(String name, String icon) {
        this.icon = icon;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
