package pl.finanse.zpi.pwr.wallet.view;

/**
 * Created by rober on 12.04.2016.
 */
public class Category {
    public String categoryName;
    public int icon;
    public String desc;

    public Category(String categoryName, int icon) {
        this.categoryName = categoryName;
        this.icon = icon;
    }

    public Category(String categoryName, int icon, String desc) {
        this.categoryName = categoryName;
        this.icon = icon;
        this.desc = desc;
    }
}
