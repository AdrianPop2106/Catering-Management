package bussiness_layer;

import java.io.Serializable;

/**
 * Clasa BaseProduct va reprezenta un produs simplu din cadrul meniului. Acesta are ca si atribute recenzia produsului,
 * numarul de calorii, cantitatea de proteine, cantiatatea de grasime si cantiatea de sodiu. Aceasta clasa extinde clasa
 * MenuItem
 */
public class BaseProduct extends MenuItem implements Serializable {
    float rating;
    int calories;
    int proteins;
    int fats;
    int sodium;

    /**
     * Se initializeaza fiecare atribut cu valoarea unuia dintre parametrii
     * @param title String titlul produsului
     * @param rating String recenzia produsului
     * @param calories String numarul de calorii
     * @param proteins String cantitatea de proteine
     * @param fats String cantitatea de grasimi
     * @param sodium String cantitatea de sodiu
     * @param price String pretul obiectului
     */
    BaseProduct(String title,float rating,int calories,int proteins,int fats,int sodium,int price){
        super(title,price);
        this.rating=rating;
        this.calories=calories;
        this.proteins=proteins;
        this.fats=fats;
        this.sodium=sodium;
    }

    /**
     * Se returneaza valoarea atributului calories
     * @return int
     */
    public int getCalories() {
        return calories;
    }

    /**
     * Se returneaza valoarea atributului fats
     * @return int
     */
    public int getFats() {
        return fats;
    }

    /**
     * Se returneaza valoarea atributului proteins
     * @return int
     */
    public int getProteins() {
        return proteins;
    }

    /**
     * Se returneaza valoarea atributului rating
     * @return float
     */
    public float getRating() {
        return rating;
    }

    /**
     * Se returneaza valoarea atributului sodium
     * @return int
     */
    public int getSodium() {
        return sodium;
    }

    /**
     * Se seteaza valoarea atributului calories cu parametrul
     * @param calories String numarul de calorii
     *
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * Se seteaza valoarea atributului fats cu parametrul
     * @param fats String cantitatea de grasime
     */
    public void setFats(int fats) {
        this.fats = fats;
    }

    /**
     * Se seteaza valoarea atributului proteins cu parametrul
     * @param proteins String cantitatea de proteine
     */
    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    /**
     * Se seteaza valoarea atributului rating cu parametrul
     * @param rating String recenzia produsului
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Se seteaza valoarea atributului sodium cu parametrul
     * @param sodium String cantitatea de sodiu
     */
    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

}
