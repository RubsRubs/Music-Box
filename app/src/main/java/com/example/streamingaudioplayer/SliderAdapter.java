package com.example.streamingaudioplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder> {

    private Context context;
    ArrayList<AudioFileModel> mAudioFileSliderModels;

    public SliderAdapter(Context context, ArrayList<AudioFileModel> audioFileSliderModels) {
        this.context = context;
        this.mAudioFileSliderModels = audioFileSliderModels;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, null);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, final int position) {

        AudioFileModel audioFileSliderModelItem = mAudioFileSliderModels.get(position);

        Glide.with(viewHolder.itemView).load(audioFileSliderModelItem.getImgURL()).fitCenter().into(viewHolder.imgViewCover);
        viewHolder.txtViewArtist.setText(audioFileSliderModelItem.getArtist());
        viewHolder.txtViewTitle.setText(audioFileSliderModelItem.getSongTitle());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* ArrayList<AudioFileModel> sliderList = new ArrayList<>();
                sliderList.add(audioFileSliderModelItem); //creamos un array de una sola posición para repdroducir una única canción en el reproductor y lo pasamos con el bundle...
                Bundle bundle = new Bundle();
                bundle.putSerializable("sliderPlayList", sliderList);
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent); //me obliga a poner context delante del método startActivity(intent)...*/
            }
        });
    }

    @Override
    public int getCount() {
        return mAudioFileSliderModels.size();
    }

// VIEW_HOLDER ----------------------------------VIEW_HOLDER---------------------------------------- VIEW_HOLDER

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder {

        ImageView imgViewCover;
        TextView txtViewArtist, txtViewTitle;

        public SliderViewHolder(View itemView) {
            super(itemView);
            imgViewCover = itemView.findViewById(R.id.imgView_Cover_ID);
            txtViewArtist = itemView.findViewById(R.id.slider_item_txt_artist_ID);
            txtViewTitle = itemView.findViewById(R.id.slider_item_txt_title_ID);
        }
    }
}
