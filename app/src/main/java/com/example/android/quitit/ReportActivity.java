package com.example.android.quitit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;

/**
 * Created by Ayush vaid on 02-07-2017.
 */

public class ReportActivity extends AppCompatActivity {

    private Entry ClickedEntry;
    private String disp;
    private StorageReference mStorageReference;
    private StorageReference mClikedEntryPhoto;
    private File direct;
    private File image;
    private Uri uri;
    private boolean directorymade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //Receiving bundle
        Bundle B = this.getIntent().getExtras();
        ClickedEntry = B.getParcelable("ClickedEntry");

        final ImageView $face_image = (ImageView) findViewById(R.id.report_activity_image);
        final TextView $image_text = (TextView) findViewById(R.id.image_text);

        final TextView $progress_text_view = (TextView) findViewById(R.id.report_progress_text_view);
        final LinearLayout $progress_parent = (LinearLayout) findViewById(R.id.report_progress_parent);
        final ProgressBar $progress_bar = (ProgressBar) findViewById(R.id.report_progress_bar);
        $progress_bar.setVisibility(View.INVISIBLE);
        $progress_parent.setVisibility(View.INVISIBLE);
        $progress_text_view.setVisibility(View.INVISIBLE);


        if(!ClickedEntry.getImageUri().equals("")) {
            $progress_bar.setScaleY(3f);
            $progress_bar.setScaleX(5f);
            $progress_bar.setVisibility(View.VISIBLE);
            $progress_parent.setVisibility(View.VISIBLE);
            $progress_text_view.setVisibility(View.VISIBLE);
            $progress_parent.getBackground().setAlpha(200);
            mStorageReference = FirebaseMethods.getFirbaseStorageReference("patient_photos");
            uri = Uri.parse(ClickedEntry.getImageUri());
            mClikedEntryPhoto = mStorageReference.child(uri.getLastPathSegment());
            direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QUIT_IT");
            if (!direct.exists()) {
                directorymade = direct.mkdirs();
            }
            if (direct.exists() || directorymade) {
                image = new File(direct, uri.getLastPathSegment());
                mClikedEntryPhoto.getFile(image).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * (taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        Log.e("UPLOADING", "progres");
                        $progress_bar.setProgress((int) progress);
                    }
                }).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        $progress_bar.setVisibility(View.GONE);
                        $progress_parent.setVisibility(View.GONE);
                        $progress_text_view.setVisibility(View.GONE);
                        Toast.makeText(getBaseContext(), "Download Completed", Toast.LENGTH_SHORT);
                        Bitmap bitmap = BitmapFactory.decodeFile(image.getPath());
                        $face_image.setImageBitmap(bitmap);
                        Log.e("SUCCESS", "image Shown in ImageView Successfully.");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Error while Downloading the image.", Toast.LENGTH_SHORT);
                        Log.e("FALIURE", "Booooo Booooo.");
                        $face_image.setVisibility(View.GONE);
                        $image_text.setVisibility(View.GONE);
                    }
                });
            }
        }
        else{
            $progress_bar.setScaleY(3f);
            $progress_bar.setScaleX(5f);
            $progress_bar.setVisibility(View.VISIBLE);
            $progress_parent.setVisibility(View.VISIBLE);
            $progress_text_view.setVisibility(View.VISIBLE);
            $progress_bar.setVisibility(View.GONE);
            $progress_parent.setVisibility(View.GONE);
            $progress_text_view.setVisibility(View.GONE);
            $progress_parent.getBackground().setAlpha(200);
            $face_image.setVisibility(View.INVISIBLE);
            $image_text.setVisibility(View.INVISIBLE);
        }

        //patient name
        TextView Name = (TextView) findViewById(R.id.nameView);
        Name.setText(ClickedEntry.getName());

        //patient gender
        TextView Sex = (TextView) findViewById(R.id.sexView);
        Sex.setText(ClickedEntry.getSex());

        //patient age
        TextView Age = (TextView) findViewById(R.id.ageView);
        Age.setText(Integer.toString(ClickedEntry.getAge()));

        //patient business
        TextView business = (TextView) findViewById(R.id.bussinessView);
        business.setText(ClickedEntry.getBusiness());

        //patient marital status
        TextView marry=(TextView) findViewById(R.id.marrital_statusView);
        marry.setText(ClickedEntry.getMarry_status());

        //how much patient smoke in a day
        TextView smokes_day=(TextView) findViewById(R.id.smokeConsumptionView);
        smokes_day.setText(Integer.toString(ClickedEntry.getSmoke_freq()));

        //how much patient chews in a day
        TextView chews_day=(TextView) findViewById(R.id.chewConsumptionView);
        chews_day.setText(Integer.toString(ClickedEntry.getChew_freq()));

        //for fraction of salary
        float spent=(ClickedEntry.getSmoke_freq()+ClickedEntry.getChew_freq())*30*((float)(ClickedEntry.getSmoke_cost()));
        float earn=(float) (ClickedEntry.getSalary());
        float save=earn-spent;
        float percent=((float)spent/earn)*100;

        disp=Float.toString(percent)+"%";
        TextView fSpenView=(TextView) findViewById(R.id.fractionSpentView);
        fSpenView.setText(disp);

        TextView personal_message =(TextView) findViewById(R.id.personalisedMessage);
        personal_message.setText(ClickedEntry.getMessage());

        Button printButton = (Button) findViewById(R.id.print_report_button);
        printButton.setOnClickListener(new Button.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               printDocument(v);
                                           }
                                       }
        );
        Button sendButton = (Button) findViewById(R.id.send_msg_button);
        sendButton.setOnClickListener(new Button.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent=new Intent(Intent.ACTION_SENDTO);
                                               intent.setData(Uri.parse("mailto:"));
                                               intent.putExtra(Intent.EXTRA_EMAIL  , new String[]{ClickedEntry.getEmail()});
                                               intent.putExtra(Intent.EXTRA_TEXT,ClickedEntry.getMessage());
                                               intent.putExtra(Intent.EXTRA_SUBJECT,"Harms of tobacco");
                                               if(intent.resolveActivity(getPackageManager())!=null){
                                                   startActivity(intent);
                                               }
                                           }
                                       }
        );
    }

    public void printDocument(View view)
    {
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = this.getString(R.string.app_name) +
                " Document";

        printManager.print(jobName, new MyPrintDocumentAdapter(this,ClickedEntry,disp),null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete? This record will be removed permanently from the Database.")
                       // .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                deletepatient();
                                Intent i = new Intent(ReportActivity.this, MainActivity.class);
                                startActivity(i);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

                return true;
            case R.id.view_all:
                Intent intent = new Intent(ReportActivity.this,ViewActivity.class);

              /*  Entry(String name,int age,String sex,String interest,String med,String contact,int chew_days,int chew_freq,float chew_cost,int smoke_days,int smoke_freq,float smoke_cost,String marry_status,
               String future,String business,int salary,String time,String date,
                    String morning_status,String family_status,String habit_reason,String habit,String aware_status,String aware_diseases,String quit_status,String quit_reason,String quit_before_status,String craving_time,int id){
                */

                Entry temp = ClickedEntry;
                Bundle B = new Bundle();

                //passing bundle
                B.putParcelable("ClickedEntry", (Parcelable) temp);
                intent.putExtras(B);
                startActivity(intent);
                finish();
                return true;


            case R.id.update:
                Intent intent2 = new Intent(ReportActivity.this,UpdateActivity.class);
                Entry temp2 = ClickedEntry;
                Bundle B1 = new Bundle();
                B1.putParcelable("ClickedEntry", (Parcelable) temp2);
                intent2.putExtras(B1);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletepatient() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query nameQuery = ref.child("doctors").child(MainActivity.currentdoctorKey).child("patients").orderByChild("name").equalTo(ClickedEntry.getName());

        nameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
