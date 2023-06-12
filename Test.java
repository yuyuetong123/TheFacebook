package com.facebook.fullstackbackend.model;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountManagement manager = new AccountManagement();
        User user = null;
        
        System.out.println("\tFacebook");
        System.out.println("------------------------------");
        System.out.println("1 - Register");
        System.out.println("2 - Login");
        int choice = sc.nextInt();
        switch(choice){
            case 1: manager.registration();
                    user = manager.login();
                    break;
            case 2: user = manager.login();
                    break;
        }
        if(user!=null){
            int choice1 = 1;
            while(choice1>0){
                System.out.println("0 - Log out");
                System.out.println("1 - Post");
                System.out.println("2 - View my page");
                System.out.println("3 - Set your role as an admin");
                System.out.println("4 - Ban an user");
                System.out.println("5 - Get remaining banned time of an user");
                System.out.println("6 - Manually remove inappropriate content");
                System.out.println("7 - Automatically remove inappropriate content in an user's page");
                System.out.println("8 - Update Prohibited Words List");
                System.out.println("9 - Delete Account");
                System.out.println("*************************");
                choice1 = sc.nextInt();
                sc.nextLine();
                System.out.println("*************************");
                switch(choice1){
                    case 1: manager.postManager.createPost(manager.user);
                            break;
                    case 2: manager.viewMyPage();
                            break;
                    case 3: manager.setAdmin();
                            break;
                    case 4: System.out.println("Please select banned duration: \n1 for 1 week; 2 for 1 month; 3 for 1 year");
                            int select=sc.nextInt();
                            System.out.println("Please enter username of the user: ");
                            String username=sc.next();
                            if(select==1){
                                manager.banUser(manager.database.getProfile(username), User.BanDuration.ONE_WEEK);
                            }else if(select==2){
                                manager.banUser(manager.database.getProfile(username), User.BanDuration.ONE_MONTH);
                            }else if(select==3){
                                manager.banUser(manager.database.getProfile(username), User.BanDuration.ONE_YEAR);
                            }
                            break;
                    case 5: System.out.println("Please enter username of the user you want to check: ");
                            String username2=sc.next();
                            manager.getRemainingBannedTime(manager.database.getProfile(username2));
                            break;
                    case 6: System.out.println("Please enter username of the user: ");
                            String username3=sc.next();
                            System.out.println();
                            System.out.println("Please enter the id of the post you want to remove: ");
                            String postNo=sc.next();
                            manager.manuallyRemoveInappropriateContent(manager.database.getProfile(username3),manager.database.getPost(postNo));
                            break;
                    case 7: System.out.println("Please enter username of the user: ");
                            String username4=sc.next();
                            System.out.println();
                            System.out.println("Please enter the id of the post you want to remove: ");
                            String postId=sc.next();
                            manager.autoRemoveInappropriateContent(manager.database.getProfile(username4), manager.database.getPost(postId));
                            break;
                    case 8: System.out.println("Please enter the word: ");
                            String word=sc.next();
                            manager.updateProhibitedWord(word);
                            break;
                    case 9: System.out.println("Please enter username of the user: ");
                            String username5=sc.next();
                            manager.deleteAccount(manager.database.getProfile(username5));
                            break;
                }
            }
        }
    }
}
