package com.example.jjamyechan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.jjamyechan.databinding.ActivityPromotionBinding;
import com.example.jjamyechan.databinding.ItemPromotionAssessmentBinding;

import java.util.HashMap;

public class PromotionActivity extends AppCompatActivity {
    private ActivityPromotionBinding binding;
    private User currentUser = UserManager.getCurrentUser();
    private final HashMap<PromotionItem, Integer> answers = new HashMap<>();
    private String selectedRank = "corporal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromotionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(view -> {
            finish();
        });

        // 간부만 설정 버튼 보이기
        if (currentUser.getMemberType().equals("officer")) {
            binding.btnRules.setVisibility(View.VISIBLE);
        } else {
            binding.btnRules.setVisibility(View.GONE);
        }
        // 상병이면 상병 탭, 병장은 병탭
        if ("corporal".equals(currentUser.getRank())) {
            selectedRank = "sergeant";
            binding.rankTabs.rbTabSergeant.setChecked(true);
        }
        // 상병 병장 누르면 전환
        binding.rankTabs.getRoot().setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_tab_corporal) {
                selectedRank = "corporal";
            } else {
                selectedRank = "sergeant";
            }
            answers.clear();   // 이전 탭에서 고른 기록 지우기
            drawRule();        // 새 계급 규칙으로 카드 다시 그리기 (안에서 updateResult 도 부름)
        });

        drawRule();
    }

    // 판정 방식 카드 + 항목 카드 그리기
    private void drawRule() {
        PromotionRule rule = PromotionRuleManager.getRule(selectedRank);
        // 판정 방식
        if (rule.getMode().equals("average")) {
            String grade = getResources().getStringArray(R.array.promo_thresholds)[rule.getAverageThreshold()];
            binding.tvRuleMode.setText(getString(R.string.promo_rule_average, grade));
        } else {
            binding.tvRuleMode.setText(R.string.promo_rule_all);
        }

        // 항목 개수
        binding.tvRuleCount.setText(getString(R.string.promo_item_count, rule.getItems().size()));
        if (rule.getItems().isEmpty()) {
            binding.layoutCalcEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.layoutCalcEmpty.setVisibility(View.GONE);
        }
        // 항목 카드 그리기
        binding.listAssessments.removeAllViews();
        for (PromotionItem item : rule.getItems()) {
            ItemPromotionAssessmentBinding card =
                    ItemPromotionAssessmentBinding.inflate(getLayoutInflater(), binding.listAssessments, false);
            card.tvItemName.setText(item.getName());
            boolean showRequired = rule.getMode().equals("average") && item.isRequired();
            card.tvRequired.setVisibility(showRequired ? View.VISIBLE : View.GONE);

            if (item.getInputType().equals("grade")) {
                card.tvItemThreshold.setText(getString(R.string.promo_threshold, item.getThreshold()));
                card.rgGrade.setVisibility(View.VISIBLE);
                card.rgPass.setVisibility(View.GONE);
            } else {
                card.tvItemThreshold.setText(R.string.promo_pass_only);
                card.rgGrade.setVisibility(View.GONE);
                card.rgPass.setVisibility(View.VISIBLE);
            }

            // 고를 때마다 기록 저장, 결과 다시 계산
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
                answers.put(item, checkedId == R.id.rb_pass ? 0 : 4);   // 합격: 0 불합격: 4
                updateResult();
            });
            binding.listAssessments.addView(card.getRoot());
        }
        updateResult();
    }

    // 결과 카드 그리기 (구현 중)
    private void updateResult() {
        PromotionRule rule = PromotionRuleManager.getRule(selectedRank);
        PromotionResult result = rule.evaluate(answers);


        // 배경 및 텍스트 색
        int background, labelColor, statusColor, descColor, valueColor, lineColor;
        int statusText, descText;
        if (result.passed) {
            background  = R.drawable.bg_promo_result_pass;
            labelColor  = R.color.promo_pass_label;
            statusColor = R.color.on_dark;
            descColor   = R.color.promo_pass_desc;
            valueColor  = R.color.on_dark;
            lineColor   = R.color.promo_pass_line;
            statusText  = R.string.promo_pass;
            descText    = R.string.promo_result_pass_desc;
        } else {
            background  = R.drawable.bg_promo_result_fail;
            labelColor  = R.color.promo_fail_label;
            statusColor = R.color.promo_fail_title;
            descColor   = R.color.promo_fail_desc;
            valueColor  = R.color.promo_fail_value;
            lineColor   = R.color.promo_fail_line;
            statusText  = R.string.promo_fail;
            descText    = R.string.promo_result_fail_desc;
        }

        // 배경 색
        binding.cardResult.setBackgroundResource(background);

        // 상병 or 병장
        String rankName = selectedRank.equals("corporal") ? getString(R.string.promo_rank_corporal) : getString(R.string.promo_rank_sergeant);
        binding.tvResultLabel.setText(getString(R.string.promo_result_label, rankName));

        // 합 불합
        binding.tvResultStatus.setText(statusText);
        binding.tvResultDesc.setText(descText);

        // 글자 색
        binding.tvResultLabel.setTextColor(ContextCompat.getColor(this, labelColor));
        binding.tvResultAverageLabel.setTextColor(ContextCompat.getColor(this, labelColor));
        binding.tvResultDateLabel.setTextColor(ContextCompat.getColor(this, labelColor));
        binding.tvResultStatus.setTextColor(ContextCompat.getColor(this, statusColor));
        binding.tvResultDesc.setTextColor(ContextCompat.getColor(this, descColor));
        binding.tvResultAverage.setTextColor(ContextCompat.getColor(this, valueColor));
        binding.tvResultDate.setTextColor(ContextCompat.getColor(this, valueColor));
        binding.lineResult.setBackgroundColor(ContextCompat.getColor(this, lineColor));
        binding.lineResultMiddle.setBackgroundColor(ContextCompat.getColor(this, lineColor));

        // 평균 없음
        if (result.average == null) {
            binding.tvResultAverage.setText(R.string.promo_result_average_none);
        } else {    // 평균값
            binding.tvResultAverage.setText(getString(R.string.promo_result_average_value, result.average));
        }
        // 이전에 있던 칩(필요한 항목) 삭제
        binding.listLacks.removeAllViews();

        // 필요한 항목들 하나씩 추가하기
        for (PromotionItem item : result.lacks) {
            TextView chip = new TextView(this);
            chip.setText(item.getName());
            chip.setTextSize(12);
            chip.setBackgroundResource(R.drawable.bg_promo_lack_chip);
            chip.setTextColor(ContextCompat.getColor(this, R.color.promo_lack_text));
            chip.setPadding(20, 12, 20, 12);    // 픽셀 단위
            binding.listLacks.addView(chip);
        }

        // 불합격이고 부족한 항목이 있을 때만 상자 보이기
        if (!result.passed && !result.lacks.isEmpty()) {
            binding.layoutLacks.setVisibility(View.VISIBLE);
        } else {
            binding.layoutLacks.setVisibility(View.GONE);
        }
    }
}
