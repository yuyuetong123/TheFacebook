package Facebook;


import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.io.*;

public class User {
    Database database;
    private String accountID;
    private String username;
    private String email;
    private String phoneNo;
    private String password;
    private String role;
    private String name;
    private String birthday;
    private int age;
    private String address;
    private char gender;
    private String status;
    private int noOfFriends;
    private ArrayList<String> hobbies;
    private Stack<String> jobs;
    private boolean isBanned;//
    private LocalDate banEndDate;//
    private List<Post> page;//
    private List<Group> groupsList;//
    enum BanDuration {
        ONE_MONTH,
        ONE_YEAR,
        PERMANENT
    }

    public User(UserBuilder builder){
        this.accountID = builder.accountID;
        this.username = builder.username;
        this.email = builder.email;
        this.phoneNo = builder.phoneNo;
        this.password = builder.password;
        this.role = builder.role;
        this.name = builder.name;
        this.birthday = builder.birthday;
        this.age = builder.age;
        this.address = builder.address;
        this.gender = builder.gender;
        this.status = builder.status;
        this.noOfFriends = builder.noOfFriends;
        this.hobbies = builder.hobbies;
        this.jobs = builder.jobs;
        page=new ArrayList<Post>();//User's Personal Page
        groupsList=new ArrayList<Group>();//Group List that the user joined
    }

    public void setAccountID(){
        this.accountID = String.valueOf(database.generateAccountID());
    }
    public String getAccountID(){
        return accountID;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setPhoneNo(String phoneNo){
        this.phoneNo = phoneNo;
    }
    public String getPhoneNo(){
        return phoneNo;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return role;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }
    public String getBirthday(){
        return birthday;
    }

    public void setAge(int age){
        this.age = age;
    }
    public int getAge(){
        return age;
    }

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }

    public void setGender(char gender){
        this.gender = gender;
    }
    public char getGender(){
        return gender;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){
        return status;
    }

    public void setNoOfFriends(int noOfFriends){
        this.noOfFriends = noOfFriends;
    }
    public int getNoOfFriends(){
        return noOfFriends;
    }

    public void setHobbies(String hobbies){
        this.hobbies.add(hobbies);
    }
    public ArrayList<String> getHobbies(){
        return hobbies;
    }

    public void setJobs(String job){
        this.jobs.push(job);
    }
    public Stack<String> getJobs(){
        return jobs;
    }
    
//******
    public List<Post> getPage() {
        return page;
    }

    public List<Group> getGroupsList() {
        return groupsList;
    }

    public LocalDate getBanEndDate() {
        return banEndDate;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanStatus(boolean isBanned, LocalDate banEndDate) {
        this.isBanned = isBanned;
        this.banEndDate = banEndDate;
    }
    
    public List<String> getGroupNames(User creator) {
        List<String> groupNames = new ArrayList<>();
        for (Group group : creator.getGroupsList()) {
            groupNames.add(group.getName());
        }
        return groupNames;
    }

}