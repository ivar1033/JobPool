package e.ravi.jobpoolcorporate;

public class PostProfile {

   public String company_name;
 
    public String contact;
    public String emailid;

    public PostProfile(String company_name, String contact, String emailid) {
        this.company_name = company_name;
     
        this.contact = contact;
        this.emailid = emailid;
    }



    public PostProfile(String company_name, String contact) {
    }
}
