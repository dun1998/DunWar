package io.github.dun1998.dunwar;

import java.util.ArrayList;
import java.util.List;

public interface ShopWindow {
    public List<ShopItem> shopItems = new ArrayList<>();
    public void openShop();
    public final int DEFAULT_PRICE= 10;
    public int addShopItem(ShopItem item,int price);//returns the item's id sets price
    default public int addShopItem(ShopItem item){
        return addShopItem(item,DEFAULT_PRICE);
    }//returns the item's id

    public void setItemPrice(ShopItem item, int price);
    public void removeShopItem(ShopItem item);


}
