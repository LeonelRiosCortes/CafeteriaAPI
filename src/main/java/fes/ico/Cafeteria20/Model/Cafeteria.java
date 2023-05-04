package fes.ico.Cafeteria20.Model;

import java.util.ArrayList;

public class Cafeteria {

    ArrayList<Comida> alimentos = new ArrayList<>();
    ArrayList<Postre> postres = new ArrayList<>();
    ArrayList<Bebida> bebidas = new ArrayList<>();
    ArrayList<ArrayList> menu = new ArrayList<>();

    public Cafeteria() {
    alimentos.add(new Comida(11,"Sandwiches de crema", 30,"Dulce"));
    alimentos.add(new Comida(12,"Osito Al Curry", 70,"Picante"));
    alimentos.add(new Comida(13,"Huevo cob Catsup", 40,"Dulce"));

    postres.add(new Postre(21,"Cupcake", 25, "Dulce"));
    postres.add(new Postre(22,"Yogurht Helado", 25, "Dulce"));
    postres.add(new Postre(23,"Helado Chocolate", 25, "Dulce/Amargo"));

    bebidas.add(new Bebida(31, "Cafe Expresso", 30,  "Amargo"));
    bebidas.add(new Bebida(32, "Refresco", 30, "Dulce"));
    bebidas.add(new Bebida(33, "Frappe", 30,  "Dulce"));

    menu.add(alimentos);
    menu.add(bebidas);
    menu.add(postres);
    }

    public ArrayList<ArrayList> getMenu() {
        return menu;
    }

    public Producto buscarProducto(int codigo) {
        for (ArrayList<Producto> categoria : this.menu) {
            for (Producto producto : categoria) {
                if (producto.getCodigo() == codigo) {
                    return producto;
                }
            }
        }
        return null; // Si no se encuentra el producto, se devuelve null
    }

    public void agregarProducto(int codigo, String nombre, int precio, String categoria, String sabor) {
        Producto nuevoProducto = null;
        switch (categoria) {
            case "Comida":
                nuevoProducto = new Comida(codigo, nombre, precio, sabor);
                alimentos.add((Comida) nuevoProducto);
                break;
            case "Postre":
                nuevoProducto = new Postre(codigo, nombre, precio, categoria);
                postres.add((Postre) nuevoProducto);
                break;
            case "Bebida":
                nuevoProducto = new Bebida(codigo, nombre, precio, sabor);
                bebidas.add((Bebida) nuevoProducto);
                break;
            default:
                // Manejo de error, la categoría no es válida
                break;
        }
        /*if (nuevoProducto != null) {
            // Agregamos el nuevo producto a la lista correspondiente
            for (ArrayList<Producto> categoriaMenu : menu) {
                if (categoriaMenu.get(0).getClass().getSimpleName().equals(categoria)) {
                    categoriaMenu.add(nuevoProducto);
                    break;
                }
            }
        }*/
    }

    public Producto eliminarProducto(Producto producto) {
        Producto productoAEliminar = buscarProducto(producto.codigo);
        if (productoAEliminar != null) {
            if (productoAEliminar instanceof Comida) {
                alimentos.remove(productoAEliminar);
                for (ArrayList<Producto> categoria : menu) {
                    if (categoria.get(0) instanceof Comida) {
                        categoria.remove(productoAEliminar);
                        break;
                    }
                }
            } else if (productoAEliminar instanceof Bebida) {
                bebidas.remove(productoAEliminar);
                for (ArrayList<Producto> categoria : menu) {
                    if (categoria.get(0) instanceof Bebida) {
                        categoria.remove(productoAEliminar);
                        break;
                    }
                }
            } else if (productoAEliminar instanceof Postre) {
                postres.remove(productoAEliminar);
                for (ArrayList<Producto> categoria : menu) {
                    if (categoria.get(0) instanceof Postre) {
                        categoria.remove(productoAEliminar);
                        break;
                    }
                }
            }
            return productoAEliminar; // Devuelve el producto eliminado
        }
        return null; // Si no se encuentra el producto, se devuelve null
    }

    public Producto actualizarPrecioProducto(int codigo, int nuevoPrecio) {
        Producto productoAActualizar = buscarProducto(codigo);
        if (productoAActualizar != null) {
            productoAActualizar.setPrecio(nuevoPrecio);
        }
        return productoAActualizar;
    }





}
