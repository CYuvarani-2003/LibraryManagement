import java.sql.*;
import java.util.Scanner;

public class Library {
    Scanner sc = new Scanner(System.in);

    public void addBook() {
        try (Connection con = Database.getConnection()) {
            System.out.print("Book Title: ");
            String title = sc.nextLine();
            System.out.print("Author: ");
            String author = sc.nextLine();

            String query = "INSERT INTO books (title, author) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, title);
            pst.setString(2, author);
            pst.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void addMember() {
        try (Connection con = Database.getConnection()) {
            System.out.print("Member Name: ");
            String name = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            String query = "INSERT INTO members (name, email) VALUES (?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.executeUpdate();
            System.out.println("Member added successfully!");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void issueBook() {
        try (Connection con = Database.getConnection()) {
            System.out.print("Book ID: ");
            int bookId = Integer.parseInt(sc.nextLine());
            System.out.print("Member ID: ");
            int memberId = Integer.parseInt(sc.nextLine());

            String queryCheck = "SELECT available FROM books WHERE id = ?";
            PreparedStatement pstCheck = con.prepareStatement(queryCheck);
            pstCheck.setInt(1, bookId);
            ResultSet rs = pstCheck.executeQuery();

            if (rs.next() && rs.getBoolean("available")) {
                String queryIssue = "INSERT INTO issued_books (book_id, member_id, issue_date) VALUES (?, ?, CURDATE())";
                PreparedStatement pstIssue = con.prepareStatement(queryIssue);
                pstIssue.setInt(1, bookId);
                pstIssue.setInt(2, memberId);
                pstIssue.executeUpdate();

                String queryUpdate = "UPDATE books SET available = FALSE WHERE id = ?";
                PreparedStatement pstUpdate = con.prepareStatement(queryUpdate);
                pstUpdate.setInt(1, bookId);
                pstUpdate.executeUpdate();

                System.out.println("Book issued successfully!");
            } else {
                System.out.println("Book not available.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listBooks() {
        try (Connection con = Database.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM books");
            System.out.println("ID | Title | Author | Available");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " + rs.getString("title") + " | " + rs.getString("author") + " | " + rs.getBoolean("available"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
