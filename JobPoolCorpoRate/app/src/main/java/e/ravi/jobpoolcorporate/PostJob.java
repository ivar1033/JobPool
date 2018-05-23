package e.ravi.jobpoolcorporate;

public class PostJob {
    public  String name;
    public String jobid;
    public String location;
    public String jobdescription;
    public String experience;
    public String jobqualification;
    public String skills;
    public String salary;
    public String contact;
    public String emailid;
    public String jobtype;
    public String date;
    public String uid;


    public PostJob(String name,String jobid, String location, String jobdescription, String experience, String jobqualification, String skills, String salary, String contact, String emailid,String jobtype,String date,String uid) {

        this.name = name;
        this.jobid = jobid;
        this.location = location;
        this.jobdescription = jobdescription;
        this.experience = experience;
        this.jobqualification = jobqualification;
        this.skills = skills;
        this.salary = salary;
        this.contact = contact;
        this.emailid = emailid;
        this.jobtype = jobtype;
        this.date = date;
        this.uid = uid;
    }

}
