package group.manager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


/**
 * Created by intel on 25-04-2017.
 */

public class Adapter_OpinionPoll_MemberList extends ArrayAdapter<RowEnvt> {
    Context context;
    List<RowEnvt> items;

    public Adapter_OpinionPoll_MemberList(Context context, int ResourceId, List<RowEnvt> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }


    public class ViewHolder {
        TextView txt1,txt2;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final RowEnvt rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_opinionpoll_memberlist,parent,false);
            holder = new ViewHolder();
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
            
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt1.setText(""+rowItem.EvtDesc+"  "+rowItem.EvtName);
        
        if(rowItem.Evtdate.length()>0)
        {
        	holder.txt2.setVisibility(View.VISIBLE);
        	holder.txt2.setText(rowItem.Evtdate+" people");
        }
        else{
        	holder.txt2.setVisibility(View.GONE);
        }

        return convertView;
    }
}
