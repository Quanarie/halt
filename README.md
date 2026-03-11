## HALT: SYSTEM DYNAMICZNEGO WYCENIANIA I REZERWACJI PRZEJAZDOW

Potrzebujemy rozwiązać problem dynamicznego kojarzenia pasażerów z kierowcami w czasie rzeczywistym:

* System obsługuje różne miasta, każde miasto jest podzielone na strefy geograficzne.
* Każde miasto ma przypisanych kierowców, którzy mogą być w jednym z trzech stanów:
    * Dostępny
    * W trakcie przejazdu
    * Przerwa
* Pojazdy dzielą się na trzy kategorie:
    * Standard (4 miejsca)
    * Premium (4 miejsca, droższy samochód, lepsza statystyka kierowcy)
    * Van (6 miejsc)

Mamy dwa typy dopasowania:
* Natychmiastowe - system szuka najbliższego kierowcy do momentu zaakceptowania.
* Planowane - użytkownik rezerwuje przejazd z wyprzedzeniem. System musi zagwarantować dostępność kierowcy
  w wyznaczonym czasie.

## Przykładowe ścieżki wykonania | Tryb natychmiastowy
### Pasażer
* **Zlecenie:** Pasażer otwiera aplikację, podaje punkt A i B. System oblicza dystans i sprawdza aktualny mnożnik popytu
  (np. $x = 1.5$ ze względu na deszcz). Pasażer widzi cenę "gwarantowaną" przez 2 minuty.
    * Pasażer klika "Zamawiam". System wykonuje pre-autoryzację środków na karcie.
      Jeśli brakuje środków, proces kończy się błędem.
    * System wysyła zdarzenie o rezerwacji przejazdu do kolejki.
    * Po akceptacji przez kierowcę, pasażer otrzymuje powiadomienie z numerem auta oraz szacowaniem
      czasu dojazdu.
* **Przejazd:** Pasażer wsiada, może śledzić trasę na żywo.
* **Zakończenie:** Pasażer wysiada, system finalizuje płatność. Pasażer potrafi zostawić review.

### Kierowca
* **Zlecenie:** Kierowca otwiera aplikację, przełącza się w tryb "Dostępny".
  Aplikacja mobilna zaczyna wysyłać koordynaty GPS co 3 sekundy.
    * System znajduje kierowcę i wysyła mu ofertę przejazdu. Kierowca ma 15 sekund na reakcję (uwzgl. Race Condition).
* **Przejazd:**
    * Kierowca jedzie do punktu A.
    * Po przyjeździe klika "Jestem na miejscu". System zaczyna naliczać czas oczekiwania po upływie 2 minut.
    * Po tym jak pasażer wsiada do pojazdu klika "Ruszam".
    * Jadąc do punktu B, otrzymuje następne oferty przejazdu.
* **Zakończenie:** Po zakończeniu kursu kierowca może przejść w tryb "Przerwa" – system przestaje uwzględniać
  go w wynikach wyszukiwania dla nowych pasażerów. Kierowca potrafi zostawić review pasażerowi.
