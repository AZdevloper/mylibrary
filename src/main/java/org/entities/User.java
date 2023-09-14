package org.entities;

import Utils.MessageUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    protected int Id;
    protected String Name;
    protected String Cin;

    final Scanner sc = new Scanner(System.in);

    public String getCin() {
        return Cin;
    }

    public void setCin(String cin) {
        Cin = cin;
    }

    public void addUser( ){
        MessageUtils.showMessage("---------- ajouter l'utilisateur ---------- ","info");

        System.out.print(" [ CIN ] : ");
        String cin = sc.nextLine();

        System.out.print(" [ nom ] : ");
        String name = sc.nextLine();

        String query = "INSERT INTO `users`(`cin`, `name`) VALUES (?,?)";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Use % around the search term to perform a partial match (LIKE)
            ps.setString(1,cin );
            ps.setString(2,name);

            int rs = ps.executeUpdate();
            if (rs > 0){
                MessageUtils.showMessage("l'utilisature est créer avec succée ","success");
            }else{
                MessageUtils.showMessage("l'utilisature n'est été pas créer ","error");

            }
        } catch (SQLException e) {
            MessageUtils.showMessage(e.getMessage(),"error");
        }
    }
    public boolean checkIfUserExist(String cin){

        String query = "SELECT * FROM users WHERE cin = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Use % around the search term to perform a partial match (LIKE)
            ps.setString(1,cin );

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            MessageUtils.showMessage(e.getMessage(),"error");
            return false;
        }

    }

}
