package com.matthew.newsapp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.matthew.newsapp.Adapter.ListSourceAdapter;
import com.matthew.newsapp.Common.Common;
import com.matthew.newsapp.Interface.NewsService;
import com.matthew.newsapp.Model.WebSite;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService mService;
    ListSourceAdapter adapter;
    SpotsDialog dialog;
    SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init cache
        Paper.init(this);

        //Init service
        mService = Common.getNewsService();

        //Init view
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadWebsiteSource(true);
            }
        });

        listWebsite = (RecyclerView) findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);


        loadWebsiteSource(false);


    }

    private void loadWebsiteSource(boolean isRefreshed)
    {
        if(!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if(cache !=null && !cache.isEmpty() && !cache.equals("null")) //if have cache
            {
                WebSite webSite = new Gson().fromJson(cache,WebSite.class); //convert cache from Json to object
                adapter = new ListSourceAdapter(getBaseContext(),webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }
            else // if no cache
            {
                dialog.show();
                //fetch new data
                mService.getSources().enqueue(new Callback<WebSite>()
                {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response)
                    {
                        adapter = new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t)
                    {

                    }
                });
            }
        }
        else //If from swipe to refresh
        {
            swipeLayout.setRefreshing(true);
            //fetch new data
            mService.getSources().enqueue(new Callback<WebSite>()
            {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response)
                {
                    adapter = new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    //save to cache
                    Paper.book().write("cache", new Gson().toJson(response.body()));

                    //dismiss refresh progressing
                    swipeLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t)
                {

                }
            });
        }
    }
}



































