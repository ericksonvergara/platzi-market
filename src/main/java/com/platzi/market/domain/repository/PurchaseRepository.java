package com.platzi.market.domain.repository;

import com.platzi.market.domain.Purchase;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    List<Purchase> getAll(); //Metodo que recupera todas las listas que han ocurrido en el market
    Optional<List<Purchase>> getByClient(String clientId); // Metodo que retorna la lista de compras realizadas por un cliente
    Purchase save(Purchase purchase); //Metdo para guardar una compra
}
