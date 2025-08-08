package org.Entities;
import jakarta.persistence.*;

@Entity
public class Customer {
    @Column
    @Id
    @SequenceGenerator(name = "custom_seq",initialValue = 3000,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="custom_seq")
    Long cust_id;

    @Column
    String cust_name;

    @Column
    Long license_no;

    @Column
    Long phone_no;

    @Column
    Boolean  occupied=false;

    @Column
    Long cust_adhaar;

     @Column
     String pass;

    @Column(name = "watch_later")
    private String watchLater;

    public String getWatchLater() {
        return watchLater;
    }

    public void setWatchLater(String watchLater) {
        this.watchLater = watchLater;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Long getCust_id() {
        return cust_id;
    }

    public void setCust_id(Long cust_id) {
        this.cust_id = cust_id;
    }

    public Long getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(Long phone_no) {
        this.phone_no = phone_no;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public Long getLicense_no() {
        return license_no;
    }

    public void setLicense_no(Long license_no) {
        this.license_no = license_no;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public Long getCust_adhaar() {
        return cust_adhaar;
    }

    public void setCust_adhaar(Long cust_adhaar) {
        this.cust_adhaar = cust_adhaar;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cust_id=" + cust_id +
                ", cust_name='" + cust_name + '\'' +
                ", license_no=" + license_no +
                ", phone_no=" + phone_no +
                ", occupied=" + occupied +
                ", cust_adhaar=" + cust_adhaar +
                '}';
    }
}
