package presentation_layer;

import bussiness_layer.*;
import bussiness_layer.MenuItem;
import data_layer.Serializator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Clasa ClientPage este folosita pentru a creea o fereastra GUI pentru ca clientii sa-si realizeze activitatile de vizualizare
 * de produse si de creeare de comenzi. Acesta are ca si atribute un String name ce reprezinta numele clientului,
 * un obiect DeliveryService, un obiect JTextField, un obiect JTextArea, un  obiect JComboBox si o lista de obiecte MenuItem
 */
public class ClientPage extends JFrame {
        String name;
        DeliveryService d;
        JTextField productInfo;
        JTextArea orderInfo;
        ArrayList<MenuItem> currentOrderProducts;
        JComboBox menu;

    /**
     * Constructorul va creea o fereastra prin care clientul va putea sa selecteze si sa vizualizeze informatii legate
     * de produse, sa deschida o fereastra de cautare, sa vizualizeze un JTable cu toate produsele valabile, sa
     * adauge produse la comanda, sa stearga produse din comanda si si plaseze comanda curenta daca este cel putin un produs
     * in aceata
     * @param name String numele clientului
     */
    ClientPage(String name,EmployeePage observer){
            d=new DeliveryService(observer);
            this.name=name;
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setSize(670,300);
            this.setLocationRelativeTo(null);

            currentOrderProducts= new ArrayList<>();

            JButton view=new JButton("Menu");
            JButton add=new JButton("Add to Order");
            JButton order=new JButton("Place Order");
            JButton search=new JButton("Search");
            JButton delete=new JButton("Delete from Order");
            JButton disconnect=new JButton("Disconnect");

            productInfo=new JTextField(15);
            productInfo.setEditable(false);

            menu=new JComboBox();
            menu.addItemListener(e -> {
                if(menu.getSelectedItem().equals("Select Product"))
                    productInfo.setText("");
                else
                {
                    bussiness_layer.MenuItem m=d.get((String)menu.getSelectedItem());
                    if(m instanceof BaseProduct)
                        productInfo.setText((m.getTitle()+":rating "+((BaseProduct)m).getRating()+";calories "+((BaseProduct)m).getCalories()
                                +";proteins "+((BaseProduct)m).getProteins()+";fat "+((BaseProduct)m).getSodium()+";sodium "
                                    +((BaseProduct)m).getSodium()+";price "+((BaseProduct)m).computePrice()+"$"));
                    else if(m instanceof CompositeProduct)
                        productInfo.setText(((CompositeProduct) m).getItems());
                }

            });
            String[] items=d.returnMenu();
            for(int i=0;i<items.length;i++)
                menu.addItem(items[i]);

            JTextField orders=new JTextField();
            orders.setSize(new Dimension(100,100));

            menu.setBounds(20,10,190,20);

            productInfo.setBounds(20,35,620,20);

            view.setBounds(20,60,140,25);
            view.addActionListener(e -> {
                String[][] data=d.view();
                String[] info={"Title","Rating","Calories","Proteins","Fat","Sodium","Price"};
                JFrame f=new JFrame();
                JTable j=new JTable(data,info);
                j.getColumnModel().getColumn(0).setPreferredWidth(610);
                f.setLocationRelativeTo(null);
                j.setBounds(30, 40, 400, 300);
                f.add(j);
                f.add(new JScrollPane(j));
                f.setSize(700,200);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setVisible(true);
            });
            add.setBounds(20,95,140,25);
            add.addActionListener(e-> {
                String s=(String)menu.getSelectedItem();
                if(!s.equals("Select Product")){
                    currentOrderProducts.add(d.get(s));
                    writeOrderInfo();
                }
            });
            delete.setBounds(20,130,140,25);
            delete.addActionListener(e -> {
                String s=(String)menu.getSelectedItem();
                if(!s.equals("Select Product")){
                    currentOrderProducts.remove(d.get(s));
                    writeOrderInfo();
                }

            });
            order.setBounds(20,165,140,25);
            order.addActionListener(e -> {
            int cID=1,oID=1;
                try {
                    Scanner s=new Scanner(new File("client.txt"));
                    while(s.hasNextLine()){
                        String[] temp=s.nextLine().split(" ");
                        if(name.equals(temp[0]+" "+temp[1]))
                            break;
                        cID++;
                    }
                    s.close();
                    ObjectInputStream possOrder = new ObjectInputStream(new FileInputStream("file.txt"));
                    oID=d.getOrders().size()+1;
                    possOrder.close();
                } catch (IOException err) {  }
                ArrayList<MenuItem> newList= (ArrayList<MenuItem>) currentOrderProducts.stream().collect(Collectors.toList());
                Collections.copy(newList,currentOrderProducts);
                d.order(new Order(oID,cID, LocalDate.now(),LocalTime.now(),newList),name);
                orderInfo.setText("");
                currentOrderProducts.clear();
                Serializator.serialize(d.getItems(), (HashMap<Order, ArrayList<MenuItem>>) d.getOrders());
                }
            );

            search.setBounds(20,200,140,25);
            search.addActionListener(e -> new Search(d));

            disconnect.setBounds(520,230,120,25);
            disconnect.addActionListener(e -> {
                new Login(observer);
                this.dispose();
            });

            orderInfo=new JTextArea();
            orderInfo.setEditable(false);
            orderInfo.setCaretPosition(0);

            JScrollPane sp=new JScrollPane(orderInfo);
            sp.setBounds(170, 60, 470, 165);

            this.getContentPane().add(menu);
            this.getContentPane().add(productInfo);
            this.getContentPane().add(view);
            this.getContentPane().add(add);
            this.getContentPane().add(delete);
            this.getContentPane().add(order);
            this.getContentPane().add(search);
            this.getContentPane().add(sp);
            this.getContentPane().add(disconnect);
            this.getContentPane().setLayout(null);
            this.setVisible(true);
        }

    /**
     * Metoda va scrie in orderInfo informatii legate de produsele din comanda si sa scrie la final pretul total pe
     * toata comanda
     */
        private void writeOrderInfo(){
            orderInfo.setText("");
            int total=0;
            for(MenuItem m:currentOrderProducts){
                orderInfo.append(m.getTitle()+":"+m.computePrice()+"$\n");
                total+=m.computePrice();
            }
            if(currentOrderProducts.size()!=0)
                orderInfo.append("Total="+total+"$");
        }

}
