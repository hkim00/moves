package com.hkim00.moves.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Move;
import com.hkim00.moves.models.Restaurant;
import com.hkim00.moves.models.UserLocation;
import com.lyft.deeplink.RideTypeEnum;
import com.lyft.lyftbutton.LyftButton;
import com.lyft.lyftbutton.RideParams;
import com.lyft.networking.ApiConfig;

import java.util.List;

public class MoveDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<Move> moves;


    public MoveDetailsAdapter(Context context, List<Move> moves) {
        this.context = context;
        this.moves = moves;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_move_details, parent, false);
            viewHolder = new MoveDetailsAdapter.DetailsViewHolder(view);
        } else if (viewType == 1) {
            View view = inflater.inflate(R.layout.item_transportation, parent, false);
            viewHolder = new MoveDetailsAdapter.TransportationViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            final DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;
            detailsViewHolder.bind(moves.get(0));
        } else if (position == 1) {
            final TransportationViewHolder transportationViewHolder = (TransportationViewHolder) holder;
            transportationViewHolder.bind(moves.get(0));
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }

    public class DetailsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvDistance;
        private TextView tvPrice;
        private TextView tvCuisine;
        private RatingBar ratingBar;


        public DetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCuisine = itemView.findViewById(R.id.tvCuisine);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }


        public void bind(Move move) {
            tvName.setText(move.name);

            if (move.lat != null && move.lng != null) {
                tvDistance.setText(move.distanceFromLocation(context) + " mi");

                if (move.moveType.equals("food")) {
                    setFoodView(move);
                }
            }
        }

        private void setFoodView(Move move) {
            tvCuisine.setText(move.cuisine);
            Restaurant restaurant = (Restaurant) move;


            String price = "";
            if (restaurant.price_level < 0) {
                price = "N/A";
            } else {
                for (int i = 0; i < restaurant.price_level; i++) {
                    price += '$';
                }
            }
            tvPrice.setText(price);

            if (restaurant.rating < 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            } else {
                float moveRate = ((Restaurant) move).rating.floatValue();
                ratingBar.setRating(moveRate > 0 ? moveRate / 2.0f : moveRate);
            }
        }

    }



    public class TransportationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        private GoogleMap mMap;
        private UiSettings mUiSettings;
        private LyftButton lyftButton;
        private Move mapMove;


        public TransportationViewHolder(@NonNull View itemView) {
            super(itemView);

            lyftButton = itemView.findViewById(R.id.lyft_button);
        }

        public void bind(Move move) {
            mapMove = move;

            if (move.lat != null && move.lng != null) {
                addMapFragment();
                setupLyftButton();
            }
        }

        @Override
        public void onMapReady(GoogleMap map) {
            mMap = map;
            mUiSettings = mMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(true);

            LatLng moveLatLng = new LatLng(mapMove.lat, mapMove.lng);
            map.addMarker(new MarkerOptions().position(moveLatLng).title(mapMove.name));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(moveLatLng, 15)); // second argument controls how zoomed in map is
        }

        private void addMapFragment() {
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            mapFragment.getMapAsync(this);
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit();
        }


        private void setupLyftButton() {
            ApiConfig apiConfig = new ApiConfig.Builder()
                    .setClientId(context.getString(R.string.client_id_lyft))
                    .setClientToken("...")
                    .build();
            lyftButton.setApiConfig(apiConfig);
            UserLocation currLocation = UserLocation.getCurrentLocation(context);

            RideParams.Builder rideParamsBuilder = new RideParams.Builder()
                    .setPickupLocation(Double.valueOf(currLocation.lat), Double.valueOf(currLocation.lng))
                    .setDropoffLocation(mapMove.lat, mapMove.lng);
            rideParamsBuilder.setRideTypeEnum(RideTypeEnum.STANDARD);

            lyftButton.setRideParams(rideParamsBuilder.build());
            lyftButton.load();
        }
    }
}
