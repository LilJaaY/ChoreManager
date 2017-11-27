package com.project.seg2105.choremanager;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHandler {

    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference taskRef;
    private DatabaseReference equipmentRef;
    private DatabaseReference tempRef;

    public FirebaseHandler() {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        taskRef = database.getReference("tasks");
        equipmentRef = database.getReference("equipment");
    }

    public void addUser(User user) {
        tempRef = userRef.push();
        user.setKey(tempRef.getKey());
        tempRef.setValue(user);
    }

    public void addEquipment (Equipment equipment) {
        tempRef = equipmentRef.push();
        equipment.setKey(tempRef.getKey());
        tempRef.setValue(equipment);
    }

    public void addTask (Task task) {
        tempRef = taskRef.push();
        task.setKey(tempRef.getKey());
        tempRef.setValue(task);
    }

    public void removeUser(User user) {
        userRef.child(user.getKey()).removeValue();
    }

    public void removeTask(Task task) {
        taskRef.child(task.getKey()).removeValue();
    }

    public void removeEquipment(Equipment equipment) {
        equipmentRef.child(equipment.getKey()).removeValue();
    }

}
