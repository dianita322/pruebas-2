package pe.edu.uni.BIBLIOTECA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.uni.BIBLIOTECA.service.GuiaService;

@RestController
@RequestMapping("/guia")
@CrossOrigin("")
public class GuiaController {



    @Autowired
    private GuiaService guiaService;

    @PutMapping("/actualizar")
    public String actualizarGuia(@RequestParam String parametro, @RequestParam int valor) {
        return guiaService.actualizarGuia(parametro, valor);
    }

    @GetMapping("/obtenerValor")
    public int obtenerValorGuia(@RequestParam String parametro) {
        return guiaService.obtenerValorGuia(parametro);
    }


}
