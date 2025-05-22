
# Food Delivery Platform – Java OOP Project

## Descriere generală

Această aplicație simulează o platformă de tip food delivery, în care utilizatorii pot comanda produse de la restaurante, iar șoferii le livrează. Aplicația este implementată folosind principiile programării orientate pe obiecte (OOP) în Java.

## Structura proiectului

1. **Pachetul `model`**:
    - **Person.java**: Clasă abstractă pentru a defini caracteristicile comune ale persoanelor (utilizatori și șoferi).
    - **User.java**: Clasa care moștenește `Person` și reprezintă un utilizator (client) al platformei.
    - **Driver.java**: Clasa care moștenește `Person` și reprezintă un șofer care livrează comenzi.
    - **Restaurant.java**: Reprezintă un restaurant de pe platformă.
    - **MenuItem.java**: Reprezintă un produs de meniu disponibil pentru comandă, cu preț și metodă de comparare a prețurilor.
    - **Address.java**: Reprezintă o adresă pentru livrare.
    - **Order.java**: Reprezintă o comandă plasată de un utilizator, cu lista de produse și adresa de livrare.
    - **Delivery.java**: Reprezintă livrarea unei comenzi de către un șofer.
    - **Review.java**: Permite utilizatorilor să lase recenzii restaurantelor.

2. **Pachetul `service`**:
    - **DeliveryService.java**: Conține logica de gestionare a utilizatorilor, restaurantelor, meniurilor și comenzilor.

3. **Pachetul `util`**:
    - **OrderComparatorByPrice.java**: Comparator care sortează comenzile în funcție de prețul total al acestora.

4. **Clasa principală `Main.java`**: 
    - Se ocupă cu testarea funcționalității aplicației, instanțierea obiectelor și apelarea metodelor din clasele de serviciu.

## Cerințele îndeplinite

### 1) Definirea sistemului
Am creat o listă de cel puțin 10 acțiuni/interogări care pot fi efectuate în cadrul platformei:
   - Adăugarea unui utilizator (client).
   - Adăugarea unui restaurant.
   - Adăugarea unui meniu pentru un restaurant.
   - Adăugarea unui produs în meniul unui restaurant.
   - Vizualizarea meniului unui restaurant.
   - Crearea unei comenzi de la un utilizator.
   - Vizualizarea detaliilor unei comenzi.
   - Calcularea prețului total al unei comenzi.
   - Livrarea unei comenzi de către un șofer.
   - Adăugarea unei recenzii pentru un restaurant.

Am creat și o listă cu cel puțin 8 tipuri de obiecte:
   - `Person`, `User`, `Driver`, `Restaurant`, `MenuItem`, `Address`, `Order`, `Delivery`, `Review`.

### 2) Implementare
Am implementat aplicația folosind următoarele concepte:

#### Clase simple cu atribute private / protected și metode de acces:
   - Atributele sunt private pentru a respecta principiul incapsulării. Metodele getter și setter sunt folosite pentru accesarea acestora.

#### Cel puțin 2 colecții diferite capabile să gestioneze obiectele definite:
   - **List**: Folosită pentru gestionarea utilizatorilor și comenzilor.
   - **Map**: Folosită pentru a mapa restaurantele la meniurile lor.
   - **Set (TreeSet)**: Folosită pentru a stoca produsele meniului, astfel încât să fie sortate după preț.

#### Utilizarea moștenirii pentru crearea de clase adiționale:
   - **User** și **Driver** moștenesc `Person`, având caracteristici comune, dar și comportamente specifice.

#### Cel puțin o clasă serviciu care expune operațiile sistemului:
   - **DeliveryService.java** expune operațiile de adăugare a utilizatorilor, restaurantelor, meniurilor și comenzilor.

#### Clasa `Main` care face apeluri către servicii:
   - În `Main.java`, am creat obiecte pentru utilizatori, restaurante, meniuri, comenzi și am efectuat acțiuni pentru a testa logica aplicației.

### 3) Persistență cu JDBC
In lucru.

### 4) Serviciu de audit
In lucru.

## Instrucțiuni de rulare

1. Clonează sau descarcă acest proiect pe computerul tău.
2. Deschide proiectul într-un IDE (IntelliJ IDEA sau Eclipse).
3. Compilează și rulează aplicația prin clasa `Main.java`.

