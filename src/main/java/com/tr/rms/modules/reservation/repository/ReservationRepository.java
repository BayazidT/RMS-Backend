// src/main/java/com/tr/rms/modules/reservation/repository/ReservationRepository.java
package com.tr.rms.modules.reservation.repository;

import com.tr.rms.modules.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID>, JpaSpecificationExecutor<Reservation> {

    List<Reservation> findByReservationDateOrderByReservationTimeAsc(LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date AND r.tableNumber = :table")
    List<Reservation> findByDateAndTable(@Param("date") LocalDate date, @Param("table") Integer table);

    List<Reservation> findByUserId(UUID userId);
}