package com.matthew.newsapp.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IconBetterIdeaClient
{
    private static Retrofit retrofit=null;

    public static Retrofit geClient()
    {
        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(/*"https://icons.better-idea.org/"*/ "https://besticon-demo.herokuapp.com/" /*"https://favicongrabber.com/"*/)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
