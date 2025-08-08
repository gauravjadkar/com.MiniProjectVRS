package org.repositories;

import org.Entities.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long>{
    @Query("""
    SELECT v FROM Vehicle v 
    WHERE v.vehicle_type = :type AND v.vehicle_available = true
    AND v.vehicle_id NOT IN (
        SELECT r.vehicle.vehicle_id FROM Rental r 
        WHERE r.rentDate <= :returnDate AND r.returnDate >= :rentDate
    )
""")
    List<Vehicle> findAvailableByTypeAndDate(
            @Param("type") String type,
            @Param("rentDate") LocalDate rentDate,
            @Param("returnDate") LocalDate returnDate
    );


}
