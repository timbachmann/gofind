package de.tim.gofind.search;

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
    private final double latitude;
    private final double longitude;

    public SearchListAdapter(LayoutInflater inflater, List<HistoricalImage> historicalImageList, double latitude, double longitude) {
        super(inflater.getContext(), R.layout.list_item, historicalImageList);
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
            holder.titleText = convertView.findViewById(R.id.image_title);
            holder.dateText = convertView.findViewById(R.id.image_date);
            holder.sourceText = convertView.findViewById(R.id.image_source);
            holder.distanceText = convertView.findViewById(R.id.image_distance);
            holder.imageView = convertView.findViewById(R.id.image_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HistoricalImage currentImage = historicalImageList.get(position);
        holder.titleText.setText(currentImage.getTitle());
        holder.dateText.setText(currentImage.getDate());
        holder.sourceText.setText(currentImage.getSource());
        holder.distanceText.setText(String.format("%om", currentImage.getDistance()));
        Picasso.get().load(currentImage.getPath()).placeholder(R.drawable.ic_baseline_image_24).into(holder.imageView);

        return convertView;
    }

    private static class ViewHolder {
        TextView titleText;
        TextView dateText;
        TextView sourceText;
        TextView distanceText;
        ImageView imageView;
    }
}
