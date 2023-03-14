package com.platzi.market.web.controller;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.service.ProductService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    @ApiOperation("Obtiene todos los productos")
    @ApiResponse(code = 200, message = "OK")
    public ResponseEntity<List<Product>> getAll() {
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Busca un producto con su ID")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Prodcuto no encontrado"),
    })

    public ResponseEntity<Product> getProduct(@ApiParam(value = "El ID del producto", required = true, example = "7") @PathVariable("id") int productId) {
        return productService.getProduct(productId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); //Si el producto no existe respondera un NOT FOUND con su respectivo codigo HTTP
    }

    @GetMapping("/category/{categoryId}")
    @ApiOperation("Buscar por categoria")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Categoria no encontrada")
    })
    public ResponseEntity<List<Product>> getByCategory(@ApiParam(value = "El Id de la categoria", required = true, example = "1") @PathVariable("categoryId") int categoryId) {
        return productService.getByCategory(categoryId)
                .map(products -> new ResponseEntity<>(products, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/save")
    @ApiOperation("Agregar un producto")
    @ApiResponse(code = 201, message = "Producto agregado correctamente")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("Eliminar un producto")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "No se encontro el producto a eliminar"),
    })
    public ResponseEntity delete(@PathVariable("id") int productId) {
        if(productService.delete(productId)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND); // En esta función Borrar preguntamos si el producto existe y realizamos la accion
        }                                                       // de lo contrario respondemos con un NOT FOUND
    }
}
