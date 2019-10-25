package group.manager;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class Adapter_OpPoll_QAns extends ArrayAdapter<RowItem_OpPoll_QAns> {
    Context context;
    List<RowItem_OpPoll_QAns> items;

    public Adapter_OpPoll_QAns(Context context, int ResourceId, List<RowItem_OpPoll_QAns> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }


    public class ViewHolder {
        TextView tvsno ,tvans;
        ImageView ImgChk;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Adapter_OpPoll_QAns.ViewHolder holder = null;
        final RowItem_OpPoll_QAns rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.opinionpoll_answerlistrow,parent,false);
            holder = new ViewHolder();
            holder.tvsno = (TextView) convertView.findViewById(R.id.tvsno);
            holder.tvans = (TextView) convertView.findViewById(R.id.tvansopt);
            holder.ImgChk=(ImageView) convertView.findViewById(R.id.imgcorrectans);

            convertView.setTag(holder);
        }
        else{
            holder = (Adapter_OpPoll_QAns.ViewHolder) convertView.getTag();
        }
        
        String Sno=rowItem.Sno;
        String Ansopt=rowItem.Ansopt;
        int CorrectAnsSno=rowItem.CorrectAnsSno;
        
        if(rowItem.flag || (position+1)==CorrectAnsSno)
        {
        	holder.ImgChk.setVisibility(View.VISIBLE);
        	
        	if(CorrectAnsSno==0)
        	{
        		holder.tvsno.setText(Html.fromHtml("<font color='#378408'><b>"+Sno+"</b></font>"));
             	holder.tvans.setText(Html.fromHtml("<font color='#378408'><b> "+Ansopt+"</b></font>"));
             	holder.ImgChk.setImageResource(R.drawable.check);
        	}
        	else if((position+1)==CorrectAnsSno)
    		{
    			holder.tvsno.setText(Html.fromHtml("<font color='#378408'><b>"+Sno+"</b></font>"));
             	holder.tvans.setText(Html.fromHtml("<font color='#378408'><b> "+Ansopt+"</b></font>"));
             	holder.ImgChk.setImageResource(R.drawable.check);
    		}
    		else
    		{
    			holder.tvsno.setText(Html.fromHtml("<font color='#f93831'><b>"+Sno+"</b></font>"));
    	    	holder.tvans.setText(Html.fromHtml("<font color='#f93831'><b> "+Ansopt+"</b></font>"));
    	    	holder.ImgChk.setImageResource(R.drawable.uncheck);
    		}
        }
        else
        {
        	holder.tvsno.setText(Sno);
        	holder.tvans.setText(Ansopt);
        	holder.ImgChk.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}
