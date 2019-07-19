package com.hkim00.moves.fragments;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hkim00.moves.HomeActivity;
import com.hkim00.moves.MovesActivity;
import com.hkim00.moves.R;
import com.hkim00.moves.models.Restaurant;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    public final static String TAG = "HomeFragment";
    public static final String API_BASE_URL = "https://maps.googleapis.com/maps/api/place";

    private String time;
    private String numberOfPeople;
    private int distance;
    private int priceLevel;
    private List<Restaurant> restaurantResults;

    private TextView tvLocation, tvDistance, tvPriceLevel;
    private ImageView ivDistance, ivPrice;
    private Button btnTime, btnPeople, btnDistance, btnPrice;

    private ConstraintLayout clCategories;
    private ImageView ivFood, ivActivities, ivAttractions, ivEvents;

    private ConstraintLayout clPrice;
    private TextView tvRightPopupTitle, tvMiles;
    private EditText etDistance;
    private Button btnPriceLevel1, btnPriceLevel2, btnPriceLevel3, btnPriceLevel4;

    private Button btnMove, btnRiskyMove;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        restaurantResults = new ArrayList<>();

        getViewIds(view);

        setupDesign();

        setupButtons();
    }

    private void getViewIds(View view) {
        tvLocation = view.findViewById(R.id.tvLocation);
        btnTime = view.findViewById(R.id.btnTime);
        btnPeople = view.findViewById(R.id.btnPeople);
        ivDistance = view.findViewById(R.id.ivDistance);
        tvDistance = view.findViewById(R.id.tvDistance);
        btnDistance = view.findViewById(R.id.btnDistance);
        ivPrice = view.findViewById(R.id.ivPrice);
        btnPrice = view.findViewById(R.id.btnPrice);
        tvPriceLevel = view.findViewById(R.id.tvPriceLevel);

        clCategories = view.findViewById(R.id.clCategories);
        ivFood = view.findViewById(R.id.ivFood);
        ivActivities = view.findViewById(R.id.ivActivities);
        ivAttractions = view.findViewById(R.id.ivAttractions);
        ivEvents = view.findViewById(R.id.ivEvents);

        clPrice = view.findViewById(R.id.clPrice);
        tvRightPopupTitle = view.findViewById(R.id.tvRightPopupTitle);
        tvMiles = view.findViewById(R.id.tvMiles);
        etDistance = view.findViewById(R.id.etDistance);
        btnPriceLevel1 = view.findViewById(R.id.btnPriceLevel1);
        btnPriceLevel2 = view.findViewById(R.id.btnPriceLevel2);
        btnPriceLevel3 = view.findViewById(R.id.btnPriceLevel3);
        btnPriceLevel4 = view.findViewById(R.id.btnPriceLevel4);

        btnMove = view.findViewById(R.id.btnMove);
        btnRiskyMove = view.findViewById(R.id.btnRiskyMove);
    }

    private void setupDesign() {
        btnTime.getLayoutParams().width= HomeActivity.screenWidth/4;
        btnPeople.getLayoutParams().width= HomeActivity.screenWidth/4;
        btnDistance.getLayoutParams().width= HomeActivity.screenWidth/4;
        btnPrice.getLayoutParams().width= HomeActivity.screenWidth/4;

        clPrice.setVisibility(View.INVISIBLE);

        etDistance.addTextChangedListener(textWatcher);
        tvDistance.setVisibility(View.INVISIBLE);
        distance = milesToMeters(1);

        tvPriceLevel.setVisibility(View.INVISIBLE);
        priceLevel = 0;

        clCategories.post(new Runnable() {
            @Override
            public void run() {
                int constraintHeight = clCategories.getLayoutParams().height;
                ivFood.getLayoutParams().height = constraintHeight/4;
                ivActivities.getLayoutParams().height = constraintHeight/4;
                ivAttractions.getLayoutParams().height = constraintHeight/4;
                ivEvents.getLayoutParams().height = constraintHeight/4;
            }
        });
    }

    private void setupButtons() {
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightPopup("price");
            }
        });

        btnDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRightPopup("distance");
            }
        });

        btnPriceLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(1);
            }
        });

        btnPriceLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(2);
            }
        });

        btnPriceLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(3);
            }
        });

        btnPriceLevel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceLevelSelected(4);
            }
        });


        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNearbyRestaurants();
            }
        });
    }

    private void toggleRightPopup(String type) {
        if (!(!tvRightPopupTitle.getText().toString().toLowerCase().equals(type) && clPrice.getVisibility() == View.VISIBLE)) {
            clPrice.setVisibility((clPrice.getVisibility() == View.INVISIBLE) ? View.VISIBLE : View.INVISIBLE);
        }

        if (type.equals("price")) {
            tvRightPopupTitle.setText("Price");

            btnPriceLevel1.setVisibility(View.VISIBLE);
            btnPriceLevel2.setVisibility(View.VISIBLE);
            btnPriceLevel3.setVisibility(View.VISIBLE);
            btnPriceLevel4.setVisibility(View.VISIBLE);
            tvMiles.setVisibility(View.INVISIBLE);
            etDistance.setVisibility(View.INVISIBLE);

        } else if (type.equals("distance")) {
            tvRightPopupTitle.setText("Distance");

            btnPriceLevel1.setVisibility(View.INVISIBLE);
            btnPriceLevel2.setVisibility(View.INVISIBLE);
            btnPriceLevel3.setVisibility(View.INVISIBLE);
            btnPriceLevel4.setVisibility(View.INVISIBLE);
            tvMiles.setVisibility(View.VISIBLE);
            etDistance.setVisibility(View.VISIBLE);
        }
    }


    private void priceLevelSelected(int priceLevel) {
        int selectedColor = getResources().getColor(R.color.selected_blue);

        if (this.priceLevel == priceLevel) {
            this.priceLevel = 0;

            ivPrice.setVisibility(View.VISIBLE);
            tvPriceLevel.setVisibility(View.INVISIBLE);

            btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
            btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
            btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

        } else {
            this.priceLevel = priceLevel;

            tvPriceLevel.setVisibility(View.VISIBLE);
            ivPrice.setVisibility(View.INVISIBLE);

            if (priceLevel == 1) {
                btnPriceLevel1.setBackgroundColor(selectedColor);
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$");

            } else if (priceLevel == 2) {
                btnPriceLevel2.setBackgroundColor(selectedColor);
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$$");

            } else if (priceLevel == 3) {
                btnPriceLevel3.setBackgroundColor(selectedColor);
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel4.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$$$");

            } else if (priceLevel == 4) {
                btnPriceLevel4.setBackgroundColor(selectedColor);
                btnPriceLevel4.setTextColor(getResources().getColor(R.color.white));

                btnPriceLevel1.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel1.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel2.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel2.setTextColor(getResources().getColor(R.color.black));
                btnPriceLevel3.setBackgroundColor(getResources().getColor(R.color.white));
                btnPriceLevel3.setTextColor(getResources().getColor(R.color.black));

                tvPriceLevel.setText("$$$$");
            }
        }
    }


    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String distanceString = etDistance.getText().toString().trim();

            if (distanceString.equals("")) {
                ivDistance.setVisibility(View.VISIBLE);
                tvDistance.setVisibility(View.INVISIBLE);
            } else {
                ivDistance.setVisibility(View.INVISIBLE);
                tvDistance.setVisibility(View.VISIBLE);

                tvDistance.setText(distanceString + "mi");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };


    private void getNearbyRestaurants() {
        if (ParseUser.getCurrentUser().getJSONArray("foodPrefList") == null) {
            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser object, ParseException e) {
                    ParseUser.getCurrentUser().put("foodPrefList", object.getJSONArray("foodPrefList"));
                }
            });
        } else {
            Log.d(TAG, "already saved food list");
        }


        String apiUrl = API_BASE_URL + "/nearbysearch/json";

        String distanceString = etDistance.getText().toString().trim();
        distance = (distanceString.equals("")) ? milesToMeters(1) : milesToMeters(Float.valueOf(distanceString));


        RequestParams params = new RequestParams();
        params.put("location","47.6289467,-122.3428731");
        params.put("radius", (distance > 50000) ? 50000 : distance);
        params.put("type","restaurant");

        if (priceLevel > 0) {
            params.put("maxprice", priceLevel);
        }

        params.put("key", getString(R.string.api_key));

        HomeActivity.client.get(apiUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                JSONArray results;
                try {
                    results = response.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        Restaurant restaurant = Restaurant.fromJSON(results.getJSONObject(i));
                        Log.d(TAG, "got restaurant");

                        restaurantResults.add(restaurant);
                    }

                    Intent intent = new Intent(getContext(), MovesActivity.class);
                    intent.putExtra("moves", Parcels.wrap(restaurantResults));
                    startActivity(intent);

                } catch (JSONException e) {
                    Log.e(TAG, "Error getting nearby");
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, responseString);
                throwable.printStackTrace();
            }
        });
    }


    private int milesToMeters(float miles) {
        return (int) (miles/0.000621317);
    }
}
