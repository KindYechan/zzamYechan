package com.example.jjamyechan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        // User 카드 설정
        User currentUser = UserManager.getCurrentUser();
        binding.tvRank.setText(currentUser.getRank());
        binding.tvName.setText(currentUser.getName());
        binding.tvDaysLeft.setText(currentUser.getLeftDays());
        binding.tvDischargeDate.setText("전역일 " + currentUser.getDischargeDate());

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