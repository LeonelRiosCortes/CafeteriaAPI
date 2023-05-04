package fes.ico.Cafeteria20.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fes.ico.Cafeteria20.Model.Cafeteria;
import fes.ico.Cafeteria20.Model.Producto;
import fes.ico.Cafeteria20.Model.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;

@Controller
public class CafeteriaController {
    Cafeteria cafeteria1;


    private final WebClient webClient;
@Autowired
    public CafeteriaController(WebClient webClient) {
        this.webClient = webClient;
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
    public String mostrarProductoID(@PathVariable String codigo, Model model) throws JsonProcessingException {
        String url = "http://127.0.0.1:8080/menu/" + codigo;
        Mono<Producto> productoMono = webClient.get()
                        .uri(url)
                        .retrieve()
                        .bodyToMono(Producto.class);
        Producto result = productoMono.block();

        model.addAttribute("prod", result);
        return "producto";
    }


    @GetMapping("/productos/agregar")
    public String mostrarFormularioAgregar(Model model) throws JsonProcessingException{

        return "agregarprod";
    }
    @PostMapping("/productos/agregando")
    public String agregarProd(
            @ModelAttribute("codigo") int codigo,
            @ModelAttribute("nombre") String nombre,
            @ModelAttribute("precio") int precio,
            @ModelAttribute("categoria") String categoria,
            @ModelAttribute("sabor") String sabor,
            Model model
    ){
        ProductoDTO productoDTO = new ProductoDTO(codigo, nombre, precio, categoria, sabor);
        System.out.println("Agregando " + productoDTO);
        String uri = "http://127.0.0.1:8080/menu/agregar";

        Mono<String> resultMono = webClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productoDTO)
                .retrieve()
                .bodyToMono(String.class);

        String result = resultMono.block();
        model.addAttribute("productoN", result);

        return "/productosN";
    }

    @GetMapping("/productos/borrar")
    public String mostrarFormularioBorrar(Model model) throws JsonProcessingException{

        return "borrarprod";
    }

    @PostMapping("/productos/borrando")
    public String borrarProd(@ModelAttribute("codigo") int codigo, Model model){

    Producto producto = buscarProducto(codigo).getBody();
        String uri = "http://127.0.0.1:8080/menu/delete/" + codigo;

        Mono<Void> resultMono = webClient.delete()
                .uri(uri)
                .retrieve()
                .bodyToMono(Void.class);

        resultMono.block(); // no es necesario almacenar el resultado en una variable

        return "productosB";
    }

    @GetMapping("/cambiarprecio")
    public String obtPrecio(Model model) throws JsonProcessingException{
        return "formP";
    }

    @PostMapping(value = "/cambiando")
    public String cambiar(
            @ModelAttribute("codigo") int codigo,
            //@ModelAttribute("nombre") int nombre,
            @ModelAttribute("precio") int precio,
            Model model
    ){
        Producto producto = new Producto(codigo, null, precio);
        String url = "http://127.0.0.1:8080/menu/replace/";

        Mono<String> resultMono = webClient.patch()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(producto)
                .retrieve()
                .bodyToMono(String.class);


        String result = resultMono.block();
        model.addAttribute("producto", result);
        return "productoM";
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
        Producto productoEliminado = cafeteria1.eliminarProducto(buscarProducto(codigo).getBody());

        if (productoEliminado == null) {
            return new ResponseEntity<>("El producto con código " + productoEliminado.getCodigo() + " no existe en el menú de la cafetería", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("El producto con código " + productoEliminado.getCodigo() + " fue eliminado del menú de la cafetería", HttpStatus.OK);
    }

    @PatchMapping("/menu/replace/")
    public ResponseEntity<String> actualizarPrecio(@RequestBody Map<String, Object> body) {
        Producto producto = cafeteria1.buscarProducto((Integer) body.get("codigo"));
        if (producto != null) {
            if (body.containsKey("precio")) {
                int nuevoPrecio = (int) body.get("precio");
                cafeteria1.actualizarPrecioProducto((Integer) body.get("codigo"), nuevoPrecio);
                return ResponseEntity.ok("El precio del producto " + (Integer) body.get("codigo") + " ha sido actualizado.");
            } else {
                return ResponseEntity.badRequest().body("El cuerpo de la solicitud no contiene la clave 'precio'.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
