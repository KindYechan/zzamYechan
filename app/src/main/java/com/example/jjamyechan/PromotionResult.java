package com.example.jjamyechan;

import java.util.ArrayList;
import java.util.List;

public class PromotionResult {
    public boolean passed;
    public Double average;                       // 등급 항목이 없으면 null
    public List<PromotionItem> lacks = new ArrayList<>();
}
