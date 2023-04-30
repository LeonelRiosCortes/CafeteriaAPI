package fes.ico.Cafeteria20.Model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString


public class ProductoDTO {
    private int codigo;
    private String nombre;
    private int precio;
    private String categoria;
    private String sabor;
}
