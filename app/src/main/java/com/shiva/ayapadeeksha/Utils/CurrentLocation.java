package com.shiva.ayapadeeksha.Utils;

import android.location.Geocoder;
import android.location.Location;

public interface CurrentLocation {
    void onLocationFeched(Geocoder geocoder, Location location);
}