package group.manager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by intel on 12-06-2017.
 */

public class AdapterDebtorLedger extends ArrayAdapter<DebtorLedgerModel> {
    Context context;
    List<DebtorLedgerModel> items;

    public AdapterDebtorLedger(Context context, int ResourceId, List<DebtorLedgerModel> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }


    public class ViewHolder {
        TextView tvdate,tvdesc,tvdebit,tvcredit,tvamt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final DebtorLedgerModel rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.rowitem_debtorledger,parent,false);
            holder = new ViewHolder();
            holder.tvdate = (TextView) convertView.findViewById(R.id.tvdate);
            holder.tvdesc = (TextView) convertView.findViewById(R.id.tvdesc);
            holder.tvdebit = (TextView) convertView.findViewById(R.id.tvdebit);
            holder.tvcredit = (TextView) convertView.findViewById(R.id.tvcredit);
            holder.tvamt = (TextView) convertView.findViewById(R.id.tvamt);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvdate.setText(""+rowItem.tvdate);
        holder.tvdesc.setText(rowItem.tvdesc);
        holder.tvdebit.setText(rowItem.tvdebit);
        holder.tvcredit.setText(rowItem.tvcredit);
        holder.tvamt.setText(rowItem.tvamt);
        return convertView;
    }

}
