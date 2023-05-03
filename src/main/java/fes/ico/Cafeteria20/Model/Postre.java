package fes.ico.Cafeteria20.Model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Postre extends Producto {
    private String categoria;
    private String Sabor;
    public Postre(int codigo, String nombre, int precio, String categoria){
        super(codigo, nombre, precio);
        this.categoria = categoria;
    }
}
