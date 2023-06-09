package com.example.BTL.MainActivity.main.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.BTL.R;
import com.example.BTL.MainActivity.main.admin.addmovie2cinema.ChooseWhichCinemaToAdd;
import com.example.BTL.widget.fragmentnavigationcontroller.PresentStyle;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdminFragment extends SupportFragment {
    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    @BindView(R.id.back_button) ImageView mBackButton;
    @BindView(R.id.title) TextView mTitle;

    @OnClick(R.id.back_button)
    void back() {
        getMainActivity().dismiss();
    }

   @OnClick(R.id.movie_panel)
   void goToMovieManagement(){
       getMainActivity().presentFragment(MovieManagement.newInstance());
   }
   
   @OnClick(R.id.cinema_panel)
   void goToCinemaManagement() {
        getMainActivity().presentFragment(CinemaManagement.newInstance());
   }

   @OnClick(R.id.add_new_showtime_panel)
   void goToChooseWhicCinemaToAdd() {
        getMainActivity().presentFragment(ChooseWhichCinemaToAdd.newInstance());
   }

   @OnClick(R.id.statistic_panel)
   void goToStatistic(){
        getMainActivity().presentFragment(StatisticFragment.newInstance());
   }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.admin_dash_board,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    public int getPresentTransition() {
        return PresentStyle.SLIDE_LEFT;
    }
}
