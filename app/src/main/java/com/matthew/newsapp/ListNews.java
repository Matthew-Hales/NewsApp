package com.matthew.newsapp;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.matthew.newsapp.Adapter.ListNewsAdapter;
import com.matthew.newsapp.Common.Common;
import com.matthew.newsapp.Interface.NewsService;
import com.matthew.newsapp.Model.Article;
import com.matthew.newsapp.Model.News;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity
{

    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    SpotsDialog dialog;
    NewsService mService;
    TextView top_author, top_title,txtSource;
    SwipeRefreshLayout swipeRefreshLayout;

    String source = "", sortBy = "", webHotURL="",name = "";

    ListNewsAdapter adapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        //Service
        mService = Common.getNewsService();

        dialog = new SpotsDialog(this);

        //View
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadNews(source,true);
            }
        });

        diagonalLayout =(DiagonalLayout)findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent detail = new Intent(getBaseContext(), DetailArticle.class);
                detail.putExtra("webURL", webHotURL);
                startActivity(detail);
            }
        });
        kbv = (KenBurnsView)findViewById(R.id.top_image);
        top_author=(TextView)findViewById(R.id.top_author);
        top_title=(TextView)findViewById(R.id.top_title);
        txtSource = (TextView)findViewById(R.id.txtSource);

        lstNews = (RecyclerView)findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        //Intent
        if(getIntent() != null)
        {
            source = getIntent().getStringExtra("source");
            name = getIntent().getStringExtra("name");
            if(!source.isEmpty())
            {
                if(!name.isEmpty())
                {
                    txtSource.setText(name);
                }
                else
                {
                    txtSource.setText(source);
                }
                loadNews(source,false);
            }

        }

    }

    private void loadNews(String source, boolean isRefreshed)
    {
        if(!isRefreshed)
        {
            dialog.show();
            mService.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>()
                    {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response)
                        {
                            dialog.dismiss();
                            //get first article
                            if (response.body().getArticles().get(0).getUrlToImage() != null)
                            {
                                if (response.body().getArticles().get(0).getUrlToImage().toString().length() > 0)
                                {
                                    Picasso.with(getBaseContext())
                                            .load(response.body().getArticles().get(0).getUrlToImage())
                                            .into(kbv);
                                }
                            }
                            top_title.setText(Html.fromHtml(response.body().getArticles().get(0).getTitle()));
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            //Load remaining atricles
                            List<Article> removeFirstItem = response.body().getArticles();
                            //First article shown on diagonal layout. remove it
                            removeFirstItem.remove(0);
                            adapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t)
                        {

                        }
                    });
        }
        else
        {
            dialog.show();
            mService.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>()
                    {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response)
                        {
                            dialog.dismiss();
                            //get first article
                            if(response.body().getArticles().get(0).getUrlToImage() != null)
                            {
                                if (response.body().getArticles().get(0).getUrlToImage().length() > 0)
                                {
                                    Picasso.with(getBaseContext())
                                            .load(response.body().getArticles().get(0).getUrlToImage())
                                            .into(kbv);
                                }
                            }
                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotURL = response.body().getArticles().get(0).getUrl();

                            //Load remaining atricles
                            List<Article> removeFirstItem = response.body().getArticles();
                            //First article shown on diagonal layout. remove it
                            removeFirstItem.remove(0);
                            adapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            adapter.notifyDataSetChanged();
                            lstNews.setAdapter(adapter);

                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t)
                        {

                        }
                    });
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}

















