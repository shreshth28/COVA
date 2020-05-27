package com.shreshth.cova.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.shreshth.cova.R;
import com.shreshth.cova.fragments.NewsListFragment;
import com.shreshth.cova.models.News;

import java.util.ArrayList;
import java.util.List;

public class CovaAppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientWidgetItemFactory(getApplicationContext(),intent);
    }

    class IngredientWidgetItemFactory implements RemoteViewsFactory{

        private Context context;
        private int appWidgetId;
        private List<News> dataList= new ArrayList<>();

        IngredientWidgetItemFactory(Context context,Intent intent)
        {
            this.context=context;
            this.appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            //connect to database
        }

        @Override
        public void onDataSetChanged() {
            //close data source
            if(NewsListFragment.fromDatabase==null)
            {

            }
            else {
                dataList = NewsListFragment.fromDatabase;
            }
            Log.d(CovaAppWidgetService.class.getSimpleName(),"Data Changed");
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views=new RemoteViews(context.getPackageName(), R.layout.news_widget_item);
            views.setTextViewText(R.id.ingredient_widget_item_text,dataList.get(position).getTitle());

            Intent fillIntent= new Intent();
            fillIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            views.setOnClickFillInIntent(R.id.ingredient_widget_btn,fillIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
