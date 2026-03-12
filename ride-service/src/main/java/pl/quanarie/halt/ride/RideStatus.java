package pl.quanarie.halt.ride;

public enum RideStatus {
  /** Pasażer wysłał prośbę o przejazd. System oczekuje na weryfikację płatności. */
  REQUESTED,

  /** Płatność została zweryfikowana. System aktywnie szuka dostępnego kierowcy w okolicy. */
  MATCHING,

  /** Kierowca zaakceptował zlecenie i jest w drodze do punktu początkowego. */
  ACCEPTED,

  /** Kierowca dotarł na miejsce startu i oczekuje na pasażera. */
  ARRIVED,

  /** Pasażer jest w pojeździe, a kierowca rozpoczął realizację trasy. */
  IN_PROGRESS,

  /** Przejazd został pomyślnie zakończony w punkcie docelowym. */
  COMPLETED,

  /** Przejazd został przerwany przez pasażera, kierowcę lub system przed zakończeniem. */
  CANCELLED
}
