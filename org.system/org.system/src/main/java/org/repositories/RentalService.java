package org.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.Entities.Rental;

import java.util.List;

@Repository
public interface RentalService extends JpaRepository<Rental,Long>{


        @Query("SELECT r FROM Rental r WHERE r.vehicle.vehicle_id = :vehicleId")
        List<Rental> findByVehicleId(@Param("vehicleId") Long vehicleId);

}
