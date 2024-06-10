package com.example.project_patt.retrieve;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_patt.Add_job;

import java.util.List;

public class SelectedItemsViewModel extends ViewModel {
    private MutableLiveData<List<Add_job>> selectedItems = new MutableLiveData<>();

    public MutableLiveData<List<Add_job>> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Add_job> items) {
        selectedItems.setValue(items);
    }
}
