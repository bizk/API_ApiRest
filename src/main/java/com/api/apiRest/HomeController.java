package com.api.apiRest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import controlador.Controlador;
import exceptions.EdificioException;
import exceptions.UnidadException;
import views.EdificioView;
import views.PersonaView;
import views.UnidadView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
			
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		return "home";
	}
	
	@RequestMapping(value="/getEdificios", method = RequestMethod.GET)
	public List<String> getEdificios() {
		Gson gson =  new Gson();
		List<String> jsonList = new ArrayList<String>();
		
		logger.info("xd");
		
		Controlador ctrl = Controlador.getInstancia();
		List<EdificioView> edificios = ctrl.getEdificiosView();
		for (EdificioView ev : edificios)
			jsonList.add(gson.toJson(ev));
		
		return jsonList;
	}
	
	//To send parametters
	//localhost:8080/testParametters?param1=1&param2=2
	@RequestMapping(value="/testParametters", method = RequestMethod.GET)
	public String test(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
		logger.info(param1);
		logger.info(param2);
		return "TEST PARAMETROS";
	}
	
	@RequestMapping(value="/getUnidadesPorEdificio", method=RequestMethod.GET)
	public void getUnidadesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			List<UnidadView> unidades = ctrl.getUnidadesPorEdificio(codigo);
			System.out.println(unidades.size());
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value="/habilitadosPorEdificio", method = RequestMethod.GET)
	public void habilitadosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		logger.info("xd");
		try {
			List<PersonaView> habilitados = ctrl.habilitadosPorEdificio(codigo);
			System.out.println(habilitados.size());
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value="/dueniosPorEdificio", method = RequestMethod.GET)
	public void dueniosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			List<PersonaView> duenios = ctrl.dueniosPorEdificio(codigo);
			System.out.println(duenios.size());
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/habitantesPorEdificio", method = RequestMethod.GET)
	public void habitantesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			List<PersonaView> habitantes = ctrl.habitantesPorEdificio(codigo);
			System.out.println(habitantes.size());
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/dueniosPorUnidad", method = RequestMethod.GET)
	public void dueniosPorUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
			try {
				List<PersonaView> duenios = ctrl.dueniosPorUnidad(codigo, piso, numero);
				System.out.println(duenios.size());
			} catch (UnidadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
