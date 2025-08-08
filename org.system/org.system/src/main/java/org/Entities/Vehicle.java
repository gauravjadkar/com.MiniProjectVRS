package org.Entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Vehicle {
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "veh_id")
    @SequenceGenerator(name="veh_id",allocationSize = 1,initialValue =1000)
  private   Long vehicle_id;

    @Column
    private String vehicle_model;

    @Column
  private  String vehicle_type;

    @Column
   private Boolean vehicle_available;


    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Rental> rentals;


    @Column
    private String Rc_no;


     @Column
   private  double price;

     @Column
    private int seats;

     @Column
     private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(Long vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public Boolean getVehicle_available() {
        return vehicle_available;
    }

    public void setVehicle_available(Boolean vehicle_available) {
        this.vehicle_available = vehicle_available;
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(List<Rental> rentals) {
        this.rentals = rentals;
    }

    public String getRc_no() {
        return Rc_no;
    }

    public void setRc_no(String rc_no) {
        this.Rc_no = rc_no;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicle_id=" + vehicle_id +
                ", vehicle_model='" + vehicle_model + '\'' +
                ", vehicle_type='" + vehicle_type + '\'' +
                ", vehicle_available=" + vehicle_available +
                ", rentals=" + rentals +
                ", Rc_no='" + Rc_no + '\'' +
                ", price=" + price +
                ", seats=" + seats +
                '}';
    }
}
