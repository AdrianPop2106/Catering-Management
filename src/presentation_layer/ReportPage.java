package presentation_layer;

import bussiness_layer.DeliveryService;

import javax.swing.*;

/**
 * Clasa ReportPage va creea o fereastra prin care administratorul va putea genera rapoarte sub forma unor JTables pe baza unor
 * criterii scrise in casetele text din dreptul butoanelor. Clasa are ca si parametrii un obiect DeliveryService d si mai multe
 * obiecte din clasa JTextField:startH,endH,clientNr,productNr,priceValue,day,month
 */
public class ReportPage extends JFrame {
    DeliveryService d;
    JTextField startH,endH,clientNr,productNr,priceValue,day,month;

    /**
     * Constructorul va creea o fereastra prin care administratorul va putea creea rapoarte in functie de butonul apasat si de
     * datele notate in casutele text corespunzatoare. Rapoartele iau forma unei noi ferestre cu un JTable ce va contine datele
     * produselor, ofertelor sau clientilor ce se incadreaza in conditiile raportului ales
     * @param d DeliveryService serviciul de livrare
     */
    public ReportPage(DeliveryService d){
        this.d=d;

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300,250);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        startH=new JTextField();
        startH.setBounds(160,23,50,20);
        endH=new JTextField();
        endH.setBounds(215,23,50,20);
        productNr=new JTextField();
        productNr.setBounds(160,63,50,20);
        clientNr=new JTextField();
        clientNr.setBounds(160,103,50,20);
        priceValue=new JTextField();
        priceValue.setBounds(160,143,50,20);
        day=new JTextField();
        day.setBounds(160,183,50,20);
        month=new JTextField();
        month.setBounds(215,183,50,20);

        JButton[] reports=new JButton[5];
        reports[0]=new JButton("Hour Interval");
        reports[0].setBounds(20,20,135,25);
        reports[0].addActionListener(e -> {
            if(checkText(startH.getText()) && checkText(endH.getText()))
                if((Integer.parseInt(startH.getText())>=1 && Integer.parseInt(startH.getText())<=24)
                        && (Integer.parseInt(endH.getText())>=1 && Integer.parseInt(endH.getText())<=24)){
                    String[] s=new String[]{"Order","Hour"};
                    createReport(d.reportTime(Integer.parseInt(startH.getText()),Integer.parseInt(endH.getText())),s,new Integer[]{75,75,150});
                }
        });
        reports[1]=new JButton("Products ordered");
        reports[1].setBounds(20,60,135,25);
        reports[1].addActionListener(e -> {
            if(checkText(productNr.getText()))
                if(Integer.parseInt(productNr.getText())>0){
                    String[] s=new String[]{"Product","Orders"};
                    createReport(d.reportNumber(Integer.parseInt(productNr.getText())),s,new Integer[]{270,50,320});
                }
        });
        reports[2]=new JButton("Clients");
        reports[2].setBounds(20,100,135,25);
        reports[2].addActionListener(e -> {
            if(checkText(clientNr.getText()))
                if(Integer.parseInt(clientNr.getText())>0){
                    String[] s=new String[]{"First Name","Last Name"};
                    createReport(d.reportClient(Integer.parseInt(clientNr.getText())),s,new Integer[]{90,90,180});
                }
        });
        reports[3]=new JButton("Order Values");
        reports[3].setBounds(20,140,135,25);
        reports[3].addActionListener(e -> {
            if(checkText(priceValue.getText()))
                if(Integer.parseInt(priceValue.getText())>0){
                    String[] s=new String[]{"Order","Price"};
                    createReport(d.reportValue(Integer.parseInt(priceValue.getText())),s,new Integer[]{90,90,180});
                }
        });
        reports[4]=new JButton("Product Date");
        reports[4].setBounds(20,180,135,25);
        reports[4].addActionListener(e -> {
            if(checkText(day.getText()) && checkText(month.getText())){
                if((Integer.parseInt(day.getText())>=1 && Integer.parseInt(day.getText())<=31)
                        && (Integer.parseInt(month.getText())>=1 && Integer.parseInt(month.getText())<=12)){
                    String[] s=new String[]{"Product","Bought"};
                    createReport(d.reportDay(Integer.parseInt(day.getText()),Integer.parseInt(month.getText())),s,new Integer[]{250,70,320});
                }
            }
        });

        for(int i=0;i<5;i++)
            this.getContentPane().add(reports[i]);
        this.getContentPane().add(startH);
        this.getContentPane().add(endH);
        this.getContentPane().add(clientNr);
        this.getContentPane().add(productNr);
        this.getContentPane().add(priceValue);
        this.getContentPane().add(day);
        this.getContentPane().add(month);
        this.setVisible(true);
    }

    /**
     * Metoda primeste ca si parametru un obiect String s. Metoda va returna false daca s are lungime 0 sau daca acesta contine
     * caractere care nu sunt cifre. In caz contrar se returneaza true
     * @param s String textul de verificat
     * @return boolean
     */
    private boolean checkText(String s){
        if(s.length()==0)
            return false;
        for(int i=0;i<s.length();i++)
            if(!(s.charAt(i)>='0' && s.charAt(i)<='9'))
                return false;
        return true;
    }

    /**
     * Metoda primeste ca si parametrii un 2D array de String-uri info, un array de obiecte String fields si un array de numere intregi.
     * Metoda va genera o noua fereastra cu un JTable ce va avea coloanele reprezentate de fields, infirmatia continuta in info si
     * unde dimensiunea coloanelor si a ferestrei in sine va depinde de valorile stocate in sizes.
     * @param info String[][] informatia din tabel
     * @param fields String[] numele coloanelor
     * @param sizes Integer[][] latimea coloanelor si a ferestrei
     */
    private void createReport(String[][] info,String[] fields,Integer[] sizes){
        JFrame f=new JFrame();
        JTable j=new JTable(info,fields);
        j.getColumnModel().getColumn(0).setPreferredWidth(610);
        f.setLocationRelativeTo(null);
        j.getColumnModel().getColumn(0).setPreferredWidth(sizes[0]);
        j.getColumnModel().getColumn(1).setPreferredWidth(sizes[1]);
        f.add(new JScrollPane(j));
        f.setSize(sizes[2],150);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }
}
