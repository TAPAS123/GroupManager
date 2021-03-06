package group.manager.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import group.manager.JudgementModel;
import group.manager.R;

/**
 * Created by intel on 31-05-2017.
 */

public class AdapterJudgement extends ArrayAdapter<JudgementModel> {
    Context context;
    List<JudgementModel> items;

    public AdapterJudgement(Context context, int ResourceId, List<JudgementModel> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }


    public class ViewHolder {
        TextView tvnames,tvtotal;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final JudgementModel rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.rowitem_judgement,parent,false);
            holder = new ViewHolder();
            holder.tvnames = (TextView) convertView.findViewById(R.id.tvnames);
            holder.tvtotal = (TextView) convertView.findViewById(R.id.tvtotal);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvnames.setText(""+rowItem.Sno+". "+rowItem.names);
        holder.tvtotal.setText(rowItem.total);
        return convertView;
    }

}
