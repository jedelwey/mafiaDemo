package mafia;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cartel {

	private Logger logger = Logger.getLogger("mafia.cartel");
	private List<Members> members;

	/**
	 * @return the members
	 */
	public List<Members> getMembers() {
		return members;
	}

	/**
	 * @param members the members to set
	 */
	public void setMembers(List<Members> members) {
		this.members = members;
	}

	//TODO
	private void establecerJerarquia() {
		List<Members> listaTemporal = new ArrayList<Members>();
		List<Members> lista1nivel = new ArrayList<Members>();
		List<Members> lista2nivel = new ArrayList<Members>();
		List<Members> lista3nivel = new ArrayList<Members>();
		
		Members jefeTemporalMembers = imprimirJefeBanda();
		lista1nivel.add(jefeTemporalMembers);
		
		for (Members miembro2nivel : members) {
			for (String nombreSubordinado : jefeTemporalMembers.getSubordinates()) {
				if(miembro2nivel.getName().equals(nombreSubordinado)) {
					lista2nivel.add(miembro2nivel);
				}
			}
		}
		for (Members miembro3nivel : members) {
			for (Members miembro2nivel : lista2nivel) {
				for (String nombreSubordinado : miembro2nivel.getSubordinates()) {
					if(nombreSubordinado.equals(miembro3nivel.getName())) {
						lista3nivel.add(miembro3nivel);
					}
				}
			}
		}
		listaTemporal = lista1nivel;
		for (Members members : lista2nivel) {
			listaTemporal.add(members);
		}
		for (Members members : lista3nivel) {
			listaTemporal.add(members);
		}
		setMembers(listaTemporal);
	}

	/**
	 * Imprime la jerarquia de la banda. Para ello parte de que la lista del Cartel está ordenada.
	 */
	private void imprimirJerarquia() {
		StringBuilder mensajeLog = new StringBuilder("Imprimiendo jerarquía del cartel\n\n");
		String mensaje = "";
		for (Members miembroBanda : members) {
			mensajeLog.append("Nombre: "+miembroBanda.getName()+"\n");
			mensajeLog.append("Antiguedad: "+miembroBanda.getSeniority()+"\n");
			mensajeLog.append("Subordinados: ");
			//Inicio subordinados
			String aux = "";
			for (String subordinado : miembroBanda.getSubordinates()) {
				if(miembroBanda.getSubordinates().size()>1) {
					aux = aux + subordinado+", ";
				}else {
					aux = aux +subordinado;
				}
					
			}
			if(null == aux || aux.equals("")) {
				aux = "Ninguno";
			}else {
				aux = aux +".";
				aux = aux.replace(", .", "");
				
			}		
			mensajeLog.append(aux+"\n");
			//Fin subordinados
			if(miembroBanda.getBoss().isEmpty()) {
				mensajeLog.append("Jefe: Ninguno \n\n");
			}else {
				mensajeLog.append("Jefe: "+miembroBanda.getBoss()+"\n\n");
			}
			mensaje = mensajeLog.toString();
		} 
		logger.log(Level.INFO, mensaje);
	}
	
	/**
	 * Imprime quien es el jefe de la banda.
	 */
	private Members imprimirJefeBanda() {
		boolean encontrado = false;
		Members jefe = null;
		for (Members miembroBanda : members) {
			if(miembroBanda.getBoss().isEmpty()) {
				logger.log(Level.INFO,"El jefe de la banda y el más buscado es: "+miembroBanda.getName()+"\n");
				jefe = miembroBanda;
				encontrado = true;
			}
		}
		//El jefe de la banda esta en prision, hay que establecer nuevo jefe.
		if(!encontrado) {
			Long antiguedad = 0L;
			Members jefeTemporal = null;
			for (Members miembroBanda : members) {
				if(antiguedad < miembroBanda.getSeniority()) {
					jefeTemporal = miembroBanda; 
					antiguedad = jefeTemporal.getSeniority();
				}
			}
			if(null != jefeTemporal) {
				logger.log(Level.INFO,"El jefe de la banda y el más buscado es: "+jefeTemporal.getName()+"\n");
				jefe = jefeTemporal;
			}
		}
		return jefe;
	}

	/**
	 * @param Nombre del miembro buscado
	 * @return Miembro de la banda con sus datos si lo encuentra, Miembro de la banda a null si no lo encuentra. 
	 */
	public Members obtenerMiembro(String buscado) {
		Members miembroBandaEncontrado = null;
		for (Members miembroBanda : members) {
			if(miembroBanda.getName().equalsIgnoreCase(buscado)) {
				 miembroBandaEncontrado = miembroBanda;
				 break;
			}
		}
		return miembroBandaEncontrado;
	}
	
	/**
	 * Se elimina al recluso del cartel
	 * @param recluso a eliminar
	 */
	public void quitarMiembro(Members recluso) {
		List<Members> listaAntigua = members;
		List<String> subordinados = new ArrayList<String>();
		Members jefeRecluso = null;
		for(Members miembroBanda : members) {
			if(!recluso.getBoss().isEmpty()) {
				if(recluso.getBoss().equals(miembroBanda.getName())) {
					jefeRecluso = miembroBanda;								
				}
			}else {
				//Se ha encarcelado al jefe banda hay que obtener el jefe de otra manera
				List<String> lista1nivel =  recluso.getSubordinates();
				Long antiguedad = 0L;
				for (String string : lista1nivel) {
					if(string.equals(miembroBanda.getName())) {
						Long antiguedadBanda = miembroBanda.getSeniority();
						if(antiguedad < antiguedadBanda) {
							jefeRecluso = miembroBanda;
						}
					}
				}
			}
		}
		for(Members miembroBanda : members) {
			if(miembroBanda.equals(recluso)) {
				aniadirSubordinadosAlNuevoJefe(recluso, subordinados);
				listaAntigua.remove(recluso);
				logger.log(Level.INFO,"Se ha eliminado del cartel al miembro "+recluso.getName()+"\n");
				break;
			}			
		}
		Members nuevoJefe = obtenerJefeDelRecluso(recluso, listaAntigua, jefeRecluso);
		List<String> listaAntiguaSubordinados = obtenerSubordinadosConJefeRecluso(recluso, listaAntigua, nuevoJefe);
		aniadirSubordinadosAlNuevoJefe(recluso, listaAntiguaSubordinados);
		setMembers(listaAntigua);
	}

	/**
	 * Obtiene los subordinados que se han quedado sin jefe al estar este recluso.
	 * @param recluso
	 * @param listaAntigua
	 * @param nuevoJefe
	 * @return
	 */
	private List<String> obtenerSubordinadosConJefeRecluso(Members recluso, List<Members> listaAntigua,
			Members nuevoJefe) {
		List<String> listaAntiguaSubordinados = new ArrayList<String>();
		for (Members miembroAux : listaAntigua) {
			for (String subordinadosSinJefe : recluso.getSubordinates()) {
				if(subordinadosSinJefe.equals(miembroAux.getName())){
					miembroAux.setBoss(nuevoJefe.getName());
				}
				if(nuevoJefe.getName().equals(miembroAux.getName())) {
					listaAntiguaSubordinados = nuevoJefe.getSubordinates();
				}
			}
		}
		return listaAntiguaSubordinados;
	}

	/**
	 * Añade al nuevo jefe sus nuevos subordinados
	 * @param recluso
	 * @param listaAntiguaSubordinados
	 */
	private void aniadirSubordinadosAlNuevoJefe(Members recluso, List<String> listaAntiguaSubordinados) {
		for (String subordinadoConNuevoJefe : recluso.getSubordinates()) {
			listaAntiguaSubordinados.add(subordinadoConNuevoJefe);
		}
	}

	/**
	 * Obtiene el Jefe del recluso para obtener el nuevo jefe de los subordinados del recluso
	 * @param recluso
	 * @param listaAntigua
	 * @param jefeRecluso
	 * @return nuevo Jefe de los subordinados del recluso
	 */
	private Members obtenerJefeDelRecluso(Members recluso, List<Members> listaAntigua, Members jefeRecluso) {
		Members nuevoJefe = null;
		for(Members miembroAux : listaAntigua) {
			for (String subordinadoEncarcelado : jefeRecluso.getSubordinates()) {
				if(subordinadoEncarcelado.equals(miembroAux.getName()) && !subordinadoEncarcelado.equals(recluso.getName())){
					nuevoJefe = miembroAux;
				}
			}
		}
		return nuevoJefe;
	}
	
	/**
	 * Añade un miembro al Cartel.
	 * @param recluso a añadir
	 */
	public void aniadirMiembro(Members recluso) {
		List<Members> listaAntigua = members;
		if(listaAntigua.contains(recluso)) {
			logger.log(Level.INFO,"El miembro "+recluso.getName()+" ya está en el cartel.\n");			
		}else {
			listaAntigua.add(recluso);
			this.setMembers(listaAntigua);
			logger.log(Level.INFO,"El exconvicto "+recluso.getName()+" ha vuelto a entrar en el cartel.\n");
		}
	}
	
	/**
	 * Establece la jerarquia, la imprime e imprime al jefe de la banda.
	 */
	public void actualizarDatos() {
		establecerJerarquia();
		imprimirJerarquia();
		imprimirJefeBanda();
	}
}
