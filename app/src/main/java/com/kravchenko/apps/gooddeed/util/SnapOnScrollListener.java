package com.kravchenko.apps.gooddeed.util;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class SnapOnScrollListener extends RecyclerView.OnScrollListener {

    private final SnapHelper snapHelper;
    private final OnSnapPositionChangeListener listener;
    private int snapPosition = RecyclerView.NO_POSITION;

    public SnapOnScrollListener(SnapHelper snapHelper, OnSnapPositionChangeListener listener) {
        super();
        this.snapHelper = snapHelper;
        this.listener = listener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
        notifySnapPositionChange(recyclerView);
    }

    private void notifySnapPositionChange(RecyclerView recyclerView) {
        int position = getSnapPosition(recyclerView);
        if (this.snapPosition != position) {
            listener.onSnapPositionChange(position);
            this.snapPosition = position;
        }
    }

    private int getSnapPosition(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) return RecyclerView.NO_POSITION;
        View snapView = snapHelper.findSnapView(layoutManager);
        if (snapView == null) return RecyclerView.NO_POSITION;
        return layoutManager.getPosition(snapView);
    }

    public interface OnSnapPositionChangeListener {

        void onSnapPositionChange(int position);

    }
}
