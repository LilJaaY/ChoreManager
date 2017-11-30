package com.project.seg2105.choremanager;

/**
 * Created by jalil on 30/11/17.
 */

public class Usage {
    private int id;
    private int tool_id;
    private int task_id;

    public Usage(int tool_id, int task_id) {
        this.tool_id = tool_id;
        this.task_id = task_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTool_id() {
        return tool_id;
    }

    public void setTool_id(int tool_id) {
        this.tool_id = tool_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
}
