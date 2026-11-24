package com.example.jjamyechan;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class User {

    // (왼쪽부터) 이메일, 이름, 병or간부, 계급, 입대일, 전역일, 부대코드, 역할(수정권한)
    private String email, name, memberType, rank, enlistmentDate, dischargeDate, unitCode, role;

    public User() {
    }

    public User(String email, String name, String memberType, String rank, String enlistmentDate, String dischargeDate) {
        this.email = email;
        this.name = name;
        this.memberType = memberType;
        this.rank = rank;
        this.enlistmentDate = enlistmentDate;
        this.dischargeDate = dischargeDate;
    }

    // ───────── getter ─────────

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

    public String getRole() {
        return role;
    }

    // ───────── setter ─────────

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

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ───────── 계산 ─────────

    // 남은 전역일 계산
    public Long getLeftDays() {
        LocalDate end = LocalDate.parse(dischargeDate);
        return ChronoUnit.DAYS.between(LocalDate.now(), end);
    }

    // 남은 월급일 계산
    public long getSalaryDay() {
        long salaryDay = LocalDate.now().lengthOfMonth() + 10;
        return salaryDay - LocalDate.now().getDayOfMonth();
    }

    // 복무율 progress
    public double getServiceProgress() {
        double allDay = ChronoUnit.DAYS.between(LocalDate.parse(enlistmentDate), LocalDate.parse(dischargeDate));
        return Math.floor((1 - getLeftDays() / allDay) * 1000) / 10;
    }

    // 총 적금
    public long getTotalSalary() {
        LocalDate start = LocalDate.parse(enlistmentDate);
        LocalDate end = LocalDate.parse(dischargeDate);
        LocalDate today = LocalDate.now();
        LocalDate limit = today.isBefore(end) ? today : end;

        // 입대 후 첫 월급날 찾기
        LocalDate payday = start.withDayOfMonth(10);
        if (payday.isBefore(start)) {
            payday = payday.plusMonths(1);
        }

        long total = 0;
        while (!payday.isAfter(limit)) {    // 지나간 월급날마다
            total += 550000;                // 적금을 더함
            payday = payday.plusMonths(1);
        }
        return total;
    }

    // 계급별 월급
    public int getSalaryOf(String rank) {
        if (rank == null) return 0;         // 간부 등
        switch (rank) {
            case "private":             return 750000;
            case "private-first-class": return 900000;
            case "corporal":            return 1200000;
            case "sergeant":            return 1500000;
            default:                    return 0;
        }
    }

    // 현재 계급의 월급
    public int getCurrentSalary() {
        return getSalaryOf(getRank());
    }

    public double getSavingAccountPercent() {
        // 지금까지 적금 / 990만 * 1000 하고 바닥함수, 나누기 10
        return getTotalSalary() / 99000;
    }

    // ───────── Object 메서드 ─────────

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
