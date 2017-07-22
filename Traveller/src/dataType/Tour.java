package dataType;

public class Tour {
	String acname;
	String destination;
	String type;
	String budget;
	String date;
	String description;
	public Tour()
	{
		
	}
	public Tour(String acname, String destination, String type,
			String budget, String date, String desciption)
	{
		this.acname = acname;
		this.destination = destination;
		this.type = type;
		this.budget = budget;
		this.date = date;
		this.description = desciption;
	}
	public String getAcname() {
		return acname;
	}
	public void setAcname(String acname) {
		this.acname = acname;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String desciption) {
		this.description = desciption;
	}
	

	
}
