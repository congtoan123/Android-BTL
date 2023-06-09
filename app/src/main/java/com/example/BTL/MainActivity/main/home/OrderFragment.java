package com.example.BTL.MainActivity.main.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.BTL.MainActivity.main.admin.TicketAdapter;
import com.example.BTL.R;
import com.example.BTL.model.Ticket;
import com.example.BTL.widget.fragmentnavigationcontroller.PresentStyle;
import com.example.BTL.widget.fragmentnavigationcontroller.SupportFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderFragment extends SupportFragment implements OnCompleteListener<QuerySnapshot>, OnFailureListener {
    public static OrderFragment newInstance() {
        return new OrderFragment();
    }
    private static final String TAG ="MyOrder";
    @BindView(R.id.back_button)
    ImageView mBackButton;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.scroll_view)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    @BindView(R.id.textTong)TextView txtTong;
    @BindView(R.id.textVe) TextView txtVe;
    @BindView(R.id.textView)TextView mErrorTextView;
    TicketAdapter mAdapter;
    FirebaseFirestore db;
    @OnClick(R.id.back_button)
    void back() {
        getMainActivity().dismiss();
    }

    int Tong=0;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        spinner.setVisibility(View.INVISIBLE);
        txtVe.setVisibility(View.INVISIBLE);
        db = getMainActivity().mDb;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new TicketAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        setSwipeOptionForRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(this::refreshData);
        refreshData();
    }
    private void setSwipeOptionForRecyclerView() {

    }
    @Override
    public int getPresentTransition() {
        return PresentStyle.SLIDE_LEFT;
    }
    @Nullable
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.admin_statistic,container,false);
    }

    public void refreshData() {
        Tong=0;
        swipeRefreshLayout.setRefreshing(true);
        db.collection("ticket").whereEqualTo("mUserUID",getMainActivity().user.getUid())
                .get()
                .addOnCompleteListener(this)
                .addOnFailureListener(this);
        db.collection("database_info").document("show_time_info").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);

                        mErrorTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        DocumentSnapshot documentSnapshot =task.getResult();
                        if (task.isSuccessful()){
                            txtVe.setText("Tổng số vé bán ra: "+documentSnapshot.getData().get("ticket_count").toString());
                        }else
                            Log.w(TAG, "Error getting documents.", task.getException());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure");
                        if (swipeRefreshLayout.isRefreshing())
                            swipeRefreshLayout.setRefreshing(false);

                        recyclerView.setVisibility(View.GONE);
                        mErrorTextView.setVisibility(View.VISIBLE);
                    }
                });
    }
    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        mErrorTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (task.isSuccessful()) {
            QuerySnapshot querySnapshot = task.getResult();

            List<Ticket> mM = querySnapshot.toObjects(Ticket.class);
            for (Ticket i:mM){
                Tong+=i.getmPrice();
            }
            txtTong.setText("Tổng tiền: "+Tong+"");
            Collections.sort(mM, (o1, o2) -> o1.getmID() - o2.getmID());
            if (mAdapter != null)
                mAdapter.setData(mM);

        } else
            Log.w(TAG, "Error getting documents.", task.getException());
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.d(TAG, "onFailure");
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);

        recyclerView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
    }
}
