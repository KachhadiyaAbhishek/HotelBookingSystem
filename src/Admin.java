import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    private Connection connection;
    Scanner sc=new Scanner(System.in);
    
    public Admin(Connection connection) {
        this.connection = connection;
    }

    public void adminMenu() {
        int choice;
        try
        {
            do{
                System.out.println();
                System.out.println("---------------------------------------------------------------------");
                System.out.println("\t\t\t\tAdmin. Menu");
                System.out.println("---------------------------------------------------------------------");
                System.out.println("1.Add Hotel\n2.Display Hotel details.\n3.Update Hotel details.\n4.Delete Hotel.\n5.Exit.");
                System.out.println("---------------------------------------------------------------------");
                System.out.print("Enter your choice according to given menu.: ");
                choice = sc.nextInt();
                System.out.println("---------------------------------------------------------------------");
                System.out.println();
                switch(choice){
                    case 1:
                        addHotel();
                    break;
                    case 2:
                        displayHotel();
                    break;
                    case 3:
                        updateHotel();
                    break;
                    case 4:
                        deleteHotel();
                    break;
                    case 5:
                    break;
                    default:
                        System.out.println("Invaild choice...");
                    break;
                }
            }while(choice != 5);
        }
        catch(InputMismatchException e){
            System.out.println("---------------------------------------------------------------------");
            System.out.println("ERROR: "+e);
        }
        catch(Exception e){
            System.out.println("---------------------------------------------------------------------");
            System.out.println("ERROR: "+e);
        }
    }
    
    public void addHotel() throws SQLException{
        sc.nextLine();
        System.out.print("Enter Hotel Name: ");
        String hotelName = sc.nextLine();
        System.out.print("Enter Hotel contact No.: ");
        long contact = sc.nextLong();
        boolean valid;
        valid = checkContact(contact);
        while(!valid){
            System.out.print("Enter Hotel contact No.: ");
            contact = sc.nextLong();
            valid = checkContact(contact);
        }
        sc.nextLine();
        System.out.print("Enter Hotel location: ");
        String location = sc.nextLine();
        System.out.print("Enter Hotel Rating (1 to 5): ");
        int rating = sc.nextInt();
        System.out.print("Enter total Ac Room: ");
        int totalAcRoom = sc.nextInt();
        System.out.print("Enter price per Ac Room: ");
        double priceAc = sc.nextDouble();
        System.out.print("Enter total non Ac Room: ");
        int totalNonAcRoom = sc.nextInt();
        System.out.print("Enter price per non Ac Room: ");
        double priceNonAc = sc.nextDouble();
        Hotel h = new Hotel(hotelName, contact, location, rating, totalAcRoom, totalNonAcRoom, priceAc, priceNonAc, totalAcRoom, totalNonAcRoom);
      
        String sql = "insert into hotel(hotelName, contact, location, rating, totalAcRoom,AcRoomPrice,totalNonAcRoom,NonAcRoomPrice,availableAcRoom,availableNonAcRoom) values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1,hotelName);
        pst.setLong(2,contact);
        pst.setString(3,location);
        pst.setInt(4, rating);
        pst.setInt(5, totalAcRoom);
        pst.setDouble(6, priceAc);
        pst.setInt(7, totalNonAcRoom);
        pst.setDouble(8, priceNonAc);
        pst.setInt(9, totalAcRoom);
        pst.setInt(10, totalNonAcRoom);
        int in = pst.executeUpdate();
        if(in > 0){
            System.out.println("Hotel insertion successfully...");
        }
        else{
            System.out.println("Hotel insetion failed...");
        }
    }

    public void displayHotel() throws SQLException{
        String sql = "select * from hotel";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        int i = 1;
        while(rs.next()){
            System.out.println();
            System.out.println("-----------------------Hotel : "+i+++"----------------------");
            System.out.println("Hotel Name           : "+rs.getString("hotelName"));
            System.out.println("Hotel contact No.    : "+rs.getLong("contact"));
            System.out.println("Hotel location       : "+rs.getString("location"));
            System.out.println("Hotel Rating         : "+rs.getInt("rating"));
            System.out.println("Total Ac Room        : "+rs.getInt("totalAcRoom"));
            System.out.println("Price pre Ac Room    : "+rs.getDouble("AcRoomPrice"));
            System.out.println("Total non Ac Room    : "+rs.getInt("totalNonAcRoom"));
            System.out.println("Price pre non Ac Room: "+rs.getDouble("NonAcRoomPrice"));
            System.out.println("Available Ac Room    : "+rs.getInt("availableAcRoom"));
            System.out.println("Available non Ac Room: "+rs.getInt("availableNonAcRoom"));
        }
    }

    public void updateHotel() throws SQLException{
        sc.nextLine();
        System.out.print("Enter Hotel name whos update: ");
        String name = sc.nextLine();
        boolean exit = false;
        Statement st = connection.createStatement();
        do{
            System.out.println("---------------------------------------------------------------------");
            System.out.println("1.Update contact Number.\n2.Update location.\n3.Update Rating.\n4.Update total Ac room.\n5.Update Ac room price.\n6.Update total non Ac room.\n7.Update non Ac room price.\n8.Exit");
            System.out.println("---------------------------------------------------------------------");
            System.out.print("Enter your choice according to given menu.: ");
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.print("Enter new Hotel contact: ");
                    long newContact = sc.nextLong();
                    String sql = "update hotel set contact = "+newContact+" where hotelName = '"+name+"'";
                    int i =st.executeUpdate(sql);
                    System.out.println((i>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 2:
                    sc.nextLine();
                    System.out.print("Enter new Hotel location: ");
                    String newLocation = sc.nextLine();
                    String sql2 = "update hotel set location = '"+newLocation+"' where hotelName = '"+name+"'";
                    int i2 =st.executeUpdate(sql2);
                    System.out.println((i2>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 3:
                    System.out.print("Enter new Hotel Rating (1 to 5): ");
                    int newRating = sc.nextInt();
                    String sql3 = "update hotel set rating = "+newRating+" where hotelName = '"+name+"'";
                    int i3 =st.executeUpdate(sql3);
                    System.out.println((i3>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 4:
                    System.out.print("Enter new total Ac Room: ");
                    int NewTotalAcRoom = sc.nextInt();
                    String sql4 = "update hotel set totalAcRoom = "+NewTotalAcRoom+" where hotelName = '"+name+"'";
                    int i4 =st.executeUpdate(sql4);
                    System.out.println((i4>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 5:
                    System.out.print("Enter new price per Ac Room: ");
                    double newPriceAc = sc.nextDouble();
                    String sql5 = "update hotel set AcRoomPrice = "+newPriceAc+" where hotelName = '"+name+"'";
                    int i5 =st.executeUpdate(sql5);
                    System.out.println((i5>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 6:
                    System.out.print("Enter total non Ac Room: ");
                    int newTotalNonAcRoom = sc.nextInt();
                    String sql6 = "update hotel set totalNonAcRoom = "+newTotalNonAcRoom+" where hotelName = '"+name+"'";
                    int i6 =st.executeUpdate(sql6);
                    System.out.println((i6>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 7:
                    System.out.print("Enter new price per non Ac Room: ");
                    double newPriceNonAc = sc.nextDouble();
                    String sql7 = "update hotel set NonAcRoomPrice = "+newPriceNonAc+" where hotelName = '"+name+"'";
                    int i7 =st.executeUpdate(sql7);
                    System.out.println((i7>0)?"Update successful...":"Update failed...");
                    exit = true;
                break;
                case 8:
                    exit = true;
                break;
                default:
                    System.out.println("Invaild choice...");
                break;
            }
        }while(!exit);
    }

    public void deleteHotel() throws SQLException{
        sc.nextLine();
        System.out.print("Enter Hotel name whos delete: ");
        String name = sc.nextLine();
        String sql = "delete from hotel where hotelName = '"+name+"'";
        PreparedStatement pst = connection.prepareStatement(sql);
        int i = pst.executeUpdate();
        System.out.println((i>0)?"Delete successful...":"Delete failed...");
    }

    public boolean checkContact(long contact){
        String str = Long.toString(contact);
        int size = str.length();
        if(size == 10){
            return true;
        }
        else{
            System.out.println("Invalid contact number..! Enter vaild 10 digit contact number...");
            return false;
        }
        
    }
}

class Hotel{
    
    String hotelName;
    long contact;
    String location;
    int rating;
    int totalAcRoom;
    int totalNonAcRoom;
    double priceAc;
    double priceNonAc;
    int availableAcRoom;
    int availableNonAcRoom;

    public Hotel(String hotelName, long contact, String location, int rating, int totalAcRoom, int totalNonAcRoom,double priceAc, double priceNonAc, int availableAcRoom, int availableNonAcRoom) {
        this.hotelName = hotelName;
        this.contact = contact;
        this.location = location;
        this.rating = rating;
        this.totalAcRoom = totalAcRoom;
        this.totalNonAcRoom = totalNonAcRoom;
        this.priceAc = priceAc;
        this.priceNonAc = priceNonAc;
        this.availableAcRoom = availableAcRoom;
        this.availableNonAcRoom = availableNonAcRoom;
    }

}