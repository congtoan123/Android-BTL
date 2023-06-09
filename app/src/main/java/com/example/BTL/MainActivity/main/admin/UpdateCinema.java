package com.example.BTL.MainActivity.main.admin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.BTL.R;
import com.example.BTL.model.Cinema;
import com.example.BTL.widget.SuccessTickView;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class UpdateCinema extends SupportFragment implements RequestListener<Drawable>, OnFailureListener, OnCompleteListener<QuerySnapshot> {
    private static final String TAG = "UpdateCinema";

    public static UpdateCinema newInstance(Cinema cinema) {
        UpdateCinema f= new UpdateCinema();
        f.cinema=cinema;
        return f;
    }

    Cinema cinema;
    FirebaseFirestore mDb;

    @BindView(R.id.back_button)
    ImageView mBackButton;

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.photo)
    ImageView mPhoto;
    @BindView(R.id.photo_empty_icon)
    ImageView mEmptyPhotoIcon;

    @BindView(R.id.option_panel)
    ConstraintLayout mOptionPanel;

    @BindView(R.id.click_to_expand_option)
    ImageView mClickToExpandOption;
    @BindView(R.id.photo_loader)
    MKLoader mPhotoLoader;

    @BindView(R.id.id)
    EditText mIDEditText;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;
    @BindView(R.id.done_button)
    ImageView mDoneButton;

    @BindView(R.id.ListOfMovies)
    EditText mListOfMovies;
    @BindView(R.id.ListOfShowTimes)
    EditText mListOfShowTimes;
    @BindView(R.id.hotline)
    EditText mHotline;
    @BindView(R.id.address)
    EditText mAddress;

    @OnClick(R.id.back_button)
    void back() {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(R.layout.alert_layout);
        dialog.findViewById(R.id.comfirm).setOnClickListener(v -> {
            dialog.dismiss();
            getMainActivity().dismiss();
        });
        dialog.show();

    }

    @OnClick(R.id.click_to_expand_option)
    void ExpandOrCollapseOptionPanel() {
        if (mOptionPanel.getVisibility() == View.VISIBLE) {
            mClickToExpandOption.setRotation((float) Math.PI);
            RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setFillAfter(true);
            ra.setDuration(350);
            mClickToExpandOption.startAnimation(ra);
            mOptionPanel.setVisibility(View.GONE);
        } else {
            //   mClickToExpandOption.setRotation((float) Math.PI);
            RotateAnimation ra = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setFillAfter(true);
            ra.setDuration(350);
            mClickToExpandOption.startAnimation(ra);
            mOptionPanel.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.photo)
    void focusImageUrlEditText() {
        mImageUrlEditText.requestFocus();
    }

    private boolean[] mDone = new boolean[]{false, false, false, false};

    @BindView(R.id.image_url)
    EditText mImageUrlEditText;
    @BindView(R.id.name)
    EditText mName;

    @OnTextChanged(R.id.name)
    void onNameChanged(CharSequence s, int start, int before, int count) {
        if (mDone[1] && s.length() == 0) mDone[1] = false;
        else if (!mDone[1] && s.length() != 0) mDone[1] = true;
        checkoutDone();
    }

    @OnTextChanged(R.id.hotline)
    void onHotlineChanged(CharSequence s, int start, int before, int count) {
        if (mDone[2] && s.length() == 0) mDone[2] = false;
        else if (!mDone[2] && s.length() != 0) mDone[2] = true;
        checkoutDone();
    }

    @OnTextChanged(R.id.address)
    void onTrailderYoutubeChanged(CharSequence s, int start, int before, int count) {
        if (mDone[3] && s.length() == 0) mDone[3] = false;
        else if (!mDone[3] && s.length() != 0) mDone[3] = true;
        checkoutDone();
    }

    @BindView(R.id.id_detail)
    TextView mIDDetail;
    @BindView(R.id.auto_fill)
    TextView mAutoFill;

    int mFoundPos = -1;

    int findIdInData(int id) {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId() == id)
                return i;
        }
        return -1;
    }

    @OnClick(R.id.auto_fill)
    void autoFillForm() {
        Log.d(TAG, "autoFillForm: " + mFoundPos);
        if (mFoundPos != -1 && mFoundPos < mData.size()) {
            Cinema c = mData.get(mFoundPos);

            mImageUrlEditText.setText(c.getImageUrl());
            mIDEditText.setText(String.format("%d", c.getId()));
            mName.setText(c.getName());
            mHotline.setText(c.getHotline());
            mAddress.setText(c.getAddress());

        }
    }

    boolean mOk = false;

    private void checkoutDone() {
        for (int i = 0; i < mDone.length; i++) {
            Log.d(TAG, "checkoutDone: done[" + i + "] = " + mDone[i]);
            if (!mDone[i]) {
                mDoneButton.setColorFilter(getResources().getColor(R.color.setting_label_color));
                mOk = false;
                return;
            }
        }
        mOk = true;
        mDoneButton.setColorFilter(getResources().getColor(R.color.FlatBlue));
    }

    @OnClick(R.id.done_button)
    void onClickDoneButton() {
        if (mOk) {
            addNewCinema();
        }
    }

    void addNewCinema() {
        Cinema c = new Cinema();

        c.setId((int) cinema.getId());
        c.setImageUrl(mImageUrlEditText.getText().toString());
        c.setHotline(mHotline.getText().toString());
        c.setAddress(mAddress.getText().toString());
        c.setName(mName.getText().toString());
        ArrayList<Integer> lmovie=new ArrayList<>();
        ArrayList<Integer> lshowtime=new ArrayList<>();
        if (mListOfMovies.getText().toString().isEmpty()){
            lmovie=new ArrayList<>();
        } else{
            String[] movie= mListOfMovies.getText().toString().split(",");

            for (int i=0;i<movie.length;i++){
                lmovie.add(Integer.parseInt(movie[i]));
            }
        }
        if (mListOfShowTimes.getText().toString().isEmpty()){
            lshowtime=new ArrayList<>();
        }else {
            String[] showtime=mListOfShowTimes.getText().toString().split(",");
            for (int i=0;i<showtime.length;i++){
                lshowtime.add(Integer.parseInt(showtime[i]));
            }
        }
        c.setMovies(lmovie);
        c.setShowTimes(lshowtime);

        Log.d(TAG, c.toString());
        mSendingDialog = new BottomSheetDialog(getActivity());

        mSendingDialog.setContentView(R.layout.send_new_movie);
        mSendingDialog.setCancelable(false);
        mSendingDialog.findViewById(R.id.close).setOnClickListener(v -> cancelSending());
        mSendingDialog.show();
        sendData(c);
    }

    void sendData(Cinema cinema1) {
        mDb.collection("cinema_list").document((cinema.getId()) + "").set(cinema1).addOnSuccessListener(aVoid -> {
            setOnSuccess();

        }).addOnFailureListener(e -> {
            setOnFailure();
        });
    }

    BottomSheetDialog mSendingDialog;
    boolean cancelled = false;

    void cancelSending() {
        if (mSendingDialog != null)
            mSendingDialog.dismiss();
        cancelled = true;
    }

    void setTextSending(String text, int color) {
        if (mSendingDialog != null) {
            TextView textView = mSendingDialog.findViewById(R.id.sending_text);
            if (textView != null) {

                AlphaAnimation aa = new AlphaAnimation(0, 1);
                aa.setFillAfter(true);
                aa.setDuration(500);
                textView.setText(text);
                textView.setTextColor(color);
                textView.startAnimation(aa);
            }
        }

    }

    void setOnSuccess() {
        if (cancelled) return;
        cancelled = false;
        if (mSendingDialog != null) {
            MKLoader mkLoader = mSendingDialog.findViewById(R.id.loading);
            if (mkLoader != null) mkLoader.setVisibility(View.INVISIBLE);
            SuccessTickView s = mSendingDialog.findViewById(R.id.success_tick_view);
            if (s != null) {

                s.postDelayed(() -> {
                    mSendingDialog.dismiss();
                    mTitle.postDelayed(() -> getMainActivity().dismiss(), 350);
                }, 2000);
                s.setVisibility(View.VISIBLE);
                s.startTickAnim();
                setTextSending("Cinema has been updated to database", getResources().getColor(R.color.FlatGreen));
            }
        }
    }

    void setOnFailure() {
        if (mSendingDialog != null) {
            mSendingDialog.dismiss();
            Toast.makeText(getContext(), "Cannot update cinema, please try again!", Toast.LENGTH_SHORT);
        }
    }

    @OnTextChanged(R.id.image_url)
    void onImageUrlChanged(CharSequence s, int start, int before, int count) {
        String url = s.toString();
        if (!url.isEmpty() && URLUtil.isValidUrl(url)) {
            mPhotoLoader.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions();
            Glide.with(getContext())
                    .load(url)
                    .addListener(this)
                    .into(mPhoto);

        } else {
            mPhoto.setImageDrawable(null);
            mEmptyPhotoIcon.setVisibility(View.VISIBLE);
            mPhotoLoader.setVisibility(View.GONE);
            mDone[0] = false;
            checkoutDone();
        }

    }

    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.admin_add_new_cinema, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mDb = getMainActivity().mDb;
        mHotline.setText(cinema.getHotline()+"");
        mAddress.setText(cinema.getAddress());
        mIDEditText.setText(cinema.getId()+"");
        mName.setText(cinema.getName());
        mImageUrlEditText.setText(cinema.getImageUrl());
        ArrayList<Integer> lmovie=cinema.getMovies();
        String tmp="";
        for (int i=0;i<lmovie.size();i++){
            if ((i==0&&lmovie.size()==1)||i==lmovie.size()-1){
                tmp+=lmovie.get(i).toString();
            }
            else {
                tmp+=lmovie.get(i)+",";
            }
        }
        mListOfMovies.setText(tmp);
        ArrayList<Integer> lshowtime=cinema.getShowTimes();
        String tmp1="";
        for (int i=0;i<lshowtime.size();i++){
            if ((i==0&&lshowtime.size()==1)||i==lshowtime.size()-1){
                tmp1+=lshowtime.get(i).toString();
            }
            else {
                tmp1+=lshowtime.get(i)+",";
            }
        }
        mListOfShowTimes.setText(tmp1);
        ExpandOrCollapseOptionPanel();
        disableIdField();
        mSwipeLayout.setOnRefreshListener(this::refreshData);
        refreshData(false);
        // autoFill();
    }

    private void disableIdField() {
        mIDEditText.setFocusable(false);
        mIDEditText.setEnabled(false);
        mIDEditText.setCursorVisible(false);
        mIDEditText.setKeyListener(null);
    }

    private void enableIDField() {
        mIDEditText.setFocusable(true);
        mIDEditText.setEnabled(true);
        mIDEditText.setCursorVisible(true);
        //  mIDEditText.setInputType(InputType.TYPE_NULL);
        mIDEditText.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        mPhoto.setImageDrawable(null);
        mEmptyPhotoIcon.setVisibility(View.VISIBLE);
        mPhotoLoader.setVisibility(View.GONE);
        mDone[0] = false;
        checkoutDone();
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        mEmptyPhotoIcon.setVisibility(View.GONE);
        mPhotoLoader.setVisibility(View.GONE);
        mDone[0] = true;
        checkoutDone();
        return false;
    }

    @Override
    public boolean isReadyToDismiss() {
        back();
        return false;
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        mIDEditText.setText(getString(R.string.fail_to_create_id));
    }

    public void refreshData() {
        refreshData(true);
    }

    public void refreshData(boolean b) {
        if (b)
            mSwipeLayout.setRefreshing(true);
        mDb.collection("cinema_list")
                .get()
                .addOnCompleteListener(this)
                .addOnFailureListener(this);
    }

//    public long mID = -1;

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (mSwipeLayout.isRefreshing())
            mSwipeLayout.setRefreshing(false);
        if (task.isSuccessful()) {
            QuerySnapshot querySnapshot = task.getResult();

            List<Cinema> mM = querySnapshot.toObjects(Cinema.class);
            Collections.sort(mM, (o1, o2) -> (int) (o1.getId() - o2.getId()));
            mData.clear();
            mData.addAll(mM);
//            createNewID(mM);

        } else
            Log.w(TAG, "Error getting documents.", task.getException());
    }

    ArrayList<Cinema> mData = new ArrayList<>();

}