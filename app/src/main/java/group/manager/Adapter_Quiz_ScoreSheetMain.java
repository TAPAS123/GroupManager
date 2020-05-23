package group.manager;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Adapter_Quiz_ScoreSheetMain extends ArrayAdapter<Row_Item_QuizScoreSheet_Main> {
    Context context;
    List<Row_Item_QuizScoreSheet_Main> items;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog ad;

    public Adapter_Quiz_ScoreSheetMain(Context context, int ResourceId, List<Row_Item_QuizScoreSheet_Main> items)
    {
        super(context, ResourceId, items);
        this.context = context;
        this.items=items;
    }

    public class ViewHolder {
        TextView txtSno,txtQues ,txtAns,txtCorrect,txtIncorrect;
        LinearLayout LvCorrect,LvInCorrect;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Row_Item_QuizScoreSheet_Main rowItem = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.listitem_quiz_scoresheet,parent,false);
            holder = new ViewHolder();
            holder.txtSno = (TextView) convertView.findViewById(R.id.txtSno);
            holder.txtQues = (TextView) convertView.findViewById(R.id.txtQues);
            holder.txtAns = (TextView) convertView.findViewById(R.id.txtAns);
            holder.txtCorrect = (TextView) convertView.findViewById(R.id.txtCorrect);
            holder.txtIncorrect = (TextView) convertView.findViewById(R.id.txtInCorrect);
            holder.LvCorrect = (LinearLayout) convertView.findViewById(R.id.LvCorrect);
            holder.LvInCorrect = (LinearLayout) convertView.findViewById(R.id.LvInCorrect);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtSno.setText(rowItem.Sno+"");
        holder.txtQues.setText(rowItem.Ques);
        holder.txtAns.setText(rowItem.Ans);
        holder.txtCorrect.setText("Correct: "+rowItem.Correct+"/"+rowItem.TotalMembers);
        holder.txtIncorrect.setText("Incorrect: "+rowItem.InCorrect+"/"+rowItem.TotalMembers);
        
        ///LinearLayout Correct answer Click
        holder.LvCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rowItem.Correct==0)
                {
                    alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("No one answered Correctly !!")
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    ad.dismiss();
                                }
                            });
                    ad = alertDialogBuilder.create();
                    ad.show();
                }
                else
                {
                    String CorrectIds = rowItem.CorrectIds;
                    Intent  intent = new Intent(context, OpinionPoll_MemberList.class);
                    intent.putExtra("Head", "The following answered correctly");
                    intent.putExtra("Mids", CorrectIds);
                    intent.putExtra("MTitle", rowItem.MTitle);
                    intent.putExtra("AppLogo", rowItem.AppLogo);
                    intent.putExtra("Mid", rowItem.Mid);
                    intent.putExtra("CFrom","1");//Comes from Score Sheet
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });

        holder.LvInCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rowItem.InCorrect==0)
                {
                    alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("No one answered Incorrectly !!")
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    ad.dismiss();
                                }
                            });
                    ad = alertDialogBuilder.create();
                    ad.show();
                }
                else
                {
                    String InCorrectIds = rowItem.InCorrectIds;
                    Intent  intent = new Intent(context, OpinionPoll_MemberList.class);
                    intent.putExtra("Head", "The following answered incorrectly");
                    intent.putExtra("Mids", InCorrectIds);
                    intent.putExtra("MTitle", rowItem.MTitle);
                    intent.putExtra("AppLogo", rowItem.AppLogo);
                    intent.putExtra("Mid", rowItem.Mid);
                    intent.putExtra("CFrom","1");//Comes from Score Sheet
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        });
        
        return convertView;
    }
}
