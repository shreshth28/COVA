package com.shreshth.cova.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shreshth.cova.R;
import com.shreshth.cova.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    List<News> myList=null;
    NewsItemClickListener listener;
    private final static int FADE_DURATION = 1000;

    public NewsAdapter(NewsItemClickListener listener) {
        this.listener = listener;
    }

    public interface NewsItemClickListener{
        void onNewsClickListener(int index);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.title.setText(myList.get(position).getTitle());
        holder.description.setText(myList.get(position).getDescription());
        if(myList.get(position).getUrlToImage().isEmpty())
        {
            Picasso.get().load(R.drawable.logo).resize(100,100).centerCrop().into(holder.newsItemImageView);
        }
        else {

            Picasso.get().load(myList.get(position).getUrlToImage()).placeholder(R.drawable.logo).resize(100, 100)
                    .centerCrop().into(holder.newsItemImageView);
        }
        setFadeAnimation(holder.itemView);

    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        if(myList==null)
        {
            return 0;
        }
        return myList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView newsItemImageView;
        TextView title;
        TextView description;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsItemImageView=itemView.findViewById(R.id.news_list_image_view);
            title=itemView.findViewById(R.id.title_text_view);
            description=itemView.findViewById(R.id.description_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int clickedIndex=getAdapterPosition();
            listener.onNewsClickListener(clickedIndex);
        }
    }

    public void setData(List list)
    {
        myList=list;
        notifyDataSetChanged();
    }
}
