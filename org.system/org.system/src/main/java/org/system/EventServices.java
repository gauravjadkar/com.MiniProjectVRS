package org.system;

import org.Entities.Customer;
import org.Entities.Rental;
import org.Entities.Vehicle;
import org.repositories.CustomerRepo;
import org.repositories.RentalService;
import org.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventServices {
    @Autowired
    VehicleRepository vehicle;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    RentalService rent;


    EventServices(VehicleRepository vehicle, CustomerRepo customerRepo,RentalService Rent)
    {
        this.vehicle=vehicle;
        this.customerRepo=customerRepo;
        this.rent=Rent;
    }

    public Rental createRental(Rental rental, Long customerId, Long vehicleId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Vehicle vehi = vehicle.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        rental.setUser(customer);
        rental.setVehicle(vehi);

        return rent.save(rental);
    }
    //Create Section

    Customer cust_create(Customer cust)
    {
        customerRepo.save(cust);
        return cust;
    }
    Vehicle veh_create(Vehicle veh)
    {
        vehicle.save(veh);
        return veh;
    }
    Rental rent_create(Rental Rent){rent.save(Rent);
       return Rent;}




//Read Section


    public List<Rental> getRentalsByVehicleId(Long vehicleId) {
        return rent.findByVehicleId(vehicleId);
    }

    public Rental getRentalById(Long id) {
        Optional<Rental> rental = rent.findById(id);
        System.out.println("Looking for rental with ID: " + id + " → Found: " + rental.isPresent());
        return rent.findById(id).orElse(null);
    }
    List<Vehicle>  veh_listing()
    {
        return vehicle.findAll();
    }

    Optional<Vehicle>  veh_by(Long Id)
    {
        return vehicle.findById(Id);
    }
    Vehicle veh_by1(Long Id)
    {
        return  vehicle.findById(Id).orElse(null);
    }
    List<Vehicle> veh_by_List(List<Long> ids)
    {
        return vehicle.findAllById(ids);
    }

     Customer cust_by(Long Id)
     {
         return customerRepo.findById(Id).orElse(null);
     }
     Optional<Customer> cust_by1(Long Id)
     {
         return customerRepo.findById(Id);
     }

     Optional<Rental> rent_read(Long id){return
             rent.findById(id);}

    List<Rental> getAllRentals(){
        return rent.findAll();
    }


    public List<Vehicle> getAvailableVehicles(String type, LocalDate rentDate, LocalDate returnDate) {
        return vehicle.findAvailableByTypeAndDate(type, rentDate, returnDate);
    }


//Updation Section

    Customer cust_update(Customer cust)
    {
      customerRepo.save(cust);
      return  cust;

    }
   Vehicle veh_update(Vehicle veh)
   {
       vehicle.save(veh);
       return veh;
   }

   //Deletion Section

    void cust_delete(Long Id)
    {
        customerRepo.deleteById(Id);
    }
    void  veh_delete(Long Id)
    {
        vehicle.deleteById(Id);
    }
}
