package com.example.BTL.MainActivity.main.booking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.BTL.R;
import com.example.BTL.model.Movie;
import com.example.BTL.widget.fragmentnavigationcontroller.PresentStyle;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetail extends SupportFragment {
    private static final String TAG = "MovieDetail";
    Movie mMovie;

    @BindView(R.id.back_image_view) ImageView mBackImageView;
    @BindView(R.id.avatar) ImageView mAvatarImageView;
    @BindView(R.id.title) TextView mTitleTextView;
    @BindView(R.id.opening_day) TextView mDescriptionTextView;

    @BindView(R.id.content_text_view) TextView mContentTextView;
    @BindView(R.id.category) TextView mCategoryTextView;
    @BindView(R.id.release) TextView mReleaseTextView;
    @BindView(R.id.director) TextView mDirectorTextView;
    @BindView(R.id.cast) TextView mCastTextView;


    @BindView(R.id.back_button) View mBackButton;
    @BindView(R.id.book_now_button) FloatingActionButton mBookNowButton;

    @OnClick(R.id.play_panel)
    void doSomething() {
        getMainActivity().presentFragment(WebViewFragment.newInstance(mMovie.getTrailerYoutube()));


    }

    @OnClick(R.id.book_now_button)
    void goToBooking() {
        getMainActivity().presentFragment(BookingFragment.newInstance(mMovie));
    }

    @OnClick(R.id.back_button)
    void dismiss() {
        getMainActivity().dismiss();
    }

    public static MovieDetail newInstance(Movie movie) {
        MovieDetail md = new MovieDetail();

        md.mMovie = movie;
        return md;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(R.layout.movie_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        bind(mMovie);
    }
    private void bind(Movie movie) {
        if(movie!=null) {

            mTitleTextView.setText(movie.getTitle());
            mCastTextView.setText(movie.getCast());
            mDescriptionTextView.setText(movie.getOpeningDay());
            mCategoryTextView.setText(movie.getGenre());
            mDirectorTextView.setText(movie.getDirector());
            mReleaseTextView.setText(movie.getOpeningDay());

            mContentTextView.setText(movie.getDescription());

            RequestOptions requestOptions = new RequestOptions().override(mAvatarImageView.getWidth());
            Glide.with(getContext())
                    .load(movie.getImageUrl())
                    .apply(requestOptions)
                    .into(mAvatarImageView);

            RequestOptions requestOptions2 = new RequestOptions().override(mAvatarImageView.getWidth());
            Glide.with(getContext())
                    .load(movie.getImageUrl())
                    .apply(requestOptions2)
                    .into(mBackImageView);
        }
    }

    @Override
    public int getPresentTransition() {
        return PresentStyle.SLIDE_UP;
    }
}
