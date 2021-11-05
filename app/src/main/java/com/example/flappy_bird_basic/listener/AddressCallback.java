package com.example.flappy_bird_basic.listener;

import android.location.Address;

public interface AddressCallback {
    void onGetAddress(Address address);
    void onGetLocation(double lat,double lng);
}
