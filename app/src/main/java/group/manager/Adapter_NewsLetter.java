package group.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 06-05-2017.
 */

public class Adapter_NewsLetter extends BaseExpandableListAdapter {
    Context context;
    private List<RowEnvt> listDataHeader; // header titles
    private HashMap<RowEnvt, List<RowEnvt>> listDataChild;
    int i=0;

    public Adapter_NewsLetter(Context context, List<RowEnvt> listDataHeader,
                                 HashMap<RowEnvt, List<RowEnvt>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    	RowEnvt rowItem = (RowEnvt) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.newsletterheaderlist, null);
        }
        LinearLayout layoutheader = (LinearLayout) convertView.findViewById(R.id.LinearLayoutHeader);

        ImageView imgvwArrow = (ImageView) convertView.findViewById(R.id.imgvwArrow);
        TextView tvtext = (TextView) convertView.findViewById(R.id.tvtext);

        tvtext.setText(rowItem.EvtName);
        if(isExpanded)
        {
          //  tvtext.setTextColor(Color.parseColor("#ff0000"));
            imgvwArrow.setImageResource(R.drawable.down);
           // layoutheader.setBackgroundResource(R.drawable.newsletterheaderlist2);
        }
        else
        {
          //  tvtext.setTextColor(Color.parseColor("#ffffff"));
            imgvwArrow.setImageResource(R.drawable.up);
         //   layoutheader.setBackgroundResource(R.drawable.newsletterheaderlist);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    	RowEnvt rowItem = (RowEnvt) getChild(groupPosition,childPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.newsletterchildlist, null);
        }
        TextView tvmonth = (TextView) convertView.findViewById(R.id.tvtextchild);

        tvmonth.setText(rowItem.EvtName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
