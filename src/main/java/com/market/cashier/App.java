package com.market.cashier;

public class App 
{
    public static void main( String[] args )
    {
        Products products = new Products();
        ShoppingCart sc = new ShoppingCart(products);
        products.showProducts();
        sc.addToCart(10, 10);
        sc.removeFromCart(10, 2);
        sc.addToCart(10, 1);
        sc.showCart();
    }
}
