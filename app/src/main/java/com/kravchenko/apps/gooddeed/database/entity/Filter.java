package com.kravchenko.apps.gooddeed.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.kravchenko.apps.gooddeed.util.annotation.FilterName;


@Entity
public class Filter {
    @PrimaryKey
    private long filterId;

    @FilterName
    private int filterName;
    private String selectedValue;

    public Filter(int filterName, String selectedValue) {
        this.filterName = filterName;
        this.selectedValue = selectedValue;
    }

    public void setFilterId(long filterId) {
        this.filterId = filterId;
    }

    public void setFilterName(int filterName) {
        this.filterName = filterName;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public long getFilterId() {
        return filterId;
    }

    public int getFilterName() {
        return filterName;
    }

    public String getSelectedValue() {
        return selectedValue;
    }
}
