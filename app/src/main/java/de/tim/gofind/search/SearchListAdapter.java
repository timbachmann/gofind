package de.tim.gofind.search;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.tim.gofind.R;

/**
 * TODO
 */
public class SearchListAdapter extends ArrayAdapter<HistoricalImage> {

    private final LayoutInflater inflater;
    private final List<HistoricalImage> historicalImageList;

    public SearchListAdapter(LayoutInflater inflater, List<HistoricalImage> historicalImageList) {
        super(inflater.getContext(), R.layout.list_item, historicalImageList);
        this.inflater = inflater;
        this.historicalImageList = historicalImageList;
    }

    /**
     * TODO
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @SuppressLint("InflateParams")
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

    /**
     * TODO
     */
    private static class ViewHolder {
        TextView titleText;
        TextView dateText;
        TextView sourceText;
        TextView distanceText;
        ImageView imageView;
    }
}
