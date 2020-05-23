package group.manager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import group.manager.AdapterClasses.Adapter_News;

public class Adapter_Event extends ArrayAdapter<Category> {

    Context context;
    private ArrayList<Category> item;

    public Adapter_Event(Context context, ArrayList<Category> item) {
        super(context, 0, item);
        this.item = item;
        this.context = context;
    }

    private class ViewHolder {
        TextView txtName,txtDate,txtDesc,txtVenue,txtTime;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_event, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            holder.txtDesc = (TextView) convertView.findViewById(R.id.txtDesc);
            holder.txtVenue = (TextView) convertView.findViewById(R.id.txtVenue);
            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(item.get(position).name);
        holder.txtVenue.setText(item.get(position).val3);

        String EventDT = item.get(position).val2;

        String[] Arr1 = EventDT.split(" ");

        holder.txtDate.setText(Arr1[0].trim());
        holder.txtTime.setText(Arr1[1] + " " + Arr1[2].toUpperCase());

        holder.txtDesc.setText(item.get(position).val4.split("#")[0]);

        return convertView;
    }
}