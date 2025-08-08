package org.system;
import org.Entities.Customer;
import org.Entities.Rental;
import org.Entities.RentalDTO;
import org.Entities.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/VehicleRentalSystem")
public class Main_CRUD {

    private final EventServices eventServices;

    @Autowired
    public Main_CRUD(EventServices eventServices) {
        this.eventServices = eventServices;
    }


    @GetMapping("/watchlater/{custId}")
    public ResponseEntity<List<Vehicle>> getWatchLaterVehicles(@PathVariable Long custId) {
        Optional<Customer> optionalCustomer = eventServices.cust_by1(custId);
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Customer customer = optionalCustomer.get();
        String watchLater = customer.getWatchLater();

        if (watchLater == null || watchLater.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Long> vehicleIds = Arrays.stream(watchLater.split(","))
                .map(Long::parseLong)
                .toList();

        List<Vehicle> vehicles =eventServices.veh_by_List(vehicleIds);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(eventServices.veh_listing());
    }
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long vehicleId) {
        Optional<Vehicle> vehicle = eventServices.veh_by(vehicleId);
        return vehicle.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register/vehicle")
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventServices.veh_create(vehicle));
    }


    @PutMapping("/update/vehicle/{vehicle_id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable("vehicle_id") Long id, @RequestBody Vehicle vehicle) {
        Vehicle existing = eventServices.veh_by1(id);
        if (existing != null) {
            existing.setVehicle_model(vehicle.getVehicle_model());
            existing.setVehicle_type(vehicle.getVehicle_type());
            existing.setRc_no(vehicle.getRc_no());
            existing.setVehicle_available(vehicle.getVehicle_available());
            existing.setPrice(vehicle.getPrice());
            existing.setSeats(vehicle.getSeats());
            return ResponseEntity.ok(eventServices.veh_update(existing));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/vehicle/{veh_id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("veh_id") Long id) {
        eventServices.veh_delete(id);
        return ResponseEntity.noContent().build();
    }

    // Customer Endpoints
    @PostMapping("/watchlater")
    public ResponseEntity<String> addToWatchLater(@RequestBody Map<String, String> data) {
        Long custId = Long.parseLong(data.get("cust_id"));
        Long vehicleId = Long.parseLong(data.get("vehicle_id"));

        Optional<Customer> optionalCustomer = eventServices.cust_by1(custId);
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }

        Customer customer = optionalCustomer.get();
        String existing = customer.getWatchLater();

        Set<String> watchLaterSet = new HashSet<>();
        if (existing != null && !existing.isEmpty()) {
            watchLaterSet.addAll(Arrays.asList(existing.split(",")));
        }

        // Add new vehicle ID
        watchLaterSet.add(String.valueOf(vehicleId));
        customer.setWatchLater(String.join(",", watchLaterSet));

        eventServices.cust_create(customer);

        return ResponseEntity.ok("Vehicle added to Watch Later");
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = eventServices.cust_by(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @PostMapping("/register/customer")
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventServices.cust_create(customer));
    }

    @PutMapping("/update/customer/{cust_id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("cust_id") Long id, @RequestBody Customer customer) {
        Customer existing = eventServices.cust_by(id);
        if (existing != null) {
            existing.setCust_adhaar(customer.getCust_adhaar());
            existing.setCust_name(customer.getCust_name());
            existing.setOccupied(customer.getOccupied());
            existing.setPhone_no(customer.getPhone_no());
            existing.setLicense_no(customer.getLicense_no());
            return ResponseEntity.ok(eventServices.cust_update(existing));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/customer/{cust_id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("cust_id") Long id) {
        eventServices.cust_delete(id);
        return ResponseEntity.noContent().build();
    }

    // Rental Endpoints
    @GetMapping("/rentals")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(eventServices.getAllRentals());
    }

    @GetMapping("/rentals/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id) {
        Rental rental = eventServices.getRentalById(id);
        return rental != null ? ResponseEntity.ok(rental) : ResponseEntity.notFound().build();
    }

    @GetMapping("/rentals/vehicle/{vehicleId}")
    public ResponseEntity<List<RentalDTO>> getRentalHistoryByVehicleId(@PathVariable Long vehicleId) {
        List<Rental> rentals = eventServices.getRentalsByVehicleId(vehicleId);

        // Convert to DTO with customer name
        List<RentalDTO> rentalDTOs = rentals.stream().map(rental -> new RentalDTO(
                rental.getUser() != null ? rental.getUser().getCust_name() : "N/A",
                rental.getRentDate().toString(),
                rental.getReturnDate().toString()
        )).toList();

        return ResponseEntity.ok(rentalDTOs);
    }


    @PostMapping("/rentals")
    public ResponseEntity<Rental> registerRental(@RequestBody Rental rental) {
        if (rental.getRentDate() == null) rental.setRentDate(LocalDate.now());
        if (rental.getReturnDate() == null) rental.setReturnDate(rental.getRentDate().plusDays(1));

        int totalDays = (int) java.time.temporal.ChronoUnit.DAYS.between(
                rental.getRentDate(), rental.getReturnDate());
        rental.setTotalDays(totalDays);

        Long customerId = rental.getUser().getCust_id();
        Long vehicleId = rental.getVehicle().getVehicle_id();

        Rental savedRental = eventServices.createRental(rental, customerId, vehicleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRental);
    }

    // Available Vehicles Based on Date and Type
    @GetMapping("/available")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles(
            @RequestParam("rentdate") String rentDateStr,
            @RequestParam("returndate") String returnDateStr,
            @RequestParam("vehicletype") String vehicleType) {

        LocalDate rentDate = LocalDate.parse(rentDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate returnDate = LocalDate.parse(returnDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        List<Vehicle> availableVehicles = eventServices.getAvailableVehicles(vehicleType, rentDate, returnDate);
        return ResponseEntity.ok(availableVehicles);
    }
}