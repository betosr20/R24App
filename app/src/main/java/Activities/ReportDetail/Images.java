package Activities.ReportDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.r24app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import Adapters.ImageAdapter;
import Models.Constants.FirebaseClasses;
import Models.POJOS.ReportPicture;

/**
 * A simple {@link Fragment} subclass.
 */
public class Images extends Fragment {
    private RecyclerView recyclerview;
    private ImageAdapter imageAdapter;
    private String idReport;
    private DatabaseReference databaseReference;
    private List<ReportPicture> imagesReference = new ArrayList<>();
    private List<StorageReference> referencesList = new ArrayList<>();
    private StorageReference storageReference;

    public Images() {
    }

    public Images(String idReport) {
        this.idReport = idReport;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        this.databaseReference = database.getReference(FirebaseClasses.ReportPicture);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View imageView = inflater.inflate(R.layout.fragment_images, container, false);
        recyclerview = imageView.findViewById(R.id.imageList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        imagesReference.add(snapshot.getValue(ReportPicture.class));
                    }
                    for (ReportPicture reportPicture : imagesReference) {
                        if (reportPicture.getReportId().equals(idReport)) {
                            StorageReference reference = storageReference.child("ReportsImages/" + reportPicture.getImageName());
                            referencesList.add(reference);
                        }
                    }
                    if (referencesList.isEmpty()) {
                        referencesList.add(storageReference.child("myImages/image-placeholder.jpg"));
                    }
                    imageAdapter = new ImageAdapter(referencesList, getContext());
                    recyclerview.setAdapter(imageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return imageView;
    }
}
