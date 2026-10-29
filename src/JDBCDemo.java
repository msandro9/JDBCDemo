import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCDemo {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("----------------------------------");
            System.out.println("1 - nova drzava");
            System.out.println("2 - izmjena postojece drzave");
            System.out.println("3 - brisanje postojece drzave");
            System.out.println("4 - prikaz svih drzava sortiranih po nazivu");
            System.out.println("5 - kraj");
            System.out.println("----------------------------------");
            System.out.println("Unesite odabir:");

            int odabir = Integer.parseInt(scanner.nextLine());

            try (Connection connection = dataSource.getConnection()) {
                Statement stmt = connection.createStatement();

                switch (odabir) {
                    case 1:
                        System.out.println("Unesite naziv drzave:");
                        String nazivInsert = scanner.nextLine();
                        int rowsInserted = stmt.executeUpdate("INSERT INTO Drzava (Naziv)  VALUES ('" + nazivInsert + "')");
                        System.out.println("Dodano drzava: " + rowsInserted);
                        break;

                    case 2:
                        System.out.println("Unesite ID drzave:");
                        int idUpdate = Integer.parseInt(scanner.nextLine());
                        System.out.println("Unesite novi naziv drzave:");
                        String nazivUpdate = scanner.nextLine();
                        int rowsUpdated = stmt.executeUpdate("UPDATE Drzava SET Naziv = '" + nazivUpdate + "' WHERE IdDrzava = " + idUpdate);
                        System.out.println("Azurirano drzava: " + rowsUpdated);
                        break;

                    case 3:
                        System.out.println("Unesite ID drzave:");
                        int idDelete = Integer.parseInt(scanner.nextLine());
                        int rowsDeleted= stmt.executeUpdate("DELETE FROM Drzava WHERE IdDrzava = " + idDelete);
                        System.out.println("Obrisano drzava: " + rowsDeleted);
                        break;

                    case 4:
                        ResultSet rs = stmt.executeQuery("SELECT IdDrzava, Naziv FROM Drzava ORDER BY Naziv ASC");
                        while (rs.next()) {
                            System.out.println(rs.getInt("IdDrzava") + " - " + rs.getString("Naziv"));
                        }
                        break;

                    case 5:
                        return;

                    default:
                        System.out.println("Krivi odabir");
                }
            } catch (SQLException e) {
                System.err.println("Greška prilikom spajanja na bazu podataka:");
                e.printStackTrace();
            }
        }
    }

    private static DataSource createDataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("AdventureWorksOBP");
        dataSource.setUser("sa");
        dataSource.setPassword("SQL");
        dataSource.setTrustServerCertificate(true);
        return dataSource;
    }
}
