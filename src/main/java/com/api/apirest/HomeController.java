package com.api.apirest;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.api.ftpConnection.FtpConnectionManager;
import com.api.ftpConnection.HashAlgo;
import com.google.gson.Gson;

import controlador.Controlador;
import exceptions.EdificioException;
import exceptions.PersonaException;
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

	private boolean loggedSuccess;
	private  String usr;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) throws IOException {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		
		//Test con archivo mockeado que recibiriamos y subiriamos
//		File file = new File("C:\\Users\\santi\\git\\ftp_cosasaenviar\\altofile.txt");
//		FtpConnectionManager.uploadFile(file);
		
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
//	@RequestMapping(value = "/getEdificios", method = RequestMethod.GET, produces = {"application/json"})
//	public @ResponseBody<json> String getEdificios() throws JsonProcessingException {
//		List<EdificioView> edificios = Controlador.getInstancia().getEdificios();
//		ObjectMapper mapper = new ObjectMapper();
//		return mapper.writeValueAsString(edificios);
//	}
//	
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
	public @ResponseBody<json> void transferirUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.transferirUnidad(codigo,piso,numero,documento);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/agregarDuenioUnidad", method = RequestMethod.POST)
	public @ResponseBody<json> void agregarDuenioUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.agregarDuenioUnidad(codigo,piso,numero,documento);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/alquilarUnidad", method = RequestMethod.POST)
	public @ResponseBody<json> void alquilarUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.alquilarUnidad(codigo,piso,numero,documento);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/agregarInquilinoUnidad", method = RequestMethod.POST)
	public @ResponseBody<json> void agregarInquilinoUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero, @RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.agregarInquilinoUnidad(codigo,piso,numero,documento);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/liberarUnidad", method = RequestMethod.POST)
	public @ResponseBody<json> void liberarUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.liberarUnidad(codigo,piso,numero);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/habitarUnidad", method = RequestMethod.POST)
	public @ResponseBody<json> void habitarUnidad(@RequestParam("codigo") int codigo, @RequestParam("piso") String piso, @RequestParam("numero") String numero) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.habitarUnidad(codigo,piso,numero);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/agregarPersona", method = RequestMethod.POST)
	public @ResponseBody<json> void agregarPersona(@RequestParam("documento") String documento, @RequestParam("nombre") String nombre) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			
			ctrl.agregarPersona(documento, nombre);
		} catch (Exception e) {
			//logger.info(e.printStackTrace());
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/eliminarPersona", method = RequestMethod.POST)
	public @ResponseBody<json> void eliminarPersona(@RequestParam("documento") String documento) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.eliminarPersona(documento);
		} catch (Exception e) {
			e.printStackTrace();
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
	public @ResponseBody<json> void agregarReclamo(@RequestParam("codigo") int codigoEdificio,
													@RequestParam("piso") String piso, 
													@RequestParam("numero") String numero, 
													@RequestParam("documento") String documento,
													@RequestParam("ubicacion") String ubicacion,
													@RequestParam("descripcion") String descripcion) {
		Controlador ctrl = Controlador.getInstancia();
		int res= -1;
		try {
			res= ctrl.agregarReclamo(codigoEdificio, piso, numero, documento, ubicacion, descripcion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(res+"");
	}
	
	//TODO agregar imagen a reclamo
	@RequestMapping(value="/agregarReclamo", method = RequestMethod.POST)
	public @ResponseBody<json> void agregarImagenReclamo(@RequestParam("numero") int numero,
			@RequestParam("imagen")File imagen, @RequestParam("tipo") String tipo) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			String direccion = HashAlgo.getFileHash(imagen);
			ctrl.agregarImagenAReclamo(numero, direccion, tipo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/cambiarEstado", method = RequestMethod.POST) //TODO ver qu� onda, se puede pasar a esto un elemento de enum?
	public @ResponseBody<json> void cambiarEstado(@RequestParam("numero") int numero, @RequestParam("estado") Estado estado) {
		Controlador ctrl = Controlador.getInstancia();
		try {
			ctrl.cambiarEstado(numero,estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 *  ###################################################################
	 * 							= DISCLAIMER =
	 * 		- We know this is a mounstrosity, we are not proud of this, 
	 * 		but it serves it purpouse, the shame of this project will
	 * 		haunt us until our time comes. :.C
	 * ###################################################################
	*/
	@RequestMapping(value="/login", method= RequestMethod.POST)
	public @ResponseBody<json> void login(@RequestParam("usr") String usr, @RequestParam("pwd") String pwd) {
		Controlador ctrl =  Controlador.getInstancia();
		Gson json = new Gson();
		if (usr.equals("admin")&&pwd.equals("admin")) {
			this.loggedSuccess = true;
			this.usr = pwd;
		} else {
			try {
				this.loggedSuccess = ctrl.login(usr, pwd);
				if (this.loggedSuccess == true)  {
					this.usr = pwd;
					System.out.println("Usuario logeado con exito");
				} else {
					System.out.println("Error de autenticacion");
				}
			} catch (Exception e) {
				System.out.println("El usuario no existe");
			}
		}
	}
	
	@RequestMapping(value="/loggedSucces", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> boolean isLogged() {
		return this.loggedSuccess;
	}
	
	@RequestMapping(value="/logOff", method= RequestMethod.POST)
	public @ResponseBody<json> void logOff() {
		this.loggedSuccess = false;
		this.usr = null;
	} 
	
	@RequestMapping(value="/getUsrInfo", method = RequestMethod.GET, produces = {"application/json"})
	public @ResponseBody<json> String getUsrInfo() throws PersonaException {
		Controlador ctrl = Controlador.getInstancia();
		Gson json = new Gson();
		if (this.loggedSuccess && this.usr != null &&!this.usr.isEmpty() && !this.usr.equals("admin"))
			return json.toJson(ctrl.userInfo(this.usr));
		else 
			return null;
	}
}
