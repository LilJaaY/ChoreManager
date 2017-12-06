package com.project.seg2105.choremanager;

/**
 * Created by jalil on 05/12/17.
 */

public class Item {

    private int id;
    private String name;
    private String category;

    public Item(String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
