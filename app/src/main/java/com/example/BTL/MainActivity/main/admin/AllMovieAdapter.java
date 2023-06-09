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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.BTL.R;
import com.example.BTL.model.Movie;
import com.example.BTL.MainActivity.main.MainActivity;
import com.example.BTL.MainActivity.main.booking.MovieDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllMovieAdapter extends RecyclerView.Adapter<AllMovieAdapter.ItemHolder> {
    private List<Movie> mData = new ArrayList<>();
    Context mContext;
    FirebaseFirestore mDb=FirebaseFirestore.getInstance();
    public AllMovieAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Movie> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<Movie> data) {
        if (data != null) {
            int posBefore = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(posBefore, data.size());
        }
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_admin_all_movie_tab, parent, false);
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
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_rating)
        TextView txtRating;

        @BindView(R.id.type_linear_layout)
        LinearLayout mTypeParent;

        @BindView(R.id.txt_director)
        TextView txtDirector;
        @BindView(R.id.txt_actors)
        TextView txtCast;
        @BindView(R.id.img)
        ImageView image;
        @BindView(R.id.panel)
        View panel;

        @BindView(R.id.editbtn)
        TextView editbtn;

        @BindView(R.id.deletebtn)
        TextView deletebtn;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @OnClick(R.id.panel)
        void clickPanel() {
            if (mContext instanceof MainActivity)
                ((MainActivity) mContext).presentFragment(MovieDetail.newInstance(mData.get(getAdapterPosition())));
        }

        @OnClick(R.id.deletebtn)
        void Delete(){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Thông báo");
            builder.setMessage("Bán muốn xóa "+mData.get(getAdapterPosition()).getTitle()+" không ?");
            builder.setIcon(R.drawable.remove);
            builder.setPositiveButton("YES",(dialogInterface, i) -> {
                mDb.collection("movie").document(mData.get(getAdapterPosition()).getId()+"").delete();
                mDb.collection("feature_movie").document(mData.get(getAdapterPosition()).getId()+"").delete();
                mDb.collection("now_showing").document(mData.get(getAdapterPosition()).getId()+"").delete();
                mDb.collection("up_coming").document(mData.get(getAdapterPosition()).getId()+"").delete();
                Query query=mDb.collection("show_time").whereEqualTo("movieID",mData.get(getAdapterPosition()).getId()+"");
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
//                Query query1=mDb.collection("ticket").whereEqualTo("movieID",mData.get(getAdapterPosition()).getId()+"");
//                query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                mDb.collection("ticket").document(document.getId()).delete();
//                            }
//                        } else {
//                            Log.d( "Error", String.valueOf(task.getException()));
//                        }
//                    }
//                });
            });
            builder.setNegativeButton("NO",(dialogInterface, i) -> {

            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }

        @OnClick(R.id.editbtn)
        void update(){
            ((MainActivity) mContext).presentFragment(UpdateMovie.newInstance(mData.get(getAdapterPosition())));
        }

        public void bind(Movie movie) {

            txtName.setText(movie.getTitle());
            List<String> types = movie.getType();
            mTypeParent.removeAllViews();
            if (mContext instanceof Activity) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                if (inflater != null)
                    for (String type : types) {
                        if (!type.isEmpty()) {
                            TextView tv = (TextView) inflater.inflate(R.layout.type_movie_text_view, mTypeParent, false);
                            tv.setText(type);
                            //tv.setBackgroundColor(Color.GREEN);
                            tv.setBackgroundResource(R.drawable.round_yellow_drawable);
                            mTypeParent.addView(tv);
                        }
                    }
            }

            txtDirector.setText(movie.getDirector());
            txtCast.setText(movie.getCast());

            RequestOptions requestOptions = new RequestOptions().override(image.getWidth());
            Glide.with(mContext)
                    .load(movie.getImageUrl())
                    .apply(requestOptions)
                    .into(image);
        }
    }
}
