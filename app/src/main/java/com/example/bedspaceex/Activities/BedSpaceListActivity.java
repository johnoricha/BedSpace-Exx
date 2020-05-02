package com.example.bedspaceex.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.Adapters.BedSpacesAdapter;
import com.example.bedspaceex.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class BedSpaceListActivity extends AppCompatActivity {

    RecyclerView rvBedSpaceList;
    ArrayList<BedSpaces> spaces;
    private BedSpacesAdapter adapter;
    private static final String TAG = "BedSpaceListActivity";
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sell_menu, menu);
        inflater.inflate(R.menu.search_menu, menu);
        inflater.inflate(R.menu.signout_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_sell:
                //saveDeal();
                //Toast.makeText(this,"Deal saved", Toast.LENGTH_LONG).show();
                //clean();
                //backToList();
                Intent intent = new Intent(BedSpaceListActivity.this, InsertBedSpaceDealActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_item_signout:
                Log.d(TAG, "signOut: signing out");
                FirebaseAuth.getInstance().signOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpFirebaseAuthStateListener();

        spaces = new ArrayList<>();

        rvBedSpaceList = (RecyclerView)findViewById(R.id.rvBedSpaceList);
        adapter = new BedSpacesAdapter(spaces);
        rvBedSpaceList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBedSpaceList.setLayoutManager(layoutManager);
        // list creation
        // BedSpaceList = new ArrayList<>();
        /*
        adding items to the list
        BedSpaceList.add(
                new BedSpace(
                        1,
                        "A16",
                        15000,
                        "John Oricha",
                        "08165215761",
                        "Zik",
                        "A"));
        BedSpaceList.add(
                new BedSpace(2,
                        "B2",
                        30000,
                        "Felix Samuel",
                        "07087506133",
                        "Mellanby",
                        "A"));
        BedSpaceList.add(
                new BedSpace(3,
                        "D12",
                        20000,
                        "Promise Dennis",
                        "08101128804",
                        "Sultan Bello",
                        "A"));*/
    }


    private void checkAuthenticationState() {
        Log.d(TAG, "checkAuthenticationState: checking authentication state.");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            Log.d(TAG, "checkAuthenticationState: user is null, navigating back to login screen.");

            Intent intent = new Intent(BedSpaceListActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "checkAuthenticationState: user is authenticated.");
        }
    }

    private void setUpFirebaseAuthStateListener() {
        Log.d(TAG, "setupFirebaseAuth: started.");
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(BedSpaceListActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                            // ...
            }
        };
    }

    @Override
    protected void onStart() {
            super.onStart();
            FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null)
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }
}
