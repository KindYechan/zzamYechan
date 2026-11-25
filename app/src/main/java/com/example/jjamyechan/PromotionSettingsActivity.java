package com.example.jjamyechan;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityPromotionSettingsBinding;
import com.example.jjamyechan.databinding.ItemPromotionSettingBinding;
import com.example.jjamyechan.databinding.SheetPromotionItemBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;

public class PromotionSettingsActivity extends AppCompatActivity {
    private ActivityPromotionSettingsBinding binding;

    // 계급 → 고치는 중인 복사본 (저장 누르기 전까지 원본은 그대로)
    private final HashMap<String, PromotionRule> editing = new HashMap<>();
    private String selectedRank = "corporal";
    private PromotionRule rule;     // 지금 탭의 복사본

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPromotionSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 두 계급 모두 복사본 만들기
        editing.put("corporal", PromotionRuleManager.getRule("corporal").copy());
        editing.put("sergeant", PromotionRuleManager.getRule("sergeant").copy());
        rule = editing.get(selectedRank);

        binding.btnBack.setOnClickListener(v -> finish());   // 저장 안 하고 나가기

        // 상병 / 병장 탭
        binding.rankTabs.getRoot().setOnCheckedChangeListener((group, checkedId) -> {
            selectedRank = (checkedId == R.id.rb_tab_corporal) ? "corporal" : "sergeant";
            rule = editing.get(selectedRank);
            drawRule();
        });

        // 판정 방식
        binding.modeAverage.setOnClickListener(v -> {
            rule.setMode("average");
            drawMode();
            drawItems();    // 필수 표시가 방식에 따라 달라져서 다시 그림
        });
        binding.modeAll.setOnClickListener(v -> {
            rule.setMode("all");
            drawMode();
            drawItems();
        });

        // 평균 기준 등급: 고른 위치(0 특급 ~ 3 3급)가 곧 점수
        binding.spAverageThreshold.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rule.setAverageThreshold(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 항목 추가 (시트는 다음 단계)
        binding.btnAddItem.setOnClickListener(v -> showItemSheet(null));
        binding.btnAddFirstItem.setOnClickListener(v -> showItemSheet(null));

        // 기본 템플릿으로 되돌리기
        binding.btnReset.setOnClickListener(v -> {
            rule = PromotionRuleManager.makeDefaultRule(selectedRank);
            editing.put(selectedRank, rule);
            drawRule();
        });

        // 저장: 복사본을 원본 자리에 넣고 나가기
        binding.btnSave.setOnClickListener(v -> {
            PromotionRuleManager.saveRule("corporal", editing.get("corporal"));
            PromotionRuleManager.saveRule("sergeant", editing.get("sergeant"));
            finish();
        });

        drawRule();
    }

    // 화면 전체 다시 그리기 (탭 바꿀 때, 되돌릴 때)
    private void drawRule() {
        drawMode();
        binding.spAverageThreshold.setSelection(rule.getAverageThreshold());
        drawItems();
    }

    // 판정 방식 상자 선택 표시 + 평균 기준 줄 보이기/숨기기
    private void drawMode() {
        boolean isAverage = rule.getMode().equals("average");
        binding.modeAverage.setSelected(isAverage);
        binding.modeAll.setSelected(!isAverage);
        binding.layoutAverageThreshold.setVisibility(isAverage ? View.VISIBLE : View.GONE);
    }

    // 항목 줄 그리기
    private void drawItems() {
        List<PromotionItem> items = rule.getItems();
        String[] grades = getResources().getStringArray(R.array.promo_thresholds);
        boolean isAverage = rule.getMode().equals("average");

        binding.listSettingItems.removeAllViews();
        for (int i = 0; i < items.size(); i++) {
            PromotionItem item = items.get(i);
            ItemPromotionSettingBinding row =
                    ItemPromotionSettingBinding.inflate(getLayoutInflater(), binding.listSettingItems, false);

            row.tvSettingName.setText(item.getName());
            if (item.getInputType().equals("grade")) {
                row.tvSettingDesc.setText(getString(R.string.promo_setting_grade_desc, grades[item.getThreshold()]));
            } else {
                row.tvSettingDesc.setText(R.string.promo_setting_pass_desc);
            }
            // 계산기 화면과 같은 규칙: 필수 표시는 평균 방식일 때만
            row.tvSettingRequired.setVisibility(isAverage && item.isRequired() ? View.VISIBLE : View.GONE);
            row.dividerTop.setVisibility(i == 0 ? View.GONE : View.VISIBLE);   // 첫 줄은 위 선 없음

            row.btnEditItem.setOnClickListener(v -> showItemSheet(item));
            row.btnDeleteItem.setOnClickListener(v -> {
                items.remove(item);
                drawItems();
            });
            binding.listSettingItems.addView(row.getRoot());
        }

        binding.tvItemsCount.setText(getString(R.string.promo_items_count, items.size()));

        // 항목이 없으면 흰 카드 대신 안내 상자
        boolean empty = items.isEmpty();
        binding.cardSettingItems.setVisibility(empty ? View.GONE : View.VISIBLE);
        binding.layoutSettingsEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    // 항목 추가(item == null) / 편집 시트 — 다음 단계
    // 항목 추가(item == null) / 편집 시트
    private void showItemSheet(PromotionItem item) {
        BottomSheetDialog sheet = new BottomSheetDialog(this);
        SheetPromotionItemBinding s = SheetPromotionItemBinding.inflate(getLayoutInflater());
        sheet.setContentView(s.getRoot());

        // ── 1. 처음 값 채우기 ──
        if (item == null) {
            // 새 항목: 제목은 XML 그대로("새 항목 추가"), 등급형 · 2급 · 필수 아님
            selectType(s, "grade");
            s.spThreshold.setSelection(2);
        } else {
            s.tvSheetTitle.setText(R.string.promo_sheet_title_edit);
            s.etItemName.setText(item.getName());
            selectType(s, item.getInputType());
            s.spThreshold.setSelection(item.getThreshold());
            s.swRequired.setChecked(item.isRequired());
        }

        // 전부 통과 방식에서는 모든 항목이 필수라 스위치를 숨김
        if (rule.getMode().equals("all")) {
            s.layoutRequired.setVisibility(View.GONE);
        }

        // ── 2. 입력 방식 고르기 ──
        s.typeGrade.setOnClickListener(v -> selectType(s, "grade"));
        s.typePass.setOnClickListener(v -> selectType(s, "pass"));

        s.btnClose.setOnClickListener(v -> sheet.dismiss());

        // ── 3. 저장 ──
        s.btnSaveItem.setOnClickListener(v -> {
            String name = s.etItemName.getText().toString().trim();
            if (name.isEmpty()) {
                s.etItemName.setError(getString(R.string.promo_error_item_name));
                return;
            }
            // 지금 선택된 상자로 입력 방식 판단
            String type = s.typeGrade.isSelected() ? "grade" : "pass";
            int threshold = s.spThreshold.getSelectedItemPosition();
            boolean required = s.swRequired.isChecked();

            if (item == null) {
                rule.getItems().add(new PromotionItem(name, type, threshold, required));
            } else {
                item.setName(name);
                item.setInputType(type);
                item.setThreshold(threshold);
                item.setRequired(required);
            }
            sheet.dismiss();
            drawItems();
        });

        sheet.show();
    }

    // 입력 방식 상자 선택 표시 + 기준 등급 줄 보이기/숨기기
    private void selectType(SheetPromotionItemBinding s, String type) {
        boolean isGrade = type.equals("grade");
        s.typeGrade.setSelected(isGrade);
        s.typePass.setSelected(!isGrade);
        s.layoutThreshold.setVisibility(isGrade ? View.VISIBLE : View.GONE);   // 통과형은 기준 등급이 필요 없음
    }
}