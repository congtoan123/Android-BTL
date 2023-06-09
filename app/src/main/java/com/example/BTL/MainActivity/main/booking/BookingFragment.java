package com.example.BTL.MainActivity.main.booking;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.BTL.MainActivity.main.MainActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.BTL.model.Movie;
import com.example.BTL.model.ShowTime;
import com.example.BTL.MainActivity.main.booking.chooseseat.ChooseSeat;
import com.example.BTL.widget.fragmentnavigationcontroller.PresentStyle;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;
import com.example.BTL.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BookingFragment extends SupportFragment implements EventListener<QuerySnapshot> ,DateAdapter.OnSelectedChangedListener, DetailShowTimeAdapter.OnTimeClickListener {
    private static final String TAG="BookingFragment";

    public static BookingFragment newInstance(Movie movie) {
        BookingFragment bf = new BookingFragment();
        bf.mMovie = movie;
        return bf;
    }

    Movie mMovie;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.movie_panel) View mMoviePanel;
    @BindView(R.id.image) ImageView mImage;
    @BindView(R.id.title) TextView mTitle;
    @BindView(R.id.genre) TextView mGenre;
    @BindView(R.id.duration) TextView mDuration;
    @BindView(R.id.rate) TextView mRate;
    @BindView(R.id.next) View mNext;

    @BindView(R.id.date_recycler_view)
    RecyclerView mDateRecyclerView;

    @BindView(R.id.cinema_recycle_view)
    RecyclerView mCinemaRecyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    @BindView(R.id.error) TextView mError;

    DateAdapter mDateAdapter;
    DetailShowTimeAdapter mDetailShowTimeAdapter;
    private FirebaseUser mUser;
    @Override
    public int getPresentTransition() {
        return PresentStyle.SLIDE_LEFT;
    }

    private void setupToolbar() {
        if(getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.setDisplayShowTitleEnabled(false);
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.booking_v2,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mDb = getMainActivity().mDb;
        setupToolbar();
        bindMovie();
        mUser = ((MainActivity)getActivity()).user;
        Date date =  Calendar.getInstance().getTime();
        String current = DateFormat.format("dd/MM/yyyy", date).toString();
        Log.d(TAG, "onViewCreated: date "+current);

        mDateAdapter = new DateAdapter(getActivity());
        mDateAdapter.setOnSelectedChangedListener(this);
        mDetailShowTimeAdapter = new DetailShowTimeAdapter(getActivity());
        mDetailShowTimeAdapter.setOnTimeClickListener(this);

        mDateRecyclerView.setAdapter(mDateAdapter);
        mCinemaRecyclerView.setAdapter(mDetailShowTimeAdapter);

        mDateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mCinemaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        createDataForDateAdapter();
        mSwipeLayout.setOnRefreshListener(this::getData);
        refreshData();

    }
    ArrayList<Date> mDateArray;
    private void createDataForDateAdapter(){
        Calendar calendar = Calendar.getInstance();
        mDateArray = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        mDetailShowTimeAdapter.setDateQuery(calendar.getTime());

        for (int i = 0; i < 14; i++) {
            Date date = calendar.getTime();
            mDateArray.add(date);
            String str = DateFormat.format("dd/MM", date).toString();
            list.add(str);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        mDateAdapter.setData(list);
    }

    @SuppressLint("DefaultLocale")
    private void bindMovie() {
        if(mMovie==null) return;
        mTitle.setText(mMovie.getTitle());
        mGenre.setText(mMovie.getGenre());
        mDuration.setText(String.format("%d min", mMovie.getDuration()));
        mRate.setText(String.format("%s", mMovie.getRate()));

        RequestOptions requestOptions = new RequestOptions();
        if(getContext()!=null) {
            Glide.with(getContext())
                    .load(mMovie.getImageUrl())
                    .apply(requestOptions)
                    .into(mImage);
        }
    }

    @OnClick(R.id.movie_panel)
    void goToMovieDetail(){
        if(mMovie!=null) {
            getMainActivity().presentFragment(MovieDetail.newInstance(mMovie));
        }
    }
    void getData() {
        mSwipeLayout.setRefreshing(true);
        if(mMovie==null) {
          mSwipeLayout.setRefreshing(false);
          mError.setVisibility(View.VISIBLE);
        } else {
            refreshData();
        }
    }
    FirebaseFirestore mDb;
    void refreshData() {
   mDb.collection("show_time").whereEqualTo("movieID",mMovie.getId()).addSnapshotListener(this);
    }

    @Override
    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
        mSwipeLayout.setRefreshing(false);
        mError.setVisibility(View.GONE);
        if(e==null) {
            if(queryDocumentSnapshots!=null) {
                List<ShowTime> list = queryDocumentSnapshots.toObjects(ShowTime.class);
                if(mDetailShowTimeAdapter!=null) mDetailShowTimeAdapter.setData(list);
                Log.d(TAG, "onEvent: "+list.toString());
            } else Log.d(TAG, "onEvent: null");
        } else {
            Log.d(TAG, "onEvent: Exception");
            mError.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSelectedChanged(int position) {
        mDetailShowTimeAdapter.setDateQuery(mDateArray.get(position));
    }

    @Override
    public void onTimeClick(ShowTime showTime, int datePos, int timePos) {
        if (mUser==null){
            Toast.makeText(getContext(), "Đăng nhập để đặt vé", Toast.LENGTH_SHORT).show();
            return;
        }else{
            ChooseSeat.newInstance(getMainActivity(),showTime,datePos,timePos);
        }
    }

    @Override
    public void onNoResult() {
        mError.setVisibility(View.VISIBLE);
        mError.setText("Sorry, there is no session :((");
    }

}
