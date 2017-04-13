package com.pavelrudenok99999.searchvkuser.main;

/**
 * Created by Pavel on 05.03.2017.
 */
public class Group {
    private long id;
    private String name;
    private long membersCount;

    public Group(){

    }

    public Group(long id, String name, long membersCount) {
        this.id = id;
        this.name = name;
        this.membersCount = membersCount;
    }

    public void setId(long id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setMembersCount(long membersCount) {
        this.membersCount = membersCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getMembersCount() {
        return membersCount;
    }
}