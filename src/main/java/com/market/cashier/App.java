package com.market.cashier;

public class App 
{
    public static void main( String[] args )
    {
        Products products = new Products();

        products.showProducts();

        System.out.println(products.hasId(1));
    }
}
