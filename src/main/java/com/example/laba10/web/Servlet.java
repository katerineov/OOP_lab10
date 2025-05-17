package com.example.laba10.web;

import com.example.laba10.model.Rental;
import com.example.laba10.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/rentals")
public class Servlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (Connection conn = DBUtil.getConnection()) {
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "rentals", new String[]{"TABLE"});
            if (!tables.next()) {
                sendError(resp, "Table not found", 500);
                return;
            }

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM rentals")) {

                List<Rental> rentals = new ArrayList<>();
                while (rs.next()) {
                    Rental rental = new Rental(
                            rs.getInt("ID"),
                            rs.getString("CLIENT_NAME"),
                            rs.getString("PHONE_NUMBER"),
                            rs.getString("RENTAL_DATE"),
                            rs.getString("ITEM_NAME"),
                            rs.getDouble("PRICE"),
                            rs.getString("DURATION")
                    );
                    rentals.add(rental);
                }

                if ("edit".equals(req.getParameter("action"))) {
                    int id = Integer.parseInt(req.getParameter("id"));
                    Rental editRental = rentals.stream()
                            .filter(r -> r.getId() == id)
                            .findFirst()
                            .orElse(null);

                    if (editRental != null) {
                        req.setAttribute("editRental", editRental);
                    }
                }

                req.setAttribute("rentals", rentals);
            }
        } catch (SQLException | NumberFormatException e) {
            sendError(resp, "Error: " + e.getMessage(), 500);
            return;
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Rental rental = new Rental();
        rental.setClientName(req.getParameter("clientName"));
        rental.setPhoneNumber(req.getParameter("phoneNumber"));
        rental.setRentalDate(req.getParameter("rentalDate"));
        rental.setItemName(req.getParameter("itemName"));
        rental.setPrice(Double.parseDouble(req.getParameter("price")));
        rental.setDuration(req.getParameter("duration"));

        String sql = "INSERT INTO rentals (CLIENT_NAME, PHONE_NUMBER, RENTAL_DATE, ITEM_NAME, PRICE, DURATION) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, rental.getClientName());
            pstmt.setString(2, rental.getPhoneNumber());
            pstmt.setString(3, rental.getRentalDate());
            pstmt.setString(4, rental.getItemName());
            pstmt.setDouble(5, rental.getPrice());
            pstmt.setString(6, rental.getDuration());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                sendError(resp, "Failed to create entry", 500);
                return;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rental.setId(generatedKeys.getInt(1));
                }
            }

            resp.sendRedirect(req.getContextPath() + "/rentals");
        } catch (SQLException e) {
            sendError(resp, "Error of database: " + e.getMessage(), 500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        String clientName = req.getParameter("clientName");
        String phoneNumber = req.getParameter("phoneNumber");
        String rentalDate = req.getParameter("rentalDate");
        String itemName = req.getParameter("itemName");
        String priceStr = req.getParameter("price");
        String duration = req.getParameter("duration");

        try {
            Rental rental = new Rental(
                    Integer.parseInt(idParam),
                    clientName,
                    phoneNumber,
                    rentalDate,
                    itemName,
                    Double.parseDouble(priceStr),
                    duration
            );

            String sql = "UPDATE rentals SET CLIENT_NAME=?, PHONE_NUMBER=?, RENTAL_DATE=?, "
                    + "ITEM_NAME=?, PRICE=?, DURATION=? WHERE ID=?";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, rental.getClientName());
                pstmt.setString(2, rental.getPhoneNumber());
                pstmt.setString(3, rental.getRentalDate());
                pstmt.setString(4, rental.getItemName());
                pstmt.setDouble(5, rental.getPrice());
                pstmt.setString(6, rental.getDuration());
                pstmt.setInt(7, rental.getId());

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    sendError(resp, "Entry not found", 404);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/rentals");
                }
            }
        } catch (SQLException | NumberFormatException e) {
            sendError(resp, "Error: " + e.getMessage(), 400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        try {
            int id = Integer.parseInt(idParam);
            String sql = "DELETE FROM rentals WHERE ID = ?";

            try (Connection conn = DBUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows == 0) {
                    sendError(resp, "Entry not found", 404);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                }
            }
        } catch (NumberFormatException e) {
            sendError(resp, "Error: Incorrect ID", 400);
        } catch (SQLException e) {
            sendError(resp, "Error of Deleting: " + e.getMessage(), 500);
        }
        resp.sendRedirect(req.getContextPath() + "/rentals");
    }

    private void sendError(HttpServletResponse resp, String message, int status)
            throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
    }
}