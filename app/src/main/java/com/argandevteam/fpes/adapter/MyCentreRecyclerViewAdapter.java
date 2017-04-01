package com.argandevteam.fpes.adapter;

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
public class MyCentreRecyclerViewAdapter extends RecyclerView.Adapter<MyCentreRecyclerViewAdapter.ViewHolder> {

    private final List<Centre> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyCentreRecyclerViewAdapter(List<Centre> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_centre, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Centre centre = mValues.get(position);
        holder.specificDenText.setText(mValues.get(position).specific_den);
        holder.provinceText.setText(mValues.get(position).province);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mListener.onListFragmentInteraction(holder.centre);
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        Centre centre;
        @BindView(R.id.tSpecificDenomination)
        TextView specificDenText;
        @BindView(R.id.tProvince)
        TextView provinceText;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }


}
