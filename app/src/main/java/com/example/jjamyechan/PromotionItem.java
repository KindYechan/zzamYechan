package com.example.jjamyechan;

public class PromotionItem {
    private String name;        // "3km 달리기"
    private String inputType;   // "grade" 또는 "pass"
    private int threshold;      // 기준 등급 점수 (0 특급 ~ 3 3급)
    private boolean required;   // 필수 통과

    public PromotionItem() {
    }

    public PromotionItem(String name, String inputType, int threshold, boolean required) {
        this.name = name;
        this.inputType = inputType;
        this.threshold = threshold;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    // 이 점수(0 특급 ~ 4 불합격)면 통과인지
    public boolean isPassed(Integer score) {
        if (score == null) return false;                    // 아직 안 고름
        if (inputType.equals("pass")) return score == 0;    // 합격
        return score <= threshold;                          // 숫자가 작을수록 좋은 등급
    }
}
