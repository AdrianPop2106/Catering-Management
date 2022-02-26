package presentation_layer;

import bussiness_layer.*;
import data_layer.Serializator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clasa AdministratorPage va creea o fereastra prin care administratorul isi va putea realiza activitati de creeare
 * si modificare a produselor din meniu. Aceasta clasa are ca si parametrii un obiect DeliveryService d, un array
 * de JTextFields fields, un obiect JTextField productInfo, un obiect JTextArea compositeInfo, un obiect JComboBox product
 * si un ArrayList de obiecte MenuItem
 */
public class AdministratorPage extends JFrame {
    DeliveryService d;
    JTextField[] fields;
    JTextField productInfo;
    JTextArea compositeInfo;
    JComboBox product;
    ArrayList<MenuItem> currentCompositeProduct;

    /**
     * Constructorul va creea fereastra prin care administratorul va putea vizualiza imformatii despre produse, va putea
     * creea si modifica produse existente cu informatii scrise in elementele din fields, va sterge produsele selectate
     * in product, va putea adauga produse pentru un nou obiect CompositeProduct si creearea obiectului CompositeProduct
     * daca lista nu este goala
     * @param employee EmployeePage fereastra angajatului
     */
    AdministratorPage(EmployeePage employee){
        d=new DeliveryService(employee);
        currentCompositeProduct=new ArrayList<>();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(670,340);
        this.setLocationRelativeTo(null);

        JButton importer=new JButton("Import products");
        importer.addActionListener(e -> {
            d.importing();
            updateCombo();
        });
        importer.setBounds(20,10,140,25);
        JButton adder=new JButton("Add Product");
        adder.addActionListener(e -> {
            boolean ok=true;
            for(int i=0;i<7;i++)
                if(fields[i].getText().equals(""))
                    ok=false;
            if(ok){
                if(checkText(fields[0].getText()) && checkFloat(fields[1].getText()) && checkInt(fields[2].getText())
                        && checkInt(fields[2].getText()) && checkInt(fields[3].getText()) && checkInt(fields[4].getText())
                            && checkInt(fields[5].getText()) && checkInt(fields[6].getText())){
                    String[] s=new String[7];
                    s[0]=fields[0].getText();
                    s[1]=fields[1].getText();
                    s[2]=fields[2].getText();
                    s[3]=fields[3].getText();
                    s[4]=fields[4].getText();
                    s[5]=fields[5].getText();
                    s[6]=fields[6].getText();
                    for(int i=0;i<7;i++)
                        fields[i].setText("");
                    d.createProduct(s);
                    updateCombo();
                }

            }
        });
        adder.setBounds(20,110,140,25);
        JButton modifyer=new JButton("Modify Product");
        modifyer.addActionListener(e -> {
            if(!(product.getSelectedItem()).equals("Select Product")){
                String[] s=new String[7];
                for(int i=0;i<7;i++)
                    if((i==1 && checkFloat(fields[i].getText())) || checkInt(fields[i].getText())){
                        s[i]=fields[i].getText();
                        fields[i].setText("");
                    }
                    else
                        s[i]="";
                d.modifyProduct(s,(String)product.getSelectedItem());
                updateCombo();
            }
        });
        modifyer.setBounds(170,110,140,25);
        JButton deleter=new JButton("Delete Product");
        deleter.setBounds(320,110,140,25);
        deleter.addActionListener(e -> {
            if(!product.getSelectedItem().equals("Select Product")){
                d.deleteProduct((String)product.getSelectedItem());
                updateCombo();
            }
        });
        JButton disconnect=new JButton("Disconnect");
        disconnect.setBounds(500,260,140,25);
        disconnect.addActionListener(e -> {
            new Login(employee);
            this.dispose();
        });
        JButton report=new JButton("Report");
        report.setBounds(20,150,140,25);
        report.addActionListener(e -> new ReportPage(d));
        JButton composer=new JButton("Add to composite");
        composer.addActionListener(e -> {
            String s=(String)product.getSelectedItem();
            if(!s.equals("Select Product")){
                currentCompositeProduct.add(d.get(s));
                writeCompositeInfo();
            }
        });
        composer.setBounds(20,190,140,25);
        JButton creator=new JButton("Create composite");
        creator.addActionListener(e -> {
            if(!fields[0].getText().equals("")){
                d.createComposite(fields[0].getText(),currentCompositeProduct);
                compositeInfo.setText("");
                fields[0].setText("");
                currentCompositeProduct.clear();
                updateCombo();
            }
            else JOptionPane.showMessageDialog(new JFrame(),"No name for product!");
        });
        creator.setBounds(20,225,140,25);
        JButton deleteComposite=new JButton("Delete from composite");
        deleteComposite.setBounds(20,260,170,25);
        deleteComposite.addActionListener(e -> {
            String s=(String)product.getSelectedItem();
            if(!s.equals("Select Product")){
                currentCompositeProduct.remove(d.get(s));
                writeCompositeInfo();
            }

        });

        product=new JComboBox();
        updateCombo();
        product.setBounds(170,10,150,25);
        product.addItemListener(e -> {
            if(product.getItemCount()!=0){
                if(product.getSelectedItem().equals("Select Product"))
                    productInfo.setText("");
                else
                {
                    MenuItem m=d.get((String)product.getSelectedItem());
                    if(m instanceof BaseProduct)
                        productInfo.setText((m.getTitle()+":rating "+((BaseProduct)m).getRating()+";calories "+((BaseProduct)m).getCalories()
                                +";proteins "+((BaseProduct)m).getProteins()+";fat "+((BaseProduct)m).getSodium()+";sodium "
                                +((BaseProduct)m).getSodium()+";price "+ m.computePrice()+"$"));
                    else if(m instanceof CompositeProduct)
                        productInfo.setText(((CompositeProduct) m).getItems());
                }
            }
        });

        productInfo=new JTextField(15);
        productInfo.setEditable(false);
        productInfo.setBounds(20,40,620,20);

        JLabel[] names=new JLabel[7];
        names[0]=new JLabel("Title");
        names[0].setBounds(20,60,80,20);
        names[1]=new JLabel("Rating");
        names[1].setBounds(110,60,80,20);
        names[2]=new JLabel("Calories");
        names[2].setBounds(200,60,80,20);
        names[3]=new JLabel("Protein");
        names[3].setBounds(290,60,80,20);
        names[4]=new JLabel("Fat");
        names[4].setBounds(380,60,80,20);
        names[5]=new JLabel("Sodium");
        names[5].setBounds(470,60,80,20);
        names[6]=new JLabel("Price");
        names[6].setBounds(560,60,80,20);

        fields=new JTextField[7];
        for(int i=0;i<7;i++)
            fields[i]=new JTextField();

        fields[0].setBounds(20,80,80,20);
        fields[1].setBounds(110,80,80,20);
        fields[2].setBounds(200,80,80,20);
        fields[3].setBounds(290,80,80,20);
        fields[4].setBounds(380,80,80,20);
        fields[5].setBounds(470,80,80,20);
        fields[6].setBounds(560,80,80,20);

        compositeInfo=new JTextArea();
        compositeInfo.setEditable(false);
        compositeInfo.setCaretPosition(0);
        JScrollPane sp=new JScrollPane(compositeInfo);
        sp.setBounds(170, 150, 470, 100);

        this.getContentPane().setLayout(null);
        this.getContentPane().add(importer);
        this.getContentPane().add(productInfo);
        this.getContentPane().add(product);
        this.getContentPane().add(adder);
        this.getContentPane().add(modifyer);
        this.getContentPane().add(deleter);
        this.getContentPane().add(report);
        this.getContentPane().add(composer);
        this.getContentPane().add(creator);
        this.getContentPane().add(sp);
        this.getContentPane().add(deleteComposite);
        this.getContentPane().add(disconnect);
        for(int i=0;i<7;i++){
            this.getContentPane().add(fields[i]);
            this.getContentPane().add(names[i]);
        }


        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Metoda va scrie in compositeInfo informatii legate de produsele din lista currentCompositeProduct urmat de pretul
     * total al tuturor produselor
     */
    private void writeCompositeInfo(){
        compositeInfo.setText("");
        int total=0;
        for(MenuItem m:currentCompositeProduct){
            compositeInfo.append(m.getTitle()+":"+m.computePrice()+"$\n");
            total+=m.computePrice();
        }
        if(currentCompositeProduct.size()!=0)
            compositeInfo.append("Total="+total+"$");
    }

    /**
     * Metoda va actualiza product cu toate produsele curente din d
     */
    private void updateCombo(){
        product.removeAllItems();
        product.addItem("Select Product");
        String[] items=d.returnMenu();
        for (String item : items) product.addItem(item);
        ArrayList<MenuItem> products=d.getItems();
        Map<Order,ArrayList<MenuItem>> o=d.getOrders();
        Serializator.serialize(products, (HashMap<Order, ArrayList<MenuItem>>) o);
    }

    /**
     * Metoda verifica daca s este format doar din litere si spatii si returneaza true daca respecta conditiile si false in caz
     * contrar
     * @param s String textul de verificat
     * @return boolean
     */
    private boolean checkText(String s){
        for(int i=0;i<s.length();i++)
            if(!((s.charAt(i)>='a' && s.charAt(i)<='z') || (s.charAt(i)>='A' && s.charAt(i)<='Z')))
                if(s.charAt((i))!=' ')
                    return false;
            return true;
    }

    /**
     * Metoda va verifica daca s reprezinta o numar real si returneaza true daca respecta conditiile si false in caz
     * contrar
     * @param s String textul de verificat
     * @return boolean
     */
    private boolean checkFloat(String s){
        for(int i=0;i<s.length();i++)
            if(s.charAt(i)<'0' || s.charAt(i)>'9')
                if(s.charAt(i)!='.')
                    return false;
        return true;
    }

    /**
     * Metoda va verifica daca s reprezinta un numar intreg si returneaza true daca respecta conditiile si false in caz
     * contrar
     * @param s String textul de verificat
     * @return boolean
     */
    private boolean checkInt(String s){
        for(int i=0;i<s.length();i++)
            if(s.charAt(i)<'0' || s.charAt(i)>'9')
                    return false;
        return true;
    }
}
