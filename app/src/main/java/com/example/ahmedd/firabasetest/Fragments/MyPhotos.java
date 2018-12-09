package com.example.ahmedd.firabasetest.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmedd.firabasetest.Adapters.PhotosAdapter;
import com.example.ahmedd.firabasetest.Model.Photos;
import com.example.ahmedd.firabasetest.MyFireBase.MyFireBase;
import com.example.ahmedd.firabasetest.PhotoActivity;
import com.example.ahmedd.firabasetest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPhotos extends Fragment {

    private RecyclerView recyclerView;
    private PhotosAdapter adapter;
    private List<Photos> photosList;
    private View view;

    private TextView txt_empty_cardView;


    public MyPhotos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_photos, container, false);
        txt_empty_cardView = view.findViewById(R.id.txt_empty_cardView);
        // Inflate the layout for this fragment
        fillRecyclerViewWithPhotos();

        txt_empty_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity(),PhotoActivity.class);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();

            }
        });

    return view;
    }
    private void fillRecyclerViewWithPhotos() {
        LinearLayoutManager layoutManager =  new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView_photos);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setStackFromEnd(false);

        photosList = new ArrayList<>();



        MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                .child("Myphotos").orderByChild("uploadedTime")
                .limitToLast(2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        photosList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Photos photosItem = snapshot.getValue(Photos.class);
                            Log.e("photoname",photosItem.getName());
                            photosItem.setKey(snapshot.getKey());
                            photosList.add(photosItem);
                        }
                        List<Photos> updatePhotoList = new ArrayList<>();
                        for(int i = (photosList.size())-1; i>=0 ;i--){
                            Photos updatePhotoItem = photosList.get(i);
                            updatePhotoList.add(updatePhotoItem);
                        }

                        if (photosList.isEmpty()){
                            txt_empty_cardView.setVisibility(View.VISIBLE);
                        }else {
                            txt_empty_cardView.setVisibility(View.GONE);
                        }

                        adapter = new PhotosAdapter(getActivity(),photosList);
                        recyclerView.setAdapter(adapter);

                        onClickListenerInRecyclerView(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    private void onClickListenerInRecyclerView(PhotosAdapter photosAdapter) {
        photosAdapter.setOnAsProfileImgClickListener(new PhotosAdapter.MyOnClickListener() {
            @Override
            public void myOnClickListener(int position, Photos photosItem) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ImageURL", photosItem.getUrl());
                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                        .updateChildren(hashMap);
                Snackbar.make(view, "Profile Picture Updated Successful  ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        photosAdapter.setOnDeleteClickListener(new PhotosAdapter.MyOnClickListener() {
            @Override
            public void myOnClickListener(int position, Photos photosItem) {

                MyFireBase.getReferenceOnPhotos().child(MyFireBase.getCurrentUser().getUid())
                        .child(photosItem.getKey()).removeValue();
                Snackbar.make(view, "Photo Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            //we can restore the photo by the snackabar listener

            }
        });

        photosAdapter.setOnDescriptionClickListener(new PhotosAdapter.MyOnClickListener() {
            @Override
            public void myOnClickListener(int position, Photos photosItem) {
           /*     HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("ImageURL", photosItem.getUrl());
                MyFireBase.getReferenceOnAllUsers().child(MyFireBase.getCurrentUser().getUid())
                        .updateChildren(hashMap);
                Snackbar.make(view, "Profile Picture Updated Successful  ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

}
