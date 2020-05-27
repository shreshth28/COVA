package com.shreshth.cova.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shreshth.cova.R;
import com.shreshth.cova.activity.NewsDetailActivity;
import com.squareup.picasso.Picasso;

public class NewsDetailFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView detailImageView;
    TextView contentTextView;
    TextView publishedAtView;
    TextView descriptionView;
    TextView authorView;
    TextView linkView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rv=inflater.inflate(R.layout.fragment_news_detail,container,false);
        detailImageView=rv.findViewById(R.id.detail_image_view);
        contentTextView=rv.findViewById(R.id.content_text_view);
        collapsingToolbarLayout=rv.findViewById(R.id.collapsing_toolbar);
        publishedAtView=rv.findViewById(R.id.publishedAt);
        descriptionView=rv.findViewById(R.id.description);
        authorView=rv.findViewById(R.id.author);
        linkView=rv.findViewById(R.id.link);

        return rv;
    }

    @Override
    public void onStart() {
        super.onStart();
        String title=((NewsDetailActivity)this.getActivity()).title;
        String imageUrl=((NewsDetailActivity)this.getActivity()).url;
        String content=((NewsDetailActivity)this.getActivity()).content;
        String author=((NewsDetailActivity)this.getActivity()).author;
        String publishedAt=((NewsDetailActivity)this.getActivity()).publishedAt;
        String description=((NewsDetailActivity)this.getActivity()).description;
        String link=((NewsDetailActivity)this.getActivity()).link;
        Picasso.get()
                .load(imageUrl)
                .into(detailImageView);
        contentTextView.setText(content);
        collapsingToolbarLayout.setTitle(title);
        publishedAtView.setText(publishedAt);
        descriptionView.setText(description);
        linkView.setText(link);
        authorView.setText(author);
        if(author==null)
        {
            authorView.setText("Data Unavailable");
        }

    }
}
