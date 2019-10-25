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
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

/**
 * Created by intel on 01-06-2017.
 */

public class AdapterJudgementRating extends ArrayAdapter<JudgementModel> {
    Context context;
    List<JudgementModel> items;
    int sum = 0;
    TextView tvtotal;

    public AdapterJudgementRating(Context context, int ResourceId, List<JudgementModel> items,TextView tvtotal)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
        this.tvtotal = tvtotal;

    }


//    public class ViewHolder {
//        TextView tvoptions,tvrange,tv;
//        EditText edtrating;
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    //    ViewHolder holder = null;
        final JudgementModel rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

       // if (convertView == null)
       // {
            convertView = mInflater.inflate(R.layout.rowitem_judgement_rating,parent,false);
          //  holder = new ViewHolder();
            TextView tvoptions = (TextView) convertView.findViewById(R.id.tvoptions);
            TextView tvrange = (TextView) convertView.findViewById(R.id.tvrange);
            final EditText edtrating = (EditText) convertView.findViewById(R.id.edtrating);
         //   convertView.setTag(holder);
      //  }
     //   else
     //   {
      //      holder = (ViewHolder) convertView.getTag();
     //   } CriteriaMid,int Sno, String option, String range,String marks)
        tvoptions.setText(""+rowItem.Sno+". "+rowItem.option);
        tvrange.setText(rowItem.range);
        edtrating.setText(rowItem.marks);
        sum = Integer.parseInt(tvtotal.getText().toString());
        edtrating.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = edtrating.getText().toString();
                if(!str.equals(""))
                {
                    if(!(Integer.valueOf(str) >5 || Integer.valueOf(str) <1)) {
                        int getmarks = Integer.valueOf(str);
                        sum = sum - getmarks;
                        tvtotal.setText("" + sum);
                    }
                }
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = edtrating.getText().toString();
                    if (!str.equals(""))
                    {
                        if(Integer.valueOf(str)>5 || Integer.valueOf(str) < 1 )
                        {
                            edtrating.setError("not valid!");
                        }
                        else
                        {
                            int getmarks = Integer.valueOf(str);
                            sum = sum + getmarks;
                            //   Toast.makeText(context, "Sum " + sum, Toast.LENGTH_SHORT).show();
                            tvtotal.setText("" + sum);
                        }
                    }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return convertView;
    }
}
