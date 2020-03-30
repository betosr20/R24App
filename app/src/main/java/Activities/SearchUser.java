package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.r24app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class SearchUser extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    ArrayList<User> userList = new ArrayList<>();
    User user =  new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(FirebaseClasses.User);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        user = snapshot.getValue(User.class);
                        userList.add(user);
                        getImageFile(user.getProfileImage(), "png");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void setImagesOnView() {
        for (User user: userList) {
            getImageFile(user.getProfileImage(), "png");
        }
    }
    private void getImageFile(String idImage, String typeImage) {
        System.out.println(idImage);
       if (idImage != null) {
           String[] splitResult = idImage.split(".png",2);
           idImage = splitResult[0];
           System.out.println(idImage);
       }


    }
}
