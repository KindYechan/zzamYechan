package com.example.jjamyechan;

import java.util.ArrayList;
import java.util.List;

// 진급 판정 결과 (PromotionRule.evaluate 가 만들어 줌)
public class PromotionResult {
    public boolean passed;
    public Double average;                                  // 등급 항목이 없으면 null
    public List<PromotionItem> lacks = new ArrayList<>();   // 부족한 항목
}
