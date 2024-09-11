package com.example.clearmind;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragCallback extends ItemTouchHelper.Callback {
    private final ValueAdapter adapter;

    public DragCallback(ValueAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // Enable drag in the up and down directions
        return makeMovementFlags(dragFlags, 0); // No swipe actions in this case
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.d("DragCallback", "onMove: position " + viewHolder.getAdapterPosition() + " to " + target.getAdapterPosition());
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Handle swipe action if necessary, for this setup, swiping is not handled
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // Return true to enable drag on long press, alternatively, can be handled via touch listener in the adapter
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // Return false as swipe actions are not supported in this setup
        return false;
    }
}

