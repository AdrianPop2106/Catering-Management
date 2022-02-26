package bussiness_layer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clasa Order are rolul de reprezentare a unei comenzi care poate sa o aplice un client. Aceasta are ca si atribute ID-ul
 * clientului, ID-ul comenzii, data comenzii, ora comenzii, daca acesta a fost procesat de un angajat si lista de produse
 * din comanda
 */
public class Order implements Serializable {
    private final int orderID;
    private final int clientID;
    private final LocalDate orderDate;
    private final LocalTime orderHour;
    private final ArrayList<MenuItem> items;
    private boolean processed;

    /**
     * Se initializeaza obiectul cu parametrii dati
     * @param oID int ID-ul comenzii
     * @param cID int ID-ul clientului
     * @param d LocalDate data comenzii
     * @param hour LocalHour ora comenzii
     * @param m ArrayList<MenuItem> lista de produse din comanda
     */
    public Order(int oID, int cID, LocalDate d, LocalTime hour, ArrayList<MenuItem> m){
        orderID=oID;
        clientID=cID;
        orderDate=d;
        this.orderHour=hour;
        items=new ArrayList<>(m);
        processed=false;
    }

    /**
     * Metoda folosita pentru a oferii o functie de dispersie unica pentru obiectele din clasa Order
     * @return int
     */
    @Override
    public int hashCode(){
        int value;
        value=orderID+clientID+(orderDate.getDayOfYear()*orderDate.getMonthValue())+orderHour.getHour();
        return value;
    }

    /**
     * Metoda va returna suma preturilor tuturor produselor din lista items
     * @return int
     */
    public int getPrice(){
        int total=0;
        for(MenuItem i:items)
            total+=i.computePrice();
        return total;
    }

    /**
     * Metoda va returna atributul items
     * @return ArrayList<MenuItem>
     */
    public ArrayList<MenuItem> getItems() {
        return items;
    }

    /**
     * Metoda va returna atributul orderHour
     * @return LocalTime
     */
    public LocalTime getOrderHour() {
        return orderHour;
    }
    /**
     * Metoda va returna atributul orderDate
     * @return LocalDate
     */
    public LocalDate getOrderDate() {
        return orderDate;
    }
    /**
     * Metoda va returna atributul clientID
     * @return int
     */
    public int getClientID() {
        return clientID;
    }
    /**
     * Metoda va returna atributul orderID
     * @return int
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Metoda va seta parametrul processed ca fiind true
     */
    public void process(){processed=true;}

    /**
     * Metoda va returna atributul processed
     * @return boolean
     */
    public boolean getProcessed(){return processed;}

}
