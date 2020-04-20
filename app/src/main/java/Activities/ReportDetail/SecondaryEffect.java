package Activities.ReportDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.r24app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.Constants.FirebaseClasses;
import Models.POJOS.NaturalDisaster;

public class SecondaryEffect extends Fragment {
    private String naturalDisasterName;
    private TextView secondaryEffectTextView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private NaturalDisaster naturalDisaster;

    public SecondaryEffect() {}

    public SecondaryEffect(String naturalDisasterName) {
        this.naturalDisasterName = naturalDisasterName;
        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(FirebaseClasses.NaturalDisaster).child(naturalDisasterName);
        this.naturalDisaster = new NaturalDisaster();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View secondaryEffectView = inflater.inflate(R.layout.fragment_secondary_effect, container, false);
        secondaryEffectTextView = secondaryEffectView.findViewById(R.id.tv_secondaryEffectText);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    naturalDisaster = dataSnapshot.getValue(NaturalDisaster.class);
                    secondaryEffectTextView.setText(naturalDisaster.getSecondaryEffect());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        return  secondaryEffectView;
    }
}
