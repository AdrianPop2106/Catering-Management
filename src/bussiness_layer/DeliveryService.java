package bussiness_layer;
import data_layer.Serializator;
import presentation_layer.EmployeePage;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @invariant items.size()==0 implies orders.size()==0
 *
 * Clasa DeliveryService are rolul de-a realiza creeare si modificare de comenzi reprezentate de clasa Order
 * si produse din clasa MenuItem. Are ca si atribute un Map ce foloseste obiecte Orders ca si chei si ArrayLists
 * de obiecte MenuItem ca si valori si un ArrayList de obiecte MenuItem
 */
public class DeliveryService extends Observable implements IDeliveryServiceProcessing {
        Map<Order, ArrayList<MenuItem>> orders;
        ArrayList<MenuItem> items;
    /**
     * Se deserealizeaza informatia din fisierul "file.txt" si se stocheaza in atributele orders si items
     */
    public DeliveryService(EmployeePage observer) {
            orders = new HashMap<>();
            items = new ArrayList<>();
            this.addObserver(observer);
            Serializator.deserealize(items,  orders);
        }
    /**
     * metoda importing() va incarca obiecte BaseProduct nexistente in atributul items, obiecte formate pe baza informatiei
     * din fisierul "products.csv"
     */
    @Override
    public void importing() {
        assert new File("products.csv").exists() : "File products.csv in not present";
        String[] words;
        try {
            Scanner s=new Scanner(new File("products.csv"));
            s.useDelimiter("\n");
            s.nextLine();
            while(s.hasNext()){
                String product=s.nextLine();
                words=product.split(",");
                if(!newProduct(words[0]))
                    continue;
                MenuItem m=new BaseProduct(words[0], Float.parseFloat(words[1]),Integer.parseInt(words[2]),Integer.parseInt(words[3]),Integer.parseInt(words[4]),Integer.parseInt(words[5]),Integer.parseInt(words[6]));
                items.add(m);
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda primeste ca si parametrii o ora initiala si o ora finala. Se va returna un 2d Array de String-uri ce va contine
     * numarul comenzii si ora in care a fost pusa comanda. Comanda este aleasa doar daca ora aestuia se incadreaza in
     * intervalul [starH,endH]
     * @param startH numar ce reprezinta o ora
     * @param endH numar ce reprezinta o ora
     * @return String[][]
     */
    @Override
    public String[][] reportTime(int startH,int endH){
       if(invariant(1)){
           Order[] o= orders.keySet().toArray(new Order[0]);
           Order[] result=Arrays.stream(o).filter(i->i.getOrderHour().getHour()>=startH && i.getOrderHour().getHour()<=endH).toArray(Order[]::new);
           String[][] res=new String[result.length][2];
           for(int i=0;i<result.length;i++){
               res[i][0]="Order#"+result[i].getOrderID();
               res[i][1]= String.valueOf(result[i].getOrderHour().getHour());
           }
           return res;
       }
       return null;
    }

    /**
     * Metoda primeste ca si parametrii o un numar ce reprezinta numarul de comenzi puse de un client. Se va returna un 2d Array
     * de String-uri ce va contine numele si prenumele clientului ce a depus un numar de comenzi mai mare sau egal cu valoarea
     * lui nr.
     * @param nr numar ce reprezinta numarul de comenzi depuse de un client
     * @return String[][]
     */
    @Override
    public String[][] reportClient(int nr){
        if(invariant(1)){
            Order[] o= orders.keySet().toArray(new Order[0]);
            Integer[] ids=Arrays.stream(o).map(Order::getClientID).toArray(Integer[]::new);
            Integer[] result=Arrays.stream(ids).filter(p->Collections.frequency(Arrays.asList(ids.clone()), p)>nr).distinct().toArray(Integer[]::new);
            String[][] res=new String[result.length][2];
            Scanner s;
            try {
                for(int i=0;i<result.length;i++){
                    s = new Scanner(new File("client.txt"));
                    for(int j=0;j<result[i]-1;j++)
                        s.nextLine();
                    String[] name=s.nextLine().split(" ");
                    res[i][0]=name[0];
                    res[i][1]=name[1];
                    s.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return res;
        }
        return null;
    }

    /**
     * Metoda primeste ca si parametru un numar ce reprezinta pretul unei comenzi. Se va returna un 2d Array de String-uri
     * ce va contine numarul comenzii si pretul acesteia. Comanda este aleasa doar daca are pretul mai mare decat valoarea lui nr
     * @param nr numar ce reprezinta pretul unei comenzi
     * @return String[][]
     */
    @Override
    public String[][] reportValue(int nr){
        if(invariant(1)){
            Order[] o= orders.keySet().toArray(new Order[0]);
            ArrayList<Order> result= (ArrayList<Order>) Arrays.stream(o).filter(p->p.getPrice()>nr).collect(Collectors.toList());
            String[][] res=new String[result.size()][2];
            for(Order i:result){
                res[result.indexOf(i)][0]="Order#"+i.getOrderID();
                res[result.indexOf(i)][1]= String.valueOf(i.getPrice());
            }
            return res;
        }
       return null;
    }

    /**
     * Metoda primeste ca si parametru o valoarea ce reprezinta numarul total de comenzi a unui anumit produs.
     * Se va returna un 2d Array de String-uri ce va contine numele unui produs si numarul total de comenzi a acestuia
     * Produsul este ales doar daca numarul de aparitii a acestuia este mai mare sau egal cu nr
     * @param nr numar ce reprezinta numarul total de comenzi a unui produs
     * @return String[][]
     */
    @Override
    public String[][] reportNumber(int nr){
        if(invariant(1)){
            ArrayList<MenuItem>[] o= orders.values().toArray(new ArrayList[0]);
            ArrayList<MenuItem> lists= (ArrayList<MenuItem>) Arrays.stream(o).flatMap(Collection::stream).collect(Collectors.toList());
            MenuItem[] result=lists.stream().filter(p->Collections.frequency(lists,p)>=nr).distinct().toArray(MenuItem[]::new);
            String[][] res=new String[result.length][2];
            for(int i=0;i<result.length;i++){
                res[i][0]=result[i].getTitle();
                int finalI = i;
                res[i][1]= String.valueOf(lists.stream().filter(p->p.getTitle().equals(res[finalI][0])).count());
            }
            return res;
        }
        return null;
    }

    /**
     * Metoda primeste ca si parametrii doua valori ce reprezinta ziua, respectiv nuamrul lunii in care a fost depusa o comanda.
     * Se va returna un 2D array de String-uri ce va contine numele unui produs si de cate ori a fost acesta comandat in ziua
     * si luna desemnate de parametrii day si month.
     * @param day numar ce reprezinta o zi a lunii
     * @param month numar ce reprezinta o luna
     * @return String[][]
     */
    @Override
    public String[][] reportDay(int day,int month){
        if(invariant(1)){
            Order[] o= orders.keySet().toArray(new Order[0]);
            Order[] result=Arrays.stream(o).filter(i->i.getOrderDate().getDayOfMonth()==day && i.getOrderDate().getMonthValue()==month).toArray(Order[]::new);
            ArrayList<MenuItem> p= new ArrayList<>();
            for(int i=0;i<result.length;i++){
                for(MenuItem m:result[i].getItems())
                    p.add(m);
            }
            ArrayList<MenuItem> unique= (ArrayList<MenuItem>) p.stream().distinct().collect(Collectors.toList());
            String[][] res=new String[(int)p.stream().distinct().count()][2];
            for(int i=0;i<unique.size();i++){
                res[i][0]=unique.get(i).getTitle();
                int finalI = i;
                res[i][1]= String.valueOf(p.stream().filter(prod->prod.getTitle().equals(res[finalI][0])).count());
            }
            return res;
        }
        return null;
    }
    /**
     * metoda va returna true daca nu sunt obiecte MenuItem in lista items care sa detina un titlu identic cu s
     * @param s String cu numele produsului
     * @return boolean
     */
    private boolean newProduct(String s){
            for(MenuItem m:items)
                if(s.equals(m.getTitle()))
                    return false;
            return true;
    }
    /**
     * Metoda returneaza un array de String-uri cu titlul tuturor obiectelor din lista items
     * @return String[]
     */
    public String[] returnMenu(){
        String[] c=new String[items.size()+1];
        for(int i=0;i<items.size();i++)
            c[i]= items.get(i).getTitle();
        return c;
    }
    /**
     * Metoda va returna un obiect MenuItem ai carui titlu este identic cu String-ul s sau null in caz contrar
     * @param s String cu numele produsului
     * @return MenuItem
     */
    @Override
    public MenuItem get(String s) {
        if(invariant(0)){
            assert s!=null: "String provided is null";
            MenuItem i=null;
            for(MenuItem m:items)
                if(s.equals(m.getTitle()))
                    i=m;
            assert i!=null: "Returned value is null";
            return i;
        }
        return null;
    }
    /**
     * Metoda returneaza true daca lista de produse sau daca map-ul de comenzi nu sunt goale si false in caz contrar
     * @return boolean
     */
    private boolean invariant(int c){
        if((items.size()!=0 && c==0) || (orders.size()!=0 && c==1))
            return true;
        return false;
    }
    /**
     * Metoda va introduce un nou element in HashMap-ul orders si va creea o noua chitanta cu informatia corespunzatoare
     * din parametrii m,l si user
     * @param m obiect Order
     * @param user String cu numele clientului
     */
    @Override
    public void order(Order m,String user) {
            assert m!=null:"Order provided is null";
            assert  m.getItems().size()!=0:"List provided is empty";
        if(m.getItems().size()!=0){
            try {
                FileWriter f=new FileWriter("Order#"+(orders.size()+1)+".txt",true);
                f.append("Order#"+(orders.size()+1)+"\n"+"Date:"+m.getOrderHour()+" "+m.getOrderDate()+"\nClient:"+user+"\nItems:");
                for(MenuItem i:m.getItems())
                    f.append("\n"+i.getTitle() + "-> " + i.computePrice() + "$");

                f.append("\nTotal:"+m.getPrice()+"$");
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            orders.put(m,m.getItems());
            setChanged();
            notifyObservers(m);
        }
        else
            JOptionPane.showMessageDialog(new JFrame(),"Order empty!");
    }
    /**
     * Metoda va returna un 2D array ce va contine informatii legate de produsele ce contin in atributul title String-ul s
     * si care vor fi ordonate eventual in functie de atributul desemnat de valoarea parametrului index
     * @param s String cu keyword-ul folosit in cautare
     * @param index indexul atributului dupa care se face ordonarea
     * @return String[][]
     */
    @Override
    public String[][] search(String s,int index) {
        if(invariant(0)){
            assert s!=null:"String is null";
            assert index>0 && index<7:"Index is lower than 0 or bigger than 7";
            ArrayList<MenuItem> itemData= items;
            BaseProduct[] result;
            if(!s.equals(""))
                result=itemData.stream().filter(i->i.getTitle().toLowerCase().contains(s.toLowerCase()) && i instanceof BaseProduct).toArray(BaseProduct[]::new);
            else
                result=itemData.toArray(BaseProduct[]::new);
            switch(index){
                case 1: {result= Arrays.stream(result).sorted((m1, m2)->Float.compare(m1.getRating(), m2.getRating())).toArray(BaseProduct[]::new); break;}
                case 2: {result= Arrays.stream(result).sorted(Comparator.comparingInt(m -> m.getCalories())).toArray(BaseProduct[]::new); break;}
                case 3: {result= Arrays.stream(result).sorted(Comparator.comparingInt(m -> m.getProteins())).toArray(BaseProduct[]::new); break;}
                case 4: {result= Arrays.stream(result).sorted(Comparator.comparingInt(m -> m.getFats())).toArray(BaseProduct[]::new); break;}
                case 5: {result= Arrays.stream(result).sorted(Comparator.comparingInt(m -> m.getSodium())).toArray(BaseProduct[]::new); break;}
                case 6: {result= Arrays.stream(result).sorted(Comparator.comparingInt(m -> m.computePrice())).toArray(BaseProduct[]::new); break;}
            }
            String[][] data=new String[result.length][7];
            for(int i=0;i<result.length;i++){
                data[i]= new String[]{result[i].getTitle(),result[i].getRating()+"",result[i].getCalories()+"",result[i].getProteins()+"",
                        result[i].getSodium()+"", result[i].getSodium()+"",result[i].computePrice()+""};
            }
            assert data!=null:"String matrix is null";
            return data;
        }
       return null;
    }
    /**
     * Metoda va creea un obiect BaseProduct cu atribute desemnate de array-ul fields si care va fi adaugat in items
     * @param fields array de String cu atributele noului produs
     */
    @Override
    public void createProduct(String[] fields) {
        assert fields.length==7:"Number of String array elements is not equal with 7";
        if(get(fields[0])==null){
            MenuItem m=new BaseProduct(fields[0], Float.parseFloat(fields[1]),Integer.parseInt(fields[2]),Integer.parseInt(fields[3]),Integer.parseInt(fields[4]),Integer.parseInt(fields[5]),Integer.parseInt(fields[6]));
            items.add(m);
        }
    }
    /**
     * metoda va returna un 2d Array ce va contine toate informatiile de la fiecare obiect MenuItem din items
     * @return String[]
     */
    @Override
    public String[][] view(){
        if(invariant(0)){
            String[][] every=new String[items.size()][7];
            int i=0;
            for(MenuItem m:items){
                if(m instanceof CompositeProduct)
                    continue;
                String[] fields={m.getTitle(),((BaseProduct)m).getRating()+"",((BaseProduct)m).getCalories()+"",((BaseProduct)m).getProteins()+"",((BaseProduct)m).getFats()+"",((BaseProduct)m).getSodium()+"",m.computePrice()+""};
                every[i]=fields;
                i++;
            }
            assert every.length!=0:"The matrix is empty";
            return every;
        }
        return null;
    }
    /**
     * Metoda va prelua obiectul din items ai carui titlu este identic cu parametrul title, si ii va seta valorile atributelor
     * cu informatia regasita in String-urile din parametrul fields care nu sunt goale
     * @param fields array de String ce reprezinta informatia cu care se modifica atributele
     * @param title String ce reprezinta numele produsului de modificat
     */
    @Override
    public void modifyProduct(String[] fields,String title) {
        if(invariant(0)){
            assert fields!=null:"String array is null";
            assert title!=null:"String title is null";
            assert fields.length>0 && fields.length<8:"Length of String array is lower than 0 or higher than 8";
            BaseProduct p=(BaseProduct) get(title);
            for(int i=0;i< fields.length;i++)
                if(!fields[i].equals("")){
                    switch(i){
                        case 0:{ p.setTitle(fields[i]); break;}
                        case 1:{p.setRating(Float.parseFloat(fields[i])); break;}
                        case 2:{p.setCalories(Integer.parseInt(fields[i])); break;}
                        case 3:{p.setProteins(Integer.parseInt(fields[i])); break;}
                        case 4:{p.setFats(Integer.parseInt(fields[i])); break;}
                        case 5:{p.setSodium(Integer.parseInt(fields[i])); break;}
                        case 6:{p.setPrice(Integer.parseInt(fields[i])); break;}
                    }
                }
        }
    }
    /**
     * Metoda va sterge din lista items obiectul care are titlul identic cu parametrul s
     * @param s String ce reprezinta titlul produsului
     */
    @Override
    public void deleteProduct(String s) {
        if(invariant(0)){
            assert s!=null:"String s is null";
            for(MenuItem i:items)
                if(i.getTitle().equals(s)){
                    items.remove(i);
                    break;
                }
        }
    }
    /**
     * Metoda va creea un obiect CompositeProduct cu informatia prevazuta de parametrii title si products. Obiectul creat
     * este aduagat in lista items
     * @param title String ce reprezinta numele noului produs
     * @param products ArrayList de obiecte MenuItem ce reprezinta produsele ce o sa faca parte din noul CompositeProduct
     */
    @Override
    public void createComposite(String title,ArrayList<MenuItem> products) {
        assert title!=null:"String title is null";
        assert products.size()>0:"Product list is empty";
        if(products.size()!=0) {
            if (get(title) == null) {
                ArrayList<MenuItem> newList = new ArrayList<>(products);
                CompositeProduct m = new CompositeProduct(title, newList);
                items.add(m);
            }
        }
        else
            JOptionPane.showMessageDialog(new JFrame(),"No products!");
    }
    /**
     * Metoda returneaza atributul items
     * @return ArrayList
     */
    public ArrayList getItems(){
            return items;
    }
    /**
     * Metoda returneaza atributul orders
     * @return Map<Order, ArrayList<MenuItem>>
     */
    public Map<Order, ArrayList<MenuItem>> getOrders() { return orders; }
}