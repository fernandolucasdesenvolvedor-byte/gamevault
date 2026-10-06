package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import enums.Gender;
import enums.Plataform;
import enums.Status;

public class Game {
	
	private Long id;
	private String name;
	private List<Gender> genders = new ArrayList<>();
	private List<Plataform> plataforms = new ArrayList<>();
	private String note;
	private Status status;
	
	public Game(Long id, String name, List<Gender> genders, List<Plataform> plataforms, String note, Status status) {
		this.id = id;
		this.name = name;
		this.genders = genders;
		this.plataforms = plataforms;
		this.note = note;
		this.status = status;
	}
	
	public Game(Long id,String name, List<Gender> genders,String note,Status status) {
		this.id = id;
		this.name = name;
		this.genders = genders;
		this.note = note;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public List<Gender> getGenders() {
		return genders;
	}

	public List<Plataform> getPlataforms() {
		return plataforms;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		return Objects.equals(id, other.id);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(id+" - ");
		sb.append(name+" | ");
		sb.append(genders+" | ");
		sb.append(plataforms+" | ");
		sb.append(status+" | ");
		sb.append("Nota: '"+note+"'"+" ");
		
		return sb.toString();
	}
}
