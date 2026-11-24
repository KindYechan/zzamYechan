package com.example.jjamyechan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 로그인 안 된 상태로 열리면 (앱 실행 시 첫 화면이 이 화면일 때) 로그인 화면으로 보냄
        if (!UserManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // User 카드 설정
        User currentUser = UserManager.getCurrentUser();
        binding.tvRank.setText(currentUser.getRank());
        binding.tvName.setText(currentUser.getName());
        binding.tvDaysLeft.setText(Long.toString(currentUser.getLeftDays()));
        binding.tvDischargeDate.setText("전역일 " + currentUser.getDischargeDate());

        binding.tvPromotionPercent.setText(Double.toString(currentUser.getServiceProgress()) + '%');
        binding.progressPromotion.setProgress((int) (currentUser.getServiceProgress() * 10));

        // 월급 / 적금 카드
        binding.tvSalaryValue.setText(String.format(Locale.KOREA, "%,d", currentUser.getCurrentSalary()));
        binding.tvSalarySub.setText(getString(R.string.home_salary_dday, currentUser.getSalaryDay()));
        binding.tvSavingValue.setText(String.format(Locale.KOREA, "%,d", currentUser.getTotalSalary()));
        binding.tvSavingSub.setText(getString(R.string.home_saving_progress, currentUser.getSavingAccountPercent()));

        // 설정 화면 들어가기
        binding.topBar.btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        });

        // 바텀 네비게이션
        BottomNavigationView nav = binding.bottomNav.getRoot();
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_admin) {
                if (currentUser.getUnitCode() == null) {
                    Intent intent = new Intent(this, AdminEntryActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    Intent intent = new Intent(this, AdminRoomActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }
            }
            return true;
        });
    }
}
