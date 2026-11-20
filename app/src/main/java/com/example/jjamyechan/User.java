package com.example.jjamyechan;

import java.time.LocalDate;
import java.util.Objects;
import java.time.temporal.ChronoUnit;
public class User {

    // (왼쪽부터) 이메일, 이름, 병or간부, 계급, 입대일, 전역일, 부대코드, 역할(수정권한)
    private String email, name, memberType, rank, enlistmentDate, dischargeDate, unitCode, role;

    public User(){}

    public User(String email, String name, String memberType, String rank, String enlistmentDate, String dischargeDate) {
        this.email = email;
        this.name = name;
        this.memberType = memberType;
        this.rank = rank;
        this.enlistmentDate = enlistmentDate;
        this.dischargeDate = dischargeDate;
    }

    // getter
    public String getEmail() {
        return email;
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

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
    // 남은 전역일 계산
    public Long getLeftDays(){
        LocalDate end = LocalDate.parse(dischargeDate);

        return ChronoUnit.DAYS.between(LocalDate.now(), end);
    }
    public long getSalaryDay(){
        long salaryDay = LocalDate.now().lengthOfMonth() + 10;
        return salaryDay - LocalDate.now().getDayOfMonth();
    }
    public double getServiceProgress(){
        double allDay = ChronoUnit.DAYS.between(LocalDate.parse(enlistmentDate), LocalDate.parse(dischargeDate));
        return Math.floor((1 - getLeftDays() / allDay) * 1000) / 10;
    }

    public String getSalary(){
        if (rank.equals("private"))
            return "750,000";
        else if (rank.equals("private-first-class")) {
            return "900,000";
        } else if (rank.equals("corporal")) {
            return "1,200,000";
        } else if (rank.equals("sergean")) {
            return "1,500,000";
        }
        else{
            return "2,000,000";
        }
    }

    // setter
    public void setEmail(String email) {
        this.email = email;
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", memberType='" + memberType + '\'' +
                ", rank='" + rank + '\'' +
                ", enlistmentDate='" + enlistmentDate + '\'' +
                ", dischargeDate='" + dischargeDate + '\'' +
                ", unitCode='" + unitCode + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
