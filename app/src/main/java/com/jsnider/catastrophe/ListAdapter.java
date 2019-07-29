package com.jsnider.catastrophe;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class ListAdapter extends
        RecyclerView.Adapter<ListAdapter.ViewHolder>  {

    private final LinkedList<String> mWordList;
    private LayoutInflater mInflater;

    public static final String ID =
            "com.jsnider.catastrophe.ID";

    public ListAdapter(Context context,
                           LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_item,
                parent, false);
        return new ViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
                                implements View.OnClickListener {

        public final TextView wordItemView;
        final ListAdapter mAdapter;

        public ViewHolder(View itemView, ListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.itemTextView);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int mPosition = getLayoutPosition();
            String element = mWordList.get(mPosition);
            TextView listItem = (TextView) view.findViewById(R.id.itemTextView);
            String id = listItem.getText().toString();
            mWordList.set(mPosition, "Clicked! " + element);

            //add start second activity
            Intent intent = new Intent(view.getContext(), ViewActivity.class);
            intent.putExtra(ID, id);

            view.getContext().startActivity(intent);

            mAdapter.notifyDataSetChanged();

        }
    }
}
