package com.example.rangingbeacon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.estimote.coresdk.observation.region.RegionUtils;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.repackaged.okhttp_v2_2_0.com.squareup.okhttp.internal.Util;
import com.example.rangingbeacon.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.BeaconViewHolder> {
    private Context context;
    private List<Beacon> beacons = new ArrayList<>();

    public BeaconAdapter(Context context) {
        this.context = context;
    }

    public void replaceWith(Collection<Beacon> newBeacons) {
        this.beacons.clear();
        this.beacons.addAll(newBeacons);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BeaconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_recy_view, parent, false);
        BeaconViewHolder beaconViewHolder = new BeaconViewHolder(view);
        return beaconViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BeaconViewHolder holder, int position) {
        Beacon beacon = beacons.get(position);
        holder.macTv.setText("Mac address: %s" + beacon.getMacAddress().toStandardString());
        holder.distanceTV.setText("Distance: (%.2m)" + RegionUtils.computeAccuracy(beacon));
    }

    public Beacon getItemAt(int position) {
        return beacons.get(position);
    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    public class BeaconViewHolder extends RecyclerView.ViewHolder {
        TextView macTv, distanceTV;
        public BeaconViewHolder(@NonNull View itemView) {
            super(itemView);
            macTv = itemView.findViewById(R.id.mac_address);
            distanceTV = itemView.findViewById(R.id.distance);
        }
    }
}
