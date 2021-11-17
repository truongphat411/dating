package com.application.dating.main;

import androidx.recyclerview.widget.DiffUtil;

import com.application.dating.model.File_Image_Male;

import java.util.List;

public class CardStackCallback extends DiffUtil.Callback {

    private List<File_Image_Male> old, baru;

    public CardStackCallback(List<File_Image_Male> old, List<File_Image_Male> baru) {
        this.old = old;
        this.baru = baru;
    }

    @Override
    public int getOldListSize() {
        return old.size();
    }

    @Override
    public int getNewListSize() {
        return baru.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition).getImagepath().equals(baru.get(newItemPosition).getImagepath());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old.get(oldItemPosition) == baru.get(newItemPosition);
    }
}
