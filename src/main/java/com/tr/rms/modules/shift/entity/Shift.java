package com.tr.rms.modules.shift.entity;


import com.tr.rms.modules.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Table(name = "shifts",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "shift_date"}))
@Getter @Setter @NoArgsConstructor
@ToString(exclude = "user")
public class Shift {

    @Id
    @GeneratedValue
    @Column(name = "shift_id", updatable = false, columnDefinition = "uuid")
    private UUID shiftId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;    // stored with zone offset (TIMESTAMPTZ)

    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    // Helper to get just the time part in restaurant timezone (for display)
    public LocalTime getStartTimeLocal() {
        return startTime.atZoneSameInstant(ZoneId.of(getRestaurantTimezone())).toLocalTime();
    }

    public LocalTime getEndTimeLocal() {
        return endTime.atZoneSameInstant(ZoneId.of(getRestaurantTimezone())).toLocalTime();
    }

    // You need a tiny service or @EntityGraph to fetch this when needed
    private String getRestaurantTimezone() {
        // In real code: inject RestaurantService or use static singleton
        return "Europe/Berlin";
    }
}