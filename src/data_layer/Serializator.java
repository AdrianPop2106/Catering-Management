package data_layer;

import bussiness_layer.BaseProduct;
import bussiness_layer.CompositeProduct;
import bussiness_layer.MenuItem;
import bussiness_layer.Order;
import presentation_layer.EmployeePage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clasa Serializator ofera metode folosite pentru serializarea si deserealizarea datelor din DeliveryService in fisierul
 * "file.txt"
 */
public class Serializator {
    /**
     * Metoda va deserealiza continutul din fisierul "file.txt" in parametrii items si order
     * @param items ArrayList<MenuItem> lista de produse din meniu
     * @param orders Map<Order, ArrayList<MenuItem>> comenzile aplicate
     */
    public static void deserealize(ArrayList<MenuItem> items, Map<Order, ArrayList<MenuItem>> orders){
        if (new File("file.txt").exists()) {
            try {
                boolean ok=true;
                ArrayList<Order> l=new ArrayList<>();
                FileInputStream inp = new FileInputStream("file.txt");
                ObjectInputStream inpValue = new ObjectInputStream(inp);
                while(ok){
                    Object o=inpValue.readObject();
                    if(o==null)
                        ok=false;
                    if(o instanceof BaseProduct)
                        items.add((BaseProduct)o);
                    else if(o instanceof CompositeProduct)
                        items.add((CompositeProduct)o);
                    else if(o instanceof HashMap)
                        orders.putAll((Map<Order,ArrayList<MenuItem>>) o);
                }
                inp.close();
            } catch (IOException | ClassNotFoundException e) {

            }
        }
    }

    /**
     * Metoda va serializa continutul din parametrii products si orders in fisierul "file.txt"
     * @param products ArrayList<MenuItem> lista de produse din meniu
     * @param orders  HashMap<Order, ArrayList<MenuItem>> comenzile aplicate
     */
    public static void serialize(ArrayList<MenuItem> products, HashMap<Order, ArrayList<MenuItem>> orders){
        try {
            new FileWriter("file.txt",false).close();

            FileOutputStream outp = new FileOutputStream("file.txt");
            ObjectOutputStream outpValue = new ObjectOutputStream(outp);

            for(MenuItem i:products)
                outpValue.writeObject(i);

            outpValue.writeObject(orders);
            outp.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
