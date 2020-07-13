package com.example.newcomer_io.ui.main.FindFriends;

public class Friend {
    private String Location;
    private String FirstName;
    private String ImageName;
    private String Uuid;
    public Friend(){

    }

    public Friend(String name, String location,String Uuid, String imageName){
        this.Location = location;
        this.FirstName = name;
        this.ImageName = imageName;
        this.Uuid = Uuid;

    }
    public String getFirstName() {
        return FirstName;
    }

    public String getLocation() {
        return Location;
    }

    public String getImageName() {
        return this.ImageName;
    }

    public String getUuid() {
        return Uuid;
    }

}
