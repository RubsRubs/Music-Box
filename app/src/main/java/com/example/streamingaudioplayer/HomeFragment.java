package com.example.streamingaudioplayer;


import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.streamingaudioplayer.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    SliderView sliderView1;
    SliderView sliderView2;
    ArrayList<AudioFileModel> audioFileModelArrayList;
    SliderAdapter sliderAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        sliderView1 = binding.sliderViewRecomendacionesID;
        sliderView1.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView1.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView1.setIndicatorSelectedColor(Color.WHITE);
        sliderView1.setIndicatorUnselectedColor(Color.GRAY);
        sliderView1.setScrollTimeInSec(2);
        sliderView1.startAutoCycle();

        sliderView2 = binding.sliderViewNovedadesID;
        sliderView2.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView2.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView2.setIndicatorSelectedColor(Color.WHITE);
        sliderView2.setIndicatorUnselectedColor(Color.GRAY);
        sliderView2.setScrollTimeInSec(3);
        sliderView2.startAutoCycle();

        loadRecomendacionesSlidersData();
        loadNovedadesSlidersData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void loadRecomendacionesSlidersData() {

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();

        dbr.child("recomendaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                audioFileModelArrayList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    AudioFileModel audioFile = data.getValue(AudioFileModel.class);
                    audioFileModelArrayList.add(audioFile);
                    sliderAdapter = new SliderAdapter(getContext(), audioFileModelArrayList); //al utilizar el adaptador con sliderView hay que crear un nuevo objeto adaptador cada vez que se cogen los datos de firebase, ya que al ser un slider se cargan los objetos uno a uno en pantalla, al contrario que el RecyclerView que carga todos a la vez en una lista y con solo decalarar el objeto adaptador una vez y meterle el arraylist como argumento es suficiente.
                    sliderView1.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadNovedadesSlidersData() {

        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference();

        dbr.child("novedades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                audioFileModelArrayList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {

                    AudioFileModel audioFile = data.getValue(AudioFileModel.class);
                    audioFileModelArrayList.add(audioFile);
                    sliderAdapter = new SliderAdapter(getContext(), audioFileModelArrayList); //al utilizar el adaptador con sliderView hay que crear un nuevo objeto adaptador cada vez que se cogen los datos de firebase, ya que al ser un slider se cargan los objetos uno a uno en pantalla, al contrario que el RecyclerView que carga todos a la vez en una lista y con solo decalarar el objeto adaptador una vez y meterle el arraylist como argumento es suficiente.
                    sliderView2.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}