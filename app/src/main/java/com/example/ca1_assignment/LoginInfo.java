package com.example.ca1_assignment;

public class LoginInfo {
    int _id;
    String _name;
    String _password;
    String _location;

    // Constructor
    public LoginInfo(){

    }

    // Constructor
    public LoginInfo(int id, String name, String _password, String _location){
        this._id = id;
        this._name = name;
        this._password = _password;
        this._location = _location;
    }

    // Constructor
    public LoginInfo(String name, String _password, String _location){
        this._name = name;
        this._password = _password;
        this._location = _location;
    }

    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getPassword(){
        return this._password;
    }

    public void setPassword(String password){
        this._password = password;
    }

    public String getLocation(){
        return this._location;
    }

    public void setLocation(String location){
        this._location = location;
    }

}
