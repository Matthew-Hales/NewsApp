package com.matthew.newsapp.Interface;

import com.matthew.newsapp.Common.Common;
import com.matthew.newsapp.Model.News;
import com.matthew.newsapp.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService
{
    @GET("v2/sources?apiKey="+ Common.API_KEY)
    Call<WebSite> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);
}
