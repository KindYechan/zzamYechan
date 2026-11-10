package com.example.jjamyechan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityAdminRoomBinding;
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