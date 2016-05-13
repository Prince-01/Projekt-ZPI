package pl.finanse.zpi.pwr.wallet.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import pl.finanse.zpi.pwr.wallet.helpers.Database;

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

    public static List<String> getAllFormattedCategoriesWithDepth(Context context) {
        Category[] categories = Database.GetAllCategories(context);
        Arrays.sort(categories);

        List<Category> formattedCategories = new LinkedList<>();

        for(int i = 0; i < categories.length; i++)
        {
            if(categories[i].superCategory == null)
                formattedCategories.add(categories[i]);
        }

        for(int i = 0; i < formattedCategories.size(); i++)
        {
            Category c = formattedCategories.get(i);

            List<Category> cat = GetSubcategoriesOf(categories, c);
            formattedCategories.addAll(i + 1, cat);
        }

        List<String> categoriesStrings = new ArrayList<>();
        for(Category c : formattedCategories)
            categoriesStrings.add(CategoryFormattedWithDepth(c));

        return categoriesStrings;
    }

    public static List<Category> GetSubcategoriesOf(Category[] categories, Category category) {
        List<Category> result = new ArrayList<>();

        for(Category c : categories) {
            if(c.superCategory == null)
                continue;

            if (c.superCategory.equals(category.categoryName)) {
                result.add(c);
                c.depth = category.depth + 1;
            }
        }

        return result;
    }

    public static String CategoryFormattedWithDepth(Category c) {
        return new String(new char[c.depth]).replace("\0", "\t\t") + c.categoryName;
    }
}
