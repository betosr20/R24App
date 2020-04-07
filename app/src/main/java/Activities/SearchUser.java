package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.r24app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class SearchUser extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference ref;
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<Uri>  uriArrayList= new ArrayList<>();
    User user =  new User();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        database = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.userList);
        databaseReference = database.getReference(FirebaseClasses.User);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);
                        userList.add(user);
                    }
                    callRecycleView();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void getImageFile(String idImage)  {
       if (idImage != null) {
            storageReference = firebaseStorage.getInstance().getReference();
            ref = storageReference.child("myImages/" + idImage);
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    uriArrayList.add(uri);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

       } else {
           uriArrayList.add(null);
       }

    }
private void callRecycleView() {

    ImageContactAdapter imageContactAdapter = new ImageContactAdapter(userList, this);
    recyclerView.setAdapter(imageContactAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
}
}