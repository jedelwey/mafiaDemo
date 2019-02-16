package mafia;

import java.util.List;

public class Members {

	private String name;
	private Long seniority;
	private List<String> subordinates;
	private String boss;
	
	public Members(String name, Long antiguedad, List<String> subordinados, String boss) {
		super();
		this.name = name;
		this.seniority = antiguedad;
		this.subordinates = subordinados;
		this.boss = boss;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the seniority
	 */
	public Long getSeniority() {
		return seniority;
	}
	/**
	 * @param seniority the seniority to set
	 */
	public void setSeniority(Long seniority) {
		this.seniority = seniority;
	}
	/**
	 * @return the subordinates
	 */
	public List<String> getSubordinates() {
		return subordinates;
	}
	/**
	 * @param subordinates the subordinates to set
	 */
	public void setSubordinates(List<String> subordinates) {
		this.subordinates = subordinates;
	}
	/**
	 * @return the boss
	 */
	public String getBoss() {
		return boss;
	}
	/**
	 * @param boss the boss to set
	 */
	public void setBoss(String boss) {
		this.boss = boss;
	}
	
	
}
