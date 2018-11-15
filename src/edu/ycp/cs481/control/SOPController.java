package edu.ycp.cs481.control;

import java.sql.SQLException;
import java.util.ArrayList;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.SOP;

public class SOPController{
	private Database db = new Database();

	public Integer insertSOP(String title, String description, int priority, int version, int authorID,  boolean isArchived){
		return db.insertAndGetID("SOP", "sop_id", 
				new String[]{"title", "description", "priority", "version", "author_id", "archive_flag"}, 
				new String[]{title, description, String.valueOf(priority), String.valueOf(version), 
						String.valueOf(authorID), String.valueOf(isArchived)});
	}
	
	public ArrayList<SOP> searchForSOPs(int sopID, boolean titlePartial, String title, boolean descPartial, String description, 
			int priority, int version, int authorID){
		try{
			StringBuilder name = new StringBuilder("");
			StringBuilder sql = new StringBuilder("select * from SOP");
			if(sopID == -1 && (title == null || title.equalsIgnoreCase("")) &&
					(description == null || description.equalsIgnoreCase("")) && priority == -1 && version == -1 && 
					authorID == -1){
				name.append("Get All SOPs");
			}else{
				name.append("Get SOP with ");
				sql.append(" where ");
				boolean first = true;
				
				if(sopID != -1){
					db.addIntSearchToSelect(first, name, sql, "sop_id", sopID);
					first = false;
				}
				
				if(title != null && !title.equalsIgnoreCase("")){
					db.addStringSearchToSelect(first, name, sql, titlePartial, "title", title);
					first = false;
				}
				
				if(description != null && !description.equalsIgnoreCase("")){
					db.addStringSearchToSelect(first, name, sql, descPartial, "description", description);
					first = false;
				}
				
				if(priority != -1){
					db.addIntSearchToSelect(first, name, sql, "priority", priority);
					first = false;
				}
				
				if(version != -1){
					db.addIntSearchToSelect(first, name, sql, "version", version);
					first = false;
				}
				
				if(authorID != -1){
					db.addIntSearchToSelect(first, name, sql, "author_id", authorID);
					first = false;
				}
			}
			ArrayList<SOP> results = db.executeQuery(name.toString(), sql.toString(), db.getSopResFormat());
			if(sopID != -1){
				if(results.size() == 0){
					System.out.println("No SOP found with ID " + sopID);
				}else if(results.size() > 1){
					System.out.println("Multiple SOPs found with ID " + sopID + "! Returning null");
					return null;
				}
			}
			return results;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	public void archiveSOP(int sopID){
		db.executeUpdate("Archive SOP with ID " + sopID, "update SOP set archive_flag = true where sop_id = " + sopID);
	}

	public void unarchiveSOP(int sopID){
		db.executeUpdate("Unarchive SOP with ID " + sopID, "update SOP set archive_flag = false where sop_id = " + sopID);
	}

	public SOP revertSOP(int sopID, int version){
		return db.revertSOP(sopID, version);
	}

	public void changeSOPPriority(SOP sop, int priority){
		db.executeUpdate("Change SOP " + sop.getID() + " to Priority " + priority, "update SOP set priority = " + priority +
				"where sop_id = " + sop.getID());
		sop.setPriority(priority);
	}
	
	public void changeSOPTitle(SOP sop, String newTitle) {
		db.executeUpdate("Change SOP " + sop.getID() + " to Title " + newTitle, "update SOP set title = " + newTitle +
				"where sop_id = " + sop.getID());
		sop.setName(newTitle);
	}
	
	public void changeSOPDescription(SOP sop, String newDesc) {
		db.executeUpdate("Change SOP " + sop.getID() + " to Description " + newDesc, "update SOP set description = " + newDesc +
				"where sop_id = " + sop.getID());
		sop.setDescription(newDesc);
	}
	
	public void changeSOPVersion(SOP sop, int newVersion) {
		db.executeUpdate("Change SOP " + sop.getID() + " to Version " + newVersion, "update SOP set version = " + newVersion +
				"where sop_id = " + sop.getID());
		sop.setRevision(newVersion);
	}
	
	public void insertCompletedSOP(int user_id, int sop_id){
		db.insert("CompletedSOP", new String[] {"user_id", "sop_id"},
				new String[] {String.valueOf(user_id), String.valueOf(sop_id)});
	}
}