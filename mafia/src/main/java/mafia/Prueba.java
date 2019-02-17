package mafia;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import institucion.Carcel;

public class Prueba {
	
	private static final String FICHERO = "D:/datos.json";
	private static Logger logger = Logger.getLogger("mafia.prueba");
	
	public static void main(String[] args) {
		try {
			
			JSONObject jsonObject = obtenerFichero();
			JSONArray listaMiembros = (JSONArray) jsonObject.get("members");

			Cartel sinaloa = new Cartel();
			ArrayList<Members> listaMiembrosCartel = new ArrayList<Members>();
			
			for (int i = 0; i < listaMiembros.size(); i++) {
				Object aux = listaMiembros.get(i);
				JSONObject miembroBanda = (JSONObject) aux;
				
				//Creo el miembro de la banda y lo asigno a la lista
				Members miembro = crearMiembroBanda(miembroBanda);
				listaMiembrosCartel.add(miembro); 
			}
			//Añado la lista de miembros al Cartel
			sinaloa.setMembers(listaMiembrosCartel);
			sinaloa.actualizarDatos();
			
			Carcel guantanamo = new Carcel();
			Members recluso1 = sinaloa.obtenerMiembro("Jhon");
			guantanamo.entrar(recluso1);
			sinaloa.quitarMiembro(recluso1);
			
			sinaloa.actualizarDatos();
			
			guantanamo.salir(recluso1);
			sinaloa.aniadirMiembro(recluso1);
			
			sinaloa.actualizarDatos();
			
			Members recluso2 = sinaloa.obtenerMiembro("Andy");
			guantanamo.entrar(recluso2);
			sinaloa.quitarMiembro(recluso2);

			sinaloa.actualizarDatos();
		} catch (UnsupportedEncodingException e) {
			logger.log(Level.WARNING, "Formato de encoding no soportado",e);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Se ha producido un error de lectura/escritura del archivo.", e);
		} catch (ParseException e) {
			logger.log(Level.WARNING, "Se ha producido un error al parsear el archivo json.", e);
		}		
	}

	/**
	 * Obtiene el fichero y lo parsea a JSONObject
	 * @return el fichero en formato JSONObject
	 * @throws IOException
	 * @throws ParseException
	 */
	private static JSONObject obtenerFichero() throws IOException, ParseException {
		String ficherito;
		ficherito = new String(Files.readAllBytes(Paths.get(FICHERO)),"UTF-8");
		
		JSONParser parser = new JSONParser();
		parser.parse(ficherito);
		JSONObject jsonObject = (JSONObject) parser.parse(ficherito);
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Crea un objeto Members con los parametros que trae el objeto JSONObject
	 * @param miembroBanda JSONObject
	 * @return Members
	 */
	private static Members crearMiembroBanda(JSONObject miembroBanda) {
		//Obtenemos los parametros
		String nombre 	= (String) miembroBanda.get("name");
		Long antiguedad = (Long) miembroBanda.get("seniority");
		String jefe 	= (String) miembroBanda.get("boss");
		List<String> subordinados = (List<String>) miembroBanda.get("subordinates");

		//Creamos el miembro de la banda
		Members miembro = new Members(nombre, antiguedad, subordinados, jefe);
		return miembro;
	}

}
