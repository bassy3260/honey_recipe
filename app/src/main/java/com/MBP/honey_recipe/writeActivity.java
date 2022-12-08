package com.MBP.honey_recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.MBP.honey_recipe.Model.Recipes;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class writeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String recipeTitle, recipeIngredient, recipeContent,recipeCost, recipeCategory,resultImageUri;;
    private ImageView resultImage;
    private RelativeLayout loaderLayout;

    private ImageView[] recipeImage;
    private EditText[] recipeStep;
    private Integer[] recipeImageId, recipeStepId;
    private Date created;
    private Uri[] StepImageUri;
    private ImageView imgView;
    private int pos;
    private Spinner categorySpinner;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //스피너
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);


        recipeImage = new ImageView[5];
        recipeStep = new EditText[5];
        resultImage = (ImageView) findViewById(R.id.resultImage);
        resultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
        StepImageUri = new Uri[5];
        recipeImageId = new Integer[]{R.id.stepImageView, R.id.recipe_image2, R.id.recipe_image3, R.id.recipe_image4, R.id.recipe_image5};
        recipeStepId = new Integer[]{R.id.stepContentEditText, R.id.recipe_step2, R.id.recipe_step3, R.id.recipe_step4, R.id.recipe_step5};
        for (int i = 0; i < 5; i++) {
            recipeStep[i] = (EditText) findViewById(recipeStepId[i]);
            recipeImage[i] = (ImageView) findViewById(recipeImageId[i]);
            imgView = recipeImage[i];
            int finalI = i;
            recipeImage[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pos = finalI;
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recipe_write_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_write_complete:

                for (int i = 0; i < 5; i++) {
                    if (recipeStep[i].getText().toString().length() == 0) {
                        count += 1;
                        if (count == 5) {
                            toast("레시피 과정을 입력해주세요");
                            count = 0;
                            break;
                        }
                    }

                    if (recipeStep[i].getText().toString().length() != 0) {
                        if (StepImageUri[i] == null) {
                            toast("이미지를 등록해주세요");
                            break;
                        } else if (i == 4 && StepImageUri[i] != null) {
                            recipeWrite();
                        }
                    } else {
                        if (StepImageUri[i] != null) {
                            toast("레시피 과정을 입력해주세요.");
                            break;
                        } else if (i == 4 && StepImageUri[i] == null) {
                            recipeWrite();
                        }
                    }
                }


                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri file;
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {

                    file = data.getData();
                    Glide.with(getApplicationContext()).load(file).centerCrop().into(recipeImage[pos]);
                    StepImageUri[pos] = file;
                } catch (Exception e) {
                }
            }
        } else if (requestCode == 1) {
            file = data.getData();
            resultImageUri = file.toString();
            Glide.with(getApplicationContext()).load(file).centerCrop().into(resultImage);
        }

    }

    public void recipeWrite() {
        loaderLayout=findViewById(R.id.loaderLayout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        //FireBase 객체 설정
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final DocumentReference recipeRef = firebaseFirestore.collection("recipes").document();

        String recipeId = recipeRef.getId(); // 게시글 아이디 가져오기
        recipeCost=((EditText)findViewById(R.id.recipeCostEditText)).getText().toString();
        recipeCategory = ((Spinner) findViewById(R.id.categorySpinner)).getSelectedItem().toString();
        loaderLayout = findViewById(R.id.loaderLayout);
        recipeTitle = ((EditText) findViewById(R.id.recipeTitle)).getText().toString();
        ;
        recipeContent = ((EditText) findViewById(R.id.writeCommentEditText)).getText().toString();
        ;
        recipeIngredient = ((EditText) findViewById(R.id.recipeIngredient)).getText().toString();
        ;
        recipeImageId = new Integer[]{R.id.stepImageView, R.id.recipe_image2, R.id.recipe_image3, R.id.recipe_image4, R.id.recipe_image5};
        recipeStepId = new Integer[]{R.id.stepContentEditText, R.id.recipe_step2, R.id.recipe_step3, R.id.recipe_step4, R.id.recipe_step5};
        for (int i = 0; i < 5; i++) {
            recipeStep[i] = (EditText) findViewById(recipeStepId[i]);
            recipeImage[i] = (ImageView) findViewById(recipeImageId[i]);

        }
        String resultUri;
        //시간 가져오기
        created = Calendar.getInstance().getTime();
        ArrayList<String> Step = new ArrayList<String>();
        ArrayList<Uri> images = new ArrayList<>();
        if(resultImageUri!=null) {
            StorageReference resultImageRef = storageRef.child("recipes/" + recipeRef.getId() + "/" + "resultImage.jpg");
            UploadTask uploadTask2 = resultImageRef.putFile(Uri.parse(resultImageUri));
            uploadTask2.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    resultImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            resultImageUri = uri.toString();
                        }
                    });
                }
            });
        }


        if (recipeCategory != "카테고리를 선택하세요.") {
            if (recipeTitle.length() != 0 && recipeIngredient.length() != 0) {
                if(resultImageUri!=null) {
                    loaderLayout.setVisibility(View.VISIBLE);
                    for (int i = 0; i < 5; i++) {
                        if (recipeStep[i].getText().toString().length() != 0) {
                            Step.add(recipeStep[i].getText().toString());
                            try {
                                StorageReference recipeImageRef = storageRef.child("recipes/" + recipeRef.getId() + "/" + i + ".jpg");
                                UploadTask uploadTask = recipeImageRef.putFile(StepImageUri[i]);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        recipeImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                images.add(uri);

                                                Recipes recipes = new Recipes(recipeId, user.getUid(), recipeTitle, recipeCategory, recipeContent, recipeIngredient,
                                                        resultImageUri, Step, images, created, 0F, 0L, Long.parseLong(recipeCost));
                                                recipeRef.set(recipes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                toast("글 업로드 완료");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                toast("글 업로드 실패");
                                                            }
                                                        });
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                    }
                                });
                            } catch (Exception e) {

                            }
                        }
                    }
                }else{
                    toast("레시피 완성 사진을 등록해주세요.");
                }
            } else {
                if (recipeTitle.length() != 0 && recipeIngredient.length() == 0) {
                    toast("레시피 재료를 입력해주세요.");
                } else {
                    toast("레시피 이름을 입력해주세요.");
                }
            }
        } else {
            toast("카테고리를 선택 해주세요.");
        }
    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
