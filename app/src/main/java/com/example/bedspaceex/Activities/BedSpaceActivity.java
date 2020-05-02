package com.example.bedspaceex.Activities;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BedSpaceActivity extends AppCompatActivity {
    TextView tvHall;
    TextView tvPrice;
    TextView tvRoomNo;
    TextView tvOwnerName;
    TextView tvPhoneNo;
    TextView tvBlock;
    TextView txtHall;
    TextView txtPrice;
    TextView txtRoomNo;
    TextView txtPhone;
    TextView txtOwnerName;
    ImageView imgOwner;
    //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mDatabaseReference;
    private static final String TAG = "BedSpaceActivity";

    private BedSpaces bedSpaces;
    public Intent mIntent;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_space);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvHall = (TextView) findViewById(R.id.tvHall);
        tvOwnerName = (TextView) findViewById(R.id.tvOwnerName);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvRoomNo = (TextView) findViewById(R.id.tvRoomNo);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        txtHall = (TextView) findViewById(R.id.txtHall);
        txtOwnerName = (TextView) findViewById(R.id.txtOwnerName);
        txtPhone = (TextView) findViewById(R.id.txtPhoneNo);
        txtRoomNo = (TextView) findViewById(R.id.txtRoomNo);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        imgOwner = (ImageView) findViewById(R.id.imgOwner);

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDatabaseReference = mFirebaseDatabase.getReference().child("sellers");

        mIntent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedSpaces");
        if (bedSpaces == null)
            bedSpaces = new BedSpaces();
        this.bedSpaces = bedSpaces;
        tvPrice.setText(String.valueOf(bedSpaces.getPrice()));
        tvOwnerName.setText(bedSpaces.getOwnerName());
        tvRoomNo.setText(String.valueOf(bedSpaces.getRoomNumber()));
        tvPhoneNo.setText(bedSpaces.getPhoneNumber());
        tvHall.setText(bedSpaces.getHall());

        tvPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sellerPhoneNo = (String) tvPhoneNo.getText();
                Uri sellerPhoneUri = Uri.parse("tel:" + sellerPhoneNo);
                Intent callSellerIntent = new Intent(Intent.ACTION_DIAL);
                callSellerIntent.setData(sellerPhoneUri);
                startActivity(callSellerIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                Intent editActivityIntent = new Intent(this, InsertBedSpaceDealActivity.class);
                editActivityIntent.putExtra("bedspaceToEdit", bedSpaces);
                startActivity(editActivityIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }

    private void checkAuthenticationState() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.d(TAG, "onAuthStateChanged: user not authenticated");
            Intent intent = new Intent(BedSpaceActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }
    /*public void createPhoneIntent(View view) {


        String sellerPhoneNo = (String) tvPhoneNo.getText();
        Uri sellerPhoneUri = Uri.parse("tel:"+sellerPhoneNo);
        Intent callSellerIntent = new Intent(Intent.ACTION_DIAL);
        callSellerIntent.setData(sellerPhoneUri);
        startActivity(callSellerIntent);


    }*/
}