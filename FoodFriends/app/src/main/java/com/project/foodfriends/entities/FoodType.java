package com.project.foodfriends.entities;

public enum FoodType {
    PIZZA("Pizza"),
    HAMBURGER("Hamburger"),
    BARBECUE("Barbecue"),
    SUSHI("Sushi"),
    DESSERT("Dessert"),
    PASTA("Pasta"),
    FISH("Fish"),
    CHINESE("Chinese"),
    ;
    private String name;
    FoodType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
