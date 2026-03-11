package pl.quanarie.halt.ride;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
  @Id
  private UUID id;

  private UUID passengerId;
  private UUID driverId;

  private Double startLat;
  private Double startLon;
  private Double endLat;
  private Double endLon;

  @Enumerated(EnumType.STRING)
  private RideStatus status;

  private BigDecimal price;

//  @Version
//  private Long version; // TODO: Optimistic Lock

  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    if (this.id == null) this.id = UUID.randomUUID();
  }
}
