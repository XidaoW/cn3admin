package edu.pitt.sis.paws.dbsync;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class generateAffliation {
	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
		

	        ConnectHalleyDB conn = new ConnectHalleyDB();
			


	        	//String sql = "SELECT * FROM cn3Testing_new_dump.`iconf2013` WHERE organisations LIKE '%1:%' AND authors NOT LIKE '%(2,3)%' AND authors NOT LIKE '%(1,2)%'";
        		String sql = "SELECT * FROM cn3Testing_new_dump.`cscw2013_papers_part3`";

	        //System.out.println(sql);
				ResultSet rs = conn.getResultSet(sql);
	        	//System.out.println(rs);

				try {
					while(rs.next()){
						String given_name_1 = rs.getString("given_name_10");
						String family_name_1 = rs.getString("family_name_10");
						if(given_name_1.length() > 0 && family_name_1.length()>0){
							
						String affiliation_1_institution = rs.getString("affiliation_10_institution");
						
						String affiliation_1_department = rs.getString("affiliation_10_department");
						if(affiliation_1_institution == null) affiliation_1_institution = affiliation_1_department;
						String author = given_name_1 + " "+ family_name_1;
						sql = "UPDATE author SET universityAffiliation = '" + affiliation_1_institution + "' WHERE name LIKE '%"+author+"%'";
						ResultSet rs1 = conn.getResultSet(sql);
						System.out.println(sql);
					
						}
					
					/* Adding author from iconf 13
					
						String _authors = rs.getString("authors") + ';';
						String _organisations = rs.getString("organisations");
						String[] organisations = _organisations.split(";");//_authors.split(",");
						System.out.println(organisations.length);
						String[] affiliations = new String[organisations.length+15];
						for(int i=0;i < organisations.length;i++){
							organisations[i] = organisations[i].substring(3);
						
							
							System.out.println(organisations[i]);

						}
						
						String[] authors = _authors.split(";");//_authors.split(",");
						System.out.println("Authors: " + _authors);
						String[] authors_matched = new String[authors.length + 2];
						
						for(int i=0;i<authors.length;i++){

							int j = Integer.parseInt(authors[i].substring(authors[i].length()-2, authors[i].length()-1));
							if(authors[i].contains("10")){
								j = 10;
							}
							System.out.println(j);
							authors_matched[j] = authors[i];
							System.out.println(authors_matched[j]);

						
							authors_matched[j] = authors_matched[j].substring(0, authors_matched[j].length()-4);
							String[] authors_revert = authors_matched[j].split(",");
							authors_matched[j] = authors_revert[1].trim()+ " " +authors_revert[0].trim(); 			
							System.out.println(authors_matched[j]);
							
							sql = "UPDATE author SET universityAffiliation = '" + organisations[j-1] + "' WHERE name LIKE '%"+authors_matched[j]+"%'";
							ResultSet rs1 = conn.getResultSet(sql);
							System.out.println(sql);

						}
						
						*/
						/*
						sql = "UPDATE author SET universityAffiliation = '" + organisations[0] + "' WHERE name LIKE '%"+authors[i]+"%'";
						ResultSet rs1 = conn.getResultSet(sql);

						System.out.println(sql);
						*/
						
						
						/* For single affiliation 
						String _authors = rs.getString("authors") + ';';
						String _organisations = rs.getString("organisations");
						System.out.println(_organisations);
						System.out.println("_organisations: " + _organisations);

							String[] organisations = _organisations.split(",");//_authors.split(",");
							//System.out.println(organisations[0] + organisations[1]);
							
							if(!_authors.contains("(") && !_authors.contains(")") && !_organisations.isEmpty() &&!_organisations.contains(":") && !_organisations.contains("1")){
								String[] authors = _authors.split(";");//_authors.split(",");
								System.out.println("Authors: " + _authors);
								for(int i=0;i<authors.length;i++){
									if(authors[i].trim().length()==0)continue;
									
									String[] authors_revert = authors[i].split(",");
									authors[i] = authors_revert[1].trim()+ " " +authors_revert[0].trim(); 			
									System.out.println(authors[i]);
									System.out.println(organisations[0]);
	
									sql = "UPDATE author SET universityAffiliation = '" + organisations[0] + "' WHERE name LIKE '%"+authors[i]+"%'";
									ResultSet rs1 = conn.getResultSet(sql);

									System.out.println(sql);

									
							}
						}else{
							
							System.out.println("********************************************Not single!!****************************");
						}
						
						*/
					
					
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

	       
	        


	        
	    }

	    private static void print(String msg, Object... args) {
	        System.out.println(String.format(msg, args));
	    }

	    private static String trim(String s, int width) {
	        if (s.length() > width)
	            return s.substring(0, width-1) + ".";
	        else
	            return s;
		
	}
}
