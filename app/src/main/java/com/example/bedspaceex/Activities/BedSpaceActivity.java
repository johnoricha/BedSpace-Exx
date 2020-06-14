package com.example.bedspaceex.Activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bedspaceex.FirebaseUtil;
import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class BedSpaceActivity extends AppCompatActivity {
    TextView mTextViewHall;
    TextView mTextViewPrice;
    TextView mTextViewRoomNo;
    TextView mTextViewOwnerName;
    TextView mTextViewPhoneNo;
   // TextView tvBlock;
    TextView mTxtHall;
    TextView mTxtPrice;
    TextView mTxtRoomNo;
    TextView mTxtPhone;
    TextView mTxtOwnerName;
    ImageView imgHouse;
    //private FirebaseDatabase mFirebaseDatabase;
    //private DatabaseReference mDatabaseReference;
    private static final String TAG = "BedSpaceActivity";

    private BedSpaces mBedSpaces;
    public Intent mIntent;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUtil mFirebaseUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_space);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextViewHall = (TextView) findViewById(R.id.tvHall);
        mTextViewOwnerName = (TextView) findViewById(R.id.tvOwnerName);
        mTextViewPrice = (TextView) findViewById(R.id.tvPrice);
        mTextViewRoomNo = (TextView) findViewById(R.id.tvRoomNo);
        mTextViewPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        mTxtHall = (TextView) findViewById(R.id.txtHall);
        mTxtOwnerName = (TextView) findViewById(R.id.txtOwnerName);
        mTxtPhone = (TextView) findViewById(R.id.txtPhoneNo);
        mTxtRoomNo = (TextView) findViewById(R.id.txtRoomNo);
        mTxtPrice = (TextView) findViewById(R.id.txtPrice);
        imgHouse = (ImageView) findViewById(R.id.imgOwner);

        //mFirebaseDatabase = FirebaseDatabase.getInstance();
        //mDatabaseReference = mFirebaseDatabase.getReference().child("sellers");


        mIntent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedSpaces");
        if (bedSpaces == null)
            bedSpaces = new BedSpaces();
        this.mBedSpaces = bedSpaces;
        mTextViewPrice.setText(String.valueOf(bedSpaces.getPrice()));
        mTextViewOwnerName.setText(bedSpaces.getOwnerName());
        mTextViewRoomNo.setText(String.valueOf(bedSpaces.getRoomNumber()));
        mTextViewPhoneNo.setText(bedSpaces.getPhoneNumber());
        mTextViewHall.setText(bedSpaces.getHall());
        showImage(bedSpaces.getImageUrl());
        mFirebaseUtil = new FirebaseUtil(BedSpaceActivity.this);
        mFirebaseUtil.checkAdmin();

        mTextViewPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sellerPhoneNo = (String) mTextViewPhoneNo.getText();
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
        MenuItem menuItem = menu.findItem(R.id.menu_item_edit);
        if (mFirebaseUtil.isAdmin) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:
                Intent editActivityIntent = new Intent(this, InsertEditBedSpaceDealActivity.class);
                editActivityIntent.putExtra("bedspaceToEdit", mBedSpaces);
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

    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(Uri.parse(url))
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(imgHouse);
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