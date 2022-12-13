package com.MBP.honey_recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.MBP.honey_recipe.Model.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView pwcheckText;
    EditText nickname, id, pw, pwcheck, email, phone_number;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        //기입 항목
        nickname = findViewById(R.id.signUpNickNameEditText);
        pw = findViewById(R.id.signUpPasswordEditText);
        pwcheck = findViewById(R.id.signUpPasswordCheckEditText);
        email = findViewById(R.id.signUpEmailEditText);


        //회원가입 완료 버튼
        submit = findViewById(R.id.signUpButton);
        submit.setOnClickListener(v -> {
            signup();
        });

        // 비밀번호 확인
        final TextInputLayout pwL = (TextInputLayout) findViewById(R.id.pwLabel);
        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pw.getText().toString().length() < 6) {
                    pwL.setError("비밀번호는 6자 이상으로 설정해주세요.");
                } else {
                    pwL.setErrorEnabled(false);
                }

            }
        });

        // 비밀번호 확인
        final TextInputLayout pwcheckLabel = (TextInputLayout) findViewById(R.id.pwcheckLabel);
        pwcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (pwcheck.getText().toString().equals(pw.getText().toString()) == false) {
                    pwcheckLabel.setError("비밀번호가 일치하지 않습니다.");
                } else {
                    pwcheckLabel.setErrorEnabled(false);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                // User chose the "Settings" item, show the app settings UI...
                finish();
                break;
        }
        return true;
    }
    public void signup() {
        //가입 정보 가져오기
        final RelativeLayout loaderLayout = findViewById(R.id.loaderLayout);
        mAuth = FirebaseAuth.getInstance();
        String email = ((EditText) findViewById(R.id.signUpEmailEditText)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.signUpPasswordEditText)).getText().toString().trim();
        String name = ((EditText) findViewById(R.id.signUpNickNameEditText)).getText().toString().trim();
        String passwordtest = ((EditText) findViewById(R.id.signUpPasswordCheckEditText)).getText().toString().trim();


        if (email.length() > 0 && password.length() > 0 && passwordtest.length() > 0 && name.length() > 0) {

            //파이어베이스에 신규계정 등록하기
            loaderLayout.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //가입 성공시
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String uid = user.getUid();
                        ArrayList<String> favorite=new ArrayList<>();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        UserInfo userInfo = new UserInfo(name, uid, Uri.parse("null"),favorite);
                        db.collection("user").document(user.getUid()).set(userInfo);

                        loaderLayout.setVisibility(View.GONE);

                        //가입이 이루어져을시 가입 화면을 빠져나감.
                        finish();
                        toast("회원가입 성공");

                    } else {
                        toast("중복되는 이메일이 존재합니다.");
                        loaderLayout.setVisibility(View.GONE);
                        return;  //해당 메소드 진행을 멈추고 빠져나감.

                    }

                }
            });
            //비밀번호 오류시
        } else if (password.equals(passwordtest) == false) {
            toast("비밀번호 확인이 맞지 않습니다.");
            return;
            //전화번호에 숫자만 있지 않을 시
        } else if (password.length() < 6) {
            toast("비밀번호는 6자 이상으로 설정해 주세요.");
            return;
        }
        //한 항목이라도 비어 있을 시else
        else{
            toast("모든 항목을 입력해주세요.");
        }

    }


    //토스트 메세지
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void StartMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}