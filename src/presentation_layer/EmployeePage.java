package presentation_layer;

import bussiness_layer.Order;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Clasa EmployeePage este folosită pentru a creea o fereastra GUI prin care angajatii vor fi notificati cand se depunde o noua comanda.
 * Angajatii vor putea procesa noile comenzi depuse. Clasa are ca si atribute un ArrayList de obiecte Order pending, un obiect JLabel
 * newOrders si un obiect JComboBox orders
 */
public class EmployeePage extends JFrame implements Observer {
    private ArrayList<Order> pending;
    private JLabel newOrders;
    private JComboBox orders;

    /**
     * Constructorul va creea o fereastra prin care un anagajat va putea selecta, daca sunt, comenzi pentru a le procesa
     */
    public EmployeePage(){
        pending=new ArrayList<>();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(300,160);
        this.setLocationRelativeTo(null);

        orders=new JComboBox();
        orders.addItem("Select Order");
        orders.setBounds(30,40,120,20);
        newOrders=new JLabel("There are no new orders");
        newOrders.setBounds(20,20,270,20);

        JButton process=new JButton("Process order");
        process.addActionListener(e -> {
            if(!orders.getSelectedItem().equals("Select Order")){
                String s=orders.getSelectedItem().toString();
                pending.get(orders.getSelectedIndex()-1).process();
                pending.remove(orders.getSelectedIndex()-1);
                updateCombo();
                if(pending.size()!=0)
                    newOrders.setText("There are "+pending.size()+" orders that need to be processed");
                else
                    newOrders.setText("There are no new orders");
                revalidate();
                repaint();
            }
        });
        JButton disconnect= new JButton("Disconnect");
        process.setBounds(30,70,120,20);
        disconnect.setBounds(30,95,120,20);
        disconnect.addActionListener(e -> disconnect());

        this.getContentPane().setLayout(null);
        this.getContentPane().add(newOrders);
        this.getContentPane().add(orders);
        this.getContentPane().add(process);
        this.getContentPane().add(disconnect);

    }

    /**
     * Daca lista pending nu este goala, atunci se va modifica textul din eticheta newOrders
     */
    public void login(){
        if(pending.size()!=0)
            newOrders.setText("There are "+pending.size()+" orders that need to be processed");
        this.setVisible(true);
    }

    /**
     * Metoda va creea un obiect Login si va inchide fereastra curenta
     */
    public void disconnect(){
        new Login(this);
        this.dispose();
    }

    /**
     * Metoda din interfata Observer prin care se va popula fereastra obiectul orders cu datele din lista orders si va modifica
     * eticheta newOrders
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg){
        this.getContentPane().remove(newOrders);
        pending.add((Order)arg);
        newOrders.setText("There are "+pending.size()+" orders that need to be processed");
        getContentPane().add(newOrders);
        orders.addItem("Order#"+((Order)arg).getOrderID());
        revalidate();
        repaint();
    }

    /**
     * Metoda va actualiza obiectul orders cu comenzile ramase
     */
    private void updateCombo(){
        orders.removeAllItems();
        orders.addItem("Select Order");
        for(Order i:pending)
            orders.addItem("Order#"+i.getOrderID());
    }


}
