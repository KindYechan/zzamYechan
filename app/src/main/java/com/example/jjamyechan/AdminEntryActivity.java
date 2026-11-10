package com.example.jjamyechan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityAdminEntryBinding;
import com.example.jjamyechan.databinding.SheetCreateStep1Binding;
import com.example.jjamyechan.databinding.SheetCreateStep2Binding;
import com.example.jjamyechan.databinding.SheetJoinBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class AdminEntryActivity extends AppCompatActivity {
    private ActivityAdminEntryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityAdminEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // 이미 만들어진 행정반 들어가기
        binding.btnJoin.setOnClickListener(view -> {
            // 바텀시트 생성부
            BottomSheetDialog sheet = new BottomSheetDialog(this);
            SheetJoinBinding sheetBinding = SheetJoinBinding.inflate(getLayoutInflater());
            sheet.setContentView(sheetBinding.getRoot());
            sheet.show();
            // x 버튼 누르면
            sheetBinding.btnClose.setOnClickListener(view2 -> {
                sheet.dismiss();
            });
            // 제출 버튼 누르면
            sheetBinding.btnSubmit.setOnClickListener(view2 -> {
                // edit text 데이터 가져오기
                String name = sheetBinding.etName.getText().toString().trim();
                int rankPos = sheetBinding.spRank.getSelectedItemPosition();
                String rank = sheetBinding.spRank.getSelectedItem().toString();
                String unitNickname = sheetBinding.etUnitNickname.getText().toString().trim();
                // 데이터 입력 안했으면 에러
                if(name.isEmpty()){
                    sheetBinding.etName.setError("이름을 입력해주세요");
                    return;
                }
                if(rankPos == 0){
                    Toast.makeText(this, "계급을 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // intent로 데이터 전달
                Intent intent = new Intent(this, AdminRoomActivity.class);
                intent.putExtra("unit_nickname", unitNickname);
                intent.putExtra("rank", rank);
                sheet.dismiss();
                startActivity(intent);
                finish();
            });
        });
        // 새로운 행정반 생성하기
        binding.btnCreate.setOnClickListener(view -> {
            // 바텀시트 생성부
            BottomSheetDialog sheet = new BottomSheetDialog(this);
            SheetCreateStep1Binding sheetBinding1 =  SheetCreateStep1Binding.inflate(getLayoutInflater());
            sheet.setContentView(sheetBinding1.getRoot());
            sheet.show();
            // default 설정값
            sheetBinding1.spUnitType.setSelection(1);
            // x 버튼 누르면
            sheetBinding1.btnClose.setOnClickListener(view2 -> {
                sheet.dismiss();
            });
            // 다음 버튼 누르면
            sheetBinding1.btnNext.setOnClickListener(view2 -> {
                // edit text 데이터 가져오기
                String unitName = sheetBinding1.etUnitName.getText().toString().trim();
                String unitNickname = sheetBinding1.etUnitNickname.getText().toString().trim();
                int unitTypePos = sheetBinding1.spUnitType.getSelectedItemPosition();
                String unitType = sheetBinding1.spUnitType.getSelectedItem().toString();
                String unitDesc = sheetBinding1.etUnitDesc.getText().toString().trim();

                // 데이터 입력 안햇으면 이렇게
                if(unitName.isEmpty()) {
                    sheetBinding1.etUnitName.setError("부대명을 입력해주세요");
                    return;
                }
                // 두 번째 바텀시트 생성부
                sheet.dismiss();
                BottomSheetDialog sheet2 = new BottomSheetDialog(this);
                SheetCreateStep2Binding sheetBinding2 = SheetCreateStep2Binding.inflate(getLayoutInflater());
                sheet2.setContentView(sheetBinding2.getRoot());
                sheet2.show();
                // x 버튼 누르면
                sheetBinding2.btnClose.setOnClickListener(view3 -> {
                    sheet2.dismiss();
                });
                // 이전 버튼 누르면
                sheetBinding2.btnPrev.setOnClickListener(view3 -> {
                    sheet2.dismiss();
                    sheet.show();
                });
                // 제출 버튼 누르면
                sheetBinding2.btnSubmit.setOnClickListener(view3 -> {
                    // 입력 데이터 가져오기
                    boolean[] features = new boolean[6];
                    features[0] = sheetBinding2.cbFeatureCalendar.isChecked();
                    features[1] = sheetBinding2.cbFeatureQuest.isChecked();
                    features[2] = sheetBinding2.cbFeaturePromotion.isChecked();
                    features[3] = sheetBinding2.cbFeatureNotice.isChecked();
                    features[4] = sheetBinding2.cbFeatureAlert.isChecked();
                    features[5] = sheetBinding2.cbFeatureLetter.isChecked();
                    int editPermissionPos = sheetBinding2.spEditPermission.getSelectedItemPosition();
                    String editPermission = sheetBinding2.spEditPermission.getSelectedItem().toString();
                    boolean activityLog = sheetBinding2.swActivityLog.isChecked();

                    // intent로 데이터 전달
                    Intent intent = new Intent(this, AdminRoomActivity.class);
                    intent.putExtra("unit_name", unitName);
                    intent.putExtra("unit_nickname", unitNickname);
                    intent.putExtra("unit_type", unitType);
                    intent.putExtra("unit_desc", unitDesc);
                    intent.putExtra("feature", features);
                    intent.putExtra("edit_permission", editPermission);
                    intent.putExtra("activity_log", activityLog);
                    sheet2.dismiss();
                    startActivity(intent);
                    finish();
                });
            });

        });
        // 바텀 네비게이션
        BottomNavigationView nav = binding.bottomNav.getRoot();
        nav.setSelectedItemId(R.id.nav_admin);
        nav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_home){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return true;
        });
    }
}