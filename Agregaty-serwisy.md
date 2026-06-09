```mermaid
graph TD
    classDef service fill:none,stroke:#4fc3f7,stroke-width:2px,color:#fff,stroke-dasharray: 5 5;
    classDef aggregate fill:#333,stroke:#888,stroke-width:1px,color:#fff;
    classDef external fill:#4a148c,stroke:#ab47bc,stroke-width:2px,color:#fff;

    subgraph Pricing_Service [Pricing Service]
        CB[Cennik Bazowy]:::aggregate
        KP[Kategoria Pojazdu]:::aggregate
        CD[Cennik Dynamiczny]:::aggregate
        TR[Trasa A do B]:::aggregate
    end

    subgraph Fleet_Service [Fleet & Tracking Service]
        M[Miasto i Strefy]:::aggregate
        K[Kierowca i Pojazd]:::aggregate
    end

    subgraph Ride_Service [Ride Service]
        ZAP[Zapytanie o Przejazd]:::aggregate
        ZLE[Zlecenie Preautoryzowane]:::aggregate
        PR[Aktywny Przejazd]:::aggregate
    end

    subgraph History_Service [History & Payment Service]
        HR[Zamknięty Kurs i Review]:::aggregate
        PAY[Płatności]:::aggregate
    end

    KAFKA((Apache Kafka<br>Event Bus)):::external

    %% Relacje wewnątrz serwisów
    CB --- KP
    TR --- CB
    TR --- CD
    ZAP --- ZLE
    ZLE --- PR
    HR --- PAY
    M --- K

    %% Komunikacja Synchroniczna (REST/gRPC)
    ZAP -->|Sync: Odpytanie o koszt| TR
    ZAP -->|Sync: Preautoryzacja| PAY

    %% Komunikacja Asynchroniczna (KAFKA)
    ZLE -->|Async: RideRequested| KAFKA
    KAFKA -->|Konsumuje Zlecenia| Fleet_Service
    Fleet_Service -->|Async: DriverAccepted| KAFKA
    KAFKA -->|Tworzy Przejazd| PR
    PR -->|Async: RideCompleted| KAFKA
    KAFKA -->|Konsumuje Zakończenia| History_Service

    style Pricing_Service fill:none,stroke:#4fc3f7,color:#fff
    style Fleet_Service fill:none,stroke:#4fc3f7,color:#fff
    style Ride_Service fill:none,stroke:#4fc3f7,color:#fff
    style History_Service fill:none,stroke:#4fc3f7,color:#fff
```
