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
        binding.btnJoin.setOnClickListener(view -> {
            BottomSheetDialog sheet = new BottomSheetDialog(this);
            SheetJoinBinding sheetBinding = SheetJoinBinding.inflate(getLayoutInflater());
            sheet.setContentView(sheetBinding.getRoot());
            sheet.show();
            sheetBinding.btnClose.setOnClickListener(view2 -> {
                sheet.dismiss();
            });
            sheetBinding.btnSubmit.setOnClickListener(view2 -> {
                String name = sheetBinding.etName.getText().toString().trim();
                int rankPos = sheetBinding.spRank.getSelectedItemPosition();
                String rank = sheetBinding.spRank.getSelectedItem().toString();
                String unitNickname = sheetBinding.etUnitNickname.getText().toString().trim();

                if(name.isEmpty()){
                    sheetBinding.etName.setError("이름을 입력해주세요");
                    return;
                }
                if(rankPos == 0){
                    Toast.makeText(this, "계급을 설정해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this, AdminRoomActivity.class);
                intent.putExtra("unit_nickname", unitNickname);
                intent.putExtra("rank", rank);
                sheet.dismiss();
                startActivity(intent);
                finish();
            });
        });

        binding.btnCreate.setOnClickListener(view -> {
            BottomSheetDialog sheet = new BottomSheetDialog(this);
            SheetCreateStep1Binding sheetBinding1 =  SheetCreateStep1Binding.inflate(getLayoutInflater());
            sheet.setContentView(sheetBinding1.getRoot());
            sheet.show();
            sheetBinding1.spUnitType.setSelection(1);
            sheetBinding1.btnClose.setOnClickListener(view2 -> {
                sheet.dismiss();
            });
            sheetBinding1.btnNext.setOnClickListener(view2 -> {

                String unitName = sheetBinding1.etUnitName.getText().toString().trim();
                String unitNickname = sheetBinding1.etUnitNickname.getText().toString().trim();
                int unitTypePos = sheetBinding1.spUnitType.getSelectedItemPosition();
                String unitType = sheetBinding1.spUnitType.getSelectedItem().toString();
                String unitDesc = sheetBinding1.etUnitDesc.getText().toString().trim();

                if(unitName.isEmpty()) {
                    sheetBinding1.etUnitName.setError("부대명을 입력해주세요");
                    return;
                }
                sheet.dismiss();
                BottomSheetDialog sheet2 = new BottomSheetDialog(this);
                SheetCreateStep2Binding sheetBinding2 = SheetCreateStep2Binding.inflate(getLayoutInflater());
                sheet2.setContentView(sheetBinding2.getRoot());
                sheet2.show();
                sheetBinding2.btnClose.setOnClickListener(view3 -> {
                    sheet2.dismiss();
                });
                sheetBinding2.btnPrev.setOnClickListener(view3 -> {
                    sheet2.dismiss();
                    sheet.show();
                });
                sheetBinding2.btnSubmit.setOnClickListener(view3 -> {
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
    }
}