package fes.ico.Cafeteria20.Model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comida extends Producto{
    private String categoria;
    private String Sabor;

    public Comida(int codigo, String nombre, int precio, String Sabor){
        super(codigo, nombre, precio);
        this.Sabor = Sabor;
    }

}
