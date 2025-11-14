
package com.library.model;

public class Student {
    private int id;
    private String name;
    private String email;
    private String contact;
    private String rollNo;

    public Student() {}

    public Student(int id, String name, String email, String contact, String rollNo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.rollNo = rollNo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }
}
