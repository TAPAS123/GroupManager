package group.manager.AdapterClasses;

import java.util.ArrayList;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import group.manager.Category;
import group.manager.R;

public class Adapter_CPE_Program_1 extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private ArrayList<Category> mParent;
	private ExpandableListView Lv_Expand;
	public int lastExpandedGroupPosition;

	public Adapter_CPE_Program_1(Context context, ArrayList<Category> parent, ExpandableListView Lv_Expand) {
		mParent = parent;
		inflater = LayoutInflater.from(context);
		this.Lv_Expand = Lv_Expand;
	}


	@Override
	//counts the number of group/parent items so the list knows how many times calls getGroupView() method
	public int getGroupCount() {
		return mParent.size();
	}

	@Override
	//counts the number of children items so the list knows how many times calls getChildView() method
	public int getChildrenCount(int i) {
		return mParent.get(i).child_cpe.size();
	}

	@Override
	//gets the title of each parent/group
	public Object getGroup(int i) {
		return mParent.get(i).name;
	}

	@Override
	//gets the name of each item
	public Object getChild(int i, int i1) {
		return mParent.get(i).child_cpe.get(i1);
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
	public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

		if (view == null) {
			view = inflater.inflate(R.layout.settings_list_item_parent, viewGroup, false);
		}
		// set category name as tag so view can be found view later
		view.setTag(getGroup(i).toString());

		TextView textView = (TextView) view.findViewById(R.id.list_item_text_view);

		//"i" is the position of the parent/group in the list
		textView.setText(getGroup(i).toString());
		CheckedTextView chkbox = (CheckedTextView) view.findViewById(R.id.list_item_text_groupval);
		chkbox.setVisibility(View.GONE);
		ImageView img = (ImageView) view.findViewById(R.id.imageView1st);
		//return the entire view
		return view;
	}


	@Override
	//in this method you must set the text to see the children on the list
	public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
		if (view == null) {
			view = inflater.inflate(R.layout.list_item_cpe_program_1, viewGroup, false);
		}
		TextView txtCpe_LDate = (TextView) view.findViewById(R.id.txtCpe_LDate);
		TextView txtCpe_LTime_FromTo = (TextView) view.findViewById(R.id.txtCpe_LTime);
		TextView txtCpe_LHrsTitle = (TextView) view.findViewById(R.id.txtCpe);
		TextView txtCpe_LHrs = (TextView) view.findViewById(R.id.txtCpe_LHrs);
		TextView txtCpe_LName_Topic = (TextView) view.findViewById(R.id.txtCpe_LName_Topic);
		TextView txtCpe_LVenue = (TextView) view.findViewById(R.id.txtCpe_LVenue);

		txtCpe_LDate.setText(mParent.get(i).child_cpe.get(i1).getLDate());
		txtCpe_LTime_FromTo.setText(mParent.get(i).child_cpe.get(i1).getLTime_FromTo());

		String CPEHrs = mParent.get(i).child_cpe.get(i1).getLCPE_Hrs();
		txtCpe_LHrs.setText(CPEHrs);

		if (CPEHrs.length() > 0) {
			txtCpe_LHrsTitle.setVisibility(View.VISIBLE);
		} else {
			txtCpe_LHrsTitle.setVisibility(View.GONE);
		}

		txtCpe_LName_Topic.setText(Html.fromHtml("<strong>Topic: </strong>" + mParent.get(i).child_cpe.get(i1).getLProg_Name()));
		txtCpe_LVenue.setText(Html.fromHtml("<strong>Venue: </strong>" + mParent.get(i).child_cpe.get(i1).getLVenue()));

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

		if (groupPosition != lastExpandedGroupPosition) {
			Lv_Expand.collapseGroup(lastExpandedGroupPosition);
		}

		super.onGroupExpanded(groupPosition);

		lastExpandedGroupPosition = groupPosition;

	}
}
