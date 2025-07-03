<p align="center">
<h1>
Relazione del progetto 
“JavaDyno”
</h1>

<h2>

Alessandro Porcheddu  
Matteo Todeschi  
Cristofer Napolitano
Ivan Crimaldi
<br>

3 luglio 2025
</h2>

</p>

<br>

# Indice

## [1. Analisi](#capitolo-1---analisi)
- ### [1.1 Descrizione e requisiti](#11-descrizione-e-requisiti-1)
- ### [1.2 Analisi e modello del dominio](#12-analisi-e-modello-del-dominio-1)
## [2. Design](#capitolo-2---design)
- ### [2.1 Architettura](#21-architettura-1)
- ### [2.2 Design dettagliato](#22-design-dettagliato-1)
    - #### [2.2.1 Porcheddu Alessandro](#221-porcheddu-alessandro-1)
    - #### [2.2.2 Todeschi Matteo](#222-todeschi-matteo-1)
    - #### [2.2.3 Crimaldi Ivan](#223-crimaldi-ivan-1)
    - #### [2.2.4 Napolitano Cristofer](#224-napolitano-cristofer-1) 
## [3. Sviluppo](#capitolo-3---sviluppo)
- ### [3.1 Testing automatizzato](#31-testing-automatizzato-1)
- ### [3.2 Note di sviluppo](#32-note-di-sviluppo-1)
    - #### [3.2.1 Porcheddu Alessandro](#321-porcheddu-alessandro-1)
    - #### [3.2.2 Todeschi Matteo](#322-todeschi-matteo-1)
    - #### [3.2.3 Crimaldi Ivan](#323-crimaldi-ivan-1)
    - #### [3.2.4 Napolitano Cristofer](#324-napolitano-cristofer-1)
## [4. Commenti finali](#capitolo-4---commenti-finali)
- ### [4.1 Autovalutazione e lavori futuri](#41-autovalutazione-e-lavori-futuri-1)
    - #### [4.1.1 Porcheddu Alessandro](#41-autovalutazione-e-lavori-futuri-1)
    - #### [4.1.2 Todeschi Matteo](#41-autovalutazione-e-lavori-futuri-1)
    - #### [4.1.3 Crimaldi Ivan](#41-autovalutazione-e-lavori-futuri-1)
    - #### [4.1.4 Napolitano Cristofer](#41-autovalutazione-e-lavori-futuri-1)
- ### [4.2 Difficoltà incontrate e commenti per i docenti](#42-difficoltà-incontrate-e-commenti-per-i-docenti-1)
    - #### [4.2.1 Porcheddu Alessandro](#42-difficoltà-incontrate-e-commenti-per-i-docenti-1)
## [Appendice A - Guida utente](#appendice-a---guida-utente-1)
- ### [A.1 Schermata home](#a1-schermata-home)
- ### [A.2 Demo del software](#a2-demo-del-software)
- ### [A.3 Impostazioni utente](#a3-impostazioni-utente)
- ### [A.4 Confronto Grafici](#a4-confronto-grafici)
## [Appendice B - Esercitazioni di laboratorio](#appendice-b---esercitazioni-di-laboratorio-1)
- ### [B.0.1 alessandro.porcheddu@studio.unibo.it](#b01-alessandroporcheddustudiouniboit-1)
- ### [B.0.2 matteo.todeschi@studio.unibo.it](#b02-matteotodeschistudiouniboit-1)
<br>

# Capitolo 1 - Analisi
## 1.1 Descrizione e Requisiti
Il software si presenta come un ambiente per la gestione e simulazione di un dinamometro per motori di varia natura. Il sistema permette a professionisti ed appassionati di visualizzare in maniera conveniente ed intuitiva le misure raccolte dalle proprie strumentazioni riguardo il motore in esame. L'interpretazione di tali dati fornisce all'utente finale informazioni molto importanti sullo stato attuale del motore e sulle possibili modifiche che si possono apportare per migliorarne le prestazioni e/o l'efficienza.  
L’utente ha la libertà di decidere se interagire con la parte di simulazione, se avviare una prova con strumentazione fisica oppure se acquisire i dati della prova dalla propria autovettura tramite porta OBD2 (presente su tutti i veicoli immatricolati nell'UE dal [1 gennaio 2004](https://en.wikipedia.org/wiki/On-board_diagnostics#History)).  
La simulazione comprende un minigioco per poter prendere dimestichezza con le operazioni che potranno poi essere effettuate nel mondo reale.
Il software comprende diverse opzioni per la personalizzazione della GUI e per il confronto di grafici salvati, in modo da poter lavorare in qualsiasi momento sui dati raccolti.

### Requisiti funzionali
- Operazioni sui grafici
    - Salvataggio ed esportazione grafici (in diversi formati)
    - Confronto di grafici con diverse opzioni di visualizzazione
    - Caricamento da file
- Assistenza real-time per prove su banco
    - Warning per raggiungimento soglie critiche o problemi di varia natura
    - Svolgimento della prova in modalità guidata per una ripetibilità più affidabile
- Comunicazione con hardware esterno
    - Compatibilità con un ampio range di interfacce USB
    - Capacità di comunicazione wireless
### Requisiti non funzionali
- Elaborazione dei grafici e dei dati in tempo reale con elevata precisione e fluidità
- Proporzioni grafiche invariate tra dispositivi con risoluzioni molto diverse

## 1.2 Analisi e modello del dominio
Il sistema si articola attorno a diverse entità chiave tra cui il dinamometro, reale o simulato che sia, un sistema di acquisizione dati, automatismi che garantiscono sicurezza e ottimizzazione e un’interfaccia che permetta all’utente di interagire e personalizzare l’acquisizione e la lettura dei dati.  
Un dinamometro è un dispositivo che, collegato ad un asse rotante connesso al relativo motore, permette di misurarne la potenza sprigionata a diversi regimi di rotazione. Esistono diverse tipologie di dinamometro ma la più diffusa in assoluto è il [freno magnetico a correnti parassite](https://www.elprocus.com/what-is-eddy-current-dynamometer-construction-its-working/).
I dati del dinamometro  vengono raccolti in 2 modi possibili: attraverso il collegamento con hardware esterno oppure simulandoli internamente all'applicazione.  

La comunicazione con hardware esterno risulta essere un dialogo con un microcontrollore che fornisce i dati acquisiti con sensori di varia natura. La simulazione si occupa di emulare il comportamento di un motore termico nelle sue componenti di base.
L’intero applicativo si basa su pacchetti di dati che vengono man mano raffinati in modo da arrivare al punto di essere interpretabili tramite grafici.
Inoltre viene utilizzata una di verifica dei dati per evitare che vi siano problemi e/o incongruenze nei e gestisce il comportamento dell’applicativo di conseguenza.

```mermaid
classDiagram
direction TB
    MCU -- MCUCommunicator
    OBD2 --* MCUCommunicator
    RealDyno --* MCUCommunicator
    Dyno --* DataSource
    RealDyno --|> Dyno
    SimulatedDyno --|> Dyno
    Dyno -- RawData
    DataElaborator --o RawData
    DataElaborator --o UserSettings
    DataElaborator --o ElaboratedData
    DataCollector --* DataElaborator
    DataCollector --o ElaboratedData
    OBD2 --|> Dyno
    FileManager --* FileStrategy
    FileManager --> DataCollector

    class MCUCommunicator {
	    +connect()
	    +disconnect()
	    +send()
	    +addMessageListener(Consumer)
	    +removeMessageListener(Consumer)
    }

    class Dyno {
	    +getRawData() RawData
	    +getDynoType() DataSource
	    +begin()
	    +end()
	    +isActive() boolean
    }

    class DataCollector {
	    +initialize(Dyno, UserSettings)
	    +collectData() ElaboratedData
	    +getFullData() List~ElaboratedData~
    }

    class DataElaborator {
	    +getElaboratedData() ElaboratedData
    }

    class FileManager {
	    +setStrategy(FileStrategy)
	    +exportDataToFile(List~ElaboratedData~, File)
	    +importDataFromFile(File) List~ElaboratedData~
    }

    class FileStrategy {
	    +exportData(List~ElaboratedData~, File)
	    +importData(File) List~ElaboratedData~
    }

    class RawData {
    }

    class ElaboratedData {
    }

    class DataSource {
    }

    class MCU {
    }

    class OBD2 {
    }

    class RealDyno {
    }

    class SimulatedDyno {
    }

    class UserSettings {
    }

	<<interface>> MCUCommunicator
	<<interface>> Dyno
	<<interface>> DataElaborator
	<<interface>> RawData
	<<interface>> ElaboratedData
	<<enumeration>> DataSource

```

# Capitolo 2 - Design
## 2.1 Architettura
Per Java Dyno è stato scelto il pattern architetturale MVC (Model-View-Controller), che consente una chiara separazione tra logica, interfaccia e controllo, facilitando manutenzione e sviluppo futuro.  
Il **Controller** funge da coordinatore centrale per tutte le operazioni dell'applicazione. Esso gestisce il ciclo di vita delle simulazioni, coordina l'acquisizione dati dai dinamometri e orchestra l'aggiornamento dell'interfaccia utente. Inoltre accetta la registrazione di eventi dalla view (pressione pulsanti, navigazione tra schermate) e notifica gli aggiornamenti alle componenti grafiche ottenendo i dati attraverso un meccanismo di polling in thread separati.  
Il **Model** è rappresentato principalmente dall'interfaccia `Dyno` e dalle sue implementazioni, che costituiscono i punti d'ingresso per l'acquisizione e l'elaborazione dei dati. Il modello include anche il sistema di gestione dati attraverso `DataCollector` e `DataElaborator`, completamente indipendente dalle implementazioni di `Dyno`.  
Con questa architettura, è possibile sostituire completamente la view senza impattare controller o model: l'interfaccia `View` e le sue implementazioni sono completamente disaccoppiate dalla logica di core. Similmente, l'aggiunta di nuovi tipi di dinamometro richiede solo l'implementazione dell'interfaccia `Dyno` senza modificare il controller esistente.

```mermaid
classDiagram
    class View {
        <<interface>>
        +update(ElaboratedData)
        +update(List~ElaboratedData~)
        +begin(Stage)
    }
    
    class Controller {
        <<interface>>
        +startEvaluation()
        +stopEvaluation()
        +showMainMenu(Stage)
        +showView(Stage, View)
        +closeApp()
    }
    
    class Dyno {
        <<interface>>
        +getRawData() RawData
        +getgetDynoType() DataSource
        +begin()
        +end()
        +isActive() boolean
    }
    
    class DataCollector {
        +initialize(Dyno, UserSettings)
        +collectData() ElaboratedData
        +getFullData() List
    }
    
    class DataElaborator {
        <<interface>>
        +getElaboratedData() ElaboratedData
    }
    
    class SimulatedDyno {
    }
    
    class RealDyno {
    }
    
    class OBD2Dyno {
    }

    View --* Controller
    Controller --* View
    Controller --* Dyno : coordinates
    Controller --* DataCollector : manages
    DataCollector --* DataElaborator : uses
    Dyno <|-- SimulatedDyno
    Dyno <|-- RealDyno
    Dyno <|-- OBD2Dyno
```
## 2.2 Design dettagliato
### 2.2.1 Porcheddu Alessandro
#### Comunicazione con hardware esterno

```mermaid
classDiagram
    class MCUCommunicator {
        <<interface>>
        +connect()
        +disconnect()
        +isConnected() boolean
        +send(String)
        +addMessageListener(Consumer)
        +removeMessageListener(Consumer)
    }
    
    class AbstractSerialCommunicator {
        #setupChip(SerialPort)*
        #getSentDataDelimiter()* String
        #getReceivedDataDelimiter()* String
        #parseMessage()*
    }
    
    class AbstractWebSocketCommunicator {
        #parseMessage(String)* List~String~
    }
    
    class ELM327Communicator {
        +setupChip(SerialPort)
        +getSentDataDelimiter() String
        +getReceivedDataDelimiter() String
        +parseMessage()
    }
    
    class JsonWebSocketCommunicator {
        +parseMessage(String) List~String~
        +send(String)
    }

    MCUCommunicator <|.. AbstractSerialCommunicator
    MCUCommunicator <|.. AbstractWebSocketCommunicator
    AbstractSerialCommunicator <|-- ELM327Communicator
    AbstractWebSocketCommunicator <|-- JsonWebSocketCommunicator
```

**Problema:** il software deve poter permettere ai diversi `Dyno` di comunicare con il relativo hardware esterno indipendentemente dal vettore di comunicazione (USB, Bluetooth, HTTP, WebSocket, ecc) e dal protocollo finale che si aspetta il microcontrollore. Quest'ultima, inoltre, deve avvenire in modo asincrono per non interferire con l'esecuzione del programma.  

**Soluzione:** la soluzione più idonea risulta essere l'utilizzo del pattern **Template Method** che permette una facile e veloce implementazione di un nuovo mezzo per comunicare con un possibile microcontrollore. In particolare l'interfaccia `MCUCommunicator` definisce il contratto da seguire per la comunicazione. Vengono poi definite 2 classi astratte relative a comunicazione seriale e websocket. Quest'ultime permettono di sviluppare classi aggiuntive specifiche per particolari microcontrollori implementando solo piccole porzioni di codice. Infatti `JsonWebSocketCommunicator` rappresenta un generico microcontrollore nel quale ci si aspetta di inviare e ricevere dati in formato JSON, mentre `ELM327Communicator` fa riferimento alla specifica implementazione di una comunicazione con l'omonimo chip OBD2. Si noti inoltre che l'interfaccia `MCUCommunicator` utilizza un generico per permetterealle implementazioni di gestire meglio i diversi tipi di messaggi nella comunicazione (come il `Pair` nel caso di `JsonWebSocketCommunicator`).

---

#### Scambio asincrono di dati

```mermaid
classDiagram
    class MCUCommunicator {
        <<interface>>
        +connect()
        +disconnect()
        +send(String)
        +addMessageListener(Consumer~T~)
        +removeMessageListener(Consumer~T~)
    }

    class AbstractPhysicalDyno {
        -communicator: MCUCommunicator
        #handleMessage(T)
        +getRawData() RawData
    }

    class MCU {

    }


    MCUCommunicator --> AbstractPhysicalDyno : recieves
    MCU --> MCUCommunicator: sends
    MCUCommunicator --> MCU: asks
```

**Problema:** rendere la comunicazione con l'hardware esterno asincrona per rendersi indipendenti dai tempi di risposta di quest'ultimo e per garantire un'esperienza utente fluida.

**Soluzione:** per rispettare questi requisiti si ricorre al pattern **Observer**, dove l'applicazione richiede i dati in modo sincrono ma riceve una risposta asincrona. Ritroviamo nell'interfaccia `MCUCommunicator` i metodi `addMessageListener(Consumer<T>)` e il relativo `removeMessageListener(Consumer<T>)` che permettono ai clienti di registrare più di un _listener_ (e quindi di _Observers_). La classe astratta `AbstractPhysicalDyno`, attraverso il metodo astratto `handleMessage(T)`, fornisce ai clienti (ed a estensioni future) un contratto per ricevere ed elaborare i dati richiesti in ingresso. Infine, si pretende dai clienti che vengano inviati messaggi esclusivamente testuali e con un certo intervallo, per evitare fenomeno di _flooding_.

---

#### Creazione dei dati grezzi

```mermaid
classDiagram
    class RawData {
        -Optional~Integer~ engineRPM
        -Optional~Double~ engineTemperature
        ...
        +builder() Builder
    }

    class Dyno {
        <<interface>>
        +getRawData() RawData
    }

    class DataElaborator {
        <<interface>>
        +getElaboratedData() ElaboratedData
    }

    Dyno --> RawData : produces
    DataElaborator --> RawData : consumes
```

**Problema:** ogni `Dyno` genera diversi dati in base a quali sensori sono stati installati, per esempio uno potrebbe monitorare in dettaglio le condizioni atmosferiche mentre l'altro ometterle completamente. Alcuni dati potrebbero anche venir trasmessi in maniera errata o essere persi durante la comunicazione. In generale sono presenti molti dati diversi.

**Soluzione:** viene fatto ampio utilizzo degli `Optional` per immagazzinare i dati in un _record_ `RawData` attraverso il pattern **Builder**. Quest'ultimo permette di creare `RawData` in maniera facilmente interpretabile e specificando solo i dati davvero utili. Infatti i restanti risulteranno essere un `Optional` vuoto, evitando di doverli impostare a un valore nullo o zero. In questo modo i clienti possono facilmente maneggiare i dati evitando eccezioni e controlli su eventuali valori _null_.

### 2.2.2 Todeschi Matteo
#### Gestione di simulazione concorrente
```mermaid
classDiagram
    class Dyno {
        <<interface>>
        +getRawData() RawData
        +getDynoType() DataSource
        +begin()
        +end()
        +isActive() boolean
    }

    class Runnable {
        <<interface>>
        +run()
    }

    class SimulatedDyno {
        <<interface>>
    }

    class SimulatedDynoImpl {
      +getRawData() RawData
      +getDynoType() DataSource
      +begin()
      +end()
      +isActive() boolean
      +run()
    }

    Dyno <|-- SimulatedDyno
    Runnable <|-- SimulatedDyno
    SimulatedDyno <|.. SimulatedDynoImpl

```

**Problema:** 
Far si che ogni implementazione di `SimulatedDyno` possa essere eseguita in modo concorrente, permettendo in primis di generare dati e aggiornarsi molto più velocemente rispetto alla frequenza di aggiornamento dell'appplicazione e dando inoltre all'utente la possibilità di interagire con l'applicazione senza blocchi o rallentamenti.

**Soluzione:**
Per risolvere il problema si è scelto di creare un interfaccia intemedia tra `Dyno` e `SimulatedDyno` che implementa l'interfaccia `Runnable`.  
In questo modo ogni implementazione di `SimulatedDyno` deve essere eseguita in un thread separato, permettendo di generare i dati in modo asincrono.  
La classe `SimulatedDynoImpl` implementa questa logica, gestendo la generazione dei dati e l'aggiornamento dello stato in modo concorrente.  
Degno di nota è anche il fatto che a fare le chiamate al nuovo Thread generato è il `Controller` tramite un suo Thread virtuale, in modo da permettere all'utente di interagire con l'applicazione mentre la simulazione è in corso, opportunamente sincronizzato usando un `Latch` per evitare corse critiche.  
Questa scelta permette inoltre di disaccoppiare il tempo di aggiornamento dello schermo da quello della generazione dei dati, permettendo di avere un'interfaccia utente sempre reattiva ma generando comunque tutti i dati necessari per l'elaborazione e il salvataggio.

#### Gestione degli errori con Monitor dedicato 
```mermaid
classDiagram
    class AlertMonitor {
        <<utility>>
        +setController(Controller)
        +showError(String)
        +showWarning(String)
        +showInfo(String)
    }

    class AlertType {
        <<enumeration>>
        +INFORMATION
        +WARNING
        +ERROR
    }

    class Object {
    }

    class AlertDisplayer{
        <<utility>>
        +showAlert(AlertType, String, Optional~String~)
    }

    class Controller {
        <<interface>>
        +showAlert(AlertType, String, Optional~String~)
    }

    Object ..> AlertMonitor : use
    AlertMonitor ..> AlertType : use
    AlertMonitor ..> Controller : invoke
    Controller ..> AlertDisplayer : notify
    AlertDisplayer ..> AlertType : use
```

**Problema:**
Gestire gli errori in modo centralizzato e fornire un feedback all'utente senza bloccare l'applicazione.  
In particolare, è necessario gestire errori di comunicazione con l'hardware esterno, errori di elaborazione dei dati e altri errori generici.

**Soluzione:**
Per risolvere il problema si è scelto di implementare un monitor dedicato (`AlertMonitor`) implementato come utility statica.  
Ad essa viene abbinato un `Controller` che si occupa di instradare correttamente gli errori ad un componente della view che si occupa di visualizzarli (`AlertDisplayer`).  
I messaggi di errore hanno diverse priorità e vengono gestiti attraverso un enumerativo `AlertType` che permette di distinguere tra errori, avvisi e informazioni. In questo modo, l'utente può essere informato in modo chiaro e preciso senza bloccare l'applicazione.


#### Riutilizzo dei componenti della view
```mermaid
classDiagram
    class View {
        <<interface>>
        +begin(Stage)
        +update(ElaboratedData)
        +update(List~ElaboratedData~)
    }
    class EvaluationView {
    }
    class ChartsView {
    }
    class ButtonsPanel {
    }
    class GaugePanel {
    }
    class StatsPanel {
    }
    class ChartsPanel {
    }

    ButtonsPanel --|> VBox
    ChartsPanel --|> VBox
    StatsPanel --|> VBox
    GaugePanel --|> HBox
    View <|-- EvaluationView
    View <|-- ChartsView
    EvaluationView *-- ButtonsPanel
    EvaluationView *-- ChartsPanel
    EvaluationView *-- GaugePanel
    EvaluationView *-- StatsPanel
    ChartsView *-- ChartsPanel
```

**Problema:**
Le view vanno pensate in modo da essere facilmente personalizzabili e soprattutto il più scarne possibile.
Esse infatti avranno diverse parti in comune e sarebbe sbagliato doverle riscrivere ogni volta che si vuole creare una nuova schermata.  
Si vorrebbe inoltre permettere future estensioni della view.

**Soluzione:**
Per risolvere il problema si è scelto di implementare un pattern compositivo per la view, in modo da poter riutilizzare i componenti in diverse schermate.  
In particolare, sono stati creati diversi pannelli (`ButtonsPanel`, `ChartsPanel`, `GaugePanel`, `StatsPanel`) che possono essere combinati per creare diverse associazioni visive.  
Essi, nella nostra implementazione con JavaFX, estendono i componenti `VBox` e `HBox` (che a loro volta estendono `Pane`) in modo da poter essere facilmente inseriti in varie view.  
Questo tipo di implementazione permette di avere una view modulare e facilmente estendibile, in cui i componenti possono essere riutilizzati e combinati in modi diversi senza dover riscrivere il codice.

#### Visualizzazione di grafici multipli
```mermaid
classDiagram
    class View {
        <<interface>>
    }
    class GaugePanel {
        +updateGauges(Number, Number, Number)
    }

    class GaugeFactory {
        <<interface>>
        +createGaugeChart() Gauge
    }
    class ChartsPanel {
        +addSingleData(Number, Number, Number)
        +addAllData(List~Number~, List~Number~, List~Number~)
    }

    class ChartsFactory {
        <<interface>>
        +createEmptyCharts() JFreeChart
    }

    class ChartsManager {
        <<interface>>
        +addYAxis(JFreeChart, String)
        +setDarkTheme(JFreeChart)
        +setSeriesVisibility(JFreeChart, int, boolean)
    }

    GaugePanel --> GaugeFactory : uses
    ChartsPanel --> ChartsFactory : uses
    ChartsManager--> ChartsPanel : controls
    ChartsPanel *-- ChartsManager
    View *-- GaugePanel
    View *-- ChartsPanel

```

**Problema:**
Permettere la personalizzazione degli elementi presenti in `GaugePanel` e `ChartsPanel` in modo da facilitare in futuro una facile estensione e personalizzazione della view.  
Si vuole inoltre cercare di semplificare e generalizzare le operazioni che l'utente può fare sui grafici, in modo da evitare errori e rendere la personalizzazione più intuitiva.

**Soluzione:**
Utilizzare il pattern **Factory** per creare i grafici e i gauge, creando dunque un interfaccia funzionale per ognuno di questi componenti.
`GaugeFactory` e `ChartsFactory` sono quindi le interfacce che definiscono i metodi per creare i gauge e i grafici e possiedono anche una loro implementazione standard che verrà poi utilizzata per creare i componenti di default nella nostra applicazione ma nulla vieta in futuro di implementare nuove classi che estendono queste interfacce e che permettono di creare grafici e gauge personalizzati.  
Per quanto riguarda la gestione dei grafici, è stata implementata rispettando il pattern **Façade**, ovvero mediante un `ChartsManager` che permette di raccorpare sotto-operazioni in operazioni atomiche, come ad esempio aggiungere ulteriori assi Y, creare nuove serie, aggiungere un intera serie di dati in maniera più efficiente, disabilitare serie esistenti e altro.

### 2.2.3 Crimaldi Ivan
#### Salvataggio e Caricamento dati da File
```mermaid
classDiagram
    direction LR
    
    class FileManagerImpl {
        +setStrategy(strategy)
        +exportDataToFile(data, file)
        +importDataFromFile(file)
    }

    class FileStrategy {
        <<interface>>
        +exportData(data, file)
        +importData(file)
    }

    class CsvStrategy {
        ...
    }

    class JsonStrategy {
        ...
    }

    FileManagerImpl ..> FileStrategy : uses
    FileStrategy <|.. CsvStrategy : implements
    FileStrategy <|.. JsonStrategy : implements
```

**Problema:**
L'applicazione deve essere in grado di salvare i dati generati dalla prova, e di poter caricare i dati generati da prove precedenti. Inoltre, l'applicazione deve essere in grado di supportare diversi formati di file, con la possibilità di aggiungere nuovi formati supportati nel futuro.

**Soluzione:**
Per risolvere il problema, ho utilizzato il design pattern **Strategy**, così da separare la logica di gestione dei file dalla logica specifica di ogni formato. 
`FileStrategy` definisce il contratto comune per tutte le strategie, che si specializzano nel gestire un solo formato, come `CsvStrategy` e `JsonStrategy`.
`FileManager` è il Context per la Strategy, delegando alla strategia corrente l'esecuzione delle operazioni.

#### Strategy Factory
```mermaid
classDiagram
    direction LR

    class FileStrategyFactoryImpl {
        +createStrategyFor(file): Optional~FileStrategy~
    }

    class FileStrategyFactory {
        <<interface>>
        +createStrategyFor(file): Optional~FileStrategy~
    }
    
    class CsvStrategy {
        ...
    }

    class JsonStrategy {
        ...
    }

    FileStrategyFactory <|.. FileStrategyFactoryImpl : implements
    FileStrategyFactoryImpl ..> CsvStrategy : creates
    FileStrategyFactoryImpl ..> JsonStrategy : creates
```

**Problema:**
Con l'introduzione di più Strategie, il client (Controller) deve contenere la logica condizionale su quale strategia creare in base all'estensione del file.

**Soluzione:**
Per risolvere il problema, ho utilizzato  il pattern **Factory**: `FileStrategyFactory` definisce un metodo per creare la strategia appropriata, e `FileStrategyFactoryImpl` la implementa, scegliendo la strategia (oggetto `FileStrategy`) in base all'estensione del file. Sarà quindi l'unica componente del Model da cambiare qualora si volesse aggiungere un nuovo  tipo di file.

### 2.2.4 Napolitano Cristofer
#### Vehicle Builder

``` mermaid
classDiagram
    %% VehicleBuilder e dipendenze concrete
    class VehicleBuilder {
      <<Builder>>
      +static VehicleBuilder builder()
      +VehicleBuilder withBaseTorque(double baseTorqueValue)
      +VehicleBuilder withTorquePerRad(double torquePerRadValue)
      +VehicleBuilder withEngineInertia(double engineInertiaValue)
      +VehicleBuilder withGearRatios(double... gearRatioValue)
      +VehicleBuilder withWheel(double mass, double radius)
      +VehicleBuilder withRollingResistance(boolean enable, double coeff)
      +VehicleBuilder withBenchBrake(BrakeTorqueProvider provider)
      +VehicleBuilder withWeatherStation(WeatherStation ws)
      +VehicleBuilder withTemperatureModel(TemperatureModel model)
      +VehicleBuilder withThermalParams(double targetTemp, double targetTimeCoeff)
      +VehicleImpl buildVehiclewithRigidModel()
    }

    class EngineImpl {
      +EngineImpl(double inertia, TorqueMap map, TemperatureModel temperatureModel)
    }
    class ManualTransmission {
      +ManualTransmission(double[] gearRatio)
    }
    class SimpleTorqueMap {
      +SimpleTorqueMap(double baseTorque, double torquePerRad)
    }
    class TargetTemperatureModel {
      +TargetTemperatureModel(double targetTemperature, double temperature, double timeCoeff)
    }
    class WeatherStationImpl {
      +WeatherStationImpl(double temperature, int pressure, int humidity)
    }
    class BenchLoad {
      +BenchLoad(BrakeTorqueProvider provider)
    }
    class RollingResistance {
      +RollingResistance(double coefficient)
    }
    class RigidDriveTrainSim {
      +RigidDriveTrainSim(Engine engine, Transmission transmission, List<LoadModel> loads)
    }
    class VehicleImpl {
      +VehicleImpl(DriveTrain drivetrain, WeatherStation weather, double wheelRadius, double currentThrottle)
    }
    class BenchBrakeTorqueHolder {
    }

    %% Dipendenze illustrate
    VehicleBuilder ..> EngineImpl
    VehicleBuilder ..> ManualTransmission
    VehicleBuilder ..> SimpleTorqueMap
    VehicleBuilder ..> TargetTemperatureModel
    VehicleBuilder ..> WeatherStationImpl
    VehicleBuilder ..> BenchLoad
    VehicleBuilder ..> RollingResistance
    VehicleBuilder ..> RigidDriveTrainSim
    VehicleBuilder ..> VehicleImpl
    VehicleBuilder ..> BenchBrakeTorqueHolder
```

**Problema:** la creazione di un oggetto `Vehicle` richiede la configurazione di numerosi parametri e dipendenze concrete (motore, trasmissione, modelli di carico, condizioni ambientali), e un costruttore monolitico impedirebbe l’estensibilità e violerebbe i principi SRP e DIP.

**Soluzione:** si applica il Builder pattern: `VehicleBuilder` espone metodi `with…` per ogni parametro di configurazione, costruendo passo dopo passo le dipendenze concrete (`EngineImpl`, `ManualTransmission`, `SimpleTorqueMap`, `TargetTemperatureModel`, `WeatherStationImpl`, `BenchLoad`, `RollingResistance`, `RigidDriveTrainSim`) e infine invocando `buildVehiclewithRigidModel()` per ottenere un’istanza di `VehicleImpl`. Questa soluzione isola la logica di composizione, rispetta il DIP e consente di aggiungere nuove opzioni senza modificare il costruttore centrale.

#### Drive Train

``` mermaid
classDiagram
    %% Interfacce con metodi completi
    class DriveTrain {
      <<interface>>
      +void step(double throttle, double deltatime)
      +void shiftUp()
      +void shiftDown()
      +double getEngineOmega()
      +double getWheelOmega()
      +int getCurrentGear()
      +double getEngineTemperature()
      +double getGeneratedTorque()
    }
    class Engine {
      <<interface>>
      +void setThrottle(double throttle)
      +void setAngularVelocity(double newEngineOmega)
      +double getEngineTemperature()
    }
    class Transmission {
      <<interface>>
      +double getCurrentRatio()
      +void shiftUp()
      +void shiftDown()
      +int getCurrentGear()
    }
    class LoadModel {
      <<interface>>
      +double getLoadTorque(double engineOmega, double gearRatio)
    }

    %% Classi concrete con soli costruttori
    class RigidDriveTrainSim {
      +RigidDriveTrainSim(Engine engine, Transmission transmission, List<LoadModel> loads)
    }
    class EngineImpl {
      +EngineImpl(double inertia, TorqueMap map, TemperatureModel temperatureModel)
    }
    class ManualTransmission {
      +ManualTransmission(double[] gearRatio)
    }
    class BenchLoad {
      +BenchLoad(BrakeTorqueProvider provider)
    }
    class RollingResistance {
      +RollingResistance(double coefficient)
    }

    %% Realizzazioni
    DriveTrain    <|-- RigidDriveTrainSim
    Engine        <|-- EngineImpl
    Transmission  <|-- ManualTransmission
    LoadModel     <|-- BenchLoad
    LoadModel     <|-- RollingResistance

    %% Composizioni
    RigidDriveTrainSim "1" *-- "1" Engine
    RigidDriveTrainSim "1" *-- "1" Transmission
    RigidDriveTrainSim "1" *-- "*" LoadModel

```

**Problema:** occorre modellare il gruppo moto in modo da eseguire il passo di simulazione, gestire i cambi di rapporto e calcolare la coppia netta sottraendo i vari carichi, il tutto garantendo flessibilità per diverse implementazioni di motore, trasmissione e carichi.

**Soluzione:** `RigidDriveTrainSim` implementa l’interfaccia `DriveTrain` e realizza una composizione 'uno a molti' con `Engine`, `Transmission` e `LoadModel`. Grazie al DIP, ogni componente interagisce tramite le proprie interfacce, applicando SRP delegando a `EngineImpl`, `ManualTransmission`, `BenchLoad` e `RollingResistance` le rispettive responsabilità, e consentendo l’aggiunta di nuovi modelli di carico senza modificare il simulatore di base.

#### Engine

``` mermaid
classDiagram
    %% Interfacce con metodi completi
    class TorqueMap {
      <<interface>>
      +double getTorque(double throttle, double omega)
    }
    class TemperatureModel {
      <<interface>>
      +void update(double deltaTime)
      +double getTemperature()
    }
    class Engine {
      <<interface>>
      +void setThrottle(double throttle)
      +void update(double loadTorque, double deltaTime)
      +double getAngularVelocity()
      +void setAngularVelocity(double newEngineOmega)
      +double getGeneratedTorque()
      +double getEngineTemperature()
    }

    %% Classi concrete con soli costruttori
    class EngineImpl {
      +EngineImpl(double inertia, TorqueMap map, TemperatureModel temperatureModel)
    }
    class SimpleTorqueMap {
      +SimpleTorqueMap(double baseTorque, double torquePerRad)
    }
    class TargetTemperatureModel {
      +TargetTemperatureModel(double targetTemperature, double temperature, double timeCoeff)
    }

    %% Realizzazioni
    TorqueMap        <|-- SimpleTorqueMap
    TemperatureModel <|-- TargetTemperatureModel
    Engine           <|-- EngineImpl

    %% Composizioni
    EngineImpl *-- TorqueMap
    EngineImpl *-- TemperatureModel

```
**Problema:** il motore deve generare coppia in funzione di apertura del gas e regime, e al tempo stesso gestire l’evoluzione termica, ma una classe monolitica risulterebbe rigida e difficile da estendere.

**Soluzione:** si definiscono le interfacce `TorqueMap` e `TemperatureModel`; `EngineImpl` le compone e delega il calcolo della coppia a `SimpleTorqueMap` (Strategy pattern) e l’aggiornamento termico a `TargetTemperatureModel`. In tal modo si rispetta DIP, OCP e SRP: è immediato aggiungere nuove implementazioni di mappa coppia o modelli termici senza modificare `EngineImpl`.

#### Vehicle e WeatherStation

``` mermaid
classDiagram
    %% Dati
    class RawData

    %% Interfacce
    class Vehicle {
      <<interface>>
      +void setThrottle(double throttle)
      +void update(double deltaTime)
      +void shiftUp()
      +void shiftDown()
      +int getCurrentGear()
      +RawData getRawData()
    }
    class WeatherStation {
      <<interface>>
      +double getTemperature()
      +int getPressure()
      +int getHumidity()
    }
    class DriveTrain {
      <<interface>>
      +void step(double throttle, double deltaTime)
      +void shiftUp()
      +void shiftDown()
      +double getEngineOmega()
      +double getWheelOmega()
      +int getCurrentGear()
      +double getEngineTemperature()
      +double getGeneratedTorque()
    }

    %% Implementazioni
    class VehicleImpl {
      +VehicleImpl(DriveTrain drivetrain, WeatherStation weather, double wheelRadius, double currentThrottle)
    }
    class WeatherStationImpl {
      +WeatherStationImpl(double temperature, int pressure, int humidity)
    }

    %% Relazioni
    Vehicle         <|-- VehicleImpl
    WeatherStation  <|-- WeatherStationImpl
    VehicleImpl     *-- DriveTrain
    VehicleImpl     *-- WeatherStation

    %% Flusso dati
    VehicleImpl --> RawData
```
**Problema:** è necessario orchestrare l’interazione tra il veicolo, il drivetrain e la fonte dei dati ambientali, tuttociò per raccogliere i parametri operativi in un unico oggetto RawData per analisi e logging.

**Soluzione:** `VehicleImpl` realizza l’interfaccia `Vehicle`, componendo `DriveTrain` e `WeatherStation`; delega la logica di guida e cambio marcia a `DriveTrain`, interroga `WeatherStationImpl` per le condizioni ambientali e aggrega tutti i dati in `RawData`. Questa architettura a componenti riutilizzabili rispetta SRP e DIP e garantisce facilmente estensibilità e testabilità.

# Capitolo 3 - Sviluppo
## 3.1 Testing automatizzato
Sono stati realizzati test automatici per le principali parti di Model in modo da facilitare lo sviluppo fin dalle prime fasi.
Per tale scopo è stato adottato _JUnit_ e componenti derivate che, inoltre, ci hanno permesso di controllare il risultato di tali test su più piattaforme in contemporanea grazie all'integrazione con _Gradle_ e _GitHub workflows_.

* `OBD2DynoTest`: test progettato per verificare il corretto funzionamento della logica di acquisizione dati tramite l'interfaccia OBD2. Vengono inoltre simulati diversi scenari di input per assicurare che i dati, come i giri del motore e la velocità del veicolo, vengano interpretati correttamente e che le operazioni di conversione siano accurate.
* `SerialCommunicatorTest`: classe implementata per un veloce riscontro all'inizio dello sviluppo per quanto riguarda l'interfacciamento con la libreria _jSerialComm_
* `DataElaboratorImplTest`: in questo caso il test prevede la crezione di dati pseudo realistici per la verifica della correttezza dei calcoli sapendo il "trend" a priori. In particolare viene testata la componente di calcolo dell'OBD2 che risulta molto più articolata di quella del banco reale.
* `SimulatedDynoImplTest `: questa classe di test verifica il corretto funzionamento della simulazione del dinamometro, assicurandosi che le chiamate logiche su questo oggeto funzionino correttamente.
Considerando l'utilizzo di un Thread separato per l'esecuzione della gerenazione di dati al suo interno, testare il corretto avvio, fermo e verifica dello stato risultava di fondamentale importanza.
* `build-and-deploy`: questo test prevede l'esecuzione in remoto su 3 _GitHub runners_ con sistemi operativi diversi (Ubuntu, Windows e MacOs) dei test di JUnit per assicurarsi la compatibilità multipiattaforma. Se nessun test fallisce viene anche rilasciato nel repo github un _fat jar_ dell'applicazione nella sezione _Releases_. Si noti che tutto ciò avviene solo in caso di rilevamento di _git tag_ per gestire il controllo delle versioni dell'applicazione e per rispettare i consumi previsti dal piano gratuito di _GitHub_.
* `JsonStrategy` e `CsvStrategy`: I test verificano la corretta serializzazione e deserializzazione dei dati (test di "round-trip"), assicurando che i dati esportati su file possano essere re-importati senza perdita o corruzione di informazioni. Vengono coperti anche casi limite (liste vuote, campi `Optional` assenti, file malformati o non esistenti), utilizzando la feature `@TempDir` di JUnit per garantire creare file e cartelle temporanei.

Per quanto i componenti grafici, non si è optato per librerie di testing automatizzato, ma è stata piuttosto scelta la strada di creare una GUI minimale che veniva impostata come mainView dell'applicazione.  
Su di essa venivano poi aggiunti i singoli componenti grafici da testare (anche in combinazione), in modo da poter verificare il corretto funzionamento di questi ultimi.  
In `SimulatedDynoImplTest`è ancora possibile trovare lo scheletro di questa GUI di test senza componenti e metodi logici implementati.

## 3.2 Note di sviluppo
### 3.2.1 Porcheddu Alessandro
#### Utilizzo di `LoopingIterator` dalla libreria **Apache Commons Collections**
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/3ca4f9147495bf1de3cde73578cc8f701ab812c9/src/main/java/it/unibo/javadyno/model/dyno/obd2/impl/OBD2Dyno.java#L132

#### Utilizzo di `VirtualThread`
Scelti per limitare il context-switching e il consumo di risorse. Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/3ca4f9147495bf1de3cde73578cc8f701ab812c9/src/main/java/it/unibo/javadyno/model/dyno/impl/AbstractPhysicalDyno.java#L55-L57

#### Utilizzo della libreria **[jSerialComm](https://github.com/Fazecast/jSerialComm)**
Sfruttata per la parte di rilevamento e comunicazione multipiattaforma di interfacce seriali. Un esempio nel rilevamento della disconnessione: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/3ca4f9147495bf1de3cde73578cc8f701ab812c9/src/main/java/it/unibo/javadyno/model/data/communicator/impl/AbstractSerialCommunicator.java#L237

#### Utilizzo della libreria **[Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)**
Esempio di estensione e implementazione: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/3ca4f9147495bf1de3cde73578cc8f701ab812c9/src/main/java/it/unibo/javadyno/model/data/communicator/impl/AbstractWebSocketCommunicator.java#L120-L149

#### Utilizzo di `JSONObject` dalla libreria **[JSON-java](https://github.com/stleary/JSON-java)**
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/3ca4f9147495bf1de3cde73578cc8f701ab812c9/src/main/java/it/unibo/javadyno/model/dyno/real/impl/RealDynoImpl.java#L19

### 3.2.2 Todeschi Matteo

#### Utilizzo di `Optional`
Usati per rendere la descrizione degli Alert non obbligatoria  
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/7805a79e84493b5acd015c3e7004dadca600180e/src/main/java/it/unibo/javadyno/controller/impl/AlertMonitor.java#L35

#### Utilizzo di `Stream`
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/7805a79e84493b5acd015c3e7004dadca600180e/src/main/java/it/unibo/javadyno/model/graph/impl/ChartsManagerImpl.java#L47-L54

#### Utilizzo di `Thread` mediante implementazione di `Runnable`
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/0dd7871eb63943128fb35892a28813f12a068646/src/main/java/it/unibo/javadyno/model/dyno/simulated/impl/SimulatedDynoImpl.java#L111-L126

#### Utilizzo di `Latch` per sincronizzazione tra thread (reale e virtuale)
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/0dd7871eb63943128fb35892a28813f12a068646/src/main/java/it/unibo/javadyno/controller/impl/ControllerImpl.java#L119-L141

#### Utilizzo di `JavaFX` per la GUI
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/14b2e2312654e1e15f3c6588bfa4c498117637f8/src/main/java/it/unibo/javadyno/view/impl/EvaluatingView.java#L61-L91

#### Utilizzo della libreria **[JFreeChart](https://github.com/jfree/jfreechart)** per la visualizzazione dei grafici
L'utilizzo della libreria `JFreeChart` è accompagnato da un modulo dedicato alla compatibilità con JavaFX per inglobarlo in un container compatibile  
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/14b2e2312654e1e15f3c6588bfa4c498117637f8/src/main/java/it/unibo/javadyno/model/graph/impl/ChartsManagerImpl.java#L161-L176

#### Utilizzo della libreria **[Medusa](https://github.com/HanSolo/medusa)** per la visualizzazione dei gauges
Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/7805a79e84493b5acd015c3e7004dadca600180e/src/main/java/it/unibo/javadyno/model/graph/impl/DefaultGaugeFactory.java#L18

### 3.2.3 Crimaldi Ivan
#### Utilizzo della libreria **[Jackson](https://github.com/FasterXML/jackson)**
Usata per la conversione automatica tra oggetti Java e JSON. 
L'ObjectMapper è stato configurato per supportare tipi moderni, in questo esempio Instant.

Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/main/src/main/java/it/unibo/javadyno/model/filemanager/impl/JsonStrategy.java#L27-L30

#### Utilizzo della libreria **[opencsv](https://opencsv.sourceforge.net/)**
Usata per leggere e scrivere i dati in formato CSV, gestendo la scrittura dell'header e la conversione da riga ad array di stringhe (e viceversa).

Permalink esempio utilizzo di CSVWriter e scrittura header: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/main/src/main/java/it/unibo/javadyno/model/filemanager/impl/CsvStrategy.java#L59-L60

#### Utilizzo di `Optional` come return type per evitare null
La Factory restituisce un `Optional<FileStrategy>` per indicare la possibilità che non esista o non sia stata implementata una strategia per un dato tipo di file.

Permalink:https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/main/src/main/java/it/unibo/javadyno/model/filemanager/impl/FileStrategyFactoryImpl.java#L24-L41

#### Utilizzo di Lambda Experessions e `Optional.map` per trasformaziono concise
Utilizzato per gestire gli Optional e scrivere un valore di default se vuoti senza dover riempire il codice di statement if-else.

Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/main/src/main/java/it/unibo/javadyno/model/filemanager/impl/CsvStrategy.java#L65-L75

#### Utilizzo di `TypeReference` per la deserializzazione di tipi generici
Dato che Java a runtime 'dimentica' che una lista è una `List<ElaboratedData>` e la vede solo come una List, ho dovuto usare la classe `TypeReference` di Jackson. Questa classe permette a Jackson di deserializzare correttamente il JSON in una lista di oggetti ElaboratedData senza errori.

Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/main/src/main/java/it/unibo/javadyno/model/filemanager/impl/JsonStrategy.java#L48-L50

#### Uso di Riferimenti a Metodi come `Function`
Utilizzato `parseOptional` come metodo generico riutilizzabile per parsare ogni tipo di dato dal CSV, che accetta la logica specifica di parsing come un parametro di tipo `Function`. Ho inoltre usato la sinstassi concisa (`Integer: :parseInt`).

Permalink: https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/main/src/main/java/it/unibo/javadyno/model/filemanager/impl/CsvStrategy.java#L108-L118

### 3.2.4 Napolitano Cristofer
#### Uso di stream in `RigidDriveTrainSim.step()`
usato per sommare tutti i nuovi carichi dei LoadModel : https://github.com/TodeschiMatteo/OOP24-java-dyno/blob/b751bbc65c9c86bf2315d438e0d5f4e200725a7c/src/main/java/it/unibo/javadyno/model/dyno/simulated/impl/RigidDriveTrainSim.java#L37C1-L46C1

# Capitolo 4 - Commenti finali
## 4.1 Autovalutazione e lavori futuri
### 4.1.1 Porcheddu Alessandro
Nonostante le difficoltà iniziali mi ritengo molto soddisfatto del progetto, in quanto sono contento di aver lavorato su un _proof-of-concept_ orientato verso l'utilizzo nel mondo reale. Sono altrettanto contento di essere riuscito a scrivere codice e migliorarlo iterativamente ogni volta che pensavo di riuscire ad astrarre meglio alcuni concetti, anche se riconosco di aver tralasciato alcune parti secondarie per questioni di tempo. In particolare, non mi sarei dovuto far spaventare dall'idea di mettermi troppo presto a implementare il design e non mi sarei dovuto fossilizzare troppo sull'analisi progettuale. Ritengo quest'ultima molto importante ma riconosco anche che il coordinamento di gruppo sull'analisi di un dominio non molto documentato è risultata molto lenta e macchinosa. Per quanto riguarda il mio ruolo nel progetto sono molto soddisfatto di essermi messo spesso in prima fila per aiutare i colleghi e, poichè il dominio del progetto era stato proposto da me, mi ritenevo anche in parte responsabile qualora sorgesse un dubbio. Inoltre sono anche particolarmente fiero di aver iniziato ad usare diversi strumenti accessori come _GitHub workflows_, estensioni per _VSCode_ (in particolare averne creata una _ad-hoc_ per i report di _Gradle_) e macchine virtuali in locale per testare l'applicativo su diversi sistemi operativi. Aggiungo infine che sono molto propenso a continuare lo sviluppo di questo software per provare a fornire un'alternativa gratuita ed _open source_ a quelli attualmente in utilizzo nel panorama automobilistico, soprattuto amatoriale.

### 4.1.2 Todeschi Matteo
Lavorare a questo progetto è stato molto interessante per diversi motivi. In primis ho apprezzato molto lavorare su un qualcosa che potrebbe essere utilizzato nel mondo reale, in secondo luogo è stato probabilmente il primo grande progetto.
Questo mi ha permesso di imparare molto su come si sviluppa un progetto e, soprattutto, di mettere in pratica le conoscenze acquisite.
Mi sono reso conto che le prime volte che cominciavo a lavorarci sopra mi sentivo un po' perso, ma con il passare del tempo ho iniziato a capire come muovermi, dunque realizzare piccole variazioni o nuove funzionalità non mi sembrava più così difficile.
Devo inoltre riconoscere l'importanza del tempo dedicato alla progettazione e all'analisi, che mi ha permesso di avere una visione chiara di quello che dovevo fare fin dal primo momento.
Un altro aspetto che mi ha reso molto fiero è il fatto di utiizzare tecnologie e librerie che non avevo mai utilizzato prima in ottica di costruire applicazioni più complesse, rendendomi quindi cosapevole di come anche in ambito professionale venga utilizzato spesso questo approccio per la produzione di software.
Un ultimo appunto che devo fare è che l'utilizzo di strumenti per migliorare la qualità del codice, come _Checkstyle_, _SpotBugs_ e _PMD_, mi ha permesso di scrivere in modo più pulito e mantenibile, facilitando quindi non soltanto la lettura del mio codice ad altri, ma anche la mia stessa comprensione del codice scritto in precedenza.
Concludo dicendo che, come ad altri membri del gruppo, mi piacerebbe continuare a lavorare su questo progetto, in modo da poterlo rendere un prodotto aperto e utilizzabile da chiunque, cercando di generalizzare al massimo le funzionalità e rendendolo facilmente estendibile e personalizzabile.

### 4.1.3 Crimaldi Ivan
Il mio ruolo principale è stato quello di progettare ed implementare il sistema di gestione file ed assicurarmi che interagisse correttamente con il Controller e le strutture dati definite assieme al resto del gruppo, come ElaboratedData. Mi ritengo parzialmente soddisfatto del lavoro compiuto: è mancante la parte di preset e ripristino di preferenze, ma considero il filemanager molto robusto e facilmente estendibile (aggiungere nuovi formati di file richiede solo una minima modifica alla Factory). Nel gruppo c'è sempre stato un clima di collaborazione, e la fase di analisi svolta assieme ci ha permesso di iniziare il progetto con un'idea chiara su come partire e cosa fare. Ammetto che avrei dovuto passare alla fase di implementazione senza fermarmi più del dovuto sulla fase di design. Lavorare a questo progetto mi ha insegnato molto su cosa significa sviluppare in gruppo, e l'importanza di rendere ogni componente quanto più indipendente possibile dalle altre; inoltre mi ha insegnato l'importanza dell'utilizzo del SCM (Git), e degli strumenti per migliorare la leggibilità e la qualità del codice (come CheckStyle). In futuro, per espandere il progetto, vorrei aggiungere la parte di preset impostazioni e preferenze, e supporto per nuovi formati.

### 4.1.4 Napolitano Cristofer
L'idea di lavorare su un progetto che avrebbe avuto dei riscontri nel mondo reale sicuramente è stata una delle fonti di maggior stimolo per me, soprattutto nella ricerca di una soluzione che mi potesse dare in futuro la possibilità di espandere il progetto, proprio per questo le nozioni sui pattern e i principi di programmazione appresi durante il corso hanno subito trovato un luogo nel quale esser applicati e compresi appieno nelle nuove possibilità di espansione che offrono. Per appunto, la cosa che mi ha lasciato più soddisfatto dal lavoro svolto sono l'utilizzo estensivo di pattern come Strategy che mi daranno in futuro la possibilità di ampliare la soluzione.
Dirigendomi verso le note di più amare dello sviluppo del progetto sicuramente c'è stata la fase di analisi e design architetturale, considerando la mia inesperienza, e quella del gruppo, avremmo potuto perder meno tempo ed esser molto più obiettivi nei nostri lavori iniziali, questo è stato il motivo principale per la consegna in ritardo. Se fossi stato meno vago nel mio design iniziale, e quindi se avessi avuto più esperienza, sarei sicuramente stato in grado finire con largo anticipo la mia parte di model e di dare una mano molto più concreta nelle implementazioni del controller e della view senza lasciare un onere troppo grande ai miei colleghi.
Nel breve futuro spero vivamente di trovare il tempo di aggiungere implementazioni ulteriori al mio modello di simulazione, così da ricreare il comportamento di un veicolo con maggior fedeltà.

## 4.2 Difficoltà incontrate e commenti per i docenti
### 4.2.1 Porcheddu Alessandro
Le difficoltà iniziali accennate nel paragrafo precedente fanno riferimento al non aver mai svolto un lavoro di gruppo prima d'ora, il che ha portato a una mia sottovalutazione della necessità di organizzarsi fin da subito in maniera efficace. Ho personalmente riscontrato anche difficoltà soprattutto nella parte iniziale di analisi, per la quale mi sembrava di avere pochi strumenti a mia disposizione. In particolare sarebbe stato utile aver avuto maggiore esposizione a sviluppare concetti _UML_ con una struttura guidata. Non nascondo, infatti, che la maggior difficoltà è stata mettersi a scrivere codice con ancora qualche dubbio sull'analisi e il design di alto livello. A questo proposito suggerirei ai docenti di fornine qualche tipo di strumento (anche magari una semplice _check list_) che permetta agli studenti di aver dei parametri oggettivi su cui basarsi per capire quando poter passare all'implementazione con un grado di confidenza mediamente alto.

### 4.2.2 Todeschi Matteo
La difficoltà principale cho ho riscontrato è stata principalmente quella di dover lavorare in gruppo, cosa che non avevo mai fatto prima su scala così grande.
Questo mi ha portato a dover imparare a gestire le dinamiche di gruppo, e soprattutto mi ha fatto capire l'importanza di avere una buona comunicazione e collaborazione tra i membri del gruppo, cosa che purtroppo non sempre è stata presente.
Una conseguente difficoltà è stata quella di trovarsi a iniziare a scrivere il codice così tardi rispetto alla deadline prefissata per la consegna del progetto, cosa che ha portato a dover lavorare in modo molto intenso per cercare di recuperare il tempo perso pur comunque sforando il limite massimo.
Credo infatti che sia stato impiegato troppo tempo nella fase di analisi e progettazione, che sebbene sia stata molto utile per avere una visione chiara del progetto, avrebbe potuto essere fatta in modo più rapido.
Inoltre il dover partecipare alle numerose lezioni del secondo semestre e iniziare la sessione estiva ha fatto slittare di molto la data che ci eravamo prefissati per l'inizio dell'implementazione.
Come consiglio per i docenti, oltre che concordare con il collega Alessandro su delle eventuali _check list_ da seguire, chiederei di rivalutare la scelta di non far coincidere la deadline con il periodo delle lezioni: capisco i motivi che hanno portato a questa scelta, ma credo che sarebbe più motivante per gli studenti poter lavorare al progetto nel periodo che preferiscono.

# Appendice A - Guida utente
## A.1 Schermata Home

In questa schermata l'utente può rapidamente decidere di premere i pulsanti che, in ordine dall'alto verso il basso, permettono di:
- **Dyno**: accedere alle prove con strumentazione fisica (OBD2 o banco prova)
- **Simulation**: accedere alla sala simulazioni
- **Charts**: accedere all'interfaccia per confrontare i grafici salvati
- **Settings**: accedere alle impostazioni utente che servono per gestire sia simulazione che prove fisiche  

![](src/main/resources/images/Main_View.png)

## A.2 Demo del software
Per avviare una demo dell'applicativo basta cliccare il pulsante per entrare nella **simulazione** e successivamente premere **Start Simulation**.
Verrà simulata una prova a banco di un motore endotermico generico che terminerà in automatico una volta raggiunto il numero di giri impostato nei [Settings](#a3-impostazioni-utente).
Una volta conclusa la prova si potrà:
- farla ripartire da zero
- salvare i dati generati dalla simulazione in un file
- importare dei dati per confrontarli _on-the-fly_
- tornare alla [Schermata Home](#a1-schermata-home)

![](src/main/resources/images/Simulation_View.png)

## A.3 Impostazioni utente
In questa sezione si possono personalizzare i valori di _default_ per quanto riguarda simulazione e prova reale.
Si può anche scegliere il tipo di dinamometro da utilizzare per la prova (quindi reale oppure attraverso OBD2) attraverso una selezione a tendina.
Si noti che il pulsante **Save** salva i dati in un file nella _home directory_ dell'utente che verrà poi utilizzato da quel momento in avanti.

## A.4 Confronto grafici
L'interfaccia di questa schermata è molto semplice e permette di importare dei grafici attraverso il relativo pulsante.
Una volta importato almeno un grafico si potrà scegliere di nascondere o rivelare le curve importate con il pulsante nella parte inferiore e il relativo menù a comparsa.

![](src/main/resources/images/Charts_View.png)

# Appendice B - Esercitazioni di laboratorio
## B.0.1 alessandro.porcheddu@studio.unibo.it
- Laboratorio 07: https://virtuale.unibo.it/mod/forum/discuss.php?d=177162#p246062
- Laboratorio 08: https://virtuale.unibo.it/mod/forum/discuss.php?d=178723#p247255
- Laboratorio 09: https://virtuale.unibo.it/mod/forum/discuss.php?d=179154#p248270
- Laboratorio 10: https://virtuale.unibo.it/mod/forum/discuss.php?d=180101#p248857
- Laboratorio 11: https://virtuale.unibo.it/mod/forum/discuss.php?d=181206#p250774
- Laboratorio 12: https://github.com/D3stan/oop-lab12

## B.0.2 matteo.todeschi@studio.unibo.it

- Laboratorio 06: https://virtuale.unibo.it/mod/forum/discuss.php?d=176282#p244944
- Laboratorio 07: https://virtuale.unibo.it/mod/forum/discuss.php?d=177162#p245974
- Laboratorio 08: https://virtuale.unibo.it/mod/forum/discuss.php?d=178723#p247203
- Laboratorio 09: https://virtuale.unibo.it/mod/forum/discuss.php?d=179154#p248253
- Laboratorio 10: https://virtuale.unibo.it/mod/forum/discuss.php?d=180101#p249647
- Laboratorio 11: https://virtuale.unibo.it/mod/forum/discuss.php?d=181206#p250772
- Laboratorio 12: https://github.com/TodeschiMatteo/oop-lab12.git