package com.project.foodfriends.entities;

import java.io.Serializable;
import java.util.Calendar;

public class User implements Serializable{
    private FoodType preferredFood;
    private boolean preferredMale;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String name;
    private String email;
    private String id;
    public User(){}
    public User(FoodType preferredFood, boolean preferredMale, int startHour, int startMinute, int endHour, int endMinute) {
        this.preferredFood = preferredFood;
        this.preferredMale = preferredMale;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public FoodType getPreferredFood() {
        return preferredFood;
    }

    public void setPreferredFood(FoodType preferredFood) {
        this.preferredFood = preferredFood;
    }

    public boolean isPreferredMale() {
        return preferredMale;
    }

    public void setPreferredMale(boolean preferredMale) {
        this.preferredMale = preferredMale;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static User getDefault() {
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        return new User(FoodType.PIZZA, true, hour, minute, hour, minute);
    }

    @Override
    public String toString() {
        return this.getEmail();
    }
}
