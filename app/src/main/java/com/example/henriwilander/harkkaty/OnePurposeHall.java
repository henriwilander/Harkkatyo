package com.example.henriwilander.harkkaty;

public class OnePurposeHall extends CommunityCenter {

    private String purpose;

    public OnePurposeHall(String name, int id, int capacity, String purpose, int status) {
        super(name, id, capacity, false, status);
        this.purpose = purpose;
    }

    public void setNewPurpose(String newPurpose) {
        purpose = newPurpose;
    }

    public String getPurpose () {
        return purpose;
    }

}
