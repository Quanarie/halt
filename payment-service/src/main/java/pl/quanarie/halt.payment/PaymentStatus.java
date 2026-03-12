package pl.quanarie.halt.payment;

public enum PaymentStatus {
  /** Płatność została zainicjowana po evencie z Ride Service, ale jeszcze nie przetworzona. */
  PENDING,

  /** Środki zostały pomyślnie pobrane lub zablokowane na koncie pasażera. */
  COMPLETED,

  /** Proces płatności nie udał się (np. brak środków, błąd komunikacji z bankiem). */
  FAILED,

  /** Płatność została zwrócona pasażerowi (np. po anulowaniu przejazdu). */
  REFUNDED
}
