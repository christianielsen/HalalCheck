package com.chris.firebaseauth.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chris.firebaseauth.map.models.NearbyPlaces;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {
    private MutableLiveData<List<NearbyPlaces.Result>> resultsData = new MutableLiveData<>();

    public void setResultsData(List<NearbyPlaces.Result> results) {
        resultsData.setValue(results);
    }

    public LiveData<List<NearbyPlaces.Result>> getResultsData() {
        return resultsData;
    }

    public void addResultsData(List<NearbyPlaces.Result> results) {
        List<NearbyPlaces.Result> currentResults = resultsData.getValue();
        if (currentResults == null) {
            currentResults = new ArrayList<>();
        }
        currentResults.addAll(results);
        resultsData.setValue(currentResults);
    }

    public int getTotalResultsCount() {
        List<NearbyPlaces.Result> results = resultsData.getValue();
        if (results != null) {
            return results.size();
        } else {
            return 0;
        }
    }
}
