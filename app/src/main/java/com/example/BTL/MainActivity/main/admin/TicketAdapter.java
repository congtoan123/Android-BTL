package com.example.BTL.MainActivity.main.admin;

import android.app.Activity;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import com.example.BTL.R;

import com.example.BTL.model.Ticket;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ItemHolder> {
    private List<Ticket> mData = new ArrayList<>();
    Context mContext;
    FirebaseFirestore mDb=FirebaseFirestore.getInstance();
    private boolean mAdminMode = false;


    public void turnOnAdminMode() {
        mAdminMode = true;
    }

    public TicketAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Ticket> data) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Ticket> data) {
        if(data!=null) {
            int posBefore = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(posBefore,data.size());
        }
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_admin_statistic, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_ticket) TextView txid;
        @BindView(R.id.room) TextView txtroom;
        @BindView(R.id.time) TextView txttime;
        @BindView(R.id.date) TextView date;
        @BindView(R.id.seat) TextView seat;

        @BindView(R.id.price) TextView price;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void bind(Ticket cinema) {

            txid.setText(cinema.getmID()+"");
            if(mContext instanceof Activity) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            }
            txtroom.setText(cinema.getmRoom()+"");
            txttime.setText(cinema.getmTime()+"");
            date.setText(cinema.getmDate()+"");
            seat.setText(cinema.getmSeat()+"");
            price.setText(cinema.getmPrice()+"");

        }
    }
}
