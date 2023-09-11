package org.entities;

public class User {
    protected int Id;
    protected String Name;
    protected String Cin;

    public String getCin() {
        return Cin;
    }

    public void setCin(String cin) {
        Cin = cin;
    }

    public void addUser(int id, String name, String cin){}

}
