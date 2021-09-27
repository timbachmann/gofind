package de.tim.gofind.utils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import de.tim.gofind.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final View mContents;

    @SuppressLint("InflateParams")
    public CustomInfoWindowAdapter(LayoutInflater layoutInflater) {
        mWindow = layoutInflater.inflate(R.layout.custom_info_window, null);
        mContents = layoutInflater.inflate(R.layout.custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view) {
        Picasso.get().load(marker.getSnippet()).placeholder(R.drawable.ic_baseline_image_24).into(((ImageView) view.findViewById(R.id.badge)));

        String title = marker.getTitle();
        TextView titleUi = view.findViewById(R.id.title);
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }
    }
}
