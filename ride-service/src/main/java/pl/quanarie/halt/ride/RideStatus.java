package pl.quanarie.halt.ride;

public enum RideStatus {
  REQUESTED,   // Pasażer wysłał prośbę
  MATCHING,    // Szukamy kierowcy (po pre-autoryzacji płatności)
  ACCEPTED,    // Kierowca przyjął zlecenie
  ARRIVED,     // Kierowca jest pod adresem
  IN_PROGRESS, // Przejazd trwa
  COMPLETED,   // Koniec trasy
  CANCELLED    // Anulowano
}
