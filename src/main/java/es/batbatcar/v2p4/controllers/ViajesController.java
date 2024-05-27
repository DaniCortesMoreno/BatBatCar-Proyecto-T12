package es.batbatcar.v2p4.controllers;

import es.batbatcar.v2p4.exceptions.ViajeAlreadyExistsException;
import es.batbatcar.v2p4.exceptions.ViajeNotFoundException;
import es.batbatcar.v2p4.modelo.dto.viaje.EstadoViaje;
import es.batbatcar.v2p4.modelo.dto.viaje.Viaje;
import es.batbatcar.v2p4.modelo.repositories.ViajesRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViajesController {

    @Autowired
    private ViajesRepository viajesRepository;
    
    /**
     * Endpoint que muestra el listado de todos los viajes disponibles
     *
     * */
    @GetMapping("/viajes")
    public String getViajesAction(Model model) {
        model.addAttribute("viajes", viajesRepository.findAll());
        model.addAttribute("titulo", "Listado de viajes");
        return "viaje/listado";
    }
    
    @GetMapping("/viaje/add")
    public String viajeForm() {
		return "viaje/viaje_form";
	}
    
    @PostMapping(value = "/viaje/add")
    public String addViaje(@RequestParam Map<String, String> params) {

    	int codViaje = viajesRepository.getNextCodViaje();
    	
    	String propietario = params.get("propietario");
    	
    	String ruta = params.get("ruta");
    	

    	String horaSalidaString = params.get("horaSalida");
		String fechaSalidaString = params.get("fechaSalida");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime fechaSalida = LocalDateTime.parse(fechaSalidaString + " " + horaSalidaString,
				timeFormatter);
    	
    	/*String fechaSalidaString = params.get("fechaSalida");
    	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    	LocalDateTime fechaSalida = LocalDateTime.parse(fechaSalidaString, timeFormatter);*/

    	String duracionString = params.get("duracion");
    	long duracion = Long.valueOf(duracionString);
    	
    	String precioString = params.get("precio");
    	float precio = Float.valueOf(precioString);
    	
    	String plazasOfertadasString = params.get("plazasOfertadas");
    	int plazasOfertadas = Integer.valueOf(plazasOfertadasString);
    	
    	String estadoViajeString = params.get("estadoViaje");
    	EstadoViaje estadoViaje = EstadoViaje.parse(estadoViajeString);
    	
    	String seHanRealizadoReservasString = params.get("seHanRealizadoReservas");
    	boolean seHanRealizadoReservas = seHanRealizadoReservasString != null && seHanRealizadoReservasString.equalsIgnoreCase("true");
    	
    	
    	try {
    		Viaje viaje = new Viaje(codViaje, propietario, ruta, fechaSalida, duracion, precio, plazasOfertadas, estadoViaje);
			viajesRepository.save(viaje);
		} catch (ViajeAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ViajeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return "Se ha añadido el viaje";

    }
}
