package com.project.seg2105.choremanager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataSet {

    private User[] userArray;
    private Task[] taskArray;
    private Equipment[] equipmentArray;
    private FirebaseHandler dbHandler;

    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference taskRef;
    private DatabaseReference equipmentRef;

    public DataSet() {
        userArray = new User[10];
        taskArray = new Task[10];
        equipmentArray = new Equipment[10];
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");
        taskRef = database.getReference("tasks");
        equipmentRef = database.getReference("equipment");
        dbHandler = new FirebaseHandler();

        ChildEventListener userListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                boolean inserted = false;
                int index = 0;
                while (!inserted){
                    if (userArray[index] == null){
                        userArray[index] = user;
                        inserted = true;
                    }
                    index++;
                    if (index == userArray.length){
                        User[] newArray = new User[(userArray.length)*2];
                        for (int x = 0; x < userArray.length; x++){
                            newArray[x] = userArray[x];
                        }
                        userArray = newArray;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                int index = 0;
                for (int i = 0; i < userArray.length; i++) {
                    if (userArray[i].getKey() == user.getKey()) {
                        index = i;
                    }
                }
                userArray[index] = user;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                int index = 0;
                for (int i = 0; i < userArray.length; i++) {
                    if (userArray[i].getKey() == user.getKey()) {
                        index = i;
                    }
                }
                userArray[index] = null;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        ChildEventListener taskListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Task task = dataSnapshot.getValue(Task.class);
                boolean inserted = false;
                int index = 0;
                while (!inserted){
                    if (taskArray[index] == null){
                        taskArray[index] = task;
                        inserted = true;
                    }
                    index++;
                    if (index == taskArray.length){
                        Task[] newArray = new Task[(taskArray.length)*2];
                        for (int x = 0; x < taskArray.length; x++){
                            newArray[x] = taskArray[x];
                        }
                        taskArray = newArray;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Task task = dataSnapshot.getValue(Task.class);
                int index = 0;
                for (int i = 0; i < taskArray.length; i++) {
                    if (taskArray[i].getKey() == task.getKey()) {
                        index = i;
                    }
                }
                taskArray[index] = task;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                int index = 0;
                for (int i = 0; i < taskArray.length; i++) {
                    if (taskArray[i].getKey() == task.getKey()) {
                        index = i;
                    }
                }
                taskArray[index] = null;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        ChildEventListener equipmentListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Equipment equipment = dataSnapshot.getValue(Equipment.class);
                boolean inserted = false;
                int index = 0;
                while (!inserted){
                    if (equipmentArray[index] == null){
                        equipmentArray[index] = equipment;
                        inserted = true;
                    }
                    index++;
                    if (index == equipmentArray.length){
                        Equipment[] newArray = new Equipment[(equipmentArray.length)*2];
                        for (int x = 0; x < equipmentArray.length; x++){
                            newArray[x] = equipmentArray[x];
                        }
                        equipmentArray = newArray;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Equipment equipment = dataSnapshot.getValue(Equipment.class);
                int index = 0;
                for (int i = 0; i < equipmentArray.length; i++) {
                    if (equipmentArray[i].getKey() == equipment.getKey()) {
                        index = i;
                    }
                }
                equipmentArray[index] = equipment;
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Equipment equipment = dataSnapshot.getValue(Equipment.class);
                int index = 0;
                for (int i = 0; i < equipmentArray.length; i++) {
                    if (equipmentArray[i].getKey() == equipment.getKey()) {
                        index = i;
                    }
                }
                equipmentArray[index] = null;
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        userRef.addChildEventListener(userListener);
        taskRef.addChildEventListener(taskListener);
        equipmentRef.addChildEventListener(equipmentListener);
    }

    public User[] getUserArray(){
        return userArray;
    }

    public Equipment[] getEquipmentArray(){
        return equipmentArray;
    }

    public Task[] getTaskArray(){
        return taskArray;
    }

    public void addTask(Task task){
        dbHandler.addTask(task);
    }

    public void addUser(User user){
        dbHandler.addUser(user);
    }

    public void addEquipment(Equipment equipment){
        dbHandler.addEquipment(equipment);
    }

    public void modifyTask(Task task){
        dbHandler.removeTask(task);
        dbHandler.addTask(task);
    }

    public void modifyUser(User user){
        dbHandler.removeUser(user);
        dbHandler.addUser(user);
    }

    public void modifyEquipment(Equipment equipment){
        dbHandler.removeEquipment(equipment);
        dbHandler.addEquipment(equipment);
    }

    public void removeTask(Task task){
        dbHandler.removeTask(task);
    }

    public void removeUser(User user){
        dbHandler.removeUser(user);
    }

    public void removeEquipment(Equipment equipment){
        dbHandler.removeEquipment(equipment);
    }
}
