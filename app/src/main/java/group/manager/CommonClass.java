package group.manager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import static android.content.Context.TELEPHONY_SERVICE;

public class CommonClass {


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
}
