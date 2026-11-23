package com.example.jjamyechan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromotionRule {
    private String mode;              // "average" 또는 "all"
    private int averageThreshold;     // 평균 기준 (0~3)
    private List<PromotionItem> items = new ArrayList<>();

    public PromotionRule() { }



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

    public PromotionResult evaluate(Map<PromotionItem, Integer> answers) {
        PromotionResult result = new PromotionResult();

        // 1) 평균: 등급 항목만, 안 고른 항목은 불합격(4)으로
        int sum = 0, count = 0;
        for (PromotionItem item : items) {
            if (item.getInputType().equals("grade")) {
                Integer score = answers.get(item);
                sum += (score == null) ? 4 : score;
                count++;
            }
        }
        result.average = (count == 0) ? null : (double) sum / count;

        // 2) 부족한 항목: 전부 통과면 모든 항목, 평균 방식이면 필수 항목만 검사
        for (PromotionItem item : items) {
            boolean check = mode.equals("all") || item.isRequired();
            if (check && !item.isPassed(answers.get(item))) {
                result.lacks.add(item);
            }
        }

        // 3) 최종 판정
        boolean averageOk = mode.equals("all")
                || (result.average != null && result.average <= averageThreshold);
        result.passed = !items.isEmpty() && result.lacks.isEmpty() && averageOk;
        return result;
    }
}

