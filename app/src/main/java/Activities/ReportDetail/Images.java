package Activities.ReportDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.r24app.R;

import java.util.ArrayList;
import java.util.List;

import Models.POJOS.Image;

/**
 * A simple {@link Fragment} subclass.
 */
public class Images extends Fragment {
   private View imageView;//Fragmento
    private RecyclerView recyclerview;
    List<Image> images;
    ImageAdapter imageAdapter;
    public Images() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        imageView = inflater.inflate(R.layout.fragment_images, container, false);
        recyclerview =  imageView.findViewById(R.id.imageList);
        onViewCreated(null,null);
        imageAdapter =  new ImageAdapter(images, getContext());
        recyclerview.setLayoutManager( new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(imageAdapter);
        return imageView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        images = new ArrayList<>();
        images.add(new Image(R.drawable.image1));
        images.add(new Image(R.drawable.imagen2));
        images.add(new Image(R.drawable.image3));
        images.add(new Image(R.drawable.imagen4));
        images.add(new Image(R.drawable.imagen5));

    }
}
