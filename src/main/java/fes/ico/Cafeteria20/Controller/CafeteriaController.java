package fes.ico.Cafeteria20.Controller;

import fes.ico.Cafeteria20.Model.Cafeteria;
import fes.ico.Cafeteria20.Model.Producto;
import fes.ico.Cafeteria20.Model.ProductoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CafeteriaController {
    Cafeteria cafeteria1;

    public CafeteriaController() {
        cafeteria1 = new Cafeteria();
    }

    @GetMapping("/productos")
    public String mostrarProductos(Model model) {
        RestTemplate restConsumer = new RestTemplate();
        String uri = "http://127.0.0.1:8080/menu";
        ArrayList<ArrayList> menuCom = restConsumer.getForObject(uri, ArrayList.class);
        model.addAttribute("menuCom", menuCom);
        return "productos";
    }

    @GetMapping("/productos/{codigo}")
    public String mostrarProductoID(@PathVariable String codigo, Model model) {
        RestTemplate restConsumer = new RestTemplate();
        String uri = "http://127.0.0.1:8080/menu/" + codigo;
        Producto prod = restConsumer.getForObject(uri, Producto.class);
        model.addAttribute("prod", prod);
        return "producto";
    }











    @GetMapping("/menu")
    public ResponseEntity<ArrayList<ArrayList>> getAllMenu(){
        return new ResponseEntity<>(cafeteria1.getMenu(), HttpStatus.OK);
    }

    @GetMapping("/menu/{codigo}")
    public ResponseEntity<Producto> buscarProducto(@PathVariable int codigo) {
        Producto producto = cafeteria1.buscarProducto(codigo);
        if (producto != null) {
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/menu/agregar")
    public ResponseEntity<String> agregarProducto(@RequestBody ProductoDTO productoDTO) {
        cafeteria1.agregarProducto(productoDTO.getCodigo(), productoDTO.getNombre(), productoDTO.getPrecio(), productoDTO.getCategoria(), productoDTO.getSabor());
        return new ResponseEntity<>("Producto agregado con éxito", HttpStatus.CREATED);
    }

    @DeleteMapping("/menu/delete/{codigo}")
    public ResponseEntity<String> eliminarProducto(@PathVariable int codigo) {
        Producto productoEliminado = cafeteria1.eliminarProducto(codigo);

        if (productoEliminado == null) {
            return new ResponseEntity<>("El producto con código " + codigo + " no existe en el menú de la cafetería", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("El producto con código " + codigo + " fue eliminado del menú de la cafetería", HttpStatus.OK);
    }

    @PatchMapping("/menu/replace/{codigo}")
    public ResponseEntity<String> actualizarPrecio(@PathVariable int codigo, @RequestBody Map<String, Object> body) {
        Producto producto = cafeteria1.buscarProducto(codigo);
        if (producto != null) {
            if (body.containsKey("precio")) {
                int nuevoPrecio = (int) body.get("precio");
                cafeteria1.actualizarPrecioProducto(codigo, nuevoPrecio);
                return ResponseEntity.ok("El precio del producto " + codigo + " ha sido actualizado.");
            } else {
                return ResponseEntity.badRequest().body("El cuerpo de la solicitud no contiene la clave 'precio'.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
