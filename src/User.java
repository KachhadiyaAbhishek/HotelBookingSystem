import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    Scanner sc=new Scanner(System.in);
    ArrayList<booking> users = new ArrayList<>();
    private Connection connection;
    public User(Connection connection) {
        this.connection = connection;
    }

    class booking{
        String userName;
        long userContact;
        long userAdhar;
        String userCity;
        String userEmail;
        String hotelName; 
        int selectRoom;
        String checkInDate;
        String checkOutDate;
        double totalPrice;    

        public booking(String userName, long userContact, long userAdhar, String userCity, String userEmail) {
            this.userName = userName;
            this.userContact = userContact;
            this.userAdhar = userAdhar;
            this.userCity = userCity;
            this.userEmail = userEmail;
        }

        public booking(String userName, long userContact, long userAdhar, String userCity, String userEmail,String hotelName, int selectRoom, String checkInDate, String checkOutDate, double totalPrice) {
            this.userName = userName;
            this.userContact = userContact;
            this.userAdhar = userAdhar;
            this.userCity = userCity;
            this.userEmail = userEmail;
            this.hotelName = hotelName;
            this.selectRoom = selectRoom;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.totalPrice = totalPrice;
        }

        @Override
        public String toString() {
            return "booking [userName=" + userName + ", userContact=" + userContact + ", userAdhar=" + userAdhar
                    + ", userCity=" + userCity + ", userEmail=" + userEmail + ", hotelName=" + hotelName
                    + ", selectRoom=" + selectRoom + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate
                    + ", totalPrice=" + totalPrice + "]";
        }
        
    }
    public void userMenu() {
        int choice;
        try
        {
            do{
                System.out.println();
                System.out.println("---------------------------------------------------------------------");
                System.out.println("\t\t\tUser sign-up & sign-in");
                System.out.println("---------------------------------------------------------------------");
                System.out.println("1.sign-up.\n2.sign-in.\n3.Exit.");
                System.out.println("---------------------------------------------------------------------");
                System.out.print("Enter your choice according to given menu.: ");
                choice = sc.nextInt();
                System.out.println("---------------------------------------------------------------------");
                System.out.println();
                switch(choice){
                    case 1:
                        signUp();                    
                    break;
                    case 2:
                        signIn();
                    break;
                    case 3:
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

    public void signUp() throws SQLException{
        sc.nextLine();
        System.out.print("Enter user email id: ");
        String userEmail = sc.nextLine();

        String sql = "select * from customer where emailID = ? ";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, userEmail);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            System.out.println("Email Id already exists..! Sign-up failed...");
            return;
        }

        System.out.print("Enter user password: ");
        String userPass = sc.nextLine();
        System.out.print("Enter user name: ");
        String userName = sc.nextLine();
        System.out.print("Enter user contact number: ");
        long userContact = sc.nextLong();
        boolean valid;
        valid = checkContact(userContact);
        while(!valid){
            System.out.print("Enter user contact number: ");
            userContact = sc.nextLong();
            valid = checkContact(userContact);
        }
        sc.nextLine();
        System.out.print("Enter user city name: ");
        String userCity = sc.nextLine();
        System.out.print("Enter user adharcard number: ");
        long userAdhar = sc.nextLong();
        valid = checkAdhar(userAdhar);
        while(!valid){
            System.out.print("Enter user adharcard number: ");
            userAdhar = sc.nextLong();
            valid =checkAdhar(userAdhar);
        }
        String insertsql = "insert into customer(Name,contactNo,cityName,adharcardNo,emailID,password) values(?,?,?,?,?,?)";
        PreparedStatement pst2 = connection.prepareStatement(insertsql);
        pst2.setString(1,userName);
        pst2.setLong(2, userContact);
        pst2.setString(3,userCity);
        pst2.setLong(4, userAdhar);
        pst2.setString(5,userEmail); 
        pst2.setString(6,userPass);
        int in = pst2.executeUpdate();
        if(in > 0){
            System.out.println("sign-up sucessfully...");
        }
        else{
            System.out.println("sign-up failed...");
        }
    }

    public void signIn() throws SQLException{
        sc.nextLine();
        System.out.print("Enter user email id: ");
        String email = sc.nextLine();
        System.out.print("Enter user password: "); 
        String pass = sc.nextLine();
        String sql = "select * from customer where emailID = ? and password = ? ";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, email);
        pst.setString(2, pass);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            System.out.println("User sign-in successful...");

            String selectQql = "select * from customer where emailID = '"+email+"'";
            PreparedStatement pst2 = connection.prepareStatement(selectQql);
            ResultSet rs2 = pst2.executeQuery();
            if(rs2.next()){
                String userName = rs.getString("Name");
                long userContact = rs.getLong("contactNo");
                long userAdhar = rs.getLong("adharcardNo");
                String userCity = rs.getString("cityName");
                String userEmail = rs.getString("emailID");
                booking book = new booking(userName, userContact, userAdhar, userCity, userEmail);
                viewMemu(book);
            }
        }
        else
        {
            System.out.println("Sign-in failed..!");
        }
    }

    public void viewMemu(booking book) throws SQLException{
        int choice;
        try{
            do{
                System.out.println();
                System.out.println("---------------------------------------------------------------------");
                System.out.println("\t\t\t  User Menu");
                System.out.println("---------------------------------------------------------------------");
                System.out.println("1.Display all Hotel details.\n2.Search hotel by city.\n3.hotel booking.\n4.cancel booking.\n5.View booking.\n6.View booking history.\n7.Logout.\n8.Exit.");
                System.out.println("---------------------------------------------------------------------");
                System.out.print("Enter your choice according to given menu.: ");
                choice = sc.nextInt();
                System.out.println("---------------------------------------------------------------------");
                System.out.println();
                switch(choice){
                    case 1:
                        displayHotel();
                    break;
                    case 2:
                        searchHotelByArea();
                    break;
                    case 3:
                        BookHotel(book);
                    break;
                    case 4:
                        cancelBooking();
                    break;
                    case 5:
                        viewBooking();
                    break;
                    case 6:
                        viewBookingHistory(book);
                    break;
                    case 7:
                        logOut(book);
                        choice = 8;
                    break;
                    case 8:
                        System.out.println("Thanks..! Visit again...");
                    break;
                    default:
                        System.out.println("Invaild choice...");
                    break;
                }

            }while(choice != 8);
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

    public void searchHotelByArea() throws SQLException{
        sc.nextLine();
        System.out.print("Enter youer Location (city name).: ");
        String city = sc.nextLine();
        
        String searchSql = "select * from hotel where location = '"+city+"'";
        PreparedStatement pst = connection.prepareStatement(searchSql);
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
        if(i == 1){
            System.out.println("Hotel not found near by...");
        }
    }

    public void BookHotel(booking book) throws SQLException{
        displayHotel();
        System.out.println("---------------------------------------------------------------------");
        sc.nextLine();
        System.out.print("Enter hotel name whos book.: ");
        String hotelName = sc.nextLine();
        int user_id;
        String searchSql = "select userID from customer where emailID = '"+book.userEmail+"'";
        PreparedStatement pst = connection.prepareStatement(searchSql);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            user_id = rs.getInt("userID");
        }
        String searchSql2 = "select * from hotel where hotelName = '"+hotelName+"'";
        PreparedStatement pst2 = connection.prepareStatement(searchSql2);
        ResultSet rs2 = pst2.executeQuery();
        if(rs2.next()){
            System.out.println("---------------------------------------------------------------------");
            System.out.println("Hotel ID:            : "+rs2.getInt("hotelID"));
            System.out.println("Hotel Name           : "+rs2.getString("hotelName"));
            System.out.println("Hotel contact No.    : "+rs2.getLong("contact"));
            System.out.println("Hotel location       : "+rs2.getString("location"));
            System.out.println("Hotel Rating         : "+rs2.getInt("rating"));
            System.out.println("Total Ac Room        : "+rs2.getInt("totalAcRoom"));
            System.out.println("Price pre Ac Room    : "+rs2.getDouble("AcRoomPrice"));
            System.out.println("Total non Ac Room    : "+rs2.getInt("totalNonAcRoom"));
            System.out.println("Price pre non Ac Room: "+rs2.getDouble("NonAcRoomPrice"));
            System.out.println("Available Ac Room    : "+rs2.getInt("availableAcRoom"));
            System.out.println("Available non Ac Room: "+rs2.getInt("availableNonAcRoom"));
            System.out.println("---------------------------------------------------------------------");
        
            int selectRoom;
            double totalPrice;
            System.out.print("Enter check-in date(forment-dd/mm/yyyy): ");
            String checkInDate = sc.nextLine();
            System.out.print("Enter check-out date(forment-dd/mm/yyyy): ");
            String checkOutDate = sc.nextLine();
            System.out.print("Enter room type.(Ac / Non Ac).: ");
            String choice = sc.nextLine();
            System.out.print("Enter hotel room booking for how many days: ");
            int day = sc.nextInt();
            if(choice.equalsIgnoreCase("Ac")){
                System.out.print("Enter No. of room whos book.: ");
                selectRoom = sc.nextInt();
                if(selectRoom > (rs2.getInt("availableAcRoom"))){
                    System.out.println(selectRoom+" not available in hotel...");
                    System.out.println("Available Ac Room    : "+rs2.getInt("availableAcRoom"));
                    viewMemu(book);
                }
                else{
                    totalPrice = (day * selectRoom )* (rs2.getDouble("AcRoomPrice"));

                    String updateSql = "update hotel set availableAcRoom = "+(rs2.getInt("availableAcRoom") - selectRoom)+" where hotelName ='" +hotelName+"'";
                    PreparedStatement pst3 = connection.prepareStatement(updateSql);
                    int i = pst3.executeUpdate();
                    if(i > 0){
                        System.out.println("-----------------------------------------");
                        sc.nextLine();
                        System.out.println("press y Book hotel.\npress n cancel booking.");
                        String ch = sc.nextLine();
                        System.out.println("-----------------------------------------");
                        if(ch.equalsIgnoreCase("y")){
                            booking b = new booking(book.userName,book.userContact,book.userAdhar,book.userCity,book.userEmail,hotelName,selectRoom,checkInDate,checkOutDate,totalPrice);
                            users.add(b);
                            
                            String insertSQl = "insert into history(hotelID,userID,userName,checkInDate,checkOutDate,room,amount) values(?,?,?,?,?,?,?)";
                            PreparedStatement insertpst =connection.prepareStatement(insertSQl);
                            insertpst.setInt(1, rs2.getInt("hotelID"));
                            insertpst.setInt(2, rs.getInt("userID"));
                            insertpst.setString(3,book.userName);
                            insertpst.setString(4, checkInDate);
                            insertpst.setString(5, checkOutDate);
                            insertpst.setInt(6, selectRoom);
                            insertpst.setDouble(7, totalPrice);
                            int insert = insertpst.executeUpdate();
                            if(insert > 0){
                                System.out.println("Booking successful...");
                            }
                            else{
                                System.out.println("Booking failed...");
                            }
                            System.out.println(b);
                        }
                        else if(ch.equalsIgnoreCase("n")){
                            System.out.println("Booking cancel...");
                        }
                        else{
                            System.out.println("Invaild choice...");
                        }
                    }
                    else{
                        System.out.println("Booking fialed...");
                    }
                }
            }
            else if(choice.equalsIgnoreCase("Non Ac")){
                System.out.print("Enter No. of room whos book.: ");
                selectRoom = sc.nextInt();
                if(selectRoom > (rs2.getInt("availableNonAcRoom"))){
                    System.out.println(selectRoom+" not available in hotel...");
                    System.out.println("Available non Ac Room    : "+rs2.getInt("availableNonAcRoom"));
                    viewMemu(book);
                }
                else{
                    totalPrice = (day * selectRoom) * (rs2.getDouble("NonAcRoomPrice"));

                    String updateSql = "update hotel set availableNonAcRoom = "+(rs2.getInt("availableNonAcRoom") - selectRoom)+" where hotelName ='" +hotelName+"'";
                    PreparedStatement pst3 = connection.prepareStatement(updateSql);
                    int i = pst3.executeUpdate();
                    if(i > 0){
                        System.out.println("-----------------------------------------");
                        sc.nextLine();
                        System.out.println("press y Book hotel.\npress n cancel booking.");
                        String ch = sc.nextLine();
                        System.out.println("-----------------------------------------");
                        if(ch.equalsIgnoreCase("y")){
                            booking b = new booking(book.userName,book.userContact,book.userAdhar,book.userCity,book.userEmail,hotelName,selectRoom,checkInDate,checkOutDate,totalPrice);
                            users.add(b);
                                
                            String insertSQl = "insert into history(hotelID,userID,userName,checkInDate,checkOutDate,room,amount) values(?,?,?,?,?,?,?)";
                            PreparedStatement insertpst =connection.prepareStatement(insertSQl);
                            insertpst.setInt(1,rs2.getInt("hotelID"));
                            insertpst.setInt(2, rs.getInt("userID"));
                            insertpst.setString(3,book.userName);
                            insertpst.setString(4, checkInDate);
                            insertpst.setString(5, checkOutDate);
                            insertpst.setInt(6, selectRoom);
                            insertpst.setDouble(7, totalPrice);
                            int insert = insertpst.executeUpdate();
                            if(insert > 0){
                                System.out.println("Booking successful...");
                            }
                            else{
                                System.out.println("Booking failed...");
                            }
                            System.out.println(b);
                        }
                        else if(ch.equalsIgnoreCase("n")){
                            System.out.println("Booking cancel...");
                        }
                        else{
                            System.out.println("Invaild choice...");
                        }
                    }
                    else{
                        System.out.println("Booking fialed...");
                    }
                }
            }
            else{
                System.out.println("Enter vaild input...");
                viewMemu(book);
            }
        }
        else{
            System.out.println("Hotel not found...");
        }
    }

    public void cancelBooking(){
        sc.nextLine();
        System.out.print("Enter user name: ");
        String name = sc.nextLine();
        boolean cancel = false;
        for(booking book : users){
            if(name.equalsIgnoreCase(book.userName)){
                users.remove(book);
                cancel = true;
                System.out.println("Hotel booking cancel succesful...");
            }
        }
        if(!cancel){
            System.out.println("Hotel booking canceletion failed...");
        }
    }

    public void viewBooking(){
        int i=0;
        for(booking book : users){
            System.out.println("-----------------Your Booking.-----------------");
            System.out.println("Your name is        : "+book.userName);
            System.out.println("Hotel Name          : "+book.hotelName);
            System.out.println("Selected No. room   : "+book.selectRoom);
            System.out.println("Check-in date       : "+book.checkInDate);
            System.out.println("check-out date      : "+book.checkOutDate);
            System.out.println("Total payment amount: "+book.totalPrice);
            i++;
        }
        if(i == 0){
            System.out.println("Not booking found...");
        }
    }

    public void viewBookingHistory(booking book) throws SQLException{
        int user_id;
        String searchSql = "select userID from customer where emailID = '"+book.userEmail+"'";
        PreparedStatement pst = connection.prepareStatement(searchSql);
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            user_id = rs.getInt("userID");
        }
        String viewSql = "select * from history where userID = "+rs.getInt("userID");
        PreparedStatement pst2 = connection.prepareStatement(viewSql);
        ResultSet rs2 = pst2.executeQuery();
        int i = 0;
        while(rs2.next()){
            System.out.println("-----------------Your Booking.-----------------");
            System.out.println("Your name is        : "+rs2.getString("userName"));
            
            String sql = "select hotelName from hotel where hotelID = "+rs2.getInt("hotelID"); 
            PreparedStatement pst3 = connection.prepareStatement(sql);
            ResultSet rs3 = pst3.executeQuery();     
            if(rs3.next()){
                System.out.println("Hotel Name      : "+rs3.getString("hotelName"));   
            }
            System.out.println("Selected No. room   : "+rs2.getInt("room"));
            System.out.println("Check-in date       : "+rs2.getString("checkInDate"));
            System.out.println("check-out date      : "+rs2.getString("checkOutDate"));
            System.out.println("Total payment amount: "+rs2.getDouble("amount"));
            i++;
        }
        if(i == 0){
            System.out.println("Not booking found...");
        }
    }

    public void logOut(booking book) throws SQLException{
        
        String sql = "delete from customer where emailID = '"+book.userEmail+"'";
        PreparedStatement pst = connection.prepareStatement(sql);
        int i = pst.executeUpdate();
        if(i > 0){
            System.out.println("Logout successful...");
        }
        else{
            System.out.println("Logout failed...");
        }
        
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

     public boolean checkAdhar(long userAdhar){
        String str = Long.toString(userAdhar);
        int size = str.length();
        if(size == 12){
            return true;
        }
        else{
            System.out.println("Invalid contact number..! Enter vaild 12 digit adhar card number...");
            return false;
        }
        
    }
}
