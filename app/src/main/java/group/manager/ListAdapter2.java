package group.manager;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter2 extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<Product> objects;
	Product p;
	Bitmap bm=null;
	Dialog dialog;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;
	boolean blview=false;
	
	ListAdapter2(Context context, ArrayList<Product> products) {
		ctx = context;
		objects = products;
		lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		alertDialogBuilder3 = new AlertDialog.Builder(context);
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.itemlistss, parent, false);
		}
		p = getProduct(position);
		((TextView) view.findViewById(R.id.txtname)).setText(p.name);
		((TextView) view.findViewById(R.id.txtcity)).setText(p.city);
		TextView txtmob=(TextView) view.findViewById(R.id.txtmobile);
		txtmob.setText(p.mob);
		ImageView iv=(ImageView) view.findViewById(R.id.imageViewPic);
		View divider= (View) view.findViewById(R.id.divider); 
		blview=p.view;
		bm=p.imageId1;
		
		if(bm==null){
			iv.setImageResource(R.drawable.person1);
		}else{
		    iv.setImageBitmap(bm);
		}
		
		if(p.val==null){
			iv.setVisibility(View.VISIBLE);
		}else if(p.val.equals("0")){
			iv.setVisibility(View.GONE);
		}
		
		if(blview==true){
			 divider.setVisibility(View.VISIBLE);
			 divider.setBackgroundColor(Color.BLUE);
		}else{
			 //divider.setBackgroundColor(Color.WHITE);
			 divider.setVisibility(View.GONE);
		}
		
		txtmob.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	 p = getProduct(position);
		    	 final String mob=p.mob;
		    	 System.out.println(mob);
		    	 if(mob.length()!=0){
		    		 alertDialogBuilder3
			                .setPositiveButton("Call",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                    	dialog.dismiss();
			                    	callOnphone(mob);
			                    }
			                })
			                .setNeutralButton("SMS",new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog,int id) {
			                     	dialog.dismiss();
			                     	callOnSms(mob);
			                     }
			                });
		       		 ad = alertDialogBuilder3.create();
			         ad.show();	
		    	 }
		    }
		});
		
		 iv.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		     p = getProduct(position);
		     bm=p.imageId1;
		     if(bm!=null){
		      Dialog dialog = new Dialog(ctx);
			  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	  dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			  dialog.setContentView(R.layout.showbigimage);
			  ImageView IVzoomimage = (ImageView)dialog.findViewById(R.id.webView1Imagezoom);
			  //LinearLayout llay= (LinearLayout)dialog.findViewById(R.id.llimg);
			  LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)IVzoomimage.getLayoutParams();
			  params.setMargins(20, 0, 20, 0); //substitute parameters for left, top, right, bottom
			  IVzoomimage.setLayoutParams(params);
			  RelativeLayout RRlay= (RelativeLayout)dialog.findViewById(R.id.rrbtn);
			  RRlay.setVisibility(View.GONE);
			  IVzoomimage.setTag(position); 
			  IVzoomimage.setImageBitmap(bm);
		      dialog.show(); 
		     }
		    }
		 });
		 
		CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
		cbBuy.setOnCheckedChangeListener(myCheckChangList);
		cbBuy.setTag(position);
		cbBuy.setChecked(p.box);
		
		return view;
	}

	Product getProduct(int position) {
		return ((Product) getItem(position));
	}

	ArrayList<Product> getBox() {
		ArrayList<Product> box = new ArrayList<Product>();
		for (Product p : objects) {
				if (p.box){
					box.add(p);	
				}
		}
		return box;
	}
	
	public void callOnphone(String MobCall) {
		try {
			 Intent callIntent = new Intent(Intent.ACTION_DIAL);
		     callIntent.setData(Uri.parse("tel:"+MobCall));
		     ctx.startActivity(callIntent);
	       } catch (ActivityNotFoundException activityException) {
	    	   Toast.makeText(ctx, "Call failed", 0).show();
	       }
		}

	public void callOnSms(String MobCall) {
		try {
			String uri= "smsto:"+MobCall;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra("compose_mode", true);
            ctx.startActivity(intent);
	       } catch (ActivityNotFoundException activityException) {
	    	Toast.makeText(ctx, "Sms failed", 0).show();
	       }
		}

	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getProduct((Integer) buttonView.getTag()).box = isChecked;
		}
	};
	
}