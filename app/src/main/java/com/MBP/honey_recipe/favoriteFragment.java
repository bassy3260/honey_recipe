package com.MBP.honey_recipe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.MBP.honey_recipe.adapter.favoriteListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class favoriteFragment extends Fragment {

    private FirebaseFirestore database;
    private FirebaseUser user;
    private favoriteListViewAdapter adapter;
    private RecyclerView favoriteRecyclerView;
    private ArrayList<String> favoriteList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_favorite, container, false);
        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        favoriteList = new ArrayList<>();
        favoriteRecyclerView = (RecyclerView) rootView.findViewById(R.id.favoriteRecyclerView);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(user==null){
            toast("즐겨찾기 기능을 이용하려면 로그인이 필요합니다.");
        }else{
            getFavorite();
        }
    }

    public void getFavorite(){
        CollectionReference collectionReference = database.collection("user");
        collectionReference.whereEqualTo("uid", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                favoriteList = (ArrayList<String>) document.getData().get("favorite");
                            }
                            adapter = new favoriteListViewAdapter(getActivity(), favoriteList);
                            favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            favoriteRecyclerView.setHasFixedSize(true);
                            favoriteRecyclerView.setAdapter(adapter);
                        } else {
                            Log.d("로그", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //토스트메시지 함수
    public void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}