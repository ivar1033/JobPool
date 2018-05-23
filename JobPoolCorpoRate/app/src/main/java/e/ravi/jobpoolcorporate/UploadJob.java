package e.ravi.jobpoolcorporate;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UploadJob extends AppCompatActivity {

    EditText u_jobid,u_jobdesc,u_jobqual,u_salary,u_skills,u_location,u_contact,u_cname;


Button post;
Spinner expspinner,jobtypespinner,ug,pg;

String [] exp={"0 Years","1 Years","2 Years","3 Years","4 Years","5 Years","6 Years"};
String [] jobtype={"IT","NON-IT"};
String [] ugcourse={"BCA","Bcom","BBA","BTech"};
String [] pgcourse={"NO","MCA","MCom","MBA","MTech"};
TextView jobdate;

    FirebaseAuth mAuth;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_job);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        u_cname=(EditText)findViewById(R.id.et_cname);
        u_jobid=(EditText)findViewById(R.id.et_jobid);
        u_location=findViewById(R.id.et_joblocation);
        u_jobdesc=(EditText)findViewById(R.id.et_jobdesc);
        
        u_skills=(EditText)findViewById(R.id.et_Skills);
        u_salary=(EditText)findViewById(R.id.et_Salary);
        u_contact=(EditText)findViewById(R.id.et_contact);
        expspinner=(Spinner)findViewById(R.id.sp_exp);
        jobtypespinner=(Spinner)findViewById(R.id.sp_jobtype);
        ug=(Spinner)findViewById(R.id.sp_ug);
        pg=(Spinner)findViewById(R.id.sp_pg);

        jobdate=(TextView)findViewById(R.id.et_date);
       ImageView date=(ImageView)findViewById(R.id.date);
        ArrayAdapter<String> expadapter=new ArrayAdapter<>(UploadJob.this,android.R.layout.simple_list_item_1,exp);
        expspinner.setAdapter(expadapter);
        ArrayAdapter<String> jobtypeadapter=new ArrayAdapter<>(UploadJob.this,android.R.layout.simple_list_item_1,jobtype);
        jobtypespinner.setAdapter(jobtypeadapter);

        ArrayAdapter<String> ugadapter=new ArrayAdapter<>(UploadJob.this,android.R.layout.simple_list_item_1,ugcourse);
        ug.setAdapter(ugadapter);


        ArrayAdapter<String> pgadapter=new ArrayAdapter<>(UploadJob.this,android.R.layout.simple_list_item_1,pgcourse);
        pg.setAdapter(pgadapter);


        post=(Button)findViewById(R.id.btn_uploadjob);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(UploadJob.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date=dayOfMonth+"/"+(month+1)+"/"+year;
                        jobdate.setText(date);

                    }
                },year,month,day);
                dialog.show();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user=mAuth.getCurrentUser();

                String name=u_cname.getText().toString().trim();
                String jobid=u_jobid.getText().toString().trim();
                String location=u_location.getText().toString().trim();
                String jobdescription=u_jobdesc.getText().toString().trim();
                String experience=expspinner.getSelectedItem().toString().trim();
                String jobtype=jobtypespinner.getSelectedItem().toString().trim();
                String undergrad=ug.getSelectedItem().toString().trim();
                String postgrad=pg.getSelectedItem().toString().trim();
                String jobqualification=undergrad.toString()+","+postgrad.toString();
                String skills=u_skills.getText().toString().trim();
                String sa=u_salary.getText().toString().trim();
                String contact=u_contact.getText().toString().trim();
                String emailid = user.getEmail();
                String salary=sa+" "+"L/PA";
                String date=jobdate.getText().toString();
                String uid=user.getUid().toString();



                if (name.isEmpty()){
                    u_cname.setError("Name Required");
                    u_cname.requestFocus();
                    return;
                }

                if (jobid.isEmpty()){
                    u_jobid.setError("Job Id Required");
                    u_jobid.requestFocus();
                    return;
                } if (location.isEmpty()){
                    u_location.setError("Location Required");
                    u_location.requestFocus();
                    return;
                }
                if (jobdescription.isEmpty()){
                    u_jobdesc.setError("Job Description Required");
                    u_jobdesc.requestFocus();
                    return;
                }
                if (jobqualification.isEmpty()){
                    u_jobqual.setError("Job Id Required");
                    u_jobqual.requestFocus();
                    return;
                }
                if (skills.isEmpty()){
                    u_skills.setError("Job Id Required");
                    u_skills.requestFocus();
                    return;
                }

                if (salary.isEmpty()){
                    u_salary.setError("Job Id Required");
                    u_salary.requestFocus();
                    return;
                }
                if (contact.isEmpty()){
                    u_contact.setError("Job Id Required");
                    u_contact.requestFocus();
                    return;
                }

                PostJob pj=new PostJob(name,jobid,location,jobdescription,experience,jobqualification,skills,salary,contact,emailid,jobtype,date,uid);


                try {
                    databaseReference.child("Jobs").child(jobid).setValue(pj);
                }catch (Exception e){

                    Toast.makeText(UploadJob.this, "Error", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(UploadJob.this, "Job Posted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
