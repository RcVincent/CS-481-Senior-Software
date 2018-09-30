
import java.util.ArrayList;
import java.util.List;

public class SOP {
	private int id; 
	private String name;
	private String priority;
	
	private List<Position> positionsAffected = new ArrayList<Position>(); 
	
	
	
	public SOP() {
	}
	
	public ArrayList<Position> showPositionsAffected(int id) {
		ArrayList<Position> positions = new ArrayList<Position>();
	
		for(int i = 0; i < positionsAffected.size(); i++) {
			if(positionsAffected.get(i).getRegulatingSOPs().get(i).getSopIdNumber() == sopID) {
				positions.add(positionsAffected.get(i));
			}
		}
		return positions;
	}

	//Auto generated methods 
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getSopIdNumber() {
		return sopIdNumber;
	}
	public void setSopIdNumber(int sopIdNumber) {
		this.sopIdNumber = sopIdNumber;
	}
	public String getSopName() {
		return sopName;
	}
	public void setSopName(String sopName) {
		this.sopName = sopName;
	}
	
	public List<Position> getPositionsAffected() {
		return positionsAffected;
	}

	public void setPositionsAffected(List<Position> positionsAffected) {
		this.positionsAffected = positionsAffected;
	}

	public boolean isIsComplete() {
		return IsComplete;
	}

	public void setIsComplete(boolean isComplete) {
		IsComplete = isComplete;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getSopPurpose() {
		return sopPurpose;
	}

	public void setSopPurpose(String sopPurpose) {
		this.sopPurpose = sopPurpose;
	}
}