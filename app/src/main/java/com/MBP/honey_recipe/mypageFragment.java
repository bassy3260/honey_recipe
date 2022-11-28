package com.MBP.honey_recipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MBP.honey_recipe.Model.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;


public class mypageFragment extends Fragment {
    private FirebaseAuth mAuth;
    Button logout;
    private FirebaseUser user;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    private Button mypost, mycomment,mypageEdit;
    private TextView name;
    private ArrayList<UserInfo> userinfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);

        userinfo = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        name = (TextView) view.findViewById(R.id.mypageNameText);

        imageView = view.findViewById(R.id.profileimageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        //로그아웃 버튼
        logout = view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("로그아웃");
            builder.setMessage("로그아웃 하시겠습니까?");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog, int id) {
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    toast("로그아웃에 성공하였습니다.");
                }
            });
            builder.setNegativeButton("취소",  new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog, int id) {

                }
            });
            builder.create().show();
        });


        mypost = view.findViewById(R.id.myRecipeButton);
        mypost.setOnClickListener(v -> {

        });

        mycomment = view.findViewById(R.id.myCommentButton);
        mycomment.setOnClickListener(v -> {

        });

        mypageEdit = view.findViewById(R.id.mypageEditButton);
        mypageEdit.setOnClickListener(v -> {


        });
        return view;
    }
    public void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}