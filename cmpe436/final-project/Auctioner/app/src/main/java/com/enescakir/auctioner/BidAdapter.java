package com.enescakir.auctioner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enescakir.auctioner.Model.Bid;

import java.util.ArrayList;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.MyViewHolder> {

    ArrayList<Bid> bids;
    LayoutInflater inflater;

    public BidAdapter(Context context, ArrayList<Bid> bids) {
        inflater = LayoutInflater.from(context);
        this.bids = bids;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_bid, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(bids.get(position), position);

    }

    @Override
    public int getItemCount() {
        return bids.size();
    }

    public void addBid(Bid bid) {
        bids.add(0, bid);
        notifyItemInserted(0);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView usernameView, pointView, dateView;
        Bid bid;
        int position;

        public MyViewHolder(View view) {
            super(view);
            usernameView = (TextView) view.findViewById(R.id.usernameView);
            pointView = (TextView) view.findViewById(R.id.pointView);
            dateView = (TextView) view.findViewById(R.id.dateView);
        }

        public void setData(Bid bid, int position) {
            this.bid = bid;
            this.position = position;
            this.usernameView.setText(bid.getUsername());
            this.pointView.setText(String.format("%d points", bid.getPoint()));
            this.dateView.setText(bid.getHumanDate());
        }
    }
}