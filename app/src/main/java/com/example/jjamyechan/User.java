package com.example.jjamyechan;
public class User {
    private String username, name, memberType, rank, enlistmentDate, dischargeDate;

    public User(){}

    public User(String username, String name, String memberType, String rank, String enlistmentDate, String dischargeDate) {
        this.username = username;
        this.name = name;
        this.memberType = memberType;
        this.rank = rank;
        this.enlistmentDate = enlistmentDate;
        this.dischargeDate = dischargeDate;
    }

    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getMemberType() {
        return memberType;
    }
    public String getRank() {
        return rank;
    }
    public String getEnlistmentDate() {
        return enlistmentDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setEnlistmentDate(String enlistmentDate) {
        this.enlistmentDate = enlistmentDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }
}
