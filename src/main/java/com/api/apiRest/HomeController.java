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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import controlador.Controlador;
import exceptions.EdificioException;
import exceptions.UnidadException;
import views.EdificioView;
import views.Estado;
import views.PersonaView;
import views.ReclamoView;
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
		logger.info("se conecto!");
		return "home";
	}
	
	@RequestMapping(value="/getEdificios", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String getEdificios() {
		Gson gson =  new Gson();
		Controlador ctrl = Controlador.getInstancia();
		List<EdificioView> edificios = ctrl.getEdificiosView();
		return gson.toJson(edificios);
	}	
/*	@RequestMapping(value = "/getEdificios", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String getEdificios() throws JsonProcessingException {
		List<EdificioView> edificios = Controlador.getInstancia().getEdificios();
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(edificios);
	}*/
	
	//To send parametters
	//localhost:8080/testParametters?param1=1&param2=2
	@RequestMapping(value="/testParametters", method = RequestMethod.GET)
	public String test(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
		logger.info(param1);
		logger.info(param2);
		return "TEST PARAMETROS";
	}
	
	@RequestMapping(value="/getUnidadesPorEdificio", method=RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String getUnidadesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		//List<String> jsonlist = new ArrayList<String>();
		try {
			List<UnidadView> unidades = ctrl.getUnidadesPorEdificio(codigo);
			return json.toJson(unidades);
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="/habilitadosPorEdificio", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String habilitadosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
			List<PersonaView> habilitados = ctrl.habilitadosPorEdificio(codigo);
			return json.toJson(habilitados);
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="/dueniosPorEdificio", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String dueniosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
			List<PersonaView> duenios = ctrl.dueniosPorEdificio(codigo);
			
			return json.toJson(duenios);
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/habitantesPorEdificio", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String habitantesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
			List<PersonaView> habitantes = ctrl.habitantesPorEdificio(codigo);
			return json.toJson(habitantes);
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/dueniosPorUnidad", method = RequestMethod.GET, produces = {"application/json"})
	public  @ResponseBody<json> String dueniosPorUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
				List<PersonaView> duenios = ctrl.dueniosPorUnidad(codigo, piso, numero);
				return json.toJson(duenios);
			} catch (UnidadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/transferirUnidad", method = RequestMethod.POST)
	public boolean transferirUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.transferirUnidad(codigo,piso,numero,documento);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/agregarDuenioUnidad", method = RequestMethod.POST)
	public boolean agregarDuenioUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.agregarDuenioUnidad(codigo,piso,numero,documento);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/alquilarUnidad", method = RequestMethod.POST)
	public boolean alquilarUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.alquilarUnidad(codigo,piso,numero,documento);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/agregarInquilinoUnidad", method = RequestMethod.POST)
	public boolean agregarInquilinoUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.agregarInquilinoUnidad(codigo,piso,numero,documento);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/liberarUnidad", method = RequestMethod.POST)
	public boolean liberarUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.liberarUnidad(codigo,piso,numero);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/habitarUnidad", method = RequestMethod.POST)
	public boolean habitarUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.habitarUnidad(codigo,piso,numero);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/agregarPersona", method = RequestMethod.POST)
	public boolean agregarPersona(@RequestParam("documento") String documento, @RequestParam("nombre") String nombre) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.agregarPersona(documento, nombre);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/eliminarPersona", method = RequestMethod.POST)
	public boolean eliminarPersona(@RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.eliminarPersona(documento);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/reclamosPorEdificio", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String reclamosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
				List<ReclamoView> recl = ctrl.reclamosPorEdificio(codigo);
				return json.toJson(recl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/reclamosPorUnidad", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String reclamosPorUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
				List<ReclamoView> recl = ctrl.reclamosPorUnidad(codigo, piso, numero);
				return json.toJson(recl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/reclamosPorNumero", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String reclamosPorNumero(@RequestParam("numero") int numero) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
				ReclamoView recl = ctrl.reclamosPorNumero(numero);
				System.out.println(recl.getNumero());
				return json.toJson(recl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/reclamosPorPersona", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String reclamosPorPersona(@RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		try {
				List<ReclamoView> recl = ctrl.reclamosPorPersona(documento);
				return json.toJson(recl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/agregarReclamo", method = RequestMethod.POST)
	public int agregarReclamo(@RequestParam("codigo") int codigoEdificio,@RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento,@RequestParam("ubicacion") String ubicacion,@RequestParam("descripcion") String descripcion) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			return ctrl.agregarReclamo(codigoEdificio, piso, numero, documento, ubicacion, descripcion);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	//TODO agregar imagen a reclamo
	
	@RequestMapping(value="/cambiarEstado", method = RequestMethod.POST) //TODO ver qué onda, se puede pasar a esto un elemento de enum?
	public boolean cambiarEstado(@RequestParam("numero") int numero, @RequestParam("estado") Estado estado) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.cambiarEstado(numero,estado);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	
	
}
