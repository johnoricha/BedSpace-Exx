package com.example.bedspaceex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bedspaceex.Activities.BedSpaceActivity;
import com.example.bedspaceex.Models.BedSpaces;
import com.example.bedspaceex.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HallsAdapter extends RecyclerView.Adapter<HallsAdapter.BedSpacesViewHolder> implements Filterable {

    private static final String TAG = "BedSpacesAdapter";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private List<BedSpaces> mSpaces;
    private List<BedSpaces> mSpacesFiltered;



    public HallsAdapter(ArrayList<BedSpaces> spaces){
        this.mSpaces = spaces;
        this.mSpacesFiltered = spaces;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("offers");

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BedSpaces bedSpaces = dataSnapshot.getValue(BedSpaces.class);
                bedSpaces.setId(dataSnapshot.getKey());
                HallsAdapter.this.mSpaces.add(bedSpaces);
                notifyItemInserted(HallsAdapter.this.mSpaces.size()-1);
                String bedspaceId = bedSpaces.getId();
                Log.d(TAG, "onChildAdded: " + bedspaceId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public BedSpacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        Context mCtx = parent.getContext();
        View view = LayoutInflater.from(mCtx).
                inflate(R.layout.rv_halls_item_layout, parent, false);
        BedSpacesViewHolder holder = new BedSpacesViewHolder(view);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BedSpacesViewHolder holder, int position) {
        for (BedSpaces bedSpaces: mSpacesFiltered) {
            if (position>0 && mSpacesFiltered.get(position).getHall().equals(bedSpaces.getHall())) {
                holder.mCardView.setVisibility(View.GONE);
                break;
            }
            else {
                holder.mCardView.setVisibility(View.VISIBLE);
                bedSpaces = mSpacesFiltered.get(position);
                holder.tvHall.setText(bedSpaces.getHall());
                break;
            }
        }
    }
    @Override
    public int getItemCount() {
        return mSpacesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return spacesFilter;
    }

    private Filter spacesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
             String searchString  = constraint.toString();
             if(searchString == null || searchString.length() == 0)
                 mSpacesFiltered = mSpaces;
             else {
                 String filterPattern = searchString.toLowerCase().trim();
                 List<BedSpaces> filteredList = new ArrayList<>();

                 for (BedSpaces bedSpaces: mSpaces) {
                     if (bedSpaces.getRoomNumber().toLowerCase().contains(filterPattern) ||
                             bedSpaces.getHall().toLowerCase().contains(filterPattern))
                     {
                         filteredList.add(bedSpaces);
                     }
                 }
                 mSpacesFiltered = filteredList;
             }
                FilterResults results = new FilterResults();
                results.values = mSpacesFiltered;
                Log.d("filter", "filtered list");
                return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mSpacesFiltered = (ArrayList<BedSpaces>)results.values;
            notifyDataSetChanged();
        }

    };

    class BedSpacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvHall;
        CardView mCardView;

        public BedSpacesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHall = (TextView) itemView.findViewById(R.id.tvHall);
            itemView.setOnClickListener(this);
            mCardView = itemView.findViewById(R.id.cardView);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("click",String.valueOf(position));
//            BedSpaces selectedBedSpace = mSpacesFiltered.get(position);
//            Intent intent = new Intent(view.getContext(), BedSpaceActivity.class);
//            intent.putExtra("bedSpaces",selectedBedSpace);
//            //intent.putExtra("bedSpace", (Serializable) selectedBedSpace);
//            view.getContext().startActivity(intent);
            Snackbar.make(view, "list of available bedspaces in selected hall to be displayed",
                    BaseTransientBottomBar.LENGTH_LONG).show();

            //TODO: display list of offers available from selected hall

        }
    }


}
