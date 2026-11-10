package com.example.jjamyechan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityAdminRoomBinding;
import com.example.jjamyechan.databinding.DialogBroadcastBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminRoomActivity extends AppCompatActivity {
    private ActivityAdminRoomBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityAdminRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // intent로 전달된 데이터 저장부
        String unitName = getIntent().getStringExtra("unit_name");
        int memberCount = getIntent().getIntExtra("member_count", 0);
        binding.tvRoomTitle.setText(getString(R.string.unit_name, unitName));

        // 행정반 전파사항
        binding.cardBroadcast.setOnClickListener(view -> {
            // dialog 생성부
            Dialog dialog = new Dialog(this);
            DialogBroadcastBinding dialogBinding = DialogBroadcastBinding.inflate(getLayoutInflater());
            dialog.setContentView(dialogBinding.getRoot());
            // dialog 배경 투명색으로, 크기 xml파일에 맞추기
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            dialog.show();
            // x, 취소 버튼 누르면
            dialogBinding.btnClose.setOnClickListener(view2-> {
                dialog.dismiss();
            });
            dialogBinding.btnCancel.setOnClickListener(view2 -> {
                dialog.dismiss();
            });
            // 제출 버튼 누르면 (노티피케이션을 구현합시다!)
            dialogBinding.btnSubmit.setOnClickListener(view2 -> {
                String broadcast = dialogBinding.etBroadcast.getText().toString().trim();
                int broadcastType = dialogBinding.rgBroadcastType.getCheckedRadioButtonId();
                // 식사의 경우
                if (broadcastType == R.id.rb_type_meal){
                    Toast.makeText(this, "대원들에게 식사 알림을 전파하였습니다", Toast.LENGTH_LONG).show();
                }
                // 호출의 경우
                else if (broadcastType == R.id.rb_type_call) {
                    Toast.makeText(this, "대원들에게 호출 알림을 전파하였습니다", Toast.LENGTH_LONG).show();
                }
                // 긴급의 경우
                else{
                    Toast.makeText(this, "대원들에게 긴급 알림을 전파하였습니다", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
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