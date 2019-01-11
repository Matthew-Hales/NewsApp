package com.matthew.newsapp.Common;

import com.matthew.newsapp.Interface.IconBetterIdeaService;
import com.matthew.newsapp.Interface.NewsService;
import com.matthew.newsapp.Model.IconBetterIdea;
import com.matthew.newsapp.Remote.IconBetterIdeaClient;
import com.matthew.newsapp.Remote.RetrofitClient;

public class Common
{
    private static final String BASE_URL="https://newsapi.org/";

    public static final String API_KEY="dca601493d804275a51257e88af036cf";

    public static NewsService getNewsService()
    {
        return RetrofitClient.geClient(BASE_URL).create(NewsService.class);
    }

    public static IconBetterIdeaService getIconService()
    {
        return IconBetterIdeaClient.geClient().create(IconBetterIdeaService.class);
    }

    public static String getAPIUrl(String source, String sortBy, String apiKEY)
    {
        StringBuilder apiUrl = new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source)
                .append("&pageSize=100")
                .append("&apiKey=")
                .append(apiKEY)
                .toString();

    }
}
