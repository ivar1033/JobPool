package e.ravi.jobpool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

FirebaseAuth mAuth;
    DatabaseReference myRef,checkRef;
    RecyclerView rcv;
    MyAdapter adapter=new MyAdapter();
    ArrayList<JobList> joblist=new ArrayList<>();
    CheckBox it,nonit;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        final EditText searchquery = (EditText) findViewById(R.id.searchquery);
        ImageView search = (ImageView) findViewById(R.id.search);
        final CheckBox it = (CheckBox) findViewById(R.id.it);
        final CheckBox noit = (CheckBox) findViewById(R.id.nonit);

        ImageView referesh=(ImageView)findViewById(R.id.referesh);

        //TextView email=(TextView)findViewById(R.id.profileemail);
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        checkRef=FirebaseDatabase.getInstance().getReference("Users"+"/"+user.getUid()+"/");
        checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue(String.class);
                if (name==null){
                    Intent intent=new Intent(ProfileActivity.this,EditProfile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








        myRef = FirebaseDatabase.getInstance().getReference("Jobs");


        myRef.keepSynced(true);

        rcv = (RecyclerView) findViewById(R.id.rcv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ProfileActivity.this);
        rcv.setLayoutManager(manager);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        referesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getIntent());
            }
        });



        


            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                    String company_name = dataSnapshot.child("name").getValue(String.class).toUpperCase();
                    String jobid = dataSnapshot.child("jobid").getValue(String.class).toUpperCase();
                    String contact = dataSnapshot.child("contact").getValue(String.class).toUpperCase();
                    String emailid = dataSnapshot.child("emailid").getValue(String.class).toUpperCase();
                    String experience = dataSnapshot.child("experience").getValue(String.class).toUpperCase();
                    String jobdescription = dataSnapshot.child("jobdescription").getValue(String.class).toUpperCase();
                    String jobqualification = dataSnapshot.child("jobqualification").getValue(String.class).toUpperCase();
                    String salary = dataSnapshot.child("salary").getValue(String.class).toUpperCase();
                    String skills = dataSnapshot.child("skills").getValue(String.class).toUpperCase();
                    String location = dataSnapshot.child("location").getValue(String.class).toUpperCase();
                    String jobtype = dataSnapshot.child("jobtype").getValue(String.class);
                    String date = dataSnapshot.child("date").getValue(String.class);


                    JobList data = new JobList();

                    data.setJobid(jobid);
                    data.setContact(contact);
                    data.setCompany_name(company_name);
                    data.setEmailid(emailid);
                    data.setExperience(experience);
                    data.setJobdescription(jobdescription);
                    data.setJobqualification(jobqualification);
                    data.setSalary(salary);
                    data.setSkills(skills);
                    data.setLocation(location);
                    data.setJobtype(jobtype);
                    data.setDate(date);

                    joblist.add(data);

                    if (joblist.size() > 0) {

                        rcv.setAdapter(adapter);
                    }


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = searchquery.getText().toString();
                if (s.isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "No Result", Toast.LENGTH_SHORT).show();

                } else {
                    filter(s);
                }
            }
        });
        it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                
                noit.setChecked(false);
                String it = "IT";
                filterjob(it);
            }
        });
        noit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               
                it.setChecked(false);
                String nonit = "Non-IT";
                filter(nonit);
            }
        });

    }



    private void filterjob(String text){

        ArrayList<JobList> filteredlist=new ArrayList<>();

        for(JobList item : joblist){



            if (item.getJobtype().toLowerCase().equalsIgnoreCase(text) ){

                filteredlist.add(item);
            }




        }



        adapter.filterlist(filteredlist);



    }

    private void filter(String text){

        ArrayList<JobList> filteredlist=new ArrayList<>();

        for(JobList item : joblist){



            if (item.getLocation().toLowerCase().contains(text.toLowerCase()) ){

                filteredlist.add(item);
            }
            else if (item.getCompany_name().toLowerCase().contains(text.toLowerCase()) ){

                filteredlist.add(item);
            }
            else if (item.getJobdescription().toLowerCase().contains(text.toLowerCase()) ){

                filteredlist.add(item);
            }
            else if (item.getJobtype().toLowerCase().contains(text.toLowerCase())){
                filteredlist.add(item);
            }


        }



        adapter.filterlist(filteredlist);



    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView company_name
                ,jobdescription,jobid,location,jobtype;
        public MyViewHolder(View itemView) {
            super(itemView);

            company_name=itemView.findViewById(R.id.tv_name);
            
            jobdescription=itemView.findViewById(R.id.tv_jobdescription);
            jobid=itemView.findViewById(R.id.tv_jobid);
           
            location=itemView.findViewById(R.id.tv_location);
            jobtype=itemView.findViewById(R.id.tv_jobtype);

        }
    }

    public  class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getLayoutInflater();
            View v=inflater.inflate(R.layout.joblist,parent,false);
            MyViewHolder holder=new MyViewHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
                final JobList list=joblist.get(position);

            holder.company_name.setText(list.getCompany_name());
            
                holder.jobdescription.setText(list.getJobdescription());
                holder.jobid.setText(list.getJobid());
                holder.location.setText(list.getLocation());
                holder.jobtype.setText(list.getJobtype());
              
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            String company_name=list.getCompany_name();
                            String contact=list.getContact();
                            String emailid=list.getEmailid();
                            String experience=list.getExperience();
                            String jobdescription=list.getJobdescription();
                            String jobid=list.getJobid();
                            String jobqualification=list.getJobqualification();
                            String salary=list.getSalary();
                            String skills=list.getSkills();
                            String location=list.getLocation();
                            String jobtype=list.getJobtype();
                            String date=list.getDate();

                            Intent intent=new Intent(ProfileActivity.this,ApplyJob.class);
                            intent.putExtra("company_name",company_name);
                            intent.putExtra("contact",contact);
                            intent.putExtra("emailid",emailid);
                            intent.putExtra("experience",experience);
                            intent.putExtra("jobdescription",jobdescription);
                            intent.putExtra("jobid",jobid);
                            intent.putExtra("jobqualification",jobqualification);
                            intent.putExtra("salary",salary);
                            intent.putExtra("skills",skills);
                            intent.putExtra("location",location);
                            intent.putExtra("jobtype",jobtype);
                            intent.putExtra("date",date);

                            startActivity(intent);





                }
            });

        }

        @Override
        public int getItemCount() {
            return joblist.size();
        }

        public void filterlist(ArrayList<JobList> filteredlist){

            joblist =filteredlist;
            adapter.notifyDataSetChanged();
        }
    }


















    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){

            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(this, "Logout Successfull", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                break;


        }


       return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            startActivity(new Intent(this,EditProfile.class));
        } else if (id == R.id.nav_show) {
            startActivity(new Intent(this,ShowProfile.class));
        } else if (id == R.id.nav_appliedjob) {
            startActivity(new Intent(ProfileActivity.this,AppliedJob.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
