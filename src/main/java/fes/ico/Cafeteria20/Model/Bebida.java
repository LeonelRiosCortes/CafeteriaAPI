package fes.ico.Cafeteria20.Model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Bebida extends Producto{

    private String Sabor;

    public Bebida(int codigo, String nombre, int precio, String Sabor){
        super(codigo, nombre, precio);

        this.Sabor = Sabor;
    }
}
