package bussiness_layer;

import java.awt.*;
import java.io.Serializable;

/**
 * Clasa MenuItem reprezinta orice produs din cadrul meniului firmei de catering. Acesta are ca si atribute un String ce
 * reprezinta titlul produsului si un int ce reprezinta pretul produsului
 */
public class MenuItem implements Serializable {
    private String title;
    protected int price;

    /**
     * Constructorul va initializa atributele cu datele furnizate ca parametrii
     * @param title String titlul produsului
     * @param price int pretul produsului
     */
    MenuItem(String title,int price){
        this.title=title;
        this.price=price;
    }

    /**
     * Metoda va returna atributul price
     * @return int
     */
    public int computePrice(){
        return price;
    }

    /**
     * Metoda va returna atributul title
     * @return String
     */
    public String getTitle() { return title;}

    /**
     * Metoda va seta valaorea parametrului title la atributul title
     * @param title String titlul produsului
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Metoda va seta valoarea parametrului price la atributul price
     * @param price int pretul produsului
     */
    public void setPrice(int price){this.price=price;}

}
