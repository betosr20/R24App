package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de usuarios  ");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUser(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUser(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    private void callRecycleView() {

    ImageContactAdapter imageContactAdapter = new ImageContactAdapter(userList, this);
    recyclerView.setAdapter(imageContactAdapter);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
}
private void searchUser(String searchText) {
        System.out.println(searchText);
}
}