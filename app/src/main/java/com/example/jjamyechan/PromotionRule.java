package com.example.jjamyechan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromotionRule {
    private String mode;              // "average" 또는 "all"
    private int averageThreshold;     // 평균 기준 (0~3)
    private List<PromotionItem> items = new ArrayList<>();

    public PromotionRule() {
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getAverageThreshold() {
        return averageThreshold;
    }

    public void setAverageThreshold(int averageThreshold) {
        this.averageThreshold = averageThreshold;
    }

    public List<PromotionItem> getItems() {
        return items;
    }

    public void setItems(List<PromotionItem> items) {
        this.items = items;
    }

    // 복사본 만들기 (설정 화면에서 저장 전까지 원본을 건드리지 않으려고)
    public PromotionRule copy() {
        PromotionRule c = new PromotionRule();
        c.setMode(mode);
        c.setAverageThreshold(averageThreshold);
        for (PromotionItem item : items) {
            // 내부 항목들 복사
            c.items.add(new PromotionItem(item.getName(), item.getInputType(),
                    item.getThreshold(), item.isRequired()));
        }
        return c;
    }
    // 고른 기록(항목 → 점수)으로 합격 여부 판정
    public PromotionResult evaluate(Map<PromotionItem, Integer> answers) {
        PromotionResult result = new PromotionResult();

        // 평균 구하기
        // 기본값은 4급, 합불합은 계산 x
        int sum = 0, count = 0;
        for (PromotionItem item : items) {
            if (item.getInputType().equals("grade")) {
                Integer score = answers.get(item);
                if(score == null)
                    sum += 4;
                else
                    sum += score;
                count++;
            }
        }
        if(count == 0)
            result.average = null;
        else
            result.average = (double) sum /count;

        // 보완이 필요한 항목 체크
        // 전부 통과 or 필요한 것들만 lacks(list)에 추가
        for (PromotionItem item : items) {
            boolean check = mode.equals("all") || item.isRequired();
            if (check && !item.isPassed(answers.get(item))) {
                result.lacks.add(item);
            }
        }

        // 최종 판정
        // 1. 평균이 넘는가 2. 필수 항목을 다 통과했는가
        // 3. 모든 항목을 체크했는가, 필수 항목을 통과했는가
        boolean averageOk = mode.equals("all") || (result.average != null && result.average <= averageThreshold);
        result.passed = !items.isEmpty() && result.lacks.isEmpty() && averageOk;
        return result;
    }
}
