package com.example.jjamyechan;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivitySettingsBinding;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding.btnBack.setOnClickListener(view -> {
            finish();
        });
        User user = UserManager.getCurrentUser();
        binding.tvProfileName.setText(user.getName());
        binding.tvProfileSub.setText(user.getRank() + " · 전역일 " + user.getDischargeDate());
        if(user.getUnitCode() == null){
            binding.groupUnit.setVisibility(View.GONE);
        }
        else{
            binding.groupUnit.setVisibility(View.VISIBLE);
        }


        binding.rowLogout.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.set_logout_confirm)
                    .setPositiveButton(R.string.set_confirm_yes, (dialog, which) -> {
                        UserManager.logout();
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton(R.string.set_confirm_no, null)   // null = 그냥 닫기
                    .show();
        });
        binding.rowLeaveUnit.setOnClickListener(view ->{
            new AlertDialog.Builder(this)
                    .setMessage(R.string.set_logout_confirm)
                    .setPositiveButton(R.string.set_confirm_yes, (dialog, which) -> {
                        UserManager.leaveUnit();
                    })
                    .setNegativeButton(R.string.set_confirm_no, null)   // null = 그냥 닫기
                    .show();
        });
    }


}