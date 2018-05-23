package e.ravi.jobpoolcorporate;

import android.content.Intent;
import android.net.Uri;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppliedCandidates extends AppCompatActivity {

    DatabaseReference myRef;
    RecyclerView rcv;
    AppliedcandiAdapter appliedcandiAdapter=new AppliedcandiAdapter();
    ArrayList<CandidateList> candilist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applied_candidates);

        final String jobid=getIntent().getExtras().getString("jobid").toUpperCase();


        myRef= FirebaseDatabase.getInstance().getReference("Users");

        rcv=(RecyclerView)findViewById(R.id.applied_rcv);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(AppliedCandidates.this);
        rcv.setLayoutManager(manager);



       myRef.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot uniquekeySnapshot : dataSnapshot.getChildren()){

                   for (DataSnapshot appliedjobSnapshot : uniquekeySnapshot.child("Appliedjob").getChildren()){


                       if (appliedjobSnapshot.getKey().equalsIgnoreCase(jobid)){
                           try {
                               String name = uniquekeySnapshot.child("name").getValue(String.class);
                               String emailid = uniquekeySnapshot.child("email").getValue(String.class);
                               String skills = uniquekeySnapshot.child("skills").getValue(String.class);
                               String workexp = uniquekeySnapshot.child("workexp").getValue(String.class);
                               String cv = uniquekeySnapshot.child("cv").child("url").getValue(String.class);
                               String videocv = uniquekeySnapshot.child("videocv").child("videocvurl").getValue(String.class);

                               CandidateList list = new CandidateList();

                               list.setName(name);
                               list.setEmail(emailid);
                               list.setSkills(skills);
                               list.setWorkexp(workexp);
                               list.setCv(cv);
                               list.setVideocv(videocv);

                               candilist.add(list);
                               if (candilist.size() > 0) {
                                   rcv.setAdapter(appliedcandiAdapter);
                               }
                           }catch (Exception e){

                           }
                       }

                   }

               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               Toast.makeText(AppliedCandidates.this, "Error", Toast.LENGTH_SHORT).show();
           }
       });



    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,skills,emailid,workexp;
        Button downcv,downvideocv;
        public MyViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.tv_name);
            
            emailid=itemView.findViewById(R.id.tv_email);
            
            skills=itemView.findViewById(R.id.tv_skills);
        
            workexp=itemView.findViewById(R.id.tv_workexp);
            downcv=itemView.findViewById(R.id.btn_downcv);
            downvideocv =itemView.findViewById(R.id.btn_showvideocv);


        }
    }

    public class  AppliedcandiAdapter extends RecyclerView.Adapter<MyViewHolder>{


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.appliedcandidatelist, parent, false);
            MyViewHolder holdercandi=new MyViewHolder(v);

            return holdercandi;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final CandidateList list = candilist.get(position);

            holder.name.setText(list.getName());
            
            holder.emailid.setText(list.getEmail());
            
            holder.skills.setText(list.getSkills());

            holder.workexp.setText(list.getWorkexp());
           
            holder.downcv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cv=list.getCv();
                    try {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(cv));
                        startActivity(intent);
                    }catch (Exception e){
                        String error="No Cv";
                        Toast.makeText(AppliedCandidates.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.downvideocv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String videocv=list.getVideocv();
                    if (videocv!=null) {
                        Intent intent = new Intent(AppliedCandidates.this, ShowVideoCv.class);
                        intent.putExtra("videocv", videocv);
                        startActivity(intent);
                    }else {
                        Toast.makeText(AppliedCandidates.this, "No Video Cv", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return candilist.size();
        }
    }
}
