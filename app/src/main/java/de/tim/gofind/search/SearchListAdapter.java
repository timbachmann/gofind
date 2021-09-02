package de.tim.gofind.search;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.tim.gofind.R;
import de.tim.gofind.utils.Utils;

public class SearchListAdapter extends ArrayAdapter<HistoricalImage> {

    private final LayoutInflater inflater;
    private final List<HistoricalImage> historicalImageList;
    private double latitude;
    private double longitude;

    public SearchListAdapter(LayoutInflater inflater, List<HistoricalImage> historicalImageList, double latitude, double longitude) {
        super(inflater.getContext(), R.layout.list_item, (List<HistoricalImage>) historicalImageList);

        this.latitude = latitude;
        this.longitude = longitude;
        this.inflater = inflater;
        this.historicalImageList = historicalImageList;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.titleText = (TextView) convertView.findViewById(R.id.image_title);
            holder.dateText = (TextView) convertView.findViewById(R.id.image_date);
            holder.sourceText = (TextView) convertView.findViewById(R.id.image_source);
            holder.distanceText = (TextView) convertView.findViewById(R.id.image_distance);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoricalImage currentImage = historicalImageList.get(position);
        holder.titleText.setText(currentImage.getTitle());
        holder.dateText.setText(currentImage.getDate());
        holder.sourceText.setText(currentImage.getSource());
        holder.distanceText.setText(String.format("%om", (int) Utils.haversineDistance(latitude, longitude, currentImage.getLatitude(), currentImage.getLongitude())));
        Picasso.get().load(currentImage.getPath()).placeholder(R.drawable.ic_baseline_image_24).into(holder.imageView);

        return convertView;
    }

    static class ViewHolder {
        TextView titleText;
        TextView dateText;
        TextView sourceText;
        TextView distanceText;
        ImageView imageView;
    }
}
