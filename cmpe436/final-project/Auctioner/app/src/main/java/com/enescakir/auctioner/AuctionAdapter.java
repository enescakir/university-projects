package com.enescakir.auctioner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.enescakir.auctioner.Model.Auction;

import java.util.ArrayList;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.MyViewHolder> {

    ArrayList<Auction> auctions;
    LayoutInflater inflater;

    public AuctionAdapter(Context context, ArrayList<Auction> auctions) {
        inflater = LayoutInflater.from(context);
        this.auctions = auctions;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_auction, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setData(auctions.get(position), position);
    }

    @Override
    public int getItemCount() {
        return auctions.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView, dateView;
        ImageView imageView;
        Auction auction;
        int position;

        public MyViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.nameView);
            dateView = (TextView) view.findViewById(R.id.dateView);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setOnClickListener(this);
        }

        public void setData(Auction auction, int position) {
            this.auction = auction;
            this.position = position;
            this.nameView.setText(auction.getName());
            this.dateView.setText(String.format("Until: %s", auction.getHumanDate()));
            Context context = imageView.getContext();
            int image = context.getResources().getIdentifier(auction.getImageName(), "drawable", context.getPackageName());
            this.imageView.setImageResource(image);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra("auctionId", auction.getId());
            intent.putExtra("auctionName", auction.getName());
            intent.putExtra("auctionDescription", auction.getDescription());
            intent.putExtra("auctionDate", auction.getDate());
            context.startActivity(intent);
        }
    }
}