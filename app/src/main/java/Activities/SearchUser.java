package Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.r24app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class SearchUser extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference ref;
    FirebaseRecyclerOptions<User> options;
    FirebaseRecyclerAdapter<User, ImageContactAdapter> adapter;
    ArrayList<User> userList = new ArrayList<>();
    User user =  new User();
    RecyclerView recyclerView;
   private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lista de usuarios");

        actionBar.setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        databaseReference = database.getReference(FirebaseClasses.User);
        loadDataIntoRecycleView("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchUser(query);
                loadDataIntoRecycleView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                searchUser(newText);
                loadDataIntoRecycleView(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    private void loadDataIntoRecycleView(String searchText) {
        String firstLetterCapital = searchText;
        String realText = "";
        if (!searchText.isEmpty()) {
             firstLetterCapital = searchText.substring(0, 1).toUpperCase();
            realText  =  searchText.substring(1).toLowerCase();
            realText = firstLetterCapital + realText;

        }
        Query query = databaseReference.orderByChild(FirebaseClasses.UsersNameAttribute).startAt(realText).endAt(realText + "\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query,User.class).build();
        adapter =  new FirebaseRecyclerAdapter<User, ImageContactAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ImageContactAdapter holder, int position, @NonNull User model) {
                holder.progressBar.setIndeterminate(true);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.fullName.setText(" "+model.getName() + " " + model.getLastName());
                holder.cellPhone.setText(" "+model.getCellPhone());

                if(model.isNeedHelp()){
                    holder.needHelp.setText(" Auxilio");
                    holder.needHelp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sos, 0, 0, 0);
                }else{
                    holder.needHelp.setText(" Estoy bien");
                    holder.needHelp.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_okstatus, 0, 0, 0);
                }

                if (!model.getProfileImage().isEmpty()) {
                    holder.progressBar.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load(model.getProfileImage())
                            .resize(300,300)
                            .into(holder.imageView);


                }else {

                    holder.progressBar.setVisibility(View.INVISIBLE);
                    Picasso.get()
                            .load("https://firebasestorage.googleapis.com/v0/b/r24app-e1e7d.appspot.com/o/myImages%2FemptyImage.jpg?alt=media&token=c216dd47-5de0-46ed-9ef1-79f573d25014")
                            .resize(300,300)
                            .into(holder.imageView);

                }
            }

            @NonNull
            @Override
            public ImageContactAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_contact,parent, false);
                return new ImageContactAdapter(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}


