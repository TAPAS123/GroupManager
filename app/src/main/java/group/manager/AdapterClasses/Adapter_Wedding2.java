package group.manager.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import group.manager.R;
import group.manager.ModelClasses.WeddingPersonDetailsModel;
import group.manager.R.id;
import group.manager.R.layout;

import java.io.ByteArrayInputStream;
import java.util.List;


public class Adapter_Wedding2 extends ArrayAdapter<WeddingPersonDetailsModel>{

    Context context;
    List<WeddingPersonDetailsModel> items;

    public Adapter_Wedding2(Context context, int ResourceId, List<WeddingPersonDetailsModel> items) {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }

    public class ViewHolder {
        TextView tvname, tvfathername ,tvage,tvmobileno;
        ImageView imgperson;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final WeddingPersonDetailsModel rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.listrow,parent,false);
            holder = new ViewHolder();
            holder.tvname = (TextView) convertView.findViewById(R.id.tvname);
            holder.tvfathername = (TextView) convertView.findViewById(R.id.tvfathername);
            holder.tvage = (TextView) convertView.findViewById(R.id.tvage);
            holder.tvmobileno = (TextView) convertView.findViewById(R.id.tvmobile);
            holder.imgperson = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvname.setText(rowItem.name);
        holder.tvfathername.setText(rowItem.fathername);
        holder.tvage.setText(rowItem.age);
        holder.tvmobileno.setText(rowItem.mobileno);
        if(rowItem.imgByteArray!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(rowItem.imgByteArray);
            Bitmap img = BitmapFactory.decodeStream(imageStream);
            holder.imgperson.setImageBitmap(img);
        }
        else 
        {
        	holder.imgperson.setImageResource(R.drawable.person1);
        }

        return convertView;
    }


}
