package com.jsnider.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class ListAdapter extends
        RecyclerView.Adapter<ListAdapter.ViewHolder>  {

    private final ArrayList<String> mWordList;
    private final ArrayList<String> urlList;
    private LayoutInflater mInflater;
    RequestQueue queue;

    public static final String ID =
            "com.jsnider.catastrophe.ID";
    public static final String URL =
            "com.jsnider.catastrophe.URL";

    public ListAdapter(Context context,
                       ArrayList<String> wordList,
                       ArrayList<String> urlList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
        this.urlList = urlList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_item,
                parent, false);
        return new ViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        holder.itemImageView.getContext();

        //final ImageView mImageView = itemView.findViewById(R.id.itemImageView);
        queue = Volley.newRequestQueue(holder.itemImageView.getContext());

        String itemUrl = urlList.get(position);

        ImageRequest request = new ImageRequest(itemUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        //mImageView.setImageBitmap(bitmap);
                        holder.itemImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Loading thumbnail", error.toString());
                    }
                });
        queue.add(request);

    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener {


        //public final TextView wordItemView;
        public final ImageView itemImageView;
        final ListAdapter mAdapter;

        public ViewHolder(View itemView, ListAdapter adapter) {
            super(itemView);
            //wordItemView = itemView.findViewById(R.id.itemTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);

            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int mPosition = getLayoutPosition();
            String element = mWordList.get(mPosition);
            String url = urlList.get(mPosition);

            //TextView listItem = (TextView) view.findViewById(R.id.itemTextView);
            //String id = listItem.getText().toString();
            //mWordList.set(mPosition, "Clicked! " + element);

            //add start second activity
            Intent intent = new Intent(view.getContext(), ViewActivity.class);
            //intent.putExtra(ID, id);
            intent.putExtra(URL, url);

            view.getContext().startActivity(intent);

            mAdapter.notifyDataSetChanged();

        }
    }
}
