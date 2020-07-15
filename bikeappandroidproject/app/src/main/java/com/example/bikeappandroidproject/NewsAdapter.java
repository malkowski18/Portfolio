package com.example.bikeappandroidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

        private Context mContext;
        private ArrayList<NewsItem> mNewsList;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

        public NewsAdapter(Context context, ArrayList<NewsItem> exampleList) {
            mContext = context;
            mNewsList = exampleList;
        }
        @NotNull
        @Override
        public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(v);
        }
        @Override
        public void onBindViewHolder(NewsViewHolder holder, int position) {
            NewsItem currentItem = mNewsList.get(position);
            String imageUrl = currentItem.getImageUrl();
            String creatorName = currentItem.getCreator();
            int likeCount = currentItem.getLikeCount();
            holder.mTextViewCreator.setText(creatorName);
            holder.mTextViewLikes.setText("Likes: " + likeCount);
            Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
        }
        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
        public class NewsViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public TextView mTextViewCreator;
            public TextView mTextViewLikes;

            public NewsViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.news_image_view);
                mTextViewCreator = itemView.findViewById(R.id.news_text_view_creator);
                mTextViewLikes = itemView.findViewById(R.id.news_text_view_likes);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                mListener.onItemClick(position);
                            }
                        }
                    }
                });

            }
        }


}
