package group.manager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

public class CommonClass {

    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public String getIMEINumber(Context context) {
        String IMEINumber = "";
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        if(Build.VERSION.SDK_INT>=29){
            IMEINumber = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        else if(Build.VERSION.SDK_INT<29 && Build.VERSION.SDK_INT>=23) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    IMEINumber = telephonyMgr.getImei();
                } else {
                    IMEINumber = telephonyMgr.getDeviceId();
                }
            }
        }else{
            IMEINumber = telephonyMgr.getDeviceId();
        }
        return IMEINumber;
    }

    //// Added on 14-03-2019 ////
    public void SEND_SMS_WithBody(Context context,String MobNos,String messageBody){
        try {
            ///// Commented on 14-03-2019 bcoz SEND_SMS permission remove //////
			   /*int currentVer = android.os.Build.VERSION.SDK_INT;
			   System.out.println(MobCall+" "+messageBody+" "+currentVer);
			   if(currentVer>=19){
				   MobCall=MobCall.replace(",", "#").trim();
				   String [] temp=MobCall.split("#");
				   for(int i=0;i<temp.length;i++){
					   String numb=temp[i].toString().trim();
					   System.out.println(i+" : "+numb);
					   SmsManager smsManager = SmsManager.getDefault();
				       smsManager.sendTextMessage(numb, null, messageBody, null, null);
				       System.out.println("SMS sent.");
				       Toast.makeText(getApplicationContext(), "Your message has been send.", 1).show();
				   }
			   }else{*/
            String uri= "smsto:"+MobNos;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra("compose_mode", true);
            intent.putExtra( "sms_body", messageBody);
            context.startActivity(intent);
            //}
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Toast.makeText(context,"Sending SMS failed.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    //Added on 25-03-2020 (Call Phone)
    public void callOnPhone(Context context,String MobCall) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+MobCall));
            context.startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Toast.makeText(context, "CALL failed", Toast.LENGTH_SHORT).show();
        }
    }

    //Added on 25-03-2020 (Send SMS withOut SMSBody)
    public void callOnSms(Context context,String MobCall) {
        try {
            String uri= "smsto:"+MobCall;
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(uri));
            intent.putExtra("compose_mode", true);
            context.startActivity(intent);
        } catch (ActivityNotFoundException activityException) {
            Toast.makeText(context, "Sending SMS failed", Toast.LENGTH_SHORT).show();
        }
    }

    //Added on 25-03-2020 (Send EMail)
    public void callEmail(Context context,String Tomail,String ClubName){
        try{
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            String aEmailList[] = {Tomail};
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Group Manager ("+ClubName+")");
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
            context.startActivity(Intent.createChooser(emailIntent, "Send your email in:"));
        }catch(Exception ex){
            Toast.makeText(context, "Sending Email failed", Toast.LENGTH_SHORT).show();
        }
    }


    //Added on 25-03-2020 (Detect Url a make url a hyperlink clickable)
    public String Url_hyperlink(String val) {
        String UUrl="";
        Matcher matcher = urlPattern.matcher(val);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            // now you have the offsets of a URL match
            UUrl=val.substring(matchStart,matchEnd);
        }
        if(UUrl.length()>1){
            String NUrl="<a href='"+UUrl+"' target='_blank'> "+ UUrl + "</a>";
            val=val.replace(UUrl,NUrl);
        }
        return val.trim();
    }
}
