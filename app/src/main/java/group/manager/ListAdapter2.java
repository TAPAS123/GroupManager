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
import android.graphics.Typeface;
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
	Bitmap bm = null;
	Dialog dialog;
	AlertDialog.Builder alertDialogBuilder3;
	AlertDialog ad;

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
			view = lInflater.inflate(R.layout.list_items_bday_anni, parent, false);
		}
		p = getProduct(position);
		TextView tvname = (TextView) view.findViewById(R.id.txtname);
		TextView tvcity = (TextView) view.findViewById(R.id.txtcity);
		TextView txtmob = (TextView) view.findViewById(R.id.txtmobile);
		ImageView iv = (ImageView) view.findViewById(R.id.imageViewPic);

		tvname.setText(p.name);
		tvcity.setText(p.city);
		txtmob.setText(p.mob);

		ImageView ivloc = (ImageView) view.findViewById(R.id.imgloc);

		Typeface face = Typeface.createFromAsset(ctx.getAssets(), "calibri.ttf");
		//tvname.setTypeface(face);
		txtmob.setTypeface(face);
		tvcity.setTypeface(face);

		bm = p.imageId1;

		if (bm == null) {
			iv.setImageResource(R.drawable.person1);
		} else {
			iv.setImageBitmap(bm);
		}

		if (p.val == null) {
			iv.setVisibility(View.VISIBLE);
		} else if (p.val.equals("0")) {
			iv.setVisibility(View.GONE);
		}

		if (p.city.length() == 0) {
			ivloc.setVisibility(View.GONE);
		} else {
			ivloc.setVisibility(View.VISIBLE);
		}


		txtmob.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				p = getProduct(position);
				final String mob = p.mob;
				System.out.println(mob);
				if (mob.length() != 0) {
					alertDialogBuilder3
							.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
									new CommonClass().callOnPhone(ctx,mob);
								}
							})
							.setNeutralButton("SMS", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.dismiss();
									new CommonClass().callOnSms(ctx,mob);;
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
				bm = p.imageId1;
				if (bm != null) {
					Dialog dialog = new Dialog(ctx);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
					dialog.setContentView(R.layout.showbigimage);
					ImageView IVzoomimage = (ImageView) dialog.findViewById(R.id.webView1Imagezoom);
					//LinearLayout llay= (LinearLayout)dialog.findViewById(R.id.llimg);
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) IVzoomimage.getLayoutParams();
					params.setMargins(20, 0, 20, 0); //substitute parameters for left, top, right, bottom
					IVzoomimage.setLayoutParams(params);
					RelativeLayout RRlay = (RelativeLayout) dialog.findViewById(R.id.rrbtn);
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
			if (p.box) {
				box.add(p);
			}
		}
		return box;
	}


	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
									 boolean isChecked) {
			getProduct((Integer) buttonView.getTag()).box = isChecked;
		}
	};

}