package group.manager;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 *
 * @author Lauri Nevala
 * 
 * Based on example by LAURA SUCIU
 * @see http://myandroidsolutions.blogspot.com/2012/08/android-expandable-list-example.html
 *
 */

public class EventSettingsListAdapter extends BaseExpandableListAdapter {
 
 
    private LayoutInflater inflater;
    private ArrayList<Category> mParent;
    private ExpandableListView accordion;
    public int lastExpandedGroupPosition;    
    
 
    public EventSettingsListAdapter(Context context, ArrayList<Category> parent, ExpandableListView accordion) {
        mParent = parent;        
        inflater = LayoutInflater.from(context);
        this.accordion = accordion;       
	}
 
 
    @Override
    //counts the number of group/parent items so the list knows how many times calls getGroupView() method
    public int getGroupCount() {
        return mParent.size();
    }
 
    @Override
    //counts the number of children items so the list knows how many times calls getChildView() method
    public int getChildrenCount(int i) {
        return mParent.get(i).children.size();
    }
 
    @Override
    //gets the title of each parent/group
    public Object getGroup(int i) {
        return mParent.get(i).name;
    }
 
    @Override
    //gets the name of each item
    public Object getChild(int i, int i1) {
        return mParent.get(i).children.get(i1);
    }
 
    @Override
    public long getGroupId(int i) {
        return i;
    }
 
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
 
    @Override
    public boolean hasStableIds() {
        return true;
    }
 
    @Override
    //in this method you must set the text to see the parent/group on the list
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
    	
        if (view == null) {
            view = inflater.inflate(R.layout.settings_list_item_parent, viewGroup,false);
        }
        // set category name as tag so view can be found view later
        view.setTag(getGroup(i).toString());
        
        TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);
        ImageView Img1 = (ImageView) view.findViewById(R.id.imageView1st);
        
        if (isExpanded) {
        	Img1.setImageResource(R.drawable.up);
        } else {
        	Img1.setImageResource(R.drawable.down);
        }
        
        //"i" is the position of the parent/group in the list
        textView.setText(getGroup(i).toString());
        CheckedTextView chkbox = (CheckedTextView) view.findViewById(R.id.list_item_text_groupval);
        chkbox.setVisibility(View.GONE);
        ImageView img=(ImageView) view.findViewById(R.id.imageView1st);
        //return the entire view
        return view;
    }
    
 
    @Override
    //in this method you must set the text to see the children on the list
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.evntlist, viewGroup,false);
        }
        TextView txtEvtName = (TextView) view.findViewById(R.id.txteventname);
        TextView txtEvtVenue = (TextView) view.findViewById(R.id.txtevtvenue);
        TextView TxtEvtDate = (TextView) view.findViewById(R.id.txtevtdate);
        TextView TxtEvtTime = (TextView) view.findViewById(R.id.txtevtTime);
        TextView TxtEvtDesc = (TextView) view.findViewById(R.id.txtEvtDesc);
        
        LinearLayout LLDT = (LinearLayout) view.findViewById(R.id.LLDT);
        
        LLDT.setBackgroundColor(Color.parseColor("#30ffffff"));
        boolean IsUserEvent=mParent.get(i).children.get(i1).isUserEvent;
        if(!IsUserEvent)
        	LLDT.setBackgroundColor(Color.parseColor("#FAFC89"));
        
        //"i" is the position of the parent/group in the list and 
        //"i1" is the position of the child
        txtEvtName.setText(mParent.get(i).children.get(i1).name);    
        txtEvtVenue.setText(mParent.get(i).children.get(i1).val3);    
        
        String EventDT=mParent.get(i).children.get(i1).val2;
        String[] Arr1=EventDT.split(" ");
        
        TxtEvtDate.setText(Arr1[0]);  
        TxtEvtTime.setText(Arr1[1]+" "+Arr1[2].toUpperCase());
        
        TxtEvtDesc.setText(mParent.get(i).children.get(i1).val4.split("#")[0]); 
        
        //return the entire view
        return view;
    }
 
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    
    @Override
    /**
     * automatically collapse last expanded group
     * @see http://stackoverflow.com/questions/4314777/programmatically-collapse-a-group-in-expandablelistview
     */    
    public void onGroupExpanded(int groupPosition) {
    	
    	if(groupPosition != lastExpandedGroupPosition){
            accordion.collapseGroup(lastExpandedGroupPosition);
        }
    	
        super.onGroupExpanded(groupPosition);
     
        lastExpandedGroupPosition = groupPosition;
        
    }
    
    
    
}

