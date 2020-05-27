package com.example.newcomer_io.ui.main.EventDetails;

import com.plumillonforge.android.chipview.Chip;

public class Tag implements Chip {
    private String mName;
    private int mType = 0;

    public Tag(String name, int type) {
        this(name);
        mType = type;
    }

    public Tag(String name) {
        mName = name;
    }

    @Override
    public String getText() {
        return mName;
    }

    public int getType() {
        return mType;
    }
}