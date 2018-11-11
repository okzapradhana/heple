package com.example.allysaas.heple.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allysaas.heple.R;
import com.example.allysaas.heple.model.Post;

import java.util.List;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> komentarList;

    public KomentarAdapter(Context mContext, List<Post> komentarList) {
        this.mContext = mContext;
        this.komentarList = komentarList;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.komentar_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return komentarList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView email, komentar;

        public ViewHolder(View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.email_komentar);
            komentar = itemView.findViewById(R.id.isi_komentar);
        }
    }

}
