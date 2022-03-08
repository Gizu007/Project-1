package com.revature.model;

public class Person {

    public String firstName;
    public String lastName;
    public int age;

    public void speak() {
        System.out.println(firstName + " Says hello, my name is " + firstName + " " + lastName + " and I am " + age + " years old");
    }
}
