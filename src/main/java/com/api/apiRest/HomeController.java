package com.api.apiRest;

import java.text.DateFormat;
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

import controlador.Controlador;
import exceptions.EdificioException;
import views.EdificioView;
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
	public String getEdificios() {
		Controlador ctrl = Controlador.getInstancia();
		List<EdificioView> edificios = ctrl.getEdificiosView();
		for (EdificioView ev : edificios)
			logger.info(ev.toString());
		
		return "getEdificios";
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
	public String getUnidadesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			List<UnidadView> unidades = ctrl.getUnidadesPorEdificio(codigo);
			System.out.println(unidades.size());
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "UnidadPorEdificio";
	}
}
