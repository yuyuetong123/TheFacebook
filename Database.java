package Facebook;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;//For Encryption
import java.security.NoSuchAlgorithmException;//For Encryption


public class Database {
    Random rand = new Random();
    UserBuilder builder;
    String csvFile = "userData.csv";

    public Database(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))){
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String hashPassword(String password) {
        try {
            //Create MessageDigest instance for SHA-256 hashing
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hashed password bytes
            byte[] hashedBytes = md.digest();
            //Convert the hashed bytes to a hexadecimal representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            //Return the hashed password as a string
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void registerUser(User user){
        //Hash the password before storing it
        String hashedPassword = hashPassword(user.getPassword());
        String[] user_data = {user.getAccountID(), user.getUsername(), user.getEmail(), user.getPhoneNo(), hashedPassword, user.getRole()};
        String line = String.join(",", user_data);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            // Write the new user data into CSV file
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLogin(String emailOrPhoneNo, String password){
        //Hash the provided password for comparison
        String hashedPassword = hashPassword(password);
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if((rowData[2].equals(emailOrPhoneNo) || rowData[3].equals(emailOrPhoneNo)) && rowData[4].equals(hashedPassword))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Incorrect email address/phone number or incorrect password");
        return false;
    }

    public void setupProfile(User user){
        String accountID = user.getAccountID();
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if(rowData[0].equals(accountID)){
                    lines.add(String.format("%s,%s,%s,%s,%s,%s,%s,%s,\"%s\",\"%s\"", line, user.getName(), user.getBirthday(), user.getAge(), user.getAddress(), user.getGender(), user.getStatus(), user.getNoOfFriends(), user.getHobbies(), user.getJobs()));
                }else
                    lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (String rowData : lines) {
                String line = String.join(",", rowData);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getProfile(String emailOrPhoneNo){
        UserBuilder builder = new UserBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if(rowData[1].equals(emailOrPhoneNo) || rowData[2].equals(emailOrPhoneNo)){
                    builder.setAccountID(rowData[0]);
                    builder.setUsername(rowData[1]);
                    builder.setEmail(rowData[2]);
                    builder.setPhoneNo(rowData[3]);
                    builder.setPassword(rowData[4]);
                    builder.setRole(rowData[5]);
                    if(rowData.length>6){
                        builder.setName(rowData[6]);
                        builder.setBirthday(rowData[7]);
                        builder.setAge(Integer.parseInt(rowData[8]));
                        builder.setAddress(rowData[9]);
                        builder.setGender(rowData[10].charAt(0));
                        builder.setStatus(rowData[11]);
                        builder.setNoOfFriends(Integer.parseInt(rowData[12]));
                        builder.setHobbies(new ArrayList<String>(Arrays.asList(rowData[13].split(","))));
                        String[] tempStack = rowData[14].split(",");
                        Stack<String> jobs = new Stack<>();
                        for(String x : tempStack){
                            jobs.push(x);
                        }
                        builder.setJobs(jobs);
                    }
                    System.out.println(builder.getAccountID());
                    return builder.build();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("No such user found.");
        return null;
    }

    public boolean verifyUsername(String username){
        // Check length of username
        if(username.length()<5 || username.length()>20){
            System.out.println("Your username must be between 5-20 characters.");
            return false;
        }
        
        // Username regex pattern
        String usernameRegex = "^[a-zA-Z0-9.!#$%&��*+=?^_~]*$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(usernameRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(username);

        // Perform the matching
        if(!matcher.matches()){
            System.out.println("Invalid username.");
            return false;
        }

        // Check for duplicated usernames
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if(rowData[1].equals(username)){
                    System.out.println("This username is occupied, please use another username.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean verifyEmail(String email){
        // Email regex pattern
        String emailRegex = "^[a-zA-Z0-9.!#$%&��*+/=?^_`{|}\\-~]+@[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Perform the matching
        if(!matcher.matches()){
            System.out.println("Invalid email address.");
            return false;
        }

        // Check for duplicated emails
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if(rowData[2].equals(email)){
                    System.out.println("This email address is occupied, please use another email address.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean verifyPhoneNo(String phoneNo){
        // Check length of phone number
        if(phoneNo.length()<7 || phoneNo.length()>14){
            System.out.println("Your phone number must be between 7-14 digits.");
            return false;
        }
        
        // Email regex pattern
        String emailRegex = "^[0-9]+-[0-9]*$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(phoneNo);

        // Perform the matching
        if(!matcher.matches()){
            System.out.println("Invalid phone number.");
            return false;
        }
        

        // Check for duplicated emails
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if(rowData[3].equals(phoneNo)){
                    System.out.println("This phone number is occupied, please use another phone number.");
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int generateAccountID() {
        int temp = rand.nextInt(100000);
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.csv"))) {
            Set<String> accountIDs = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String storedAccountID = userData[0];
                accountIDs.add(storedAccountID);
            }

            do {
                temp = rand.nextInt(100000);
            } while (accountIDs.contains(String.valueOf(temp)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
    
    
    public List<String[]> readCsvFileOfDatabase(){
        List<String[]> csvData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                csvData.add(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return csvData;
    }
    
    public void writeCsvFileOfDatabase(List<String[]> csvData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (String[] rowData : csvData) {
                String line = String.join(",", rowData);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}