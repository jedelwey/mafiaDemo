package mafia;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Prueba {
	
	private static final String FICHERO = "D:/datos.json";
	
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
			sinaloa.establecerJerarquia();
			sinaloa.imprimirJerarquia();
			sinaloa.imprimirJefeBanda();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
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
