package group.manager.AdapterClasses;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import group.manager.R;
import group.manager.R.drawable;
import group.manager.R.id;
import group.manager.R.layout;

import java.util.HashMap;
import java.util.List;


public class Adapter_Wedding3 extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;

    public Adapter_Wedding3(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
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
    public Object getChild(int groupPosition, int childPosition)
    {
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
        String header = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listheaderlayout, null);
        }

        TextView tvheader = (TextView) convertView.findViewById(R.id.tvheader);
        tvheader.setTypeface(null, Typeface.BOLD);
        ImageView imgicon1 = (ImageView)convertView.findViewById(R.id.imageView1);
        ImageView imgicon2 = (ImageView)convertView.findViewById(R.id.imageView2);
        ExpandableListView mExpandableListView = (ExpandableListView) parent;
        if(groupPosition == 0)
        {
            imgicon1.setImageResource(R.drawable.personaldetailicon);
        }
        else if(groupPosition == 1)
        {
            imgicon1.setImageResource(R.drawable.addressicon);
        }
        else  if(groupPosition == 2)
        {
            imgicon1.setImageResource(R.drawable.occupationicon);
        }
        else if(groupPosition == 3)
        {
            imgicon1.setImageResource(R.drawable.familydetailicon);
        }
        tvheader.setText(header);

        mExpandableListView.expandGroup(groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listchildlayout, null);
        }

        TextView tvchild = (TextView) convertView.findViewById(R.id.tvchild);
        tvchild.setText(Html.fromHtml(childText));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return false;
    }

}
