package com.MBP.honey_recipe;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.MBP.honey_recipe.Model.UserInfo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;


public class mypageFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Button logout,login;
    private RelativeLayout loaderLayout;
    private FirebaseUser user;
    private FirebaseFirestore database;
    private FirebaseStorage storage;
    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    private Button mypost, mycomment,nameEdit;
    private TextView name;
    private EditText nameEditText;
    private ArrayList<UserInfo> userinfo;
    private View dlgView;
    private DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);

        mAuth = FirebaseAuth.getInstance();
        loaderLayout = view.findViewById(R.id.loaderLayout);
        userinfo = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        login=(Button) view.findViewById(R.id.mypageLoginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        userRef = database.collection("user").document(user.getUid());
        storage = FirebaseStorage.getInstance();
        name = (TextView) view.findViewById(R.id.mypageNameText);
        nameEdit=(Button)view.findViewById(R.id.mypageEditButton);
        nameEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (user == null) {
                    toast("로그인이 필요합니다.");
                } else {
                    dlgView = (View) View.inflate(getActivity(), R.layout.name_edit_view, null);
                    nameEditText = (EditText) dlgView.findViewById(R.id.nameEditText);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                    dlg.setTitle("닉네임 변경");

                    dlg.setView(dlgView);

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (nameEditText.getText().toString().length() == 0) {
                                toast("변경할 닉네임을 입력해주세요.");
                            } else {
                                userRef.update("name", nameEditText.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                toast("닉네임 변경 완료");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                toast("닉네임 변경 실패");
                                            }
                                        });

                                name.setText(nameEditText.getText().toString());
                            }
                        }
                    });

                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dlg.show();
                }
            }
        });
        imageView = view.findViewById(R.id.profileimageView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (user == null) {
                  toast("로그인이 필요합니다.");

                } else {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
        mycomment = (Button) view.findViewById(R.id.myCommentButton);
        mycomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {

                } else {
                    Intent intent = new Intent(getActivity(), myCommentActivity.class);
                    startActivity(intent);
                }
            }
        });
        //로그아웃 버튼
        logout = view.findViewById(R.id.logoutButton);
        if (user == null) {
            logout.setClickable(false);
        }
        logout.setOnClickListener(v -> {
            if(user==null){
                toast("로그인이 필요합니다.");
            }else {
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
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();
            }
        });


        mypost = view.findViewById(R.id.myRecipeButton);
        mypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    toast("로그인이 필요합니다.");
                }else {
                    Intent intent = new Intent(getActivity(), myRecipeActivity.class);
                    startActivity(intent);
                }
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri file;
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {

                    loaderLayout.setVisibility(View.VISIBLE);
                    file = data.getData();
                    StorageReference storageRef = storage.getReference();
                    StorageReference riversRef = storageRef.child("profilephoto/" + user.getUid() + "/profile.jpg");
                    UploadTask uploadTask = riversRef.putFile(file);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            loaderLayout.setVisibility(View.GONE);
                            toast("업로드 실패");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String profile_uri = uri.toString();

                                    userRef.update("profile_pic", profile_uri)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    loaderLayout.setVisibility(View.GONE);
                                                    Glide.with(mypageFragment.this).load(file).centerCrop().override(500).into(imageView);
                                                    toast("업로드 성공");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loaderLayout.setVisibility(View.GONE);
                                                    toast("업로드 실패");
                                                }
                                            });
                                }

                            });


                        }
                    });

                } catch (Exception e) {
                }
            }
        } else {

        }
    }

    public void onResume() {
        super.onResume();

        if (user == null) {
            toast("마이페이지를 이용하기 위해서는 로그인이 필요합니다.");
        } else {
            mypageUpdate();
        }
    }

    public void mypageUpdate() {
        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userinfo = new ArrayList<>();
        database.collection("user")
                // 카테고리에 따라 게시글 받아오기
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        userinfo.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                login.setVisibility(View.GONE);
                                name.setVisibility(View.VISIBLE);

                                name.setText(document.getData().get("name").toString());
                                if (document.getData().get("profile_pic").toString().equals(null)) {
                                    imageView.setImageResource(R.drawable.default_profile);
                                } else {
                                    String url = document.getData().get("profile_pic").toString();
                                    Uri file = Uri.parse(url);
                                    Glide.with(mypageFragment.this).load(url).centerCrop().override(500).into(imageView);
                                }
                            }
                        } else {
                        }
                    }
                });
    }

    public void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}