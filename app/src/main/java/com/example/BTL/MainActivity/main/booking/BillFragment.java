package com.example.BTL.MainActivity.main.booking;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.BTL.R;
import com.example.BTL.model.Ticket;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillFragment extends SupportFragment {
    public static BillFragment newInstance(Ticket t) {
        BillFragment p = new BillFragment();
        p.mTicket = t;
        return p;
    }
    @OnClick(R.id.back_button)
    void back() {
        getMainActivity().dismiss();
    }
    Ticket mTicket;
    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.ticket_print,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setContent();
    }


    @BindView(R.id.movie) TextView mMovie;
    @BindView(R.id.cinema) TextView mCinema;
    @BindView(R.id.room) TextView mRoom;
    @BindView(R.id.seat) TextView mSeat;
    @BindView(R.id.date) TextView mDate;
    @BindView(R.id.time) TextView mTime;
    @BindView(R.id.price) TextView mPrice;
    void setContent() {
        mMovie.setText(mTicket.getmMovieName());
        mCinema.setText(mTicket.getmCinemaName());
        mRoom.setText(mTicket.getmRoom()+"");
        mSeat.setText(mTicket.getmSeat());
        mDate.setText(mTicket.getmDate());
        mTime.setText(mTicket.getmTime());
        mPrice.setText(mTicket.getmPrice()+"");
    }
}
