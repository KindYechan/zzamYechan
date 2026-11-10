package com.example.jjamyechan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private boolean isQuest1Checked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // 식단 설정 토스트
        binding.btnMealSetup.setOnClickListener(view ->{
            Toast.makeText(this, "식단을 설정합니다", Toast.LENGTH_SHORT).show();
        });

        // 버튼 클릭리스너 연습 및 테스트
        binding.tvDaysLeft.setOnClickListener(view -> {
            binding.tvDaysLeft.setText("100");
        });

        // 체크박스 버튼 연습 및 테스트
        binding.checkQuest1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isQuest1Checked = !isQuest1Checked;
                if(!isQuest1Checked){
                    binding.checkQuest1.setAlpha(0.4f);
                }
                else{
                    binding.checkQuest1.setAlpha(1.0f);
                }
            }
        });

        // 알림 테스트
        binding.topBar.btnNotifications.setOnClickListener(view -> {
            Toast.makeText(this, "알림을 표시합니다", Toast.LENGTH_SHORT).show();
        });

        // 바텀 네비게이션
        BottomNavigationView nav = binding.bottomNav.getRoot();
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_admin){
                Intent intent = new Intent(this, AdminEntryActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return true;
        });
    }
}