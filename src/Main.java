import java.sql.Connection;
import java.sql.DriverManager;
import java.util.InputMismatchException;
import java.util.Scanner;

class Main{
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String dburl = "jdbc:mysql://localhost:3306/HotelBookingSystem";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.cj.jdbc.Driver";

        // Arrange and load driver. 
        Class.forName(drivername);

        // Establish connection.
        Connection connection = DriverManager.getConnection(dburl, dbuser, dbpass);

        // Check is connection established.
        if(connection != null){
            System.out.println("Connection successful...");
        }
        else{
            System.out.println("Connection failed...");
        } 
        
        String adminPass = "123";
        System.out.println("====================Welcome Hotel Booking System=====================");
        System.out.println();
        int choice;
        try
        {
            do{
                System.out.println();
                System.out.println("---------------------------------------------------------------------");
                System.out.println("\t\t\t\tMain Menu");
                System.out.println("---------------------------------------------------------------------");
                System.out.println("1.Admin.\n2.User.\n3.Exit.");
                System.out.println("---------------------------------------------------------------------");
                System.out.print("Enter your choice according to given menu.: ");
                choice = sc.nextInt();
                System.out.println("---------------------------------------------------------------------");
                System.out.println();
                switch(choice){
                    case 1:
                        boolean check = false;
                        while(!check){
                            System.out.print("Enter Admin. Password: ");
                            String pass = sc.next();
                            if(adminPass.equals(pass)){
                                System.out.println("signing successfully...");
                                check = true;
                            }
                            else
                            {
                                System.out.println("Wrong password.!! Enter valid password...");
                            }
                        }
                        if(check){
                            Admin admin = new Admin(connection);
                            admin.adminMenu();
                        }
                    break;
                    case 2:
                        User user = new User(connection);
                        user.userMenu();
                    break;
                    case 3:
                        System.out.println("Thanks..! Visit again...");
                    break;
                    default:
                        System.out.println("Invaild choice...");
                    break;
                }
            }while(choice != 3);
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
   
}
