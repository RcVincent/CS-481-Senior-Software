package edu.ycp.cs481.control;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.SOP;

public class SOPController {
	private SOP sop1 = new SOP("Testing purposes", "For testing", 4, 1, 123, 2, false); 
	private SOP sop2 = new SOP();
	private Database db = new Database(); 
	
	
	
	public SOPController() {
		
	}
	
	//a method to check to ensure all fields are populated 
	//to be used after inserts or edits 
	public boolean validSOP(SOP sop) {
		if(sop.getID() <= 0) {
			sop.setArchiveFlag(true);
			return false; 
		}
		else if(sop.getName() == "" || sop.getName() == " ") {
			sop.setArchiveFlag(true);
			return false; 
		}
		else if(sop.getDescription() == "" || sop.getDescription() == " ") {
			sop.setArchiveFlag(true);
			return false; 
		}
		
		else if(sop.getPriority() <=0 ) {
			sop.setArchiveFlag(true);
			return false; 
		}
		else if(sop.getRevision() <= 0) {
			sop.setArchiveFlag(true);
			return false; 
		}
		else if(sop.getAuthorID() <= 0) {
			sop.setArchiveFlag(true);
			return false; 
		}
		else {
			return true; 
		}
		
		
		
	}
	
	//another method to be used in servlets 
	//or other areas where the data is messed with. 
	public void unarchive(SOP s) {
		if(s.getArchiveFlag()) {
			s.setArchiveFlag(false);
		}
	}
	
	
	//************************************
	//Implementing DB calls 
	//************************************
	public ArrayList<SOP> searchForSOP(int sopID, String title, String description, int priority, int version, int authorID) {
		return db.searchForSOPss(sopID, title, description, priority, version, authorID);
	}
	
	public void archiveSOP(int sopID){
		db.archiveSOP(sopID);
	}
	
	public void unarchiveSOP(int sopID){
		db.unarchiveSOP(sopID);
	}
	
	public SOP revertSOP(int sopID, int version) {
		return db.revertSOP(sopID, version);
	}
	
	public SOP changeSOPPirority(int sop_id, int priority) {
		return db.changeSOPPriority(sop_id, priority);
	}
	
	public Integer insertSOP(SOP s) {
		return db.insertSOP(s); 
	}
	
}