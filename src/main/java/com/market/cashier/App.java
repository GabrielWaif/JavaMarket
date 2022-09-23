package com.market.cashier;

public class App 
{
    public static void main( String[] args )
    {
        Products products = new Products();
        ShoppingCart sc = new ShoppingCart(products);
        products.addProduct("Milk", "1 galon", 10.0);
        sc.addCart(10, 10);
        sc.addCart(10, 1);
        sc.showCart();
    }
}
