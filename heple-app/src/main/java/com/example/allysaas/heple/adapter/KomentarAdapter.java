package com.example.allysaas.heple.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.allysaas.heple.R;
import com.example.allysaas.heple.model.Komentar;

import java.util.List;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.KomentarViewHolder> {

    private Context mContext;
    private List<Komentar> komentarList;
    private List<String> memberNameList;

    public KomentarAdapter(Context mContext, List<Komentar> komentarList, List<String> memberNameList) {
        this.mContext = mContext;
        this.komentarList = komentarList;
        this.memberNameList = memberNameList;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public KomentarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        KomentarViewHolder komentarViewHolder;
        view = LayoutInflater.from(mContext).inflate(R.layout.komentar_card, parent, false);
        komentarViewHolder = new KomentarViewHolder(view);
        return komentarViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarViewHolder holder, int position) {
        Log.d("Size komentar list" , getItemCount() + "");
        Log.e("Member name komntr", memberNameList.get(position));
        holder.member.setText(memberNameList.get(position));
        holder.komentar.setText(komentarList.get(position).getIsiKomentar());
        holder.time.setText(DateUtils.getRelativeDateTimeString(mContext, (komentarList.get(position).getTime() * 1000), DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
    }

    @Override
    public int getItemCount() {
        return komentarList.size();
    }

    public class KomentarViewHolder extends RecyclerView.ViewHolder{

        public TextView member, komentar, time;

        public KomentarViewHolder(View itemView) {
            super(itemView);

            member = itemView.findViewById(R.id.member_name_komentar);
            komentar = itemView.findViewById(R.id.isi_komentar);
            time = itemView.findViewById(R.id.time_komentar);
        }
    }

}
