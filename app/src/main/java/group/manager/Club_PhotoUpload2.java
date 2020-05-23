package group.manager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Date;
import android.util.Base64;

public class Club_PhotoUpload2 extends Activity {

    String ClientId, ClubName, MTitle, Table4Name, Auth_ClubName, title = "", LogId = "", webResponse = "";
    byte[] AppLogo;
    TextView txtHead, txtAuthClubName;
    ImageView imgAddPic;
    Button btnSubmit;
    EditText edTitle;
    Context context = this;
    Chkconnection chkconn;
    WebServiceCall webcall;
    ProgressDialog Progsdial, pDialog;
    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;
    private String userChoosenTask;
    byte[] pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_photoupload2);

        txtHead = (TextView) findViewById(R.id.txtHead);
        txtAuthClubName = (TextView) findViewById(R.id.txtAuthClubName);
        edTitle = (EditText) findViewById(R.id.edTitle);
        imgAddPic = (ImageView) findViewById(R.id.imgAddPic);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Intent intent = getIntent();
        MTitle = intent.getStringExtra("MTitle");
        Auth_ClubName = intent.getStringExtra("Auth_ClubName");
        ClubName = intent.getStringExtra("Clt_ClubName");
        ClientId = intent.getStringExtra("UserClubName");
        LogId = intent.getStringExtra("Clt_LogID");
        AppLogo = intent.getByteArrayExtra("AppLogo");

        Table4Name = "C_" + ClientId + "_4";

        Set_App_Logo_Title(); // Set App Logo and Title

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        chkconn = new Chkconnection();
        webcall = new WebServiceCall();

        txtHead.setText(MTitle);  //set heading
        txtAuthClubName.setText(Auth_ClubName);/// Set Authorised ClubName
        Typeface face=Typeface.createFromAsset(getAssets(), "calibri.ttf");
        txtHead.setTypeface(face);

        imgAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SelectImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = edTitle.getText().toString().trim();
                if (title.length() == 0) {
                    AlertDisplay("Mandatory !","Please input title !",2);
                } else if (pic == null) {
                    AlertDisplay("Mandatory !","Please Add Photo !",2);
                } else {
                    boolean InternetPresent = chkconn.isConnectingToInternet(context);
                    if (InternetPresent == true) {
                        String Base64Image = getStringFromByteArray(pic);
                        String Code = GetCode();//Get Time Code
                        Sync_UploadClubPhotos_imageToM_S(Base64Image, Code);
                    } else {
                        AlertDisplay("Internet Connection", "No Internet Connection !",2);
                    }
                }
            }
        });
    }


    //Select Image Using Camera or Gallery
    private void SelectImage() {
        final CharSequence[] items = {"Take from Camera", "Select from Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;//Utility.checkPermission(this);

                if (items[item].equals("Take from Camera")) {
                    userChoosenTask = "Take from Camera";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Select from Gallery")) {
                    userChoosenTask = "Select from Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @SuppressLint("Override")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take from Camera"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Select from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    //Gallery Intent
    private void galleryIntent() {
	        /*Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        //intent.putExtra("crop", "true");
	        //intent.putExtra("aspectX", 0);
	        //intent.putExtra("aspectY", 0);
	        //intent.putExtra("outputX", 200);
	        //intent.putExtra("outputY", 150);
	        intent.putExtra("return-data", true);
	        startActivityForResult(Intent.createChooser(intent, "Select File"),PICK_FROM_GALLERY);*/

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_GALLERY);
    }

    //Camera Intent
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);//Added on 07-01-2018
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);//Added on 07-01-2018
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//Added on 07-01-2018
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FROM_GALLERY)
                SetImageByCamera_Gallery(data, 1);
            else if (requestCode == CAMERA_REQUEST)
                SetImageByCamera_Gallery(data, 2);
        }
    }


    //Set Image From Camera or Photo Gallery
    private void SetImageByCamera_Gallery(Intent intent, int Type) {
        Bitmap yourImage = null;
        if (Type == 1)//Gallery
        {
            ////Change 01-07-2017///

            try {
                final Uri imageUri = intent.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                yourImage = BitmapFactory.decodeStream(imageStream);

                //if(intent.getExtras()!=null){
                ///Comes from Gallery
                //  yourImage = intent.getExtras().getParcelable("data");
                //}else{
                //comes from media or other folders or memmory card
                //yourImage = MediaStore.Images.Media.getBitmap(this.getApplicationContext().getContentResolver(), intent.getData());
                //}

            } catch (OutOfMemoryError ex) {
                ex.printStackTrace();
                AlertDisplay("","Pic Error !",2);
            } catch (Exception e) {
                e.printStackTrace();
                AlertDisplay("","Pic Error !",2);
            }

        } else {
            //Take from Camera///
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            try {

                BitmapFactory.Options options = new BitmapFactory.Options();
                yourImage = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
                f.delete();
            } catch (Exception e) {
                AlertDisplay("","Pic Error !",2);
            }
        }


        if (yourImage != null) {
            int w = yourImage.getWidth();
            int h = yourImage.getHeight();
            //int p = yourImage.getByteCount();
            //int d = yourImage.getDensity();

            int RWidth = 600, RHeight = 600;
            if (w < RWidth) {
                RWidth = w;
            }

            if (h < RHeight) {
                RHeight = h;
            }

            //Resize the Original Image
            Bitmap ResizeImg = ScaleDownBitmap(yourImage, RWidth, RHeight, true);
            //int w1 = ResizeImg.getWidth();
            //int h1 = ResizeImg.getHeight();
            //int p1=ResizeImg.getByteCount();
            //int d1=ResizeImg.getDensity();

            // convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ResizeImg.compress(Bitmap.CompressFormat.PNG, 90, stream);

            int k = stream.size();
            int p2 = ResizeImg.getByteCount();
            pic = stream.toByteArray();
            imgAddPic.setImageBitmap(ResizeImg);
        } else {
            AlertDisplay("","Pic Error !",2);
        }
    }


    //Rezise Image Size Wise
    private Bitmap ScaleDownBitmap(Bitmap originalImage, float maxWidth, int maxHeight, boolean filter) {
        float ratio = Math.min((float) maxWidth / originalImage.getWidth(), (float) maxHeight / originalImage.getHeight());
        int width = (int) Math.round(ratio * (float) originalImage.getWidth());
        int height = (int) Math.round(ratio * (float) originalImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(originalImage, width, height, filter);
        return newBitmap;
    }

    //Resize Image with Wanted Width And Wanted Height Wise
    private Bitmap ScaleBitmap(Bitmap originalImage, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / originalImage.getWidth(), (float) wantedHeight / originalImage.getHeight());
        canvas.drawBitmap(originalImage, m, new Paint());
        return output;
    }


    private void Upload_ClubPhoto_Data(final String imgName) {
        pDialog.setMessage("Please Wait....");
        showDialog();
        Thread networkThread = new Thread() {
            public void run() {
                try {
                    webResponse = webcall.Club_PhotoUploadData(ClientId, Auth_ClubName, LogId, title, imgName);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (webResponse.contains("Saved")) {
                                AlertDisplay("Result", "Record Saved Successfully !",1);
                            } else {
                                AlertDisplay("Result","Something went wrong. Please try again !",2);
                            }
                        }
                    });
                    hideDialog();
                } catch (Exception localException) {
                    System.out.println(localException.getMessage());
                }
            }
        };
        networkThread.start();
    }


    private void Sync_UploadClubPhotos_imageToM_S(final String ImageStr, final String Image_No) {

        String tag_string_req = "req_upload";

        pDialog.setMessage("Please Wait....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, "http://mybackup.co.in/GM_Image_Uplaod.aspx", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                String Result = response.toString();

                if (Result.equals("1")) {
                    String imgName = ClientId + "_0_" + Image_No + ".jpg";
                    Upload_ClubPhoto_Data(imgName);//Upload ClubPhoto Data with image name
                } else if (Result.contains("Error:")) {
                    hideDialog();
                    AlertDisplay("Error ","Something went wrong. Please try again !",2);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    //Toast.makeText(getApplicationContext(), "Internet Connection Unavailable", Toast.LENGTH_LONG).show();
                    AlertDisplay("Internet Connection", "No Internet Connection !",2);
                }
                AlertDisplay("Error ","Something went wrong. Please try again !",2);
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("ITI_Code", ClientId);
                params.put("QNo", "0");
                params.put("Image", ImageStr);
                params.put("Image_No", Image_No);

                return params;
            }

        };

        //Method to limit retry policy of request
        strReq.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq);
    }


    /*** This functions converts Byte array to a string**/
    private String getStringFromByteArray(byte[] b) {

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    private String GetCode() {
        Date cdate = new Date();//Current Date
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyddHHMMss");
        String dt = sdf1.format(cdate);
        return dt;
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



    private void Set_App_Logo_Title() {
        setTitle(ClubName); // Set Title
        // Set App LOGO
        if (AppLogo == null) {
            getActionBar().setIcon(R.drawable.ic_launcher);
        } else {
            Bitmap bitmap = BitmapFactory.decodeByteArray(AppLogo, 0, AppLogo.length);
            BitmapDrawable icon = new BitmapDrawable(getResources(), bitmap);
            getActionBar().setIcon(icon);
        }
    }

    private void AlertDisplay(String head, String body,final int chk) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setTitle(Html.fromHtml("<font color='#E3256B'>" + head + "</font>"));
        ad.setMessage(Html.fromHtml("<font color='#1C1CF0'>" + body + "</font>"));
        ad.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if(chk==1)
                    GoBack();
                else
                   dialog.dismiss();

            }
        });
        ad.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void GoBack() {
        finish();
    }
}