package com.argandevteam.fpes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.argandevteam.fpes.R;
import com.argandevteam.fpes.fragment.CentreFragment.OnListFragmentInteractionListener;
import com.argandevteam.fpes.model.Centre;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Centre} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CentreRecyclerAdapter extends RecyclerView.Adapter<CentreRecyclerAdapter.ViewHolder> {
    public interface ItemClickListener {
        public void onClick(View view, int position);
    }

    private ItemClickListener listener;
    private static final String TAG = "RecyclerViewAdapter";
    private final List<Centre> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;


    public CentreRecyclerAdapter(Context context, List<Centre> items, OnListFragmentInteractionListener listener) {
        this.context = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.centre_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, listener);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Centre centre = mValues.get(position);
        holder.specificDenText.setText(mValues.get(position).specific_den);
        holder.addressText.setText(mValues.get(position).address);
        //holder.provinceText.setText(mValues.get(position).province);
        //holder.cityText.setText(mValues.get(position).municipality);
        holder.typeText.setText(mValues.get(position).nature);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mListener.onListFragmentInteraction(holder.centre);
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mView;
        Centre centre;
        @BindView(R.id.tSpecificDenomination)
        TextView specificDenText;
        @BindView(R.id.tType)
        TextView typeText;
        @BindView(R.id.commentsCount)
        TextView commentsCount;
        @BindView(R.id.tAddress)
        TextView addressText;
        /*@BindView(R.id.tProvince)
        TextView provinceText;
        @BindView(R.id.tCity)
        TextView cityText;*/


        ItemClickListener mListener;

        public ViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mView = itemView;
            mListener = listener;

        }

//        public void setOnItemClickListener(ItemClickListener mListener) {
//            this.mListener = mListener;
//        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }


    }


}
