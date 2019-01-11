package com.matthew.newsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matthew.newsapp.Common.Common;
import com.matthew.newsapp.Interface.IconBetterIdeaService;
import com.matthew.newsapp.Interface.ItemClickListener;
import com.matthew.newsapp.ListNews;
import com.matthew.newsapp.Model.IconBetterIdea;
import com.matthew.newsapp.Model.WebSite;
import com.matthew.newsapp.R;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ListSourceViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener
{

    ItemClickListener itemClickListener;

    TextView source_title;
    TextView description;
    CircleImageView source_image;

    public ListSourceViewHolder(View itemView)
    {
        super(itemView);
        source_image = (CircleImageView) itemView.findViewById(R.id.source_image);
        source_title = (TextView) itemView.findViewById(R.id.source_name);
        description = (TextView) itemView.findViewById(R.id.description);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder>
{

    private Context context;
    private WebSite webSite;

    private IconBetterIdeaService mService;

    public ListSourceAdapter(Context context, WebSite webSite)
    {
        this.context = context;
        this.webSite = webSite;

        mService = Common.getIconService();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout,parent,false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder holder, int position)
    {
        StringBuilder iconBetterAPI = new StringBuilder(/*"https://icons.better-idea.org/allicons.json?url="*/
                                                        "https://besticon-demo.herokuapp.com/allicons.json?url="
                                                        /*"https://favicongrabber.com/api/grab/"*/);
        iconBetterAPI.append(webSite.getSources().get(position).getUrl());
        /*try
        {
            iconBetterAPI.append(getHostName(webSite.getSources().get(position).getUrl()));
        } catch (URISyntaxException e)
        {
            e.printStackTrace();
        }*/

        mService.getIconUrl(iconBetterAPI.toString())
                .enqueue(new Callback<IconBetterIdea>()
                {
                    @Override
                    public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response)
                    {
                        if(response.body() != null)
                        {
                            if (response.body().getIcons().size() > 0)
                            {
                                Picasso.with(context)
                                        .load(response.body().getIcons().get(0).getUrl())
                                        .into(holder.source_image);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IconBetterIdea> call, Throwable t)
                    {

                    }
                });

        holder.source_title.setText(webSite.getSources().get(position).getName());
        holder.description.setText(webSite.getSources().get(position).getDescription());

        holder.setItemClickListener(new ItemClickListener()
        {
            @Override
            public void onClick(View view, int position, boolean isLongClick)
            {
                Intent intent = new Intent(context, ListNews.class);
                intent.putExtra("source",webSite.getSources().get(position).getId());
                intent.putExtra("name",webSite.getSources().get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return webSite.getSources().size();
    }

    public static String getHostName(String url) throws URISyntaxException
    {
        URI uri = new URI(url);
        String hostname = uri.getHost();
        // to provide faultproof result, check if not null then return only hostname, without www.
        if (hostname != null) {
            return hostname.startsWith("www.") ? hostname.substring(4) : hostname;
        }
        return hostname;
    }
}



















