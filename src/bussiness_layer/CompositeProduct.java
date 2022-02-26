package bussiness_layer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clasa CompositeProduct este folosita pentru a reprezenta un produs format dintr-o colectie de alte produse. Aceasta clasa
 * extinde MenuItem si are ca si atribut un ArrayList de obiecte MenuItem
 */
public class CompositeProduct extends MenuItem implements Serializable {
    ArrayList<MenuItem> parts;

    /**
     * Constructorul initializeaza atributele title cu valoarea parametrului title si price cu 0. Atributul parts primeste
     * elementele parametrului items
     * @param title String titlul produsului
     * @param items ArrayList de obiecte MenuItem ce reprezinta produsele componente
     */
    CompositeProduct(String title,ArrayList<MenuItem> items){
        super(title,0);
        parts=new ArrayList<MenuItem>();
        for(MenuItem i:items)
            parts.add(i);
    }

    /**
     * Metoda computePrice a lui MenuItem este suprascrisa.La prima apelare valoarea lui price este recalculata
     * ca fiind suma preturilor elementelor listei parts, dupa care este returnat price
     * @return int
     */
    @Override
    public int computePrice(){
        if(price==0){
            int total=0;
            for(MenuItem i:parts)
                total+=i.computePrice();
            price=total;
        }
        return price;
    }

    /**
     * Metoda returneaza un String ce este format din titlurile fiecarui produs din items urmat de suma valorilor returnate
     * de metoda computePrice a fiecarui element
     * @return String
     */
    public String getItems(){
        String s="";
        for(MenuItem m:parts)
            s+=m.getTitle()+";";
        return s+" total="+computePrice();
    }
}
