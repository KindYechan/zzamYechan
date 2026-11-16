package com.example.jjamyechan;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivitySignupBinding;

import java.time.LocalDate;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private LocalDate enlistDate, dischargeDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.rgMemberType.setOnCheckedChangeListener( (buttonView, checkedId) -> {
            boolean soldier = (checkedId == R.id.rb_soldier);
            if(soldier)
                binding.layoutRank.setVisibility(View.VISIBLE);
            else
                binding.layoutRank.setVisibility(View.GONE);
        });
        enlistDate = LocalDate.now();
        dischargeDate = enlistDate.plusMonths(18);
        updateDate();

        // 입대일, 전역일 설정
        // 입대 후 1년 6개월 뒤로 전역일 자동 설정
        binding.tvEnlistDate.setOnClickListener(view -> {
            new DatePickerDialog(this, (picker, year, month, day) -> {
                enlistDate = LocalDate.of(year, month+1, day);
                dischargeDate = enlistDate.plusMonths(18);
                updateDate();
            }, enlistDate.getYear(), enlistDate.getMonthValue()-1, enlistDate.getDayOfMonth()).show();
        });
        binding.tvDischargeDate.setOnClickListener(view -> {
            new DatePickerDialog(this, (picker, year, month, day) -> {
                dischargeDate = LocalDate.of(year, month+1, day);
                updateDate();
            }, dischargeDate.getYear(), dischargeDate.getMonthValue()-1, dischargeDate.getDayOfMonth()).show();
        });

        // 회원가입하기 버튼
        binding.btnSignup.setOnClickListener(view -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String name = binding.etName.getText().toString().trim();
            String type;
            // 병사냐 간부냐
            if(binding.rgMemberType.getCheckedRadioButtonId() == R.id.rb_soldier)
                type = "soldier";
            else
                type = "officer";
            String rank = null;
            // 병사일때 계급이 뭔지
            if(type.equals("soldier")){
                switch(binding.spRank.getSelectedItemPosition()){
                    case 0: rank = "private"; break;
                    case 1: rank = "private-first-class"; break;
                    case 2: rank = "corporal"; break;
                    case 3: rank = "sergeant"; break;
                }
            }
            if(username.isEmpty()){
                binding.etUsername.setError("ID를 입력해주세요");
                return;
            }
            if(password.isEmpty()){
                binding.etPassword.setError("패스워드를 입력해주세요");
                return;
            }
            if(name.isEmpty()){
                binding.etName.setError("이름을 입력해주세요");
                return;
            }
            if(dischargeDate.isBefore(enlistDate)){
                Toast.makeText(this, "전역일이 입대일보다 이전입니다", Toast.LENGTH_LONG).show();
                return;
            }
            User user = new User(username, name, type, rank, enlistDate.toString(), dischargeDate.toString());
            if (!UserManager.signup(user, password)) {
                binding.etUsername.setError(getString(R.string.auth_error_duplicate));
                return;
            }

            Intent intent = new Intent(this, MainActivity.class);

            // 이거 하면 쌓인 화면을 모두 지워버림
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        binding.btnGoLogin.setOnClickListener(view -> {
            finish();
        });
    }

    // 날짜를 수정했을 때, view에 보이는 text도 수정되도록
    private void updateDate(){
        binding.tvEnlistDate.setText(enlistDate.toString());
        binding.tvDischargeDate.setText(dischargeDate.toString());
    }

}