package com.enescakir.auctioner;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.enescakir.auctioner.Model.Auction;
import com.enescakir.auctioner.Model.Packet;
import com.enescakir.auctioner.Model.SocketThread;
import com.enescakir.auctioner.Model.Bid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.enescakir.auctioner.Model.Packet.Type.*;

public class DetailActivity extends BaseActivity {

    TextView descView;
    TextView pointView;
    TextView timeView;
    ImageView imageView;
    RecyclerView bidsView;
    Button bidButton;
    Auction auction;
    Dialog bidDialog;
    BidThread bidThread;
    BidAdapter bidAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        preferences = getSharedPreferences("user", MODE_PRIVATE);

        getAuctionDetails();
        setTitle();
        setAuctionDetails();
        handleBidClick();
        setBidDialog();

        bidThread = new BidThread();
        bidThread.start();
    }

    private void getAuctionDetails() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("auctionId", 0);
        String name = intent.getStringExtra("auctionName");
        String description = intent.getStringExtra("auctionDescription");
        String date = intent.getStringExtra("auctionDate");
        auction = new Auction(id, name, description, date);
    }

    private void setTitle() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(auction.getName());
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setAuctionDetails() {
        bidButton = findViewById(R.id.bidButton);
        descView = findViewById(R.id.descView);
        descView.setText(auction.getDescription());
        pointView = findViewById(R.id.tvPoint);
        pointView.setText(String.format("Your points: %d", points));
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(getResources().getIdentifier(auction.getImageName(), "drawable", getPackageName()));
        timeView = findViewById(R.id.timeView);
        long diff = auction.getDateObject().getTime() - new Date().getTime();
        CountDown counter = new CountDown(diff, 1000);
        counter.start();
    }

    private void setBidDialog() {
        bidDialog = new Dialog(this);
        bidDialog.setContentView(R.layout.dialog_bid);
        bidDialog.setCancelable(true);
        final TextView nameTextView = bidDialog.findViewById(R.id.tvName);
        final EditText pointInput = bidDialog.findViewById(R.id.etPoint);
        final Button cancelButton = bidDialog.findViewById(R.id.bCancel);
        final Button offerButton = bidDialog.findViewById(R.id.bOffer);
        nameTextView.setText(String.format("Bid Offer for \"%s\"", auction.getName()));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidDialog.dismiss();
                pointInput.setText("");
            }
        });
        offerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bidDialog.dismiss();
                if (!TextUtils.isEmpty(pointInput.getText())) {
                    int offer = Integer.parseInt(pointInput.getText().toString());
                    pointInput.setText("");
                    final Bid bid = new Bid(username, offer, new Date());
                    runOnThread(new Thread() {
                        @Override
                        public void run() {
                            bidThread.sendMessage(new Packet(BID_STORE, bid).toJson());
                        }
                    });
                } else {
                    showError("You have to give offer that is greater than zero");
                }
            }
        });

    }

    private void handleBidClick() {
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                bidDialog.show();
            }
        });
    }

    private void setBidsView(ArrayList<Bid> bids) {
        bidsView = findViewById(R.id.bidsView);
        bidAdapter = new BidAdapter(this, bids);
        bidsView.setAdapter(bidAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bidsView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        runOnThread(new Thread() {
            @Override
            public void run() {
                bidThread.close();
            }
        });
        finish();
        return true;
    }

    public class CountDown extends CountDownTimer {
        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            timeView.setText("Finished!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%d days %02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toDays(millis),
                    (TimeUnit.MILLISECONDS.toHours(millis) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millis))),
                    (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))),
                    (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
            );
            timeView.setText(hms);
        }
    }

    class BidThread extends SocketThread {
        @Override
        public void run() {
            super.run();
            try {
                sendMessage(new Packet(BID_LISTEN, Integer.toString(auction.getId())).toJson());

                final Packet response = Packet.fromJson(in.readLine());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBidsView(response.toArrayList(Bid.class));
                    }
                });
                this.listen();

            } catch (Exception e1) {
                Log.e("LOOP", "First loop error");
                try {
                    this.listen();
                } catch (Exception e2) {
                    Log.e("LOOP", "Second loop error");
                }
            }
        }

        public void listen() throws IOException {
            String message;
            while ((message = in.readLine()) != null) {
                Packet packet;
                try {
                    packet = Packet.fromJson(message);
                } catch (Exception e) {
                    packet = new Packet(UNKNOWN, "");
                    Log.e("PACKET", "Parse error");
                }
                switch (packet.getType()) {
                    case BID_STORE:
                        final int result = packet.toObject(Integer.class);
                        if (result == -2) {
                            showError(String.format("You had to bid more than last bid that is %d points", bidAdapter.bids.get(0).getPoint()));
                        } else if (result == -1) {
                            showError(String.format("You have %d points, so it's not enough", points));
                        } else {
                            runOnThread(new Thread() {
                                @Override
                                public void run() {
                                    points = result;
                                    preferences.edit().putInt("points", result).apply();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pointView.setText(String.format("Your points: %d", points));
                                        }
                                    });
                                }
                            });
                        }
                        break;
                    case BID_NEW:
                        final Bid bid = packet.toObject(Bid.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bidAdapter.addBid(bid);
                                bidsView.scrollToPosition(0);
                            }
                        });
                        break;
                    default:
                        Log.e("UNKNOWN_PACKET", "Unknown packet from :" + message);
                }
            }
        }
    }

    public void showError(String message) {
        final String m = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog alert = new Dialog(DetailActivity.this);
                alert.setContentView(R.layout.dialog_alert);
                alert.setCancelable(true);
                final TextView nameTextView = alert.findViewById(R.id.tvMessage);
                final Button closeButton = alert.findViewById(R.id.bClose);
                nameTextView.setText(m);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                alert.show();
            }
        });
    }
}