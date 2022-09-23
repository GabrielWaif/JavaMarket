package com.market.cashier;

public class App 
{
    public static void main( String[] args )
    {
        Products products = new Products();
        products.showProducts();

        products.addProduct("tets", "1", 10.0);
    }
}
