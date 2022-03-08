package com.revature.main;

import com.revature.model.Person;

public class Driver {
    public static void main(String[] args) {
        System.out.println("Welcome to your car examination");

            methodA();
            methodB("Hyundai");

        System.out.println(" Please say your name with your age in order to verify these cars are yours");

            Person p1 = new Person();
            p1.firstName = "John";
            p1.lastName = "Smith";
            p1.age = 25;

            p1.speak();

            Person p2 = new Person();
            p2.firstName = "Brittany";
            p2.lastName = "Smith";
            p2.age = 22;

            p2.speak();

        System.out.println("End of the examination");

    }

    public static void methodA() {

        System.out.println("There is 2 car examinate");

        methodB("Elantra");
    }
    public static void methodB(String Car) {
        System.out.println("Your car is a: " + Car);

    }
}

