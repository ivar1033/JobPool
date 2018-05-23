package e.ravi.jobpool;

public class SaveUserProfile {

    public String name,city,address,pincode
            ,xmarks,xyear,xiimarks,xiiyear
            ,ugmarks,ugyear,pgmarks,pgyear
            ,skills,achievements,certifications,workexp,sex,ugcourse,pgcourse,email,dob;

    public SaveUserProfile(String name, String city, String address, String pincode, String xmarks, String xyear, String xiimarks, String xiiyear, String ugmarks, String ugyear, String pgmarks, String pgyear, String skills, String achievements, String certifications, String workexp, String sex, String ugcourse,String pgcourse,String email,String dob) {
        this.city = city;
        this.name = name;
        this.address = address;
        this.pincode = pincode;
        this.xmarks = xmarks;
        this.xyear = xyear;
        this.xiimarks = xiimarks;
        this.xiiyear = xiiyear;
        this.ugmarks = ugmarks;
        this.ugyear = ugyear;
        this.pgmarks = pgmarks;
        this.pgyear = pgyear;
        this.skills = skills;
        this.achievements = achievements;
        this.certifications = certifications;
        this.workexp = workexp;
        this.sex = sex;
        this.ugcourse = ugcourse;
        this.pgcourse = pgcourse;
        this.email=email;
        this.dob=dob;
    }

    public SaveUserProfile(){

    }
}
