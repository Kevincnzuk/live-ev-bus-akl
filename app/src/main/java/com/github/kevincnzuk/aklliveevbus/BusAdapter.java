package com.github.kevincnzuk.aklliveevbus;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder>{

    Context here;
    private List<VehicleVO> vehicleVOList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialButton btnLabel;
        MaterialButton btnId;
        MaterialButton btnRoute;
        MaterialButton btnEnergy;
        MaterialButton btnCopy;
        MaterialButton btnFullInfo;

        public ViewHolder(View view) {
            super(view);
            btnLabel = view.findViewById(R.id.item_main_list_bus_fleet);
            btnId = view.findViewById(R.id.item_main_list_bus_id);
            btnRoute = view.findViewById(R.id.item_main_list_bus_route);
            btnEnergy = view.findViewById(R.id.item_main_list_bus_icon);
            btnCopy = view.findViewById(R.id.item_main_list_bus_copy);
            btnFullInfo = view.findViewById(R.id.item_main_list_bus_full);
        }
    }

    public BusAdapter(Context context, List<VehicleVO> vehicleVOS) {
        here = context;
        vehicleVOList = vehicleVOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list_bus, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VehicleVO vo = vehicleVOList.get(position);

        String extra = vo.getId() + " / " + vo.getLicensePlate();
        String energy = here.getString(R.string.ev);

        if (vo.getEnergy().equals("fcv")) {
            energy = here.getString(R.string.fcv);
            holder.btnEnergy.setIcon(here.getDrawable(R.drawable.water_drop_black_24dp));
        } else if (vo.getEnergy().equals("hev")) {
            energy = here.getString(R.string.hev);
            holder.btnEnergy.setIcon(here.getDrawable(R.drawable.electrical_services_black_24dp));
        }

        holder.btnId.setText(extra);
        holder.btnLabel.setText(vo.getLabel());
        holder.btnRoute.setText(vo.getRouteId());
        holder.btnEnergy.setText(energy);
        holder.btnCopy.setOnClickListener(v -> {
            ClipboardManager manager = (ClipboardManager) here.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText(vo.getId(),
                    "Bus " + vo.getLabel() + " (" + vo.getId() + ", " + vo.getLicensePlate() + ") is operating on Route "+ vo.getRouteId() + ".");
            manager.setPrimaryClip(data);

            Snackbar.make(holder.itemView, "Copied to clipboard.", Snackbar.LENGTH_LONG).show();
        });
        holder.btnFullInfo.setOnClickListener(v -> {
            Intent intent = new Intent(here, DetailActivity.class);
            intent.putExtra("json", vo.getFullJson());
            here.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vehicleVOList.size();
    }
}
