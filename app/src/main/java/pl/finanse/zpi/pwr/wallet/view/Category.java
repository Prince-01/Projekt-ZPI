package pl.finanse.zpi.pwr.wallet.view;

/**
 * Created by rober on 12.04.2016.
 */
public class Category {
    public final String categoryName;
    public final String superCategory;
    public final int icon;
    public String desc;

    public Category(String categoryName, int icon) {
        this.categoryName = categoryName;
        this.icon = icon;
        this.superCategory = "";
        this.desc = "";
    }

    public Category(String categoryName, int icon, String superCategory) {
        this.categoryName = categoryName;
        this.icon = icon;
        this.superCategory = superCategory;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}
