
package com.crmretail_es.modelClass;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PersonalInfo {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("reporting")
    @Expose
    private String reporting;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("sp_designation")
    @Expose
    private String spDesignation;
    @SerializedName("doj")
    @Expose
    private String doj;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("profile_image")
    @Expose
    private String profile_image;
    @SerializedName("emp_code")
    @Expose
    private String emp_code;
    @SerializedName("department")
    @Expose
    private String department;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReporting() {
        return reporting;
    }

    public void setReporting(String reporting) {
        this.reporting = reporting;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSpDesignation() {
        return spDesignation;
    }

    public void setSpDesignation(String spDesignation) {
        this.spDesignation = spDesignation;
    }

    public String getDoj() {
        return doj;
    }

    public void setDoj(String doj) {
        this.doj = doj;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }
}
