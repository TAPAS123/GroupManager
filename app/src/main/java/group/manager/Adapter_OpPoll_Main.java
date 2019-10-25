package group.manager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class Adapter_OpPoll_Main extends ArrayAdapter<RowItem_OpPoll_Main>{
    Context context;
    List<RowItem_OpPoll_Main> items;

    public Adapter_OpPoll_Main(Context context, int ResourceId, List<RowItem_OpPoll_Main> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }


    public class ViewHolder {
    	ImageView Img1;
        TextView tvopname,tvDate;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final RowItem_OpPoll_Main rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.opinionpoll_mainquestionlistrow,parent,false);
            holder = new ViewHolder();
            holder.Img1 = (ImageView) convertView.findViewById(R.id.img1);
            holder.tvopname = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvopname.setText(rowItem.title);
        holder.tvDate.setText(rowItem.DateFromTo);
        
        if(rowItem.RType.equals("Quiz")) {
        	if(rowItem.ChkNewPoll)
        		holder.Img1.setImageResource(R.drawable.quiz_icon_new);//For New Quiz
        	else
        	    holder.Img1.setImageResource(R.drawable.quiz_icon);
        }
        else{
        	if(rowItem.ChkNewPoll)
        		holder.Img1.setImageResource(R.drawable.poll_new);//For New Poll
        	else
        	    holder.Img1.setImageResource(R.drawable.pollsmall);
        }

        return convertView;
    }
}
