package group.manager;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Group> groups;
    ArrayList<Child> chList;

    public ExpandListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {

        Child child = (Child) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_row, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.grp_child);
        TextView tv2 = (TextView) convertView.findViewById(R.id.grp_val);
        ImageView iv = (ImageView) convertView.findViewById(R.id.imageViewpix);
        CheckBox cb = (CheckBox)convertView.findViewById( R.id.check1 );
        cb.setChecked( child.getState() );
        tv.setText(child.getName().toString());
        tv2.setText(child.getVAL().toString());
        if(child.getImage()==null){
          iv.setImageResource(R.drawable.person1);
        }else{
          iv.setImageBitmap(child.getImage());
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
       chList = groups.get(groupPosition).getItems();
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        Group group = (Group) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_row, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.row_name);
        TextView tv2 = (TextView) convertView.findViewById(R.id.row_N);
        tv.setText(group.getName());
        tv2.setText(group.getNval());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    ArrayList<Child> getBox() {
		ArrayList<Child> box = new ArrayList<Child>();
		for (Child p : chList ) {
				if (p.state){
					box.add(p);	
				}
		}
		return box;
	}
    
   
	
}
