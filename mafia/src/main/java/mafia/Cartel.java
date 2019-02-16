package mafia;

import java.util.List;

public class Cartel {

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

	public void establecerJerarquia() {
		
		
	}

	public void imprimirJerarquia() {
		for (Members miembroBanda : members) {
			System.out.println("Nombre: "+miembroBanda.getName());
			System.out.println("Antiguedad: "+miembroBanda.getSeniority());
			System.out.print("Subordinados: ");
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
			
			System.out.print(aux+"\n");
			if(miembroBanda.getBoss().isEmpty()) {
				System.out.println("Jefe: Ninguno \n");
			}else {
				System.out.println("Jefe: "+miembroBanda.getBoss()+"\n");
			}
			
		} 
	}
	
	public void imprimirJefeBanda() {
		for (Members miembroBanda : members) {
			if(miembroBanda.getBoss().isEmpty()) {
				System.out.println("El jefe de la banda y el más buscado es: "+miembroBanda.getName());
			}
		}
	}

}
