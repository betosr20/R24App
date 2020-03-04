package com.example.r24app;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import Activities.MapActivity;
import Models.Constants.FirebaseClasses;
import Models.POJOS.User;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Este metodo carga un dummy user solo para probar las primeras veces que efectivamente
        //haya conexion con la bbdd
        loadUser();

        //implementar el onclick del boton de Go To Map
        goToMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadUser(){

        database = FirebaseDatabase.getInstance();
        User usuario = new User("betico2", "Alberto", "Guerra", "betico2", "betog19@gmail.com",
                                "88880666", "mi choza", "añlksdfjlaksdfljskdf", true, true, true, true, true, true);
        //databaseReference =  database.getReference("Users").child(usuario.getUserName());
        //databaseReference.setValue(usuario);

        //Aqui va el Id del usuario que se uso para guardar en la bbdd. En este caso se guardo
        //como Betico porque es solo para cargar un usuario y que se vea que hay conexion
        databaseReference = database.getReference(FirebaseClasses.User).child("betico2");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User testUser = dataSnapshot.getValue(User.class);
                    TextView id = findViewById(R.id.textViewMain);
                    id.setText(testUser.getId());

                } else {
                    TextView notFound = findViewById(R.id.textViewMain);
                    notFound.setText("No hay conexion con bbdd");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goToMap(){
        Button buttonGoToMap= findViewById(R.id.buttonGoToMap);
        buttonGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
    }
}
