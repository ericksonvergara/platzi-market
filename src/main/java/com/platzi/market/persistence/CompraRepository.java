package com.platzi.market.persistence;

import com.platzi.market.domain.Purchase;
import com.platzi.market.persistence.crud.CompraCrudRepository;
import com.platzi.market.persistence.entity.Compra;
import com.platzi.market.persistence.mapper.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompraRepository implements com.platzi.market.domain.repository.PurchaseRepository {

    @Autowired
    private CompraCrudRepository compraCrudRepository;
    @Autowired
    private PurchaseMapper mapper;

    @Override
    public List<Purchase> getAll() {
        return mapper.toPurchases((List<Compra>) compraCrudRepository.findAll());
    }

    @Override
    public Optional<List<Purchase>> getByClient(String clientId) {
        return compraCrudRepository.findByIdCliente(clientId)
                .map(compras -> mapper.toPurchases(compras)); //El .map() nos sirve para operar con lo que viene dentro del optional
    }

    @Override
    public Purchase save(Purchase purchase) {
        Compra compra = mapper.toCompra(purchase);//Convertimos a compra por medio del mapper
        compra.getProductos().forEach(producto -> producto.setCompra(compra)); //guardamos en cascada
        return mapper.toPurchase(compraCrudRepository.save(compra));
    }
}
