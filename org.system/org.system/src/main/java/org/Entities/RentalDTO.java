package org.Entities;



public class RentalDTO {
    private String cust_name;
    private String rentdate;
    private String returndate;

    // Constructor
    public RentalDTO(String cust_name, String rentdate, String returndate) {
        this.cust_name = cust_name;
        this.rentdate = rentdate;
        this.returndate = returndate;
    }

    // Getters
    public String getCust_name() {
        return cust_name;
    }

    public String getRentdate() {
        return rentdate;
    }

    public String getReturndate() {
        return returndate;
    }

    // Setters (optional)
    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public void setRentdate(String rentdate) {
        this.rentdate = rentdate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }
}

