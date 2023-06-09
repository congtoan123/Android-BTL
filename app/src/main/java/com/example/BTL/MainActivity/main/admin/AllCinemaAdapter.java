package com.example.BTL.MainActivity.main.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.BTL.R;
import com.example.BTL.model.Cinema;
import com.example.BTL.MainActivity.main.MainActivity;
import com.example.BTL.MainActivity.main.home.NowShowingMoviesOfCinema;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllCinemaAdapter extends RecyclerView.Adapter<AllCinemaAdapter.ItemHolder> {
    private List<Cinema> mData = new ArrayList<>();
    Context mContext;
    FirebaseFirestore mDb=FirebaseFirestore.getInstance();
    private boolean mAdminMode = false;

    public interface CinemaOnClickListener {
        void onItemClick(Cinema cinema);
    }
    private CinemaOnClickListener  mListener;
    public void setListener(CinemaOnClickListener listener) {
        mListener = listener;
    }
    public void removeListener() {
        mListener = null;
    }

    public void turnOnAdminMode() {
        mAdminMode = true;
    }

    public AllCinemaAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Cinema> data) {
        mData.clear();
        if (data !=null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Cinema> data) {
        if(data!=null) {
            int posBefore = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(posBefore,data.size());
        }
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_admin_cinema_tab, parent, false);

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
        @BindView(R.id.txt_name) TextView txtName;
        @BindView(R.id.txt_address) TextView txtAddress;
        @BindView(R.id.txt_hotline) TextView txtHotline;
        @BindView(R.id.img) ImageView image;
        @BindView(R.id.panel) View panel;

        @BindView(R.id.editbtn) TextView editbtn;

        @BindView(R.id.deletebtn) TextView deletebtn;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @OnClick(R.id.panel)
        void clickPanel() {
            if(mListener!=null) mListener.onItemClick(mData.get(getAdapterPosition()));
            else if(mContext instanceof MainActivity) {
                ((MainActivity) mContext).presentFragment(NowShowingMoviesOfCinema.newInstance(
                        mData.get(getAdapterPosition()).getMovies(), mData.get(getAdapterPosition()).getName()));
            }
        }

        @OnClick(R.id.deletebtn)
        void delete(){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Thông báo");
            builder.setMessage("Bán muốn xóa "+mData.get(getAdapterPosition()).getName()+" không ?");
            builder.setIcon(R.drawable.remove);
            builder.setPositiveButton("YES",(dialogInterface, i) -> {
                mDb.collection("cinema_list").document(mData.get(getAdapterPosition()).getId()+"").delete();
                mDb.collection("showing_cinema").document(mData.get(getAdapterPosition()).getId()+"").delete();
                Query query=mDb.collection("show_time").whereEqualTo("cinemaID",mData.get(getAdapterPosition()).getId());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                mDb.collection("show_time").document(document.getId()).delete();
                            }
                        } else {
                            Log.d( "Error", String.valueOf(task.getException()));
                        }
                    }
                });
            });
            builder.setNegativeButton("NO",(dialogInterface, i) -> {

            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }

        @OnClick(R.id.editbtn)
        void update(){
            ((MainActivity) mContext).presentFragment(UpdateCinema.newInstance(mData.get(getAdapterPosition())));
        }

        public void bind(Cinema cinema) {

            txtName.setText(cinema.getName());

            if(mContext instanceof Activity) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            }
            String Address = cinema.getAddress();
            txtAddress.setText(Address);
            txtHotline.setText(cinema.getHotline());

            RequestOptions requestOptions = new RequestOptions().override(image.getWidth());
            Glide.with(mContext)
                    .load(cinema.getImageUrl())
                    .apply(requestOptions)
                    .into(image);
        }
    }
}
