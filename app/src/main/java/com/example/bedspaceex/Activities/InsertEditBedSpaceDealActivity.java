package com.example.bedspaceex.Activities;


import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bedspaceex.FirebaseUtil;
import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class InsertEditBedSpaceDealActivity extends AppCompatActivity {
    private static final String TAG = "InsertBedSpaceDealActiv";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public static final int REQUEST_CODE = 590;

    EditText mEditTextOwnerName;
    EditText mEditTextRoomNo;
    Spinner mSpinnerHalls;
    Button mBtnUploadOffer;
    EditText mEditTextPhoneNo;
    EditText mEditTextPrice;
    Button mBtnSelectImage;
    ImageView mImageView;
    private Intent mIntent;
    private Uri mDownloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_bed_space_deal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUtil.getFirebaseStorage();

        mEditTextOwnerName = (EditText) findViewById(R.id.editText_owner_name);
        mEditTextRoomNo = (EditText) findViewById(R.id.editText_roomNo);
        mSpinnerHalls = (Spinner) findViewById(R.id.spinner_hall);
        mBtnUploadOffer = (Button) findViewById(R.id.btn_upload_offer);
        mEditTextPhoneNo = (EditText) findViewById(R.id.editText_phone_number);
        mEditTextPrice = (EditText) findViewById(R.id.editText_price);
        mBtnSelectImage = (Button) findViewById(R.id.btn_select_image);
        mImageView = (ImageView) findViewById(R.id.image);

        mIntent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedspaceToEdit");

        if (bedSpaces == null) {
            //do nothing
        }
        else {
            mEditTextOwnerName.setText(bedSpaces.getOwnerName());
            mEditTextRoomNo.setText(bedSpaces.getRoomNumber());
            mEditTextPrice.setText(bedSpaces.getPrice());
            mEditTextPhoneNo.setText(bedSpaces.getPhoneNumber());
            //spinnerHalls.setSelection(Integer.parseInt(String.valueOf(bedSpaces.getHall())));
            Toast.makeText(this, "make sure the right hall is selected before saving", Toast.LENGTH_LONG).show();
            mBtnUploadOffer.setEnabled(false);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("offers");

        //hallsList = new ArrayList<String>();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
        R.array.halls, android.R.layout.simple_spinner_item); //this arrayadapter uses a string array resource file

        // this array adapter refused to show selected item:
        // ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, hallsList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerHalls.setAdapter(spinnerAdapter);

        mBtnUploadOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(mEditTextOwnerName.getText().toString()) && !isEmpty(mEditTextPhoneNo.getText().toString())
                && !isEmpty(mEditTextPrice.getText().toString()) && !isEmpty(mEditTextRoomNo.getText().toString())) {
                    saveToDatabase();
                    Log.d(TAG,"deal saved");
                    Toast.makeText(InsertEditBedSpaceDealActivity.this, "Offer uploaded", Toast.LENGTH_LONG).show();
                    clean();
                    backToList();
                }else {
                    Toast.makeText(InsertEditBedSpaceDealActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBtnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "get picture"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            final StorageReference reference = FirebaseUtil.getStorageReference().child(imageUri.getLastPathSegment());
//            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    String url = taskSnapshot.getStorage().getDownloadUrl().toString();
//                    BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedspaceToEdit");
//                    if (bedSpaces == null) {
//                        bedSpaces = new BedSpaces();
//                    }
//                    bedSpaces.setImageUrl(url);
//                }
//            });
            UploadTask uploadTask = reference.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        mDownloadUri = task.getResult();
                        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedspaceToEdit");
                        if (bedSpaces == null) {
                            bedSpaces = new BedSpaces();
                            bedSpaces.setImageUrl(mDownloadUri.toString());
                        }
                        bedSpaces.setImageUrl(mDownloadUri.toString());
                        showImage(mDownloadUri.toString());
                    } else {
                        //handle failures
                    }
                }
            });
        }
    }

    private void clean() {
        mEditTextOwnerName.setText("");
        mEditTextPhoneNo.setText("");
        mEditTextPrice.setText("");
        mEditTextRoomNo.setText("");
        mEditTextOwnerName.requestFocus();

    }

    private void saveToDatabase() {

//        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedspaceToEdit");

        BedSpaces bedSpaces = new BedSpaces();

        bedSpaces.setHall(mSpinnerHalls.getSelectedItem().toString());
        bedSpaces.setOwnerName(mEditTextOwnerName.getText().toString());
        bedSpaces.setRoomNumber(mEditTextRoomNo.getText().toString());
        bedSpaces.setPhoneNumber(mEditTextPhoneNo.getText().toString());
        bedSpaces.setPrice(mEditTextPrice.getText().toString());
        bedSpaces.setImageUrl(mDownloadUri.toString());
//        mOwnerName = mEditTextOwnerName.getText().toString();
//        mRoomNumber = mEditTextRoomNo.getText().toString();
//        mHall = mSpinnerHalls.getSelectedItem().toString();
//        mPrice = mEditTextPrice.getText().toString();
//        mPhoneNumber = mEditTextPhoneNo.getText().toString();
//        BedSpaces bedSpaces = new BedSpaces(mOwnerName, mRoomNumber, mHall, mPhoneNumber, mPrice);
        if (bedSpaces.getId() == null) {
            mDatabaseReference.push().setValue(bedSpaces);
        }
        else updateOffer();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        inflater.inflate(R.menu.delete_menu, menu);
//        Intent intent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedspaceToEdit");
        if(bedSpaces != null) {
            menu.findItem(R.id.menu_item_save).setVisible(true);
            menu.findItem(R.id.menu_item_delete).setVisible(true);
        }
        else {
            menu.findItem(R.id.menu_item_save).setVisible(false);
            menu.findItem(R.id.menu_item_delete).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case(R.id.menu_item_save):
                if(!isEmpty(mEditTextOwnerName.getText().toString()) && !isEmpty(mEditTextPhoneNo.getText().toString())
                        && !isEmpty(mEditTextPrice.getText().toString()) && !isEmpty(mEditTextRoomNo.getText().toString())) {
                updateOffer();
                Toast.makeText(this, "Offer updated", Toast.LENGTH_LONG).show();
                backToList();}
                else {Toast.makeText(InsertEditBedSpaceDealActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();}
                return true;
            case (R.id.menu_item_delete):
                deleteOffer();
                Toast.makeText(this, "Offer deleted", Toast.LENGTH_LONG).show();
                backToList();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void backToList() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void updateOffer() {
        mIntent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) mIntent.getSerializableExtra("bedspaceToEdit");
        bedSpaces.setOwnerName(mEditTextOwnerName.getText().toString());
        bedSpaces.setRoomNumber(mEditTextRoomNo.getText().toString());
        bedSpaces.setPhoneNumber(mEditTextPhoneNo.getText().toString());
        bedSpaces.setPrice(mEditTextPrice.getText().toString());
        bedSpaces.setHall(mSpinnerHalls.getSelectedItem().toString());
        mDatabaseReference.child(bedSpaces.getId()).setValue(bedSpaces);

    }

    private void deleteOffer() {
        Intent intent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) intent.getSerializableExtra("bedspaceToEdit");
        if (bedSpaces == null) {
            Toast.makeText(this, "save deal before deleting", Toast.LENGTH_LONG).show();
        }
        else mDatabaseReference.child(bedSpaces.getId()).removeValue();

    }
    private boolean isEmpty(String string){
        return string.equals("");
    }

    private void showImage(String url) {
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get().load(Uri.parse(url))
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(mImageView);
        }
    }

}
