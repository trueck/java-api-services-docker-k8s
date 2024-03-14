package com.example.product.composite;


public class GreetingRequest {
    private String name;

    public GreetingRequest() {
    }

    public GreetingRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GreetingRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
