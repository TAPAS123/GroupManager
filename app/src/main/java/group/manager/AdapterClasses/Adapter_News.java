package group.manager.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import group.manager.R;
import group.manager.RowEnvt;

public class Adapter_News extends ArrayAdapter<RowEnvt> {
    Context context;
    String R_type;

    public Adapter_News(Context context, int textViewResourceId, List<RowEnvt> items, String R_type) {
        super(context, textViewResourceId, items);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.R_type = R_type;
    }

    private class ViewHolder {
        TextView txtDate,txttilte,txtnewsDesc; //txtEvtDesc
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowEnvt  rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_news_list, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.txtDatenews);
            holder.txttilte = (TextView) convertView.findViewById(R.id.txtnewsTitle);
            holder.txtnewsDesc = (TextView) convertView.findViewById(R.id.txtnewsDesc);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtDate.setText(rowItem.getEvtDesc());
        holder.txttilte.setText(rowItem.getEvtName());
        holder.txtnewsDesc.setText(rowItem.getEvtVenue());
        return convertView;
    }

}

