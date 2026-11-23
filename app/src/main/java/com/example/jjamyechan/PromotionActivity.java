package com.example.jjamyechan;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityPromotionBinding;
import com.example.jjamyechan.databinding.ItemPromotionAssessmentBinding;

import java.util.HashMap;

public class PromotionActivity extends AppCompatActivity {
    ActivityPromotionBinding binding;
    User currentUser = UserManager.getCurrentUser();
    private final HashMap<PromotionItem, Integer> answers = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityPromotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        if(currentUser.getMemberType().equals("officer")){
            binding.btnRules.setVisibility(View.VISIBLE);
        }
        else{
            binding.btnRules.setVisibility(View.GONE);
        }
    }


    private void drawRule() {
        PromotionRule rule = PromotionRuleManager.getRule(currentUser.getRank());

        // 판정 방식
        if (rule.getMode().equals("average")) {
            String grade = getResources().getStringArray(R.array.promo_thresholds)[rule.getAverageThreshold()];
            binding.tvRuleMode.setText(getString(R.string.promo_rule_average, grade));   // "평균 2급 이상"
        } else {
            binding.tvRuleMode.setText(R.string.promo_rule_all);                          // "모든 항목 통과"
        }

        // 항목 개수
        binding.tvRuleCount.setText(getString(R.string.promo_item_count, rule.getItems().size()));

        // 4) 항목 카드 그리기 (아래)
        binding.listAssessments.removeAllViews();
        for (PromotionItem item : rule.getItems()) {
            ItemPromotionAssessmentBinding card =
                    ItemPromotionAssessmentBinding.inflate(getLayoutInflater(), binding.listAssessments, false);
            card.tvItemName.setText(item.getName());
            card.tvRequired.setVisibility(item.isRequired() ? View.VISIBLE : View.GONE);

            if (item.getInputType().equals("grade")) {
                card.tvItemThreshold.setText(getString(R.string.promo_threshold, item.getThreshold()));
                card.rgGrade.setVisibility(View.VISIBLE);
                card.rgPass.setVisibility(View.GONE);
            } else {
                card.tvItemThreshold.setText(R.string.promo_pass_only);
                card.rgGrade.setVisibility(View.GONE);
                card.rgPass.setVisibility(View.VISIBLE);
            }

            // 고를 때마다 기록 저장 + 결과 다시 계산
            card.rgGrade.setOnCheckedChangeListener((group, checkedId) -> {
                int score;
                if (checkedId == R.id.rb_grade_special) score = 0;
                else if (checkedId == R.id.rb_grade_1) score = 1;
                else if (checkedId == R.id.rb_grade_2) score = 2;
                else if (checkedId == R.id.rb_grade_3) score = 3;
                else score = 4;
                answers.put(item, score);
                updateResult();
            });
            card.rgPass.setOnCheckedChangeListener((group, checkedId) -> {
                answers.put(item, checkedId == R.id.rb_pass ? 0 : 4);   // 합격 0, 불합격 4
                updateResult();
            });
            binding.listAssessments.addView(card.getRoot());
        }
    }
    private void updateResult() {
        PromotionRule rule = PromotionRuleManager.getRule(currentUser.getRank());
        PromotionResult result = rule.evaluate(answers);

        // 합격/불합격 글자와 색, 배경
        // 평균: result.average 가 null 이면 promo_result_average_none, 아니면 promo_result_average_value
        // 부족한 항목: binding.listLacks.removeAllViews() 후 result.lacks 로 칩 만들기
        //              불합격이고 lacks 가 있을 때만 layoutLacks 보이기
    }
}