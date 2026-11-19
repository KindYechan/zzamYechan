package com.example.jjamyechan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jjamyechan.databinding.ActivityAdminRoomBinding;
import com.example.jjamyechan.databinding.DialogBroadcastBinding;
import com.example.jjamyechan.databinding.DialogLetterBinding;
import com.example.jjamyechan.databinding.PopupRoomMenuBinding;
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
        boolean[] feature = getIntent().getBooleanArrayExtra("feature");
        if(feature == null){
            feature = new boolean[]{true, true, true, true, true, true};
        }
        binding.tvRoomTitle.setText(getString(R.string.unit_name, unitName));
        binding.btnUnreadNotice.setVisibility(feature[3] ? View.VISIBLE : View.GONE);
        binding.btnUrgentQuest.setVisibility(feature[1] ? View.VISIBLE : View.GONE);

        // 캘린더 카드 보이게할지
        binding.cardRoomCalendar.setVisibility(feature[0] ? View.VISIBLE : View.GONE);
        // 퀘스트 카드 보이게할지
        binding.cardRoomQuest.setVisibility(feature[1] ? View.VISIBLE : View.GONE);
        // 진급 카드 보이게할지
        binding.cardPromotion.setVisibility(feature[2] ? View.VISIBLE : View.GONE);
        // 게시판 카드 보이게할지
        binding.cardBoard.setVisibility(feature[3] ? View.VISIBLE : View.GONE);
        // 전파사항 카드 보이게할지
        binding.cardBroadcast.setVisibility(feature[4] ? View.VISIBLE : View.GONE);
        // 마음의 편지 카드 보이게할지
        binding.cardLetter.setVisibility(feature[5] ? View.VISIBLE : View.GONE);

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
                if(broadcast.isEmpty()){
                    dialogBinding.etBroadcast.setError("전파 사항을 입력해주세요");
                    return;
                }
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

        // 마음의 편지
        binding.cardLetter.setOnClickListener(view -> {
            showLettetDialog();
        });

        // 팝업 햄버거버튼
        binding.btnRoomMenu.setOnClickListener(view -> {
            // 팝업 생성부
            PopupRoomMenuBinding menuBinding = PopupRoomMenuBinding.inflate(getLayoutInflater());
//            menuBinding.menuItemCalendar.setVisibility(feature[0] ? View.VISIBLE : View.GONE);
//            menuBinding.menuItemQuest.setVisibility(feature[1] ? View.VISIBLE : View.GONE);
//            menuBinding.menuItemCalculator.setVisibility(feature[2] ? View.VISIBLE : View.GONE);
//            menuBinding.menuItemBoard.setVisibility(feature[3] ? View.VISIBLE : View.GONE);
//            menuBinding.menuItemLetter.setVisibility(feature[5] ? View.VISIBLE : View.GONE);
            // 화면이 먹어서 dimen에 있는 width를 불러와서 화면에 적용
            int width = getResources().getDimensionPixelSize(R.dimen.popup_menu_width);
            PopupWindow popup = new PopupWindow(menuBinding.getRoot(), width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            // 그림자
            popup.setElevation(16);
            popup.showAsDropDown(binding.btnRoomMenu);

//            menuBinding.menuItemQuest.setOnClickListener();
//            menuBinding.menuItemCalendar.setOnClickListener();
//            menuBinding.menuItemBoard.setOnClickListener();
//            menuBinding.menuItemCalculator.setOnClickListener();
            menuBinding.menuItemLetter.setOnClickListener(view2 -> {
                popup.dismiss();
                showLettetDialog();
            });
//            menuBinding.menuItemEdit.setOnClickListener();
//            menuBinding.menuItemLog.setOnClickListener();
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

    private void showLettetDialog() {
        // dialog 생성부
        Dialog dialog = new Dialog(this);
        DialogLetterBinding dialogBinding = DialogLetterBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();

        // 몇 시간 후 예약전송할지 고르는거
        RadioButton[] delays = {dialogBinding.rbDelayNow, dialogBinding.rbDelay1h, dialogBinding.rbDelay3h,
                dialogBinding.rbDelay6h, dialogBinding.rbDelay1d, dialogBinding.rbDelayCustom};
        // for문 돌면서
        for(RadioButton delay : delays){
            delay.setOnClickListener(view2 -> {
                for(RadioButton allDelay : delays) {
                    allDelay.setChecked(false);
                }
                delay.setChecked(true);

                // 사용자 지정시간 뷰 보여줌
                if(delay.equals(dialogBinding.rbDelayCustom)) {
                    dialogBinding.tvCustomTime.setVisibility(ViewGroup.VISIBLE);
                }
                else {
                    dialogBinding.tvCustomTime.setVisibility(ViewGroup.GONE);
                }

                // OO시간 후 예약전송 메세지 세팅
                if(delay.equals(dialogBinding.rbDelayNow)){
                    dialogBinding.btnSubmit.setText(getString(R.string.letter_submit));
                }
                else{
                    dialogBinding.btnSubmit.setText(getString(R.string.letter_submit_later, delay.getText()));
                }
            });
        }
        // x버튼 누르면
        dialogBinding.btnClose.setOnClickListener(view2 -> {
            dialog.dismiss();
        });
        // 보내기 버튼 누르면 (서버랑 연동 예정)
        dialogBinding.btnSubmit.setOnClickListener(view2 -> {
            String letter = dialogBinding.etLetter.getText().toString().trim();
            if(letter.isEmpty()){
                dialogBinding.etLetter.setError("내용을 입력해주세요");
                return;
            }
            Toast.makeText(this, letter, Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
    }
}