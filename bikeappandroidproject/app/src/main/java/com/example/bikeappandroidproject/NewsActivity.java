package com.example.bikeappandroidproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener{
        private RecyclerView mRecyclerView;
        private NewsAdapter mNewsAdapter;
        private ArrayList<NewsItem> mNewsList;
        private RequestQueue mRequestQueue;

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news);
            mRecyclerView = findViewById(R.id.news_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mNewsList = new ArrayList<>();
            mRequestQueue = Volley.newRequestQueue(this);
            parseJSON();
        }
        private void parseJSON() {
            String url = "https://pixabay.com/api/?key=16867344-a00ef5bf0f4b2992b00dea3b3&q=bicycle+city&image_type=photo&pretty=true";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("hits");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject hit = jsonArray.getJSONObject(i);
                                    String creatorName = hit.getString("user");
                                    String imageUrl = hit.getString("webformatURL");
                                    int likeCount = hit.getInt("likes");
                                    mNewsList.add(new NewsItem(imageUrl, creatorName, likeCount));
                                }
                                mNewsAdapter = new NewsAdapter(NewsActivity.this, mNewsList);
                                mRecyclerView.setAdapter(mNewsAdapter);
                                mNewsAdapter.setOnItemClickListener(NewsActivity.this);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            mRequestQueue.add(request);
        }

    @Override
    public void onItemClick(int position) {
        Intent newsIntent = new Intent(this, NewsDetail.class);
        NewsItem clickedItem = mNewsList.get(position);
        newsIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        newsIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        newsIntent.putExtra(EXTRA_LIKES, clickedItem.getLikeCount());
        startActivity(newsIntent);
    }

    }