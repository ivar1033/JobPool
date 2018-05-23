package e.ravi.jobpool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ApplyJob extends AppCompatActivity {

    TextView location,company_name,contact,emailid,salary,jobdescription,jobqualification,experience,jobid,skills,jobtype,date;
    Button apply;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);


        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();





        company_name=findViewById(R.id.tv_name);
        location=findViewById(R.id.tv_location);
        contact=findViewById(R.id.tv_contact);
        emailid=findViewById(R.id.tv_emailid);
        salary=findViewById(R.id.tv_salary);
        jobdescription=findViewById(R.id.tv_jobdescription);
        jobqualification=findViewById(R.id.tv_jobqualification);
        experience=findViewById(R.id.tv_experience);
        jobid=findViewById(R.id.tv_jobid);
        skills=findViewById(R.id.tv_skills);
        apply=findViewById(R.id.btn_apply);
        jobtype=findViewById(R.id.tv_jobtype);
        date=findViewById(R.id.tv_date);

        final String c_name=getIntent().getExtras().getString("company_name");
        final String c_contact=getIntent().getExtras().getString("contact");
        final String c_emailid=getIntent().getExtras().getString("emailid");
        final String c_salary=getIntent().getExtras().getString("salary");
        final String c_jobdescription=getIntent().getExtras().getString("jobdescription");
        final String c_jobqualification=getIntent().getExtras().getString("jobqualification");
        final String c_experience=getIntent().getExtras().getString("experience");
        final String c_jobid=getIntent().getExtras().getString("jobid");
        final String c_skills=getIntent().getExtras().getString("skills");
        final String c_location=getIntent().getExtras().getString("location");
        final String c_jobtype=getIntent().getExtras().getString("jobtype");
        final String c_date=getIntent().getExtras().getString("date");

        company_name.setText(c_name);
        contact.setText(c_contact);
        emailid.setText(c_emailid);
        salary.setText(c_salary);
        jobqualification.setText(c_jobqualification);
        jobdescription.setText(c_jobdescription);
        experience.setText(c_experience);
        jobid.setText(c_jobid);
        skills.setText(c_skills);
        location.setText(c_location);
        jobtype.setText(c_jobtype);
        date.setText(c_date);


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            String ug = dataSnapshot.child("Users").child(user.getUid()).child("ugcourse").getValue(String.class);
                            String pg = dataSnapshot.child("Users").child(user.getUid()).child("pgcourse").getValue(String.class);
                            String exp = dataSnapshot.child("Users").child(user.getUid()).child("workexp").getValue(String.class);
                            String reqexp = c_experience.toString();

                            String check_exp = exp.replaceAll("[^0-9]", "");
                            String check_reqexp = reqexp.replaceAll("[^0-9]", "");
                            int inexp = Integer.parseInt(check_exp);
                            int inreqexp = Integer.parseInt(check_reqexp);

                            String qual = ug + "," + pg;
                            String requal = c_jobqualification.toString();

                            if (qual.equalsIgnoreCase(requal) && inexp >= inreqexp) {
                                JobList applyjob = new JobList();

                                applyjob.setCompany_name(c_name);
                                applyjob.setContact(c_contact);
                                applyjob.setEmailid(c_emailid);
                                applyjob.setSalary(c_salary);
                                applyjob.setLocation(c_location);
                                applyjob.setJobqualification(c_jobqualification);
                                applyjob.setJobdescription(c_jobdescription);
                                applyjob.setExperience(c_experience);
                                applyjob.setJobid(c_jobid);
                                applyjob.setSkills(c_skills);
                                applyjob.setJobtype(c_jobtype);
                                applyjob.setDate(c_date);


                                databaseReference.child("Users").child(user.getUid()).child("Appliedjob").child(c_jobid).setValue(applyjob);

                                Toast.makeText(ApplyJob.this, "Job Applied", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ApplyJob.this, "Not Eligible For This Job", Toast.LENGTH_SHORT).show();
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
    }
}
