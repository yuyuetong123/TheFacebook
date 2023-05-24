package Facebook;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import Facebook.Database;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AccountManagement {
    Scanner sc = new Scanner(System.in);
    UserBuilder builder = new UserBuilder();
    User user;
    ConnectionGraph<String> graph = new ConnectionGraph<>();
    UsersConnection connection = new UsersConnection();
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

        // Add user to graph 
        connection.registerGraph(graph, username);
        System.out.println("*************************");
    }

    public User login(){
        System.out.println("\tLogin Page");
        System.out.println("-------------------------");
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
            user = setupAccount(user);
            database.setupProfile(user);
        }

        graph.getGraph(graph);
        System.out.println("*************************");
        System.out.println(user.getName());
        System.out.println("Welcome to Facebook!");
        System.out.println("*************************");
        return user; 
    }

    public void viewAccount(User user){
        String accountID = user.getAccountID();
        String username = user.getUsername();
        String email = user.getEmail();
        String phoneNo = user.getPhoneNo();
        String role = user.getRole();
        String name = user.getName();
        String birthday = user.getBirthday();
        int age = user.getAge();
        String address = user.getAddress();
        char gender = user.getGender();
        String status = user.getStatus();
        int noOfFriends = user.getNoOfFriends();
        ArrayList<String> hobbies = user.getHobbies();
        Stack<String> jobs = user.getJobs();

        System.out.println("\tAccount Info");
        System.out.println("-------------------------");
        System.out.println("Account ID: " + accountID);
        System.out.println("Username: " + username);
        System.out.println("Email address: " + email);
        System.out.println("Phone number: " + phoneNo);
        System.out.println("Role: " + role);
        System.out.println("Name: " + name);
        System.out.println("Birthday: " + birthday);
        System.out.println("Age: " + age);
        System.out.println("Address: " + address);
        System.out.println("Gender: " + gender);
        System.out.println("Relationship status: " + status);
        System.out.println("Number of friends: " + noOfFriends);
        System.out.println("Hobbies: ");
        for(int i=0; i<hobbies.size(); i++){
            System.out.println(hobbies.get(i));
        }
        String currentJob = jobs.pop();
        System.out.println("Current job: " + currentJob);
        if(jobs.size()>1){
            System.out.println("Previous job: " + jobs.peek());
        }
        jobs.push(currentJob);
        System.out.println("*************************");
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
        System.out.println("*************************");
        return builder.build();
    }

    // Display search friend and view account of search friend
    // Able to send or take back friend request
    public void searchFriend(){
        System.out.println("Enter search keyword:");
        sc.nextLine();
        String emailOrPhoneNoOrUsernameOrName =  sc.nextLine();
        System.out.println("*************************");
        ArrayList<User> result = database.ifContains(emailOrPhoneNoOrUsernameOrName);     // ArrayList of user objects of search result

        // Sort the names alphabetically
        for(int i=1; i<result.size(); i++){
            for(int j=0; j<i; j++){
                if(result.get(i).getName().compareTo(result.get(j).getName())<0){
                    User temp = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, temp);
                }
            }
        }

        int choice = 1;
        if(result.size()==0){
            System.out.println("No result found.");
            choice = 0;
            System.out.println("*************************");
        }
        while(choice>0){
            // Display all search result
            int count = 1;
            for(User x : result){
                String title = "New";
                if(x.getUsername().equals(user.getUsername()))
                    title = "You";
                else if(graph.hasEdge(graph, user.getUsername(), x.getUsername()))
                    title = "Friend";
                System.out.println(count + " - " + x.getName() + " \"" + title + "\"");
                count++;
            }
            
            // Select to view searched account
            System.out.println("Enter 0 to exit.");
            System.out.println("*************************");
            choice = sc.nextInt();
            System.out.println("*************************");

            // Condition if selection out of index bound
            while(choice>result.size()){
                System.out.println("Choice out of bound. Please select again.");
                choice = sc.nextInt();
            }

            // If choice in range, view account; else continue
            if(choice>0){
                viewAccount(result.get(choice-1));
                boolean statusRequest = connection.checkRequest(user, result.get(choice-1));
                System.out.println("1 - Back");
                if(result.get(choice-1).getUsername() != user.getUsername()){
                    if(statusRequest)
                        System.out.println("2 - Cancel friend request");
                    else
                        System.out.println("2 - Add friend");
                }else{
                    System.out.println("2 - Edit account");
                }
                System.out.println("*************************");
                int choiceToAdd = sc.nextInt();
                System.out.println("*************************");
                if(choiceToAdd==2){
                    if(statusRequest){
                        if(result.get(choice-1).getUsername() != user.getUsername())
                            connection.cancelRequest(user, result.get(choice-1));
                    }else
                        connection.sendRequest(user, result.get(choice-1));
                }
            }
        }
    }

    public void displayRequest(){
        ArrayList<User> requestList = database.getRequestList(user);
        for(int i=0; i<requestList.size(); i++){
            System.out.println(requestList.get(i).getName());
            System.out.println("(" + connection.getTotalMutual(user, requestList.get(i), graph) + " mutuals)");
            System.out.println("0 - Next");
            System.out.println("1 - Confirm");
            System.out.println("2 - Delete");
            System.out.println("*************************");
            int choice = sc.nextInt();
            System.out.println("*************************");
            switch(choice){
                case 0: continue;
                case 1: connection.confirmRequest(requestList.get(i), user, graph);
                        connection.cancelRequest(requestList.get(i), user);
                        break;
                case 2: connection.cancelRequest(requestList.get(i), user);
                        break;

            }
        }
    }

    public void displayFriends(){
        int choice = 1;
        while(choice>0){
            ArrayList<String> friends = connection.displayNewestFriends(user, graph);
            System.out.println("-1 - Sort friend list");
            System.out.println("0 - Back");
            System.out.println("*************************");
            choice = sc.nextInt();
            System.out.println("*************************");
            while(choice<0){
                System.out.println("1 - Newest friends first");
                System.out.println("2 - Oldest friends first");
                System.out.println("*************************");
                int sortingChoice = sc.nextInt();
                System.out.println("*************************");
                switch(sortingChoice){
                    case 1 -> friends = connection.displayNewestFriends(user, graph);
                    case 2 -> friends = connection.displayOldestFriends(user, graph);
                }
                System.out.println("-1 - Sort friend list");
                System.out.println("0 - Back");
                System.out.println("*************************");
                choice = sc.nextInt();
                System.out.println("*************************");

            }
            if(choice>0){
                User friend = database.getProfile(friends.get(choice-1));
                viewAccount(friend);
                System.out.println("0 - Back");
                System.out.println("1 - Remove friend");
                int removeStatus = sc.nextInt();
                System.out.println("*************************");
                if(removeStatus==1)
                    graph = graph.removeUndirectedEdge(graph, user.getUsername(), friend.getUsername());
            }
        }
    }

    public void displayRecommendedUsers(){
        ArrayList<User> recomUser = connection.recommendedUser(user, graph);
        for(int i=1; i<=recomUser.size(); i++){
            System.out.println(recomUser.get(i-1).getName());
            System.out.println("(" + connection.getTotalMutual(user, recomUser.get(i-1), graph) + " mutuals)");
            System.out.println("0 - Next");
            System.out.println("1 - Add friend");
            System.out.println("2 - Back");
            System.out.println("-1 - Homepage");
            System.out.println("*************************");
            int choice = sc.nextInt();
            System.out.println("*************************");
            switch(choice){
                case 0: continue;
                case 1: connection.sendRequest(user, recomUser.get(i-1));
                        break;
                case 2: i = i-2;
                        break;
            }
            if(choice<0)
                break;
        }
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
    public void setAdmin(){
        
        User updatedUser=database.getProfile(user.getUsername());
        updatedUser.setRole("admin");
        database.setupProfile(updatedUser);
        System.out.println("You are currently an admin.");
        
    }
    
    
    //Check if the given user is an admin
    public boolean isAdmin(String userName){
        
        User userAdmin=this.database.getProfile(userName);
        if(userAdmin.getRole().equalsIgnoreCase("admin")){
            System.out.println("The user "+userName+" is an admin.");
            return true;
        }else{
            System.out.println("The user "+userName+" is a normal user.");
            return false;
        }
        
    }
    
    
    //For admins only
    public void deleteAccount(String accountID){
        
        //To check if the user who is calling this method is an admin
        if(!isAdmin(user.getUsername())){
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
    
    
    //For admins only
    public void banUser(String userName, User.BanDuration duration) {
        
        //To check if the user who is calling this method is an admin
        if(!isAdmin(user.getUsername())){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        //To check if the user who is gonna be banned is an admin
        if (this.isAdmin(userName)) {
            System.out.println("You cannot ban an admin user.");
            return;
        }
        
        if (duration == User.BanDuration.ONE_MONTH) {
            LocalDate banEndDate = LocalDate.now().plusMonths(1);
            List<String[]> csvData = database.readCsvFileOfDatabase();
        
            // Check if the user is valid
            boolean valid = false;
        
            for(int i=0; i<csvData.size(); i++){
                String[] data = csvData.get(i);
                if(data[1].equals(userName)){
                    data[16]=String.valueOf(true);
                    data[17]=String.valueOf(banEndDate);
                    csvData.set(i, data);
                    database.writeCsvFileOfDatabase(csvData);
                    System.out.println("User "+userName+" has been banned successfully for ONE MONTH.");
                    valid = true;
                    break;
                }
            }
        
            if(valid==false)
                System.out.println("Invalid user");
            
        } else if (duration == User.BanDuration.ONE_YEAR) {
            LocalDate banEndDate = LocalDate.now().plusYears(1);
            List<String[]> csvData = database.readCsvFileOfDatabase();
        
            // Check if the user is valid
            boolean valid = false;
        
            for(int i=0; i<csvData.size(); i++){
                String[] data = csvData.get(i);
                if(data[1].equals(userName)){
                    data[16]=String.valueOf(true);
                    data[17]=String.valueOf(banEndDate);
                    csvData.set(i, data);
                    database.writeCsvFileOfDatabase(csvData);
                    System.out.println("User "+userName+" has been banned successfully for ONE YEAR.");
                    valid = true;
                    break;
                }
            }
        
            if(valid==false)
                System.out.println("Invalid user");
            
        }
        
    }
    
    
    //For admins only
    //Content is manually determined whether it's appropriate or not by the administrator
    public boolean manuallyRemoveInappropriateContent(String userName, int postIndex){
        
        //To check if the user who is calling this method is an admin
        if(!isAdmin(user.getUsername())){
            System.out.println("Sorry, this operation is for admin ONLY");
            return false;
        }
        
        User userUpdated=this.database.getProfile(userName);
        userUpdated.getPost().remove(postIndex);
        this.database.setupProfile(userUpdated);
        System.out.println("The inappropriate content has been removed successfully.");
        return true;
        
    }
    
    //To check whether the content is inappropriate or not
    private boolean isInappropriate(String post){
        if (containsOffensiveLanguage(post)) {
            return true;
        } else {
            return false;
        }
    }
    
    //To check if the content contains offensive words or not
    private boolean containsOffensiveLanguage(String content) {
        ArrayList<String> offensiveWords = new ArrayList<>();
        try {
            Scanner inputStream = new Scanner(new FileInputStream("C:\\s2137573\\FacebookDS\\ListOfProhibitedWords.txt"));
            //read all words from the prohibited words list
            while (inputStream.hasNextLine()){
                offensiveWords.add((String)inputStream.next());
                //store those words into ArrayList offensiveWords one by one
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found"); 
        }

        for (String word : offensiveWords) {
            //Extract each word from the ArrayList to see if they are included in the content of the post
            //If yes, then the content contains offensive words, return true
            if (content.toString().toLowerCase().contains((CharSequence) word)) {
                return true;
            }
        }
        return false;
    }
    
    //For admins only
    //Content is automatically determined whether it's appropriate or not by checking the prohibited words list
    public void automaticallyRemoveInappropriateContentInUserPage(String userName) {
        
        //To check if the user who is calling this method is an admin
        if(!isAdmin(user.getUsername())){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        User userUpdated=this.database.getProfile(userName);
        for (int i = 0; i < userUpdated.getPost().size(); i++) {
            if(isInappropriate(userUpdated.getPost().get(i))){
                userUpdated.getPost().remove(i);
                i--;
            }
        }
        this.database.setupProfile(userUpdated);
        System.out.println("Inappropriate content in "+userName+"'s page has been automatically cleaned.");
        
    }
    
    
    //For admins only
    //To update the prohibited words list
    public void addNewProhibitedWords(String newProhibitedWord){
        
        //To check if the user who is calling this method is an admin
        if(!isAdmin(user.getUsername())){
            System.out.println("Sorry, this operation is for admin ONLY");
            return;
        }
        
        try {
            PrintWriter outputStream = new PrintWriter(new FileOutputStream("C:\\s2137573\\FacebookDS\\ListOfProhibitedWords.txt", true));
            outputStream.println(newProhibitedWord);
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Problem with file output"); 
        }

    }
    
    
    //For all users
    //Post on personal pages
    public void createPost(String content){
        
        //check if the user is currently banned
        User userU=this.database.getProfile(user.getUsername());
        if(userU.getIsBanned()){
            System.out.println("Oops! You don't seem to be able to post anything on your page right now.");
            return;
        }
        
        User updatedUser=this.database.getProfile(user.getUsername());
        updatedUser.getPost().add(content);
        database.setupProfile(updatedUser);
        System.out.println("You have successfully posted your content on your page!");
        
    }
    
    
    //For all users
    public void viewMyPage(){
        
        System.out.println("Personal page of "+user.getUsername()+": ");
        System.out.println("-------------------------------------------");
        
        for (int i = 0; i < this.database.getProfile(user.getUsername()).getPost().size(); i++) {
            System.out.println("Post No."+(i)+": "+this.database.getProfile(user.getUsername()).getPost().get(i));
            System.out.println("-------------------------------------------------------");
        }
        
    }
    
    public void viewOthersPage(String userName){
        
        System.out.println("Personal page of "+userName+": ");
        System.out.println("-------------------------------------------");
        
        for (int i = 0; i < database.getProfile(userName).getPost().size(); i++) {
            System.out.println("Post No."+(i)+": "+database.getProfile(userName).getPost().get(i));
            System.out.println("-------------------------------------------------------");
        }
        
    }
    
    
    //For all users
    //To check the remaining banned time
    public Period getRemainingBannedTime(String userName) {
        
        User userUpdated=database.getProfile(userName);
        if (userUpdated.getIsBanned()) {
            LocalDate currentDate = LocalDate.now();
            Period p=Period.between(currentDate, database.getProfile(userName).getBanEndDate());
            System.out.println("The remaining time of being banned for "+userName+": "+p);
            return p;
        }else
            System.out.println("The remaining time of being banned for "+userName+" is none. The user is currently able to post.");
            return Period.ZERO;
        
    }
    
    /**
    //For all users
    public void createGroup(String groupName) {
        
        if(Group.allGroupNames.contains(groupName)){
            System.out.println("Sorry, this group name has been occupied. Please change another name and try again.");
            return;
        }
        
        Group newGroup = new Group(groupName, this.database.getProfile(this.builder.getUsername()));
        this.user.getGroupsList().add(newGroup.getGroupName());
        
    }
    
    
    //For all users
    public Group getGroup(User groupCreator, String groupName){
        
        for (int i = 0; i < groupCreator.getGroupsList().size(); i++) {
            if(groupName.equals(groupCreator.getGroupsList().get(i).getName())){
                return groupCreator.getGroupsList().get(i);
            }
        }
        System.out.println("Sorry, the group has not been found in the corresponding user's group list.");
        return null;
    }
    
    
    //For all users
    public void joinGroup(User groupCreator, String groupName) {
        for (int i = 0; i < this.user.getGroupsList().size(); i++) {
            if(groupName.equals(this.user.getGroupsList().get(i).getName())){
                System.out.println("Sorry, you have already joined this group. Please change another name and try again.");
                return;
            }
        }
        if(!groupCreator.getGroupNames(groupCreator).contains(groupName)){
            System.out.println("Sorry, the group name has not been found in the corresponding user's group list.");
            return;
        }
        this.user.getGroupsList().add(this.getGroup(groupCreator, groupName));
        this.getGroup(groupCreator, groupName).addMember(this.user);
    }
    
    
    //For all users
    public void inviteMemberToAGroup(String groupName, User invitee) {
        User creator=null;
        for (int i = 0; i < this.user.getGroupsList().size(); i++) {
            if(groupName.equals(this.user.getGroupsList().get(i).getName())){
                creator=this.user.getGroupsList().get(i).getgroupAdmin();
            }
        }
        getGroup(creator,groupName).inviteMember(this.user, invitee);
    }
    
    
    //For all users
    public List<String> getGroupNames() {
        
        return this.user.getGroupNames(this.user);
    }
    */

}