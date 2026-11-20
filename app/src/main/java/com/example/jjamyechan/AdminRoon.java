package com.example.jjamyechan;

public class AdminRoon {
    // (왼쪽부터) 부대코드, 부대이름, 닉네임, 부대타입, 설명, 수정권한(일부, 전체), 생성자 아이디
    String unitCode, unitName, nickname, unitType, description, editPermission, ownerId;
    // 기능 사용 여부 (캘린더, 퀘스트, 진급계산, 게시판, 전파사항, 마편)
    boolean useCalendar, useQuest, usePromotion, useNotice, useBroadcast, useLetter;
    // 소속 부대원
    int memberCount;

    public AdminRoon() {}

    public AdminRoon(String unitName, String nickname, String unitType, String description, String editPermission, String ownerId,
                     boolean useCalendar, boolean useQuest, boolean usePromotion, boolean useNotice, boolean useBroadcast, boolean useLetter) {
        this.unitName = unitName;
        this.nickname = nickname;
        this.unitType = unitType;
        this.description = description;
        this.editPermission = editPermission;
        this.ownerId = ownerId;
        this.useCalendar = useCalendar;
        this.useQuest = useQuest;
        this.usePromotion = usePromotion;
        this.useNotice = useNotice;
        this.useBroadcast = useBroadcast;
        this.useLetter = useLetter;
    }

    // getter
    public String getUnitCode() {
        return unitCode;
    }
    public String getUnitName() {
        return unitName;
    }
    public String getNickname() {
        return nickname;
    }
    public String getUnitType() {
        return unitType;
    }
    public String getDescription() {
        return description;
    }
    public String getEditPermission() {
        return editPermission;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public int getMemberCount() {
        return memberCount;
    }

    // setter
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEditPermission(String editPermission) {
        this.editPermission = editPermission;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}
