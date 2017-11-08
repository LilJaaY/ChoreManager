package com.project.seg2105.choremanager;

import java.util.Date;

/**
 * Created by jalilcompaore on 06/11/17.
 */

public class Task {
    private int id;
    private int assignee_id;
    private String title;
    private String description;
    private String note;
    private Date deadline;

    public Task(String title, String description, String note, Date deadline) {
        this.title = title;
        this.description = description;
        this.note = note;
        this.deadline = deadline;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setAssignee_id(int assignee_id) {
        this.assignee_id = assignee_id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getAssignee_id() {
        return assignee_id;
    }
}
