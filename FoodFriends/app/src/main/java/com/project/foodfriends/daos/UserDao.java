package com.project.foodfriends.daos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.project.foodfriends.entities.User;

public class UserDao {
    private static UserDao instance = null;
    private DatabaseReference mDatabase;

    private UserDao(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static UserDao getInstance(){
        if(instance == null){
            instance = new UserDao();
        }
        return instance;
    }

    public void insertUser(String userId, User user){
        mDatabase.child("users").child(userId).setValue(user);
    }

    public User getUser(String userId){
        return null;
    }
}
