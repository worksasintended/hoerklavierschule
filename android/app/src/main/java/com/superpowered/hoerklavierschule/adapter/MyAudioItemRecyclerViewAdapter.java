package com.superpowered.hoerklavierschule.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superpowered.hoerklavierschule.AudioItemFragment.OnListFragmentInteractionListener;
import com.superpowered.hoerklavierschule.R;
import com.superpowered.hoerklavierschule.sql.Piece;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Piece} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAudioItemRecyclerViewAdapter extends RecyclerView.Adapter<MyAudioItemRecyclerViewAdapter.ViewHolder> {

    private final List<Piece> pieces;
    private final OnListFragmentInteractionListener mListener;

    public MyAudioItemRecyclerViewAdapter(List<Piece> pieces, OnListFragmentInteractionListener listener) {
        this.pieces = pieces;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_audioitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.piece = pieces.get(position);
        holder.textView_name.setText(pieces.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.piece);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("siiiiz", Integer.toString(pieces.size()));
        return pieces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textView_name;
        public Piece piece;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textView_name = (TextView) view.findViewById(R.id.textView_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView_name.getText() + "'";
        }
    }
}
