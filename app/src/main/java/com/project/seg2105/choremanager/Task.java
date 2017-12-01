package com.project.seg2105.choremanager;

import java.util.Date;

/**
 * Created by jalilcompaore on 06/11/17.
 */

public class Task {
    private int id;
    private int creator_id;
    private int assignee_id;
    private String title;
    private String description;
    private String note;
    private String deadline;

    public Task(int creator_id, String title, String description, String note, String deadline) {
        this.creator_id = creator_id;
        this.title = title;
        this.description = description;
        this.note = note;
        this.deadline = deadline;
    }

    public Task(int creator_id, int assignee_id, String title, String description, String note, String deadline) {
        this.title = title;
        this.description = description;
        this.note = note;
        this.deadline = deadline;
        this.creator_id = creator_id;
        this.assignee_id = assignee_id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public int getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
