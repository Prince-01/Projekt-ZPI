package pl.finanse.zpi.pwr.wallet.model;

/**
 * Created by rober on 12.04.2016.
 */
public class Category implements Comparable<Category> {
    public final String categoryName;
    public final String superCategory;
    public final int icon;
    public String desc;
    public int depth = 0;

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

    @Override
    public int compareTo(Category another) {
        return categoryName.compareTo(another.categoryName);
    }
}
