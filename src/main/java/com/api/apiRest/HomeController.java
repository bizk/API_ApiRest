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
	public List<String> getUnidadesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
			List<UnidadView> unidades = ctrl.getUnidadesPorEdificio(codigo);
			System.out.println(unidades.size());
			for (UnidadView uv: unidades) {
				jsonlist.add(json.toJson(uv));
			}
			return jsonlist;
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="/habilitadosPorEdificio", method = RequestMethod.GET)
	public List<String> habilitadosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		logger.info("xd");
		try {
			List<PersonaView> habilitados = ctrl.habilitadosPorEdificio(codigo);
			System.out.println(habilitados.size());
			for(PersonaView h : habilitados) {
				jsonlist.add(json.toJson(h));
			}
			return jsonlist;
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value="/dueniosPorEdificio", method = RequestMethod.GET)
	public List<String> dueniosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
			List<PersonaView> duenios = ctrl.dueniosPorEdificio(codigo);
			System.out.println(duenios.size());	
			for(PersonaView h : duenios) {
				jsonlist.add(json.toJson(h));
			}
			return jsonlist;
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/habitantesPorEdificio", method = RequestMethod.GET)
	public List<String> habitantesPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
			List<PersonaView> habitantes = ctrl.habitantesPorEdificio(codigo);
			System.out.println(habitantes.size());
			for(PersonaView h : habitantes) {
				jsonlist.add(json.toJson(h));
			}
			return jsonlist;
		} catch (EdificioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/dueniosPorUnidad", method = RequestMethod.GET)
	public List<String> dueniosPorUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
				List<PersonaView> duenios = ctrl.dueniosPorUnidad(codigo, piso, numero);
				System.out.println(duenios.size());
				for(PersonaView h : duenios) {
					jsonlist.add(json.toJson(h));
				}
				return jsonlist;
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
	
	@RequestMapping(value="/reclamosPorEdificio", method = RequestMethod.GET)
	public List<String> reclamosPorEdificio(@RequestParam("codigo") int codigo) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
				List<ReclamoView> recl = ctrl.reclamosPorEdificio(codigo);
				System.out.println(recl.size());
				for(ReclamoView h : recl) {
					jsonlist.add(json.toJson(h));
				}
				return jsonlist;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/reclamosPorUnidad", method = RequestMethod.GET)
	public List<String> reclamosPorUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
				List<ReclamoView> recl = ctrl.reclamosPorUnidad(codigo, piso, numero);
				System.out.println(recl.size());
				for(ReclamoView h : recl) {
					jsonlist.add(json.toJson(h));
				}
				return jsonlist;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	@RequestMapping(value="/reclamosPorUnidad", method = RequestMethod.GET)
	public String reclamosPorNumero(@RequestParam("numero") int numero) {
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
	
	@RequestMapping(value="/reclamosPorPersona", method = RequestMethod.GET)
	public List<String> reclamosPorPersona(@RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		List<String> jsonlist = new ArrayList<String>();
		try {
				List<ReclamoView> recl = ctrl.reclamosPorPersona(documento);
				System.out.println(recl.size());
				for(ReclamoView h : recl) {
					jsonlist.add(json.toJson(h));
				}
				return jsonlist;
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
