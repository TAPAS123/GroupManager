package group.manager;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by intel on 05-06-2017.
 */

public class AdapterVoting extends ArrayAdapter<VotingModel> {

    Context context;
    List<VotingModel> items;
    String type="";
    //EditText edtpref;

    public AdapterVoting(Context context, int ResourceId, List<VotingModel> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }


    public class ViewHolder {
        TextView tvnames;
        ImageView imgselect;
       // EditText edtpref;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // ViewHolder holder = null;
        final VotingModel rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

      //  if (convertView == null)
      //  {
            convertView = mInflater.inflate(R.layout.rowitem_voting,parent,false);
        //    holder = new ViewHolder();
             TextView tvnames = (TextView) convertView.findViewById(R.id.tvnames);
             ImageView imgselect = (ImageView) convertView.findViewById(R.id.imgselect);
            final EditText edtpref = (EditText) convertView.findViewById(R.id.edtpref);
        //    convertView.setTag(holder);
    //    }
    //    else{
    //        holder = (ViewHolder) convertView.getTag();
    //    }
        tvnames.setText(rowItem.Sno+". "+rowItem.ptpnames);
        imgselect.setVisibility(View.GONE);
        edtpref.setVisibility(View.GONE);
        type = rowItem.choicetype;
        if(type.equals("1") || type.equals("M"))
        {
            if(rowItem.chkval == true)
            {
                imgselect.setVisibility(View.VISIBLE);
            }
            else
            {
                imgselect.setVisibility(View.GONE);
            }
        }
        else if(type.equals("P"))
        {
            edtpref.setVisibility(View.VISIBLE);
            edtpref.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }


                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = edtpref.getText().toString();
                    if (!str.equals(""))
                    {
                        if(Integer.valueOf(str)> Integer.valueOf(rowItem.multiplechoicecount) || Integer.valueOf(str) < 1 )
                        {
                            edtpref.setError("not valid!");
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        return convertView;
    }
}
