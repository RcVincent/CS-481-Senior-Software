package edu.ycp.cs481.control;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs481.db.Database;
import edu.ycp.cs481.model.SOP;

public class SOPController{
	private Database db = new Database();

	public Integer insertSOP(SOP s){
		return db.insertAndGetID("SOP", "sop_id", 
				new String[]{"title", "description", "priority", "version", "author_id", "archive_flag"}, 
				new String[]{s.getName(), s.getDescription(), String.valueOf(s.getPriority()), String.valueOf(s.getRevision()), 
						String.valueOf(s.getAuthorID()), String.valueOf(s.getArchiveFlag())});
	}
	
	public ArrayList<SOP> searchForSOPs(int sopID, String title, String description, int priority, int version, int authorID){
		try{
			String name = "";
			String sql = "select * from SOP";
			if(sopID == -1 && (title == null || title.equalsIgnoreCase("")) &&
					(description == null || description.equalsIgnoreCase("")) && priority == -1 && version == -1 && 
					authorID == -1){
				name = "Get All SOPs";
			}else{
				name = "Get SOP with ";
				sql += " where ";
				boolean prevSet = false;
				
				if(sopID != -1){
					name += "id of " + sopID;
					sql += "sop_id = " + sopID;
					prevSet = true;
				}
				
				if(title != null && !title.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "title of " + title;
					sql += "title = '" + title + "'";
					prevSet = true;
				}
				
				// TODO: Likely need to change to search partial descriptions
				if(description != null && !description.equalsIgnoreCase("")){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "description of " + description;
					sql += "description = '" + description + "'";
					prevSet = true;
				}
				
				if(priority != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "priority " + priority;
					sql += "priority = " + priority;
					prevSet = true;
				}
				
				if(version != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "version " + version;
					sql += "version = " + version;
					prevSet = true;
				}
				
				if(authorID != -1){
					if(prevSet){
						name += " and ";
						sql += " and ";
					}
					name += "author_id of" + authorID;
					sql += "author_id = " + authorID;
					prevSet = true;
				}
			}
			ArrayList<SOP> results = db.executeQuery(name, sql, db.getSopResFormat());
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
	
	//search for the sop and add it to the users position requirements or their training history
	public void assignNewSOP(int userID, int sopID) {
		
	}
}