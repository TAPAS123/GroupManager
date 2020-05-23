package group.manager.AdapterClasses;

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

import group.manager.R;
import group.manager.VotingModel;

/**
 * Created by intel on 05-06-2017.
 */

public class AdapterVoting extends ArrayAdapter<VotingModel> {

    Context context;
    List<VotingModel> items;
    String type = "";
    //EditText edtpref;

    public AdapterVoting(Context context, int ResourceId, List<VotingModel> items) {
        super(context, ResourceId, items);
        this.context = context;
        this.items = items;
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
        convertView = mInflater.inflate(R.layout.listitem_voting, parent, false);
        //    holder = new ViewHolder();
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        ImageView img1 = (ImageView) convertView.findViewById(R.id.img1);
        final EditText edPref = (EditText) convertView.findViewById(R.id.edPref);
        //    convertView.setTag(holder);
        //    }
        //    else{
        //        holder = (ViewHolder) convertView.getTag();
        //    }

        img1.setVisibility(View.GONE);
        edPref.setVisibility(View.GONE);

        txtName.setText(rowItem.Sno + ". " + rowItem.ptpnames);
        type = rowItem.choicetype;

        if (type.equals("1") || type.equals("M")) {
            if (rowItem.chkval == true) {
                img1.setVisibility(View.VISIBLE);
            } else {
                img1.setVisibility(View.GONE);
            }
        } else if (type.equals("P")) {
            edPref.setVisibility(View.VISIBLE);
            edPref.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = edPref.getText().toString();
                    if (!str.equals("")) {
                        if (Integer.valueOf(str) > Integer.valueOf(rowItem.multiplechoicecount) || Integer.valueOf(str) < 1) {
                            edPref.setError("Not Valid!");
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
