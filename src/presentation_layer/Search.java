package presentation_layer;

import bussiness_layer.DeliveryService;

import javax.swing.*;
import java.awt.*;

/**
 * Clasa Search va creea o fereastra prin care clientul va putea cauta produse pe baza unui keyword si le va ordona in
 * functie de atributul selectat. Clasa are ca si atribute un obiect DeliveryService d, un JTextField keyword si un
 * JComboBox attributes
 */
public class Search extends JFrame {
    DeliveryService d;
    JTextField keyword;
    JComboBox attributes;

    /**
     * Constructorul va creea o fereastra prin care clientul va putea sa scrie un keyword si va putea selecta un atribut
     * din JComboBox-ul attributes. Apasand pe butonul ”Search” se va deschide o noua fereastra cu un JTable unde
     * sunt listate produse ce au keyword-ul in nume si care sunt ordonate in ordine crescatoare pe baza atributului
     * selectat
     * @param d DeliveryService servicul de livrari
     */
    Search(DeliveryService d){
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300,100);
        this.setLocationRelativeTo(null);

        this.d=d;

        keyword=new JTextField(10);
        attributes=new JComboBox();
        attributes.addItem("Select Attribute");
        attributes.addItem("Rating");
        attributes.addItem("Calories");
        attributes.addItem("Proteins");
        attributes.addItem("Fat");
        attributes.addItem("Sodium");
        attributes.addItem("Price");

        JButton search=new JButton("Search");
        search.addActionListener(e -> {
            JFrame f=new JFrame();
            JTable j=new JTable(d.search(keyword.getText(),attributes.getSelectedIndex()),new String[]{"Title","Rating","Calories","Proteins","Fat","Sodium","Price"});
            j.getColumnModel().getColumn(0).setPreferredWidth(610);
            f.setLocationRelativeTo(null);
            j.setBounds(30, 40, 400, 300);
            f.add(new JScrollPane(j));
            f.setSize(700,200);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        });

        JPanel options=new JPanel();
        options.add(keyword);
        options.add(attributes);

        this.getContentPane().add(options);
        this.getContentPane().add(search);
        search.setAlignmentX(Box.CENTER_ALIGNMENT);
        this.getContentPane().add(Box.createRigidArea(new Dimension(1,5)));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
        this.setVisible(true);
    }
}
