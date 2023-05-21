package Facebook;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountManagement {
    Scanner sc = new Scanner(System.in);
    UserBuilder builder = new UserBuilder();
    User user;
    Database database = new Database();

    public AccountManagement(){}

    public void registration(){
        System.out.println("\t\tRegistration Form");

        // Input username
        System.out.print("Username: ");
        String username = sc.nextLine();
        while(!database.verifyUsername(username)){
            System.out.print("Username: ");
            username = sc.nextLine();
        }
        builder.setUsername(username);

        // Input email
        System.out.print("Email: ");
        String email = sc.nextLine();
        while(!database.verifyEmail(email)){
            System.out.print("Email: ");
            email = sc.nextLine();
        }
        builder.setEmail(email);

        // Input phone number
        System.out.print("Phone number: ");
        String phoneNo = sc.nextLine();
        while(!database.verifyPhoneNo(phoneNo)){
            System.out.print("Phone number: ");
            phoneNo = sc.nextLine();
        }
        builder.setPhoneNo(phoneNo);

        // Input password
        System.out.print("Password: ");
        String password = sc.nextLine();
        while(!verifyPassword(password)){
            System.out.print("Password: ");
            password = sc.nextLine();
        }
        // Retype password
        System.out.print("Confirm password: ");
        String retypePassword = sc.nextLine();
        while(!confirmPassword(password, retypePassword)){
            System.out.print("Confirm password: ");
            retypePassword = sc.nextLine();
        }
        builder.setPassword(password);

        // Get account ID
        builder.setAccountID();
        //builder = new UserBuilder(builder.getAccountID(), username, email, phoneNo, password, null);

        // Get user
        user = builder.build();
        
        // Store user info into CSV file
        database.registerUser(user);
    }

    public void login(){
        System.out.print("Enter your email address or phone number: ");
        String emailOrPhoneNo = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        // Check if login successful
        while(!database.isLogin(emailOrPhoneNo, password)){
            System.out.print("Reenter your email address or phone number: ");
            emailOrPhoneNo = sc.nextLine();
            System.out.print("Reenter password: ");
            password = sc.nextLine();
        }
        user = database.getProfile(emailOrPhoneNo);

        // Check if user has setup account
        if(!isSetup(user)){
            User updatedUser = setupAccount(user);
            database.setupProfile(updatedUser);
        }

        System.out.println("Welcome to Facebook!");
    }

    public boolean isSetup(User user){
        if(user.getName()==null)
            return false;
        return true;
    }

    public User setupAccount(User user){
        builder.setAccountID(user.getAccountID());
        builder.setUsername(user.getUsername());
        builder.setEmail(user.getEmail());
        builder.setPhoneNo(user.getPhoneNo());
        builder.setPassword(user.getPassword());
        builder.setRole(user.getRole());

        System.out.println("Please fill in the below information to get started!");

        // Input name
        System.out.println("What is your name?");
        builder.setName(sc.nextLine());

        //Input birthday
        System.out.println("When is your birthday? (format: YYYY-MM-DD)");
        builder.setBirthday(sc.nextLine());
        //Check validation of birthday format
        while(!validateBirthdayFormat(builder.getBirthday())){
            System.out.println("When is your birthday? (format: YYYY-MM-DD)");
            builder.setBirthday(sc.nextLine());
        }
        LocalDate birthday = LocalDate.parse(builder.getBirthday());

        // Calculate age based on birthday
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthday, currentDate);
        builder.setAge(period.getYears());

        // Get address
        System.out.println("Where do you live?");
        builder.setAddress(sc.nextLine());

        // Get gender
        System.out.println("What is your gender? (M-male, F-female, N-not applicable)");
        builder.setGender(sc.next().charAt(0));
        sc.nextLine();

        // Initialize number of friends
        builder.setNoOfFriends(0);

        // Get relationship status
        System.out.println("What is your relationship status?");
        builder.setStatus(sc.nextLine());

        // Get hobbies (ArrayList)
        System.out.println("What are your hobbies?");
        builder.setHobbies(sc.nextLine());
        System.out.print("Do you wish to add more hobbies? (y-yes, n-no)");
        char choice = sc.next().charAt(0);
        sc.nextLine();
        while(choice=='y'){
            builder.setHobbies(sc.nextLine());        
            System.out.print("Do you wish to add more hobbies? (y-yes, n-no)");
            choice = sc.next().charAt(0);
            sc.nextLine();
        }

        // Get job (Stack)
        System.out.println("What is your current job?");
        builder.setJobs(sc.nextLine());     

        // Done setup account
        System.out.println("That's all for the account setup. You are now ready to explore Facebook!");
        return builder.build();
    }

    public boolean verifyPassword(String password){
        // Check password length
        if(password.length()<8){
            System.out.println("Password must be at least 8 characters.");
            return false;
        }

        // Check if password contains space characters
        if(password.contains(" ")){
            System.out.println("Password must not contain any space.");
            return false;
        }

        // Check if password is a strong password
        boolean upper=false, lower=false, digit=false, special=false;
        for(int i=0; i<password.length(); i++){
            char ch = password.charAt(i);
            if((int)ch >= (int)'A' && (int)ch <= (int)'Z')
                upper = true;
            else if((int)ch >= (int)'a' && (int)ch <= (int)'z')
                lower = true;
            else if((int)ch >= (int)'0' && (int)ch <= (int)'9')
                digit = true;
            else if((int)ch>=32&&(int)ch<=47 || (int)ch>=58&&(int)ch<=64 || (int)ch>=91&&(int)ch<=96 || (int)ch>=123&&(int)ch<=126)
                special = true;

        }
        if(upper&&lower&&digit&&special)
            return true;
        else{
            if(!upper)
                System.out.println("Password must contains at least one uppercase letter.");
            if(!lower)
                System.out.println("Password must contains at least one lowercase letter.");
            if(!digit)
                System.out.println("Password must contains at least one digit");
            if(!special)
                System.out.println("Password must contains at least one special character.");
            return false;
        }
    }
    
    public boolean confirmPassword(String p1, String p2){
        if(p1.equals(p2))
            return true;
        else{
            System.out.println("Password is not matched. Please try again.");
            return false;
        }
    }

    public boolean validateBirthdayFormat(String birthday) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        try {
            LocalDate.parse(birthday, formatter);
            return true; // Format is valid
        } catch (DateTimeParseException e) {
            System.out.println("Invalid birthday format");
            return false; // Format is invalid
        }
    }
    
    //For all users
    //set a normal user as an Admin
    public void setAdmin(User user){
        user.setRole("admin");
        System.out.println("You are currently an admin.");
    }
    
    //For all users
    //Post on personal pages
    public void createPost(User user, String content){
        //check if the user is currently banned
        if(user.isBanned()){
            System.out.println("Oops! You don't seem to be able to post anything on your page right now.");
            return;
        }
        
        Post newPost=new Post(user,content);
        user.getPage().add(newPost);
        System.out.println("You have successfully posted your content on your page!");
    }
    
    //For all users
    //View personal page
    public void viewPage(User user){
        System.out.println("Personal page of "+user.getUsername()+": ");
        System.out.println("-------------------------------------------");
        for (int i = 0; i < user.getPage().size(); i++) {
            System.out.println("Post No."+(i+1)+": "+user.getPage().get(i).getContent());
            System.out.println("-------------------------------------------------------");
        }
    }
    
    //For all users
    //To check the remaining banned time
    public Period getRemainingBannedTime(User user) {
        if (user.isBanned() && user.getBanEndDate() != null) {
            LocalDate currentDate = LocalDate.now();
            return Period.between(currentDate, user.getBanEndDate());
        }else if(user.isBanned() && user.getBanEndDate() == null){
            System.out.println("Sorry, your account has been PERMANENTLY banned.");
            return Period.ofYears(9999999);
        }else
            return Period.ZERO;
    }
    
    //For admins only
    public void banUser(User user, User.BanDuration duration) {
        //To check if the user who is calling this method is an admin
        if(this.user.getRole().equalsIgnoreCase("normal")){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        //To check if the user who is gonna be banned is an admin
        if (user.getRole().equalsIgnoreCase("admin")) {
            System.out.println("You cannot ban an admin user.");
            return;
        }

        if (duration == User.BanDuration.ONE_MONTH) {
            LocalDate banEndDate = LocalDate.now().plusMonths(1);
            user.setBanStatus(true, banEndDate);
        } else if (duration == User.BanDuration.ONE_YEAR) {
            LocalDate banEndDate = LocalDate.now().plusYears(1);
            user.setBanStatus(true, banEndDate);
        } else if (duration == User.BanDuration.PERMANENT) {
            user.setBanStatus(true, null);
        }
    }
    
    
    //For admins only
    //To remove Inappropriate Content in user page manually by entering user and the corresponding post
    //Content is manually determined whether it's appropriate or not by the administrator
    public boolean manuallyRemoveInappropriateContent(User user, Post post){
        //To check if the user who is calling this method is an admin
        if(this.user.getStatus().equalsIgnoreCase("normal")){
            System.out.println("Sorry, this operation is for admin ONLY");
            return false;
        }
        
        //Check if the user entered match with the post
        if(user.getPage().contains(post)){
            user.getPage().remove(post);
            System.out.println("Removed inappropriate content successfully: " + post.getContent());
            return true;
        }else{
            System.out.println("Oops! User you entered does not match with the post. Please double check:)");
            return false;
        }
    }
    
    //For admins only
    //To remove Inappropriate Content in user page by entering user only
    //Content is automatically determined whether it's appropriate or not by checking the prohibited words list
    public void automaticallyRemoveInappropriateContentInUserPage(User user) {
        //To check if the user who is calling this method is an admin
        if(this.user.getRole().equalsIgnoreCase("normal")){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        for(int i=0;i<user.getPage().size();i++){
            //check all posts of the user entered
            Post post=user.getPage().get(i);
            if (post.isInappropriate()) {
                user.getPage().remove(post);
                i--;
                System.out.println("Removed inappropriate content: " + post.getContent());
            } else {
                System.out.println("No inappropriate content found in Post No."+i);
            }
        }
    }
    
    //For admins only
    //To update the prohibited words list
    public void addNewProhibitedWords(String newProhibitedWord){
        
        //To check if the user who is calling this method is an admin
        if(this.user.getRole().equalsIgnoreCase("normal")){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        try {
            PrintWriter outputStream = new PrintWriter(new FileOutputStream("C:\\s2137573\\DSAssignment\\src\\main\\java\\Facebook\\ListOfProhibitedWords.txt", true));
            outputStream.print(newProhibitedWord);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Problem with file output"); 
        }

    }
    
    //For admins only
    public void deleteAccount(String accountID){
        //To check if the user who is calling this method is an admin
        if(this.user.getRole().equalsIgnoreCase("normal")){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        List<String[]> csvData = database.readCsvFileOfDatabase();
        // Check if the user is valid
        boolean valid = false;
        for(int i=0; i<csvData.size(); i++){
            String[] data = csvData.get(i);
            if(data[0].equals(accountID)){
                // Remove the user from the data
                csvData.remove(i);
                // Write the updated data back to the CSV file
                database.writeCsvFileOfDatabase(csvData);
                System.out.println("User deleted successfully");
                valid = true;
                break;
            }
        }
        if(valid==false)
            System.out.println("Invalid user");
    }
   
    
    //For all users
    public void createGroup(User creator, String groupName) {
        Group newGroup = new Group(groupName, creator);
        creator.getGroupsList().add(newGroup);
    }
    
    //For all users
    public void joinGroup(Group group) {
        this.user.getGroupsList().add(group);
        group.addMember(this.user);
    }
    
    //For all users
    public void inviteMemberToAGroup(Group group, User inviter, User invitee) {
        group.inviteMember(inviter, invitee);
    }
    
    //For all users
    public List<String> getGroupNames() {
        List<String> groupNames = new ArrayList<>();
        for (Group group : this.user.getGroupsList()) {
            groupNames.add(group.getName());
        }
        return groupNames;
    }
}