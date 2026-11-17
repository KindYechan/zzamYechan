package com.example.jjamyechan;

import java.time.LocalDate;
import java.util.Objects;
import java.time.temporal.ChronoUnit;
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

    public String getLeftDays(){
        LocalDate start = LocalDate.parse(enlistmentDate);
        LocalDate end = LocalDate.parse(dischargeDate);

        return ChronoUnit.DAYS.between(start, end);
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



    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", memberType='" + memberType + '\'' +
                ", rank='" + rank + '\'' +
                ", enlistmentDate='" + enlistmentDate + '\'' +
                ", dischargeDate='" + dischargeDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
