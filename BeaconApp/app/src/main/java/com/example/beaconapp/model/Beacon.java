package com.example.beaconapp.model;

import androidx.annotation.Nullable;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;

import java.util.UUID;


public class Beacon {
    private UUID proximityUUID;
    private int major;
    private int minor;

    public Beacon(UUID proximityUUID, int major, int minor) {
        this.proximityUUID = proximityUUID;
        this.major = major;
        this.minor = minor;
    }

    public Beacon(String UUID, int major, int minor) {
        this(java.util.UUID.fromString(UUID), major, minor);
    }

    public UUID getProximityUUID() {
        return proximityUUID;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public BeaconRegion toBeaconRegion() {
        return new BeaconRegion(toString(), getProximityUUID(), getMajor(), getMinor());
    }

    @Override
    public String toString() {
        return getProximityUUID().toString() + ":" + getMajor() + ":" + getMinor();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
