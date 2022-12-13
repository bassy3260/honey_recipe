package com.MBP.honey_recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.MBP.honey_recipe.MainActivity;
import com.MBP.honey_recipe.R;
import com.MBP.honey_recipe.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button sign;
    Button login;
    private FirebaseAuth mAuth;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.default_toolbar);
        setSupportActionBar(toolbar);//액션바를 툴바로 바꿔줌
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("로그인");
        FirebaseUser user = mAuth.getCurrentUser();
        //회원가입 버튼
        sign = findViewById(R.id.gotoSigninButton);

        //회원가입 버튼 클릭시, 회원가입 페이지로 이동
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });

        //로그인 버튼 클릭 시
        login = findViewById(R.id.loginButton);
        login.setOnClickListener(v -> {
            login();
        });

        //자동 로그인
        if (user!=null){
            StartMyActivity(MainActivity.class);
            finish();
        }
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
    //로그인 함수
    public void login() {
        String email = ((EditText) findViewById(R.id.loginEmailEditText)).getText().toString();
        String password = ((EditText) findViewById(R.id.loginPasswordEditText)).getText().toString();
        final RelativeLayout loaderLayout = findViewById(R.id.loaderLayout);
        loaderLayout.setVisibility(View.VISIBLE);
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                loaderLayout.setVisibility(View.GONE);
                                StartMyActivity(MainActivity.class);
                                toast("로그인에 성공하였습니다.");
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                loaderLayout.setVisibility(View.GONE);
                                toast("아이디 혹은 비밀번호가 맞지 않습니다.");
                            }
                        }
                    });
        }else{
            loaderLayout.setVisibility(View.GONE);
            toast("이메일과 비밀번호를 정확히 입력해주세요.");
        }
    }

    //토스트 메세지
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    //액티비티 이동 함수
    public void StartMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    //액션바 타이틀 설정
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

}