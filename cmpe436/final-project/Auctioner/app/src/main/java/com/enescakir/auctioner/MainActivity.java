package com.enescakir.auctioner;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.enescakir.auctioner.Model.Auction;
import com.enescakir.auctioner.Model.Packet;
import com.enescakir.auctioner.Model.SocketThread;
import com.enescakir.auctioner.Model.User;

import java.util.ArrayList;

import static com.enescakir.auctioner.Model.Packet.Type.AUCTIONS;
import static com.enescakir.auctioner.Model.Packet.Type.USER;


public class MainActivity extends BaseActivity {
    RecyclerView auctionsView;
    Dialog loginDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AuctionThread thread = new AuctionThread();
        thread.start();

        preferences = getSharedPreferences("user", MODE_PRIVATE);

        if (preferences.getString("username", null) == null) {
            authUser();
        } else {
            username = preferences.getString("username", null);
            points = preferences.getInt("points", 1000);
            showToast(MainActivity.this, "Welcome " + username);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            username = null;
            points = 0;
            preferences.edit().remove("username").apply();
            preferences.edit().remove("points").apply();
            authUser();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void authUser() {
        loginDialog = new Dialog(this);
        loginDialog.setContentView(R.layout.dialog_login);
        loginDialog.setCancelable(false);
        loginDialog.show();
        final EditText nameInput = loginDialog.findViewById(R.id.etUsername);
        final EditText passwordInput = loginDialog.findViewById(R.id.etPassword);
        final Button button = loginDialog.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = nameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    nameInput.setError("Username is required!");
                    showToast(MainActivity.this, "Username is required!");
                } else if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Password is required!");
                    showToast(MainActivity.this, "Password is required!");
                } else {
                    loginDialog.dismiss();
                    showProgress(MainActivity.this);
                    passwordInput.setText("");
                    final AuthThread thread = new AuthThread(username, password);
                    thread.start();
                }
            }
        });
    }

    private void setAuctionsView(ArrayList<Auction> auctions) {
        auctionsView = findViewById(R.id.auctionsView);
        auctionsView.setAdapter(new AuctionAdapter(this, auctions));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        auctionsView.setLayoutManager(linearLayoutManager);
    }

    class AuctionThread extends SocketThread {
        @Override
        public void run() {
            super.run();
            try {
                sendMessage(new Packet(AUCTIONS, "").toJson());
                final Packet response = Packet.fromJson(in.readLine());
                close();
                Log.e("AUCTIONS", response.getData());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAuctionsView(response.toArrayList(Auction.class));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class AuthThread extends SocketThread {
        String username;
        String password;

        AuthThread(String username, String password) {
            super();
            this.username = username;
            this.password = password;
        }

        @Override
        public void run() {
            super.run();
            try {
                sendMessage(new Packet(USER, new User(username, password)).toJson());

                final Packet packet = Packet.fromJson(in.readLine());
                Log.e("USER", packet.getData());
                int response = packet.toObject(Integer.class);
                close();
                progressDialog.dismiss();
                if (response == -1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(MainActivity.this, "Wrong password");
                            loginDialog.show();
                        }
                    });
                } else {
                    preferences.edit().putString("username", username).apply();
                    preferences.edit().putInt("points", response).apply();
                    BaseActivity.username = username;
                    BaseActivity.points = response;
                    showToast(MainActivity.this, "Welcome " + username);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}