package com.example.jjamyechan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PromotionRuleManager {
    // 계급 → 규칙
    private static final HashMap<String, PromotionRule> rules = new HashMap<>();

    // 계급의 규칙 가져오기 (없으면 기본 규칙을 만들어 보관)
    public static PromotionRule getRule(String rank) {
        PromotionRule rule = rules.get(rank);
        if (rule == null) {
            rule = makeDefaultRule(rank);
            rules.put(rank, rule);
        }
        return rule;
    }

    public static void saveRule(String rank, PromotionRule rule) {
        rules.put(rank, rule);
    }

    // 기본 템플릿 (진급 기준 설정의 "기본 템플릿으로 되돌리기"에서도 사용)
    public static PromotionRule makeDefaultRule(String rank) {
        boolean isSergeant = rank.equals("sergeant");
        PromotionRule rule = new PromotionRule();

        // 상병: 평균 / 병장: 전부 통과
        if (isSergeant) {
            rule.setMode("all");
        } else {
            rule.setMode("average");
        }
        rule.setAverageThreshold(2);    // 평균 기준 2급

        List<PromotionItem> items = new ArrayList<>();
        // (이름, 입력 방식, 기준 등급 점수, 필수 여부)
        items.add(new PromotionItem("3km 달리기",    "grade", 2, !isSergeant));
        items.add(new PromotionItem("팔굽혀펴기",    "grade", 2, false));
        items.add(new PromotionItem("윗몸일으키기",  "grade", 2, false));
        items.add(new PromotionItem("개인화기 사격", "grade", 2, !isSergeant));
        items.add(new PromotionItem("정신전력",      "grade", 2, false));
        items.add(new PromotionItem("TCCC",          "pass",  2, true));
        items.add(new PromotionItem("MOPP",          "pass",  2, isSergeant));

        rule.setItems(items);
        return rule;
    }
}
