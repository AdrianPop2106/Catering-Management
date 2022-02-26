package bussiness_layer;

import java.util.ArrayList;

/**
 * Interfata ofera metode ce vor putea fi folosite pentru creearea si modificarea de obiecte din clasa MenuItem si Order
 */
public interface IDeliveryServiceProcessing {
    /**
     * @pre new File("products.csv").exists==true
     */
    void importing();

    /**
     * @pre s!=null
     * @post return!=null
     * @param s
     * @return
     */
    Object get(String s);

    /**
     * @pre m!=null
     * @pre l.size()!=0
     * @param m
     * @param l
     */
    void order(Order m,String user);

    /**
     * @pre s!=null
     * @pre index>0 && index<7
     * @post return!=null
     */
    String[][] search(String s, int index);

    /**
     * @pre fields.length==7
     * @param fields
     */
    void createProduct(String[] fields);

    /**
     * @pre fields!=null
     * @pre title!=null
     * @pre fields.length>0 && fields.length<8
     * @param fields
     * @param title
     */
    void modifyProduct(String[] fields, String title);

    /**
     * @pre s!=null
     * @param s
     */
    void deleteProduct(String s);

    /**
     * @pre title!=null
     * @pre products.size()>0
     * @param title String
     * @param products ArrayList<MenuItem>
     */
    void createComposite(String title, ArrayList<MenuItem> products);
    /**
     * @post return.length!=0
     */
    String[][] view();

    /**
     * @pre startH>=1 && startH<=23
     * @pre endH>=1 && endH<=23
     * @post return!=null
     * @param startH int
     * @param endH int
     * @return String[][]
     */
    String[][] reportTime(int startH,int endH);

    /**
     * @pre nr>0
     * @post return!=null
     * @param nr int
     * @return String[][]
     */
    String[][] reportClient(int nr);

    /**
     * @pre nr>0
     * @param nr int
     * @post return!=null
     * @return String[][]
     */
    String[][] reportValue(int nr);

    /**
     * @pre nr>0
     * @post return!=null;
     * @param nr int
     * @return String[][]
     */
    String[][] reportNumber(int nr);

    /**
     * @pre day>=1 && day<=31
     * @pre month>=1 && month<=12
     * @post return!=null
     * @param day int
     * @param month int
     * @return String[][]
     */
    String[][] reportDay(int day,int month);
}
