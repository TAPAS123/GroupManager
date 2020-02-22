package group.manager;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Adapter_Menu extends ArrayAdapter<RowItem_Menu>  {
	 Context context;
		
	 public Adapter_Menu(Context context, int textViewResourceId, List<RowItem_Menu> items) {
		super(context, textViewResourceId, items);
		// TODO Auto-generated constructor stub
		this.context = context;
	 }
	 
	 private class ViewHolder {
	    TextView txtMenuName; 
	    ImageView ImgMenu;
	    RelativeLayout RrLay;
	 }
	 
	 public View getView(int position, View convertView, ViewGroup parent) {
	    ViewHolder holder = null;
	    RowItem_Menu  rowItem = getItem(position);
	    LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	    if (convertView == null) {
	      convertView = mInflater.inflate(R.layout.list_item_menu, null);
	      holder = new ViewHolder();
	      holder.RrLay=(RelativeLayout)convertView.findViewById(R.id.rrtop);
	      holder.txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
	      holder.ImgMenu = (ImageView) convertView.findViewById(R.id.imgMenu);
	      convertView.setTag(holder);
	    } else{
	      holder = (ViewHolder) convertView.getTag();
	    }
	    
	    if(rowItem.getMenuName().equals("DIR")){
	       holder.txtMenuName.setText("Directory");
           holder.ImgMenu.setImageResource(R.drawable.directory);
        }
	    else if(rowItem.getMenuName().contains("DIR_"))
	    {
	    	String [] arr=rowItem.getMenuName().split("!");
	    	String t1=arr[0].toString().trim();
		    holder.txtMenuName.setText(arr[1].toString());
		    if(t1.equalsIgnoreCase("DIR_CCM"))//Central Council Condition
		    	holder.ImgMenu.setImageResource(R.drawable.dir1);
			else if(t1.equalsIgnoreCase("DIR_RCM"))//Regional Council Condition
				holder.ImgMenu.setImageResource(R.drawable.dir2);
			else if(t1.equalsIgnoreCase("DIR_DD"))//Default Directory(in PDWMA group)(Added on 10-6-2017)
				holder.ImgMenu.setImageResource(R.drawable.dir_dd);
			else if(t1.equalsIgnoreCase("DIR_IMP"))//Important Member Directory(Added on 10-6-2017)
				holder.ImgMenu.setImageResource(R.drawable.dir_imp);
			else
			    holder.ImgMenu.setImageResource(R.drawable.directory);
	    }
	    else if(rowItem.getMenuName().equals("PRO"))
	    {
	       holder.txtMenuName.setText("Update Profile");
	       holder.ImgMenu.setImageResource(R.drawable.profile);
	    }
	    else if(rowItem.getMenuName().equals("GOV"))
	    {
	       holder.txtMenuName.setText("Governing Body");
	       holder.ImgMenu.setImageResource(R.drawable.govrn);
	    }
	    else if(rowItem.getMenuName().equals("MAN"))
	    {
	       // MAN Same as GOV only title changed
	       holder.txtMenuName.setText("Managing Committee");
		   holder.ImgMenu.setImageResource(R.drawable.govrn);
	    }
	    else if(rowItem.getMenuName().equals("OFF"))
	    {
	       // MAN Same as GOV only title changed
	       holder.txtMenuName.setText("Office Bearers");
		   holder.ImgMenu.setImageResource(R.drawable.govrn);
	    }
	    else if(rowItem.getMenuName().equals("EXEC"))
	    {
	       // MAN Same as GOV only title changed
	       holder.txtMenuName.setText("Executive Committee");
		   holder.ImgMenu.setImageResource(R.drawable.govrn);
	    }
	    else if(rowItem.getMenuName().contains("GOVOTH"))
	    {
	    	String [] arr=rowItem.getMenuName().split("!");
	       // MAN Same as GOV only title changed
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.govrn);
	    }
	    else if(rowItem.getMenuName().contains("ADVOTH!"))
	    {
	    	String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.adv);
	    }
	    else if(rowItem.getMenuName().contains("ICAI_COMM!"))
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.adv);
	    }
	    else if(rowItem.getMenuName().contains("ICAI_MULCOMM"))
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.president);
	    }
	    else if(rowItem.getMenuName().contains("ICAI_BRANCH!"))
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.branch);
	    }
	    else if(rowItem.getMenuName().contains("ICAI_PP!"))
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.president);
	    }
	    else if(rowItem.getMenuName().contains("ICAI_CPE!"))
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.cpe_prog);
	    }
	    else if(rowItem.getMenuName().contains("ICAI_QRY!"))
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.adv);
	    }
	    else if(rowItem.getMenuName().equals("PAST"))
	    {
	       holder.txtMenuName.setText("Past President / Secretary");
	       holder.ImgMenu.setImageResource(R.drawable.president);
	    }
	    else if(rowItem.getMenuName().equals("CHAIR"))
	    {
	       // CHAIR Same as PAST only title changed
	       holder.txtMenuName.setText("Past Chairman / Secretary");
	       holder.ImgMenu.setImageResource(R.drawable.president);
	    }
	    else if(rowItem.getMenuName().equals("EVE"))
	    {
	       holder.txtMenuName.setText("Events");
	       holder.ImgMenu.setImageResource(R.drawable.event);
	    }
	    else if(rowItem.getMenuName().equals("EVE_GAL"))
	    {
	       holder.txtMenuName.setText("Photo Gallery");
	       holder.ImgMenu.setImageResource(R.drawable.gallery);
	    }
	    else if(rowItem.getMenuName().equals("NEWS"))
	    {
	       holder.txtMenuName.setText("Show News");
	       holder.ImgMenu.setImageResource(R.drawable.news);
	    }
	    else if(rowItem.getMenuName().equals("CPE"))
	    {
	       holder.txtMenuName.setText("CPE Hours");
		   holder.ImgMenu.setImageResource(R.drawable.cpe);
	    }
	    else if(rowItem.getMenuName().equals("CPE_P"))
	    {
	       holder.txtMenuName.setText("CPE Programmes");
		   holder.ImgMenu.setImageResource(R.drawable.cpe_prog);
	    }
	    else if(rowItem.getMenuName().contains("CPE_P!"))//New Menu Option Added in 04-03-2017
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.cpe_prog);
	    }
	    else if(rowItem.getMenuName().equals("ADDNEWS"))
	    {
	       holder.txtMenuName.setText("Add News");
		   holder.ImgMenu.setImageResource(R.drawable.news_edit);
	    }
	    else if(rowItem.getMenuName().equals("ADDEVENT"))
	    {
	       holder.txtMenuName.setText("Add Event");
		   holder.ImgMenu.setImageResource(R.drawable.news_edit);//New Admin Menu Option Added in 29-03-2017
	    }
	    else if(rowItem.getMenuName().equals("UTIL"))
	    {
		   holder.txtMenuName.setText("Utilities");
		   holder.ImgMenu.setImageResource(R.drawable.utilities);
		}
	    else if(rowItem.getMenuName().equals("ShDir"))
	    {
	    	holder.txtMenuName.setText("Contact Directory");
	        holder.ImgMenu.setImageResource(R.drawable.directory);
	    }
	    else if(rowItem.getMenuName().equals("EVENTACC"))
	    {
	    	holder.txtMenuName.setText("Event Confirmation");
	        holder.ImgMenu.setImageResource(R.drawable.event);
	    }
	    else if(rowItem.getMenuName().equals("JAYCEE"))
	    {
		   holder.txtMenuName.setText("About Jaycee");
		   holder.ImgMenu.setImageResource(R.drawable.info);
		}
	    else if(rowItem.getMenuName().contains("JAY"))
	    {
	       if(rowItem.getMenuName().equals("JAY1"))
		     holder.txtMenuName.setText("Jaycee Creed");
	       else if(rowItem.getMenuName().equals("JAY2"))
			 holder.txtMenuName.setText("JCI Mission");
	       else if(rowItem.getMenuName().equals("JAY3"))
			 holder.txtMenuName.setText("JCI Vision");
	       else if(rowItem.getMenuName().equals("JAY4"))
			 holder.txtMenuName.setText("JCI India");
	       
		   holder.ImgMenu.setImageResource(R.drawable.info);
		}
	    else if(rowItem.getMenuName().equals("IOCL"))
	    {
		   holder.txtMenuName.setText("Information");
		   holder.ImgMenu.setImageResource(R.drawable.info);
		}
	    else if(rowItem.getMenuName().equals("Corporate Vision"))
	    {
		   holder.txtMenuName.setText(rowItem.getMenuName());
		   holder.ImgMenu.setImageResource(R.drawable.vision);
		}
	    else if(rowItem.getMenuName().equals("Values"))
	    {
		   holder.txtMenuName.setText(rowItem.getMenuName());
		   holder.ImgMenu.setImageResource(R.drawable.values);
		}
	    else if(rowItem.getMenuName().equals("Safety at Retail Outlets"))
	    {
		   holder.txtMenuName.setText(rowItem.getMenuName());
		   holder.ImgMenu.setImageResource(R.drawable.safety);
		}
	    else if(rowItem.getMenuName().equals("TT Unloading"))
	    {
		   holder.txtMenuName.setText(rowItem.getMenuName());
		   holder.ImgMenu.setImageResource(R.drawable.tt);
		}
	    else if(rowItem.getMenuName().equals("Do and Don'ts for Retail Outlets"))
	    {
		   holder.txtMenuName.setText(rowItem.getMenuName());
		   holder.ImgMenu.setImageResource(R.drawable.do_nt);
		}
	    else if(rowItem.getMenuName().equals("RAPP"))//New Update for running apps for admin(11-05-2016)
	    {
		   holder.txtMenuName.setText("Running Apps");
		   holder.ImgMenu.setImageResource(R.drawable.cpe_prog);
		}
	    else if(rowItem.getMenuName().equals("EVENT_READ"))//New Update for Read/Unread Event for admin(19-05-2016)
	    {
		   holder.txtMenuName.setText("Read Event");
		   holder.ImgMenu.setImageResource(R.drawable.event);
		}
	    else if(rowItem.getMenuName().equals("NEWS_READ"))//New Update for Read/Unread News for admin(19-05-2016)
	    {
		   holder.txtMenuName.setText("Read News");
		   holder.ImgMenu.setImageResource(R.drawable.news);
		}
	    else if(rowItem.getMenuName().equals("MGRP"))//Create Group for Admin(01-06-2016)
	    {
		   holder.txtMenuName.setText("Group Management");
		   holder.ImgMenu.setImageResource(R.drawable.govrn);
		}
	    else if(rowItem.getMenuName().equals("SMSMGT"))//Create Group for Admin(14-06-2016)
	    {
		   holder.txtMenuName.setText("SMS Management");
		   holder.ImgMenu.setImageResource(R.drawable.sms);
		}
	    else if(rowItem.getMenuName().equals("SUGG"))//Suggestion or Complaint
	    {
		   holder.txtMenuName.setText("Suggestion / Feedback");
		   holder.ImgMenu.setImageResource(R.drawable.sugg);
		}
	    else if(rowItem.getMenuName().equals("SUGG_PHOTO"))//Suggestion or Complaint with Photo Added in 31-03-2018
	    {
		   holder.txtMenuName.setText("Suggestion / Complaint");
		   holder.ImgMenu.setImageResource(R.drawable.sugg);
		}
	    else if(rowItem.getMenuName().equals("MATRI"))//Matrimony
	    {
		   holder.txtMenuName.setText("Matrimony");
		   holder.ImgMenu.setImageResource(R.drawable.ring);//Added 25-10-2016
		}
	    else if(rowItem.getMenuName().equals("RNOTI"))//Resend Notification
	    {
		   holder.txtMenuName.setText("Resend Notification");
		   holder.ImgMenu.setImageResource(R.drawable.vision);//Added 02-11-2016
		}
	    else if(rowItem.getMenuName().contains("PIC_"))// Advertisement type option
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.president);/// Added 07-11-2016
	    }
	    else if(rowItem.getMenuName().contains("LMERATES"))// LME Rates only for BME 
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.rate);/// Added 05-04-2017
	    }
	    else if(rowItem.getMenuName().contains("RBIRATES"))// Exchange Rates only for BME 
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.rate);/// Added 29-05-2017
	    }
	    else if(rowItem.getMenuName().contains("MCXRATES"))// MCX Market Watch only for BME 
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.rate);/// Added 28-06-2017
	    }
	    else if(rowItem.getMenuName().contains("OPPOLL") || rowItem.getMenuName().contains("LOCPOLL"))// Opinion Poll option
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.poll);/// Added 11-04-2017
	    }
	    else if(rowItem.getMenuName().contains("NEWSL"))//New Menu News Letter
	    {
	       String [] arr=rowItem.getMenuName().split("!");
		   holder.txtMenuName.setText(arr[1].toString());
	       holder.ImgMenu.setImageResource(R.drawable.new_icon);///Added on 08-05-2017
	    }
	    else if(rowItem.getMenuName().contains("Booking"))//New Menu Booking
	    {
	       String [] arr=rowItem.getMenuName().split("!");
		   holder.txtMenuName.setText(arr[1].toString());
	       holder.ImgMenu.setImageResource(R.drawable.booking);///Added on 24-05-2017
	    }
	    else if(rowItem.getMenuName().contains("LEDGER"))//New Menu Ledger
	    {
	       String [] arr=rowItem.getMenuName().split("!");
		   holder.txtMenuName.setText(arr[1].toString());
	       holder.ImgMenu.setImageResource(R.drawable.ledger_icon);///Added on 14-06-2017
	    }
	    else if(rowItem.getMenuName().contains("MEDICLAIM"))//New Menu MEDICLAIM
	    {
	       String [] arr=rowItem.getMenuName().split("!");
		   holder.txtMenuName.setText(arr[1].toString());
	       holder.ImgMenu.setImageResource(R.drawable.mediclaim);///Added on 07-12-2017 only in RDBA
	    }
	    else if(rowItem.getMenuName().contains("SONG"))//New Menu SONG(District Song)
	    {
	       String [] arr=rowItem.getMenuName().split("!");
		   holder.txtMenuName.setText(arr[1].toString());
	       holder.ImgMenu.setImageResource(R.drawable.song);///Added on 23-07-2017
	    }
	    else if(rowItem.getMenuName().contains("CAM"))// Photo Upload for Club Added on 28-07-2018(Group lci321b1)
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.cam);/// Added 28-07-2018
	    }
		else if(rowItem.getMenuName().contains("DGCAL"))//DG Calendar Added on 21-02-2020
		{
			String [] arr=rowItem.getMenuName().split("!");
			holder.txtMenuName.setText(arr[1].toString());
			holder.ImgMenu.setImageResource(R.drawable.president);
		}
	    else if(rowItem.getMenuName().contains("MULTIROW"))// MULTIROW Added on 02-08-2018
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.cpe);/// Added 02-08-2018
	    }
	    else if(rowItem.getMenuName().contains("TREE"))// Family Tree Added on 05-09-2018(only for gadodia and chaparia group)
	    {
	       String [] arr=rowItem.getMenuName().split("!");
	       holder.txtMenuName.setText(arr[1].toString());
		   holder.ImgMenu.setImageResource(R.drawable.tree);/// Added 05-09-2018
	    }
	    else{
			holder.RrLay.setVisibility(View.GONE);
		}
	    
	    return convertView;
	}
}
