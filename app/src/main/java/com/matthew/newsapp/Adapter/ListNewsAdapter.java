package com.matthew.newsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.matthew.newsapp.Common.ISO8601Parse;
import com.matthew.newsapp.DetailArticle;
import com.matthew.newsapp.Interface.ItemClickListener;
import com.matthew.newsapp.Model.Article;
import com.matthew.newsapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import java.util.Calendar;

class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    ItemClickListener itemClickListener;

    TextView article_title;
    RelativeTimeTextView article_time;
    CircleImageView article_image;
    TextView article_author;
    TextView article_description;


    public ListNewsViewHolder(View itemView)
    {
        super(itemView);
        article_image = (CircleImageView)itemView.findViewById(R.id.article_image);
        article_title = (TextView) itemView.findViewById(R.id.article_title);
        article_time = (RelativeTimeTextView) itemView.findViewById(R.id.article_time);
        article_author = (TextView) itemView.findViewById(R.id.article_author);
        article_description = (TextView) itemView.findViewById(R.id.article_description);

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

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsViewHolder>
{
    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context)
    {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.news_layout,parent,false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position)
    {
        if(articleList.get(position).getUrlToImage() != null)
        {
            if (articleList.get(position).getUrlToImage().toString().length() > 0)
            {
                Picasso.with(context)
                        .load(articleList.get(position).getUrlToImage())
                        .into(holder.article_image);
            }
        }
        if(articleList.get(position).getTitle().length()>130)
            holder.article_title.setText(Html.fromHtml(articleList.get(position).getTitle().substring(0,130)+"..."));
        else
            holder.article_title.setText(Html.fromHtml(articleList.get(position).getTitle()));
        if(articleList.get(position).getAuthor() != null)
            holder.article_author.setText("-"+articleList.get(position).getAuthor());
        if(articleList.get(position).getDescription() != null)
            holder.article_description.setText(articleList.get(position).getDescription());


        Date date = null;
        try
        {
            if (articleList.get(position).getPublishedAt() != null)
                date = ISO8601Parse.parse(articleList.get(position).getPublishedAt());
            else
                date = Calendar.getInstance().getTime();
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
        if(date != null)
            holder.article_time.setReferenceTime(date.getTime());
        else
            holder.article_time.setReferenceTime(Calendar.getInstance().getTimeInMillis());


        //set event click
        holder.setItemClickListener(new ItemClickListener()
        {
            @Override
            public void onClick(View view, int position, boolean isLongClick)
            {
                Intent detail = new Intent(context, DetailArticle.class);
                detail.putExtra("webURL", articleList.get(position).getUrl());
                context.startActivity(detail);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return articleList.size();
    }
}
