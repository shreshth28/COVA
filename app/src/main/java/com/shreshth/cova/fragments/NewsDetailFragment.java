package com.shreshth.cova.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shreshth.cova.R;
import com.shreshth.cova.activity.NewsDetailActivity;
import com.shreshth.cova.database.NewsDatabase;
import com.shreshth.cova.database.NewsViewModel;
import com.shreshth.cova.models.News;
import com.shreshth.cova.network.AppExecutors;
import com.squareup.picasso.Picasso;

import static android.view.View.GONE;

public class NewsDetailFragment extends Fragment {

    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView detailImageView;
    TextView contentTextView;
    TextView publishedAtView;
    TextView descriptionView;
    TextView authorView;
    TextView linkView;
    NewsDetailActivity referenceActivity;
    private NewsViewModel newsViewModel;
    ImageButton favBtn;
    ImageButton unFavBtn;
    NewsDatabase newsDatabase;
    int id;
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
        favBtn=rv.findViewById(R.id.fav_btn);
        unFavBtn=rv.findViewById(R.id.unfav_btn);
        referenceActivity = ((NewsDetailActivity) this.getActivity());
        newsViewModel= ViewModelProviders.of(getActivity()).get(NewsViewModel.class);

        return rv;
    }

    @Override
    public void onStart() {
        super.onStart();
        final String title=((NewsDetailActivity)this.getActivity()).title;
        final String imageUrl=((NewsDetailActivity)this.getActivity()).url;
        final String content=((NewsDetailActivity)this.getActivity()).content;
        final String author=((NewsDetailActivity)this.getActivity()).author;
        final String publishedAt=((NewsDetailActivity)this.getActivity()).publishedAt;
        final String description=((NewsDetailActivity)this.getActivity()).description;
        final String link=((NewsDetailActivity)this.getActivity()).link;
        id=((NewsDetailActivity)this.getActivity()).id;
        Picasso.get()
                .load(imageUrl)
                .into(detailImageView);
        contentTextView.setText(content);
        collapsingToolbarLayout.setTitle(title);
        publishedAtView.setText(publishedAt);
        descriptionView.setText(description);
        linkView.setText(link);
        authorView.setText(author);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsViewModel.insert(new News(title,description,imageUrl,content,author,publishedAt,link));
                unFavBtn.setVisibility(View.VISIBLE);
                favBtn.setVisibility(GONE);
            }
        });
        unFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                newsDatabase=NewsDatabase.getInstance(getActivity());
                                News checkNews=newsDatabase.newsDao().getNewsByLink(link);
                                newsDatabase.newsDao().delete(checkNews);
                            }
                        }

                );
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        unFavBtn.setVisibility(GONE);
                        favBtn.setVisibility(View.VISIBLE);
                    }
                });

            }
        });
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                newsDatabase=NewsDatabase.getInstance(getContext());
                final News myNews=newsDatabase.newsDao().getNewsByLink(link);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(myNews!=null)
                        {
                            unFavBtn.setVisibility(View.VISIBLE);
                            favBtn.setVisibility(GONE);
                        }
                        else{
                            favBtn.setVisibility(View.VISIBLE);
                            unFavBtn.setVisibility(GONE);
                        }
                    }
                });

            }
        });

        if(author==null)
        {
            authorView.setText("Data Unavailable");
        }

    }
}
