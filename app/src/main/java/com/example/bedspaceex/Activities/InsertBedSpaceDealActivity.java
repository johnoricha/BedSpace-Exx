package com.example.bedspaceex.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertBedSpaceDealActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    EditText editTextOwnerName;
    EditText editTextRoomNo;
    Spinner spinnerHalls;
    Button btnUploadOffer;
    EditText editTextPhoneNo;
    EditText editTextPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_bed_space_deal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextOwnerName = (EditText) findViewById(R.id.editText_Owner_Name);
        editTextRoomNo = (EditText) findViewById(R.id.editText_RoomNo);
        spinnerHalls = (Spinner) findViewById(R.id.spinner_Hall);
        btnUploadOffer = (Button) findViewById(R.id.btn_Upload_Offer);
        editTextPhoneNo = (EditText) findViewById(R.id.editText_phone_number);
        editTextPrice = (EditText) findViewById(R.id.editText_price);

        Intent intent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) intent.getSerializableExtra("bedspaceToEdit");

        if (bedSpaces == null) {
            //do nothing
        }
        else {
            editTextOwnerName.setText(bedSpaces.getOwnerName());
            editTextRoomNo.setText(bedSpaces.getRoomNumber());
            editTextPrice.setText(bedSpaces.getPrice());
            editTextPhoneNo.setText(bedSpaces.getPhoneNumber());
            //spinnerHalls.setSelection(Integer.parseInt(String.valueOf(bedSpaces.getHall())));
            Toast.makeText(this, "make sure the right hall is selected before saving", Toast.LENGTH_LONG).show();
            btnUploadOffer.setEnabled(false);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("sellers");

        //hallsList = new ArrayList<String>();

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
        R.array.halls, android.R.layout.simple_spinner_item); //this arrayadapter uses a string array resource file

        // this array adapter refused to show selected item:
        // ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, hallsList);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHalls.setAdapter(spinnerAdapter);

        btnUploadOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty(editTextOwnerName.getText().toString()) && !isEmpty(editTextPhoneNo.getText().toString())
                && !isEmpty(editTextPrice.getText().toString()) && !isEmpty(editTextRoomNo.getText().toString())) {
                    saveToDatabase();
                    Log.d("tag","deal saved");
                    Toast.makeText(InsertBedSpaceDealActivity.this, "Offer uploaded", Toast.LENGTH_LONG).show();
                    clean();
                    backToList();
                }else {
                    Toast.makeText(InsertBedSpaceDealActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void clean() {
        editTextOwnerName.setText("");
        editTextPhoneNo.setText("");
        editTextPrice.setText("");
        editTextRoomNo.setText("");
        editTextOwnerName.requestFocus();

    }

    private void saveToDatabase() {

        String ownerName = editTextOwnerName.getText().toString();
        String roomNumber = editTextRoomNo.getText().toString();
        String hall = spinnerHalls.getSelectedItem().toString();
        String price = editTextPrice.getText().toString();
        String phoneNumber = editTextPhoneNo.getText().toString();
        BedSpaces bedSpaces = new BedSpaces(ownerName, roomNumber, hall, phoneNumber, price);
        mDatabaseReference.push().setValue(bedSpaces);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        inflater.inflate(R.menu.delete_menu, menu);
        Intent intent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) intent.getSerializableExtra("bedspaceToEdit");
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
                if(!isEmpty(editTextOwnerName.getText().toString()) && !isEmpty(editTextPhoneNo.getText().toString())
                        && !isEmpty(editTextPrice.getText().toString()) && !isEmpty(editTextRoomNo.getText().toString())) {
                updateOffer();
                Toast.makeText(this, "Offer updated", Toast.LENGTH_LONG).show();
                backToList();}
                else {Toast.makeText(InsertBedSpaceDealActivity.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();}
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
        Intent intent = new Intent(this, BedSpaceListActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    private void updateOffer() {
        Intent intent = getIntent();
        BedSpaces bedSpaces = (BedSpaces) intent.getSerializableExtra("bedspaceToEdit");
        bedSpaces.setOwnerName(editTextOwnerName.getText().toString());
        bedSpaces.setRoomNumber(editTextRoomNo.getText().toString());
        bedSpaces.setPhoneNumber(editTextPhoneNo.getText().toString());
        bedSpaces.setPrice(editTextPrice.getText().toString());
        bedSpaces.setHall(spinnerHalls.getSelectedItem().toString());
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

}
