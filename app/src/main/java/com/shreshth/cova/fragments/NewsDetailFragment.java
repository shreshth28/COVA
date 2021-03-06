package com.shreshth.cova.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
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
    NestedScrollView nestedScrollView;
    Animation slide_up;
    CoordinatorLayout coordinatorLayout;
    int id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rv=inflater.inflate(R.layout.fragment_news_detail,container,false);
        setHasOptionsMenu(true);
        detailImageView=rv.findViewById(R.id.detail_image_view);
        contentTextView=rv.findViewById(R.id.content_text_view);
        collapsingToolbarLayout=rv.findViewById(R.id.collapsing_toolbar);
        publishedAtView=rv.findViewById(R.id.publishedAt);
        descriptionView=rv.findViewById(R.id.description);
        authorView=rv.findViewById(R.id.author);
        linkView=rv.findViewById(R.id.link);
        favBtn=rv.findViewById(R.id.fav_btn);
        unFavBtn=rv.findViewById(R.id.unfav_btn);
        nestedScrollView=(NestedScrollView) rv.findViewById(R.id.nested_scroll_view);
        coordinatorLayout=rv.findViewById(R.id.coordinator_layout);
        slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);

        referenceActivity = ((NewsDetailActivity) this.getActivity());
        newsViewModel= ViewModelProviders.of(getActivity()).get(NewsViewModel.class);
        Toolbar toolbar =rv.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        nestedScrollView.startAnimation(slide_up);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(
                        new Runnable() {
                            @Override
                            public void run() {
                                newsViewModel.insert(new News(title,description,imageUrl,content,author,publishedAt,link));
                            }
                        }

                );
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        favBtn.setVisibility(GONE);
                        unFavBtn.setVisibility(View.VISIBLE);
                        Snackbar.make(coordinatorLayout, "Added To Favourites", Snackbar.LENGTH_SHORT).show();

                    }
                });


            }
        });
        linkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(linkView.getText().toString()));
                startActivity(i);
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
                        Snackbar.make(coordinatorLayout, "Removed From Favourites", Snackbar.LENGTH_SHORT).show();

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
            authorView.setText(R.string.data_unavailable);
        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
