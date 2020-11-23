import java.io.*;  
import java.sql.*;
import java.util.Scanner;


public class bankprogram {
	public static String username;
	public static String password;
  public static void main (String[] arg) {     
	  Scanner scan = new Scanner(System.in);
	  boolean loginCheck = false; 
	 
	  Connection con = null;
	  int cust_id = 0;
	 
     //Login
	  do {
		  try {
			  System.out.println("enter username");
			  username = scan.nextLine();
		  
			  System.out.println("enter password");
			  password = scan.nextLine();
			  con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
			  	  
		  
			  loginCheck = false;
			  con.close();
		  }
		  catch(SQLException e) {
			  e.getMessage();
			  System.out.println("you entered the wrong password");
			  loginCheck= true;
			
		  }
       }while(loginCheck == true);
	 

       chooseRole();
                 
    			
  }
  
  // choose role as a manager or customer
  public static void chooseRole() {     
	  
	  String role = null;
	  Scanner scan = new Scanner(System.in);
	  boolean roleCheck = true;
	  boolean goBack = false;
	  
	  do {
		 roleCheck = true;
		 while(roleCheck){
			 System.out.println("are you a manager or customer?");
			 role= scan.next();
			 scan.nextLine();
			 if(role.equals("manager")){
				 
				 goBack = manager_interface();
				 roleCheck = false;
				 
			 }
			 else if(role.equals("customer")){
				 
				 goBack = cust_interface();
				 roleCheck = false;
			 }
			 else {
				 System.out.println("wrong input try again");
				 
			 }

		     
		  }
	  }while(goBack);
	  
  }
  
  
  public static boolean manager_interface() {     
      boolean rerun = true;
      Scanner scan = new Scanner(System.in);
	  while(rerun) {
		  System.out.println("enter the number corresponding to the interface you want:");
		  System.out.println("(1) check total amount of loan");
		  System.out.println("(2) check total amount in accounts");
		  System.out.println("(3) go back to selecting role as manager or customer");
		  System.out.println("(4) quit");
		  String intDecision = scan.nextLine();
	
		  if(intDecision.equals("1")) {
			 get_loanAmt();
		  }
		  else if(intDecision.equals("2")) {
			 get_acctAmt();
		  }
		  else if(intDecision.equals("4")) {
			  System.out.println("exiting program");
			  rerun = false;
			  
		  }
		  else if(intDecision.equals("3")) {
			  return true;
			  
			  
		  }
		  else {
			  System.out.println("not a valid input, try again");
		  }
	  }
	  return false; 
	  
  }
	  
  
  
  public static boolean cust_interface() {        

	  
	  boolean custIDcheck = true;
	  Scanner scan = new Scanner(System.in);
	  System.out.println("if you want to input a specific customer id enter y, input anything else to get a customer id given to you");
	  String findCust = scan.nextLine();

	  int cust_id = 0;
	  boolean real_cust = true;
	  
		  if(findCust.equals("y")){
			  cust_id = get_cust();
			  
			  
		  }
		  else {

			  cust_id = 100;
			  System.out.println("Name: Wye Laugier, Customer_ID: "+cust_id);
			 
		  }
		  
		  boolean rerun = true;
		  
	
		  while(rerun) {
		  System.out.println("enter the number corresponding to the interface you want:");
		  System.out.println("(1) Deposit/Withdraw");
		  System.out.println("(2) Credit Card Purchase");
		  System.out.println("(3) Debit Card Purchase");
		  System.out.println("(4) Take out a new loan");
		  System.out.println("(5) create a new account");
		  System.out.println("(6) quit");
		  System.out.println("(7) go back to selecting role as customer or manager");
		  String intDecision = scan.nextLine();

		  if(intDecision.equals("1")) {
			 
			  with_dep(cust_id);
			  
		  }
		  else if(intDecision.equals("2"))
		  {
			  credit_card_purchase(cust_id);
		  }
		  else if(intDecision.equals("3"))
		  {
			  debit_card_purchase(cust_id);
		  }
		  else if(intDecision.equals("4")) {
			  
			  newloan(cust_id);
		  }
		  else if(intDecision.equals("5")) {
			  
			  add_account(cust_id);
		  }
		  else if(intDecision.equals("6")) {
			  System.out.println("exiting program");
			  rerun = false;
		  }
		  else if(intDecision.equals("7")) {
			  
			  return true;
		  }
		  else {
			  System.out.println("invalid input, try again");
		  }
		  }
		  return false; 

	  
	  }
  
  
  
  public static void debit_card_purchase(int cust_id) {     
	  
	  Scanner scan = new Scanner(System.in);
	  
	    boolean real_acct = true;
	    int account_id = -1;
	    int dummy = -1;
	    String dummy2 = null;
	    String cardnumber = null;
	  
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
	      PreparedStatement listAcct = con.prepareStatement("select account_id from owns natural join accounts natural join debit_card where customer_id = ?");
	      PreparedStatement listCards = con.prepareStatement("select deb_card_number from debit_card where account_id = ?");
		  CallableStatement deb_pur = con.prepareCall("{call debit_card_purchase(?,?,?,?,?,?,?)}");
	 			   ){
		  
		  System.out.println("These are the checking accounts ID that have a debit card associated with you: ");
		  listAcct.setInt(1, cust_id);
		  ResultSet acctList = listAcct.executeQuery();
		 
		  if(!acctList.next()) {
			  
			  System.out.println("You have no accounts with cards, can't perform this action returning to previous interface");
			  return;
			  
		  }
		  do {  
			  System.out.print(acctList.getInt(1));
			  }while(acctList.next());
		  System.out.println("");
		  
			 do {
	 			  System.out.println("Enter account ID you want to perform action on ");
		 	      while(!scan.hasNextInt()) {
					    scan.next();
					    System.out.println("bad input try again");
					}
		 	            account_id = scan.nextInt();
		 	            scan.nextLine();
		 	            listAcct.setInt(1, cust_id);
						ResultSet acctList2 = listAcct.executeQuery();
		 	        
		 	            while(acctList2.next()){
		 	  
		 			 	dummy = acctList2.getInt(1);
		 			 	
		 			 	if(dummy == account_id) {
		 			 	real_acct = false;
		 			    }
		 			 			      
		 			  }
		 	            
						if(real_acct == true) {
							  System.out.println("This is not an account you own, try again:");
						  }
						
	 			 }while(real_acct ==true);
		  
		  System.out.println("These are the debit cards associated with the account: ");
		  listCards.setInt(1,account_id);
		 
		  ResultSet cardList = listCards.executeQuery();
		  while(cardList.next()) {
			  System.out.println(cardList.getString(1));
		  }

		  ResultSet cardList2 = null;
		  
          do {
        	  cardList2 = listCards.executeQuery();
    		  
        	  System.out.println("enter the card number you want to use to buy: ");
        	  
	 	            cardnumber = scan.nextLine();

	 	         
	 	            while(cardList2.next()){
	 	  
	 			 	dummy2 = cardList2.getString(1);
	 			 	
	 			 	if(dummy2.equals(cardnumber)) {
	 			 	real_acct = false;
	 			    }
	 			 			      
	 			  }
	 	            
					if(real_acct == true) {
						  System.out.println("This is not a card you own, try again:");
					  }
				
 			 }while(real_acct ==true);
          PreparedStatement currentBal = con.prepareStatement("select get_acct_balance(?) from dual");
	      currentBal.setInt(1, account_id);
	      ResultSet bal = currentBal.executeQuery();
	      while(bal.next()){  
	      System.out.println("Current Balance for this account is : "+bal.getDouble(1));
	      }
	      currentBal.close();
          
          
          double amount = amt_in();
    

		     System.out.println("enter vendor name");
		     String v_name = scan.nextLine();
          
		      deb_pur.registerOutParameter(5, Types.DOUBLE);
			  deb_pur.registerOutParameter(6, Types.VARCHAR);
			  deb_pur.registerOutParameter(7, Types.INTEGER);
			  deb_pur.setInt(1,account_id);
			  deb_pur.setString(2,cardnumber);
			  deb_pur.setDouble(3,amount);
			  deb_pur.setString(4,v_name);
			  deb_pur.execute();
			  
			  String message = deb_pur.getString(6);
 		      double afterbal = deb_pur.getDouble(5);
 		      int transid = deb_pur.getInt(7);
		      System.out.println(message);
		      System.out.println("new  balance: "+afterbal);
		      if(transid == 0) {
 		    	  
 		      }
 		      else {
 		    	 System.out.println("Transaction's id is "+transid);
 		      }
 		      
		    
		  
		  
	  }
	  catch(Exception e){
		  e.getMessage();
		  System.out.println("something went wrong, transaction did not occur");
		  
	  }
	  
  }

  
  
  public static void credit_card_purchase(int cust_id) {          
	  Scanner scan = new Scanner(System.in);
	  
	  
	  String cred_num = null;
	  
	  
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
		  PreparedStatement listCards = con.prepareStatement("select cred_card_number from credit_card where customer_id =?");
		  CallableStatement cred_pur = con.prepareCall("{call credit_card_purchase (?,?,?,?,?,?,?,?)}");
		 			   ){
			  
			  System.out.println("These are the cards numbers that belong to this customer: ");
			  listCards.setInt(1, cust_id);
			   ResultSet cardList = listCards.executeQuery();
			  if(cardList.next()==false) {
				  System.out.println("You have no credit cards, can't perform this action returning to previous interface");
				  return;
				  
			  }
			    do{  
				  System.out.print(cardList.getString(1));
				  System.out.println("");
				  } while(cardList.next());  
			  System.out.println("");
			  
			  
			  boolean real_acct = true;
			  String dummy2 = null;
			  listCards.setInt(1, cust_id);
			  ResultSet cardList2 = null;
			  
	          do {
	        	  cardList2 = listCards.executeQuery();
	        	  System.out.println("enter the card number you want to use to buy: ");
	        	  
		 	            cred_num = scan.nextLine();
		 	            
		 	            while(cardList2.next()){
		 	  
		 			 	dummy2 = cardList2.getString(1);
		 			 	
		 			 	if(dummy2.equals(cred_num)) {
		 			 	real_acct = false;
		 			    }
		 			 			      
		 			  }
		 	            
						if(real_acct == true) {
							  System.out.println("This is not a card you own, try again:");
						  }
						
	 			 }while(real_acct ==true);
			  
	          PreparedStatement card_info = con.prepareStatement("select credit_limit, balance, payment_due from credit_card where cred_card_number = ?");
		      card_info.setString(1, cred_num);
		      ResultSet info = card_info.executeQuery();
		      while(info.next()){  
		      System.out.println("Credit limit for this card is : "+info.getDouble(1));
		      System.out.println("balance for this card is : "+info.getDouble(2));
		      System.out.println("balance due for this card is : "+info.getDouble(3));
		      }
		      card_info.close();
			  
		      double amount = amt_in();
			 
			 
			  System.out.println("enter vendor name");
			  String v_name = scan.nextLine();
			  
			  cred_pur.registerOutParameter(5, Types.DOUBLE);
			  cred_pur.registerOutParameter(6, Types.VARCHAR);
			  cred_pur.registerOutParameter(7, Types.DOUBLE);
			  cred_pur.registerOutParameter(8, Types.INTEGER);
			  cred_pur.setInt(1,cust_id);
			  cred_pur.setString(2,cred_num);
			  cred_pur.setDouble(3,amount);
			  cred_pur.setString(4,v_name);
			  cred_pur.execute();
			  
			  String message = cred_pur.getString(6);
 		      double afterbal = cred_pur.getDouble(5);
 		      double newDue = cred_pur.getDouble(7);
 		      int transid = cred_pur.getInt(8);
 		      
 		      System.out.println(message);
		      System.out.println("new outstanding balance: "+afterbal);
		      System.out.println("new payment due: "+newDue);
		      if(transid == 0) {
 		    	  
 		      }
 		      else {
 		    	 System.out.println("Transaction's id is "+transid);
 		      }
 		      
			  
			  
			  
	  }
	  catch(Exception e) {
		  e.getMessage();
		  System.out.println("something went wrong, transaction did not occur");
	  }
	  
	  
  }

  
  
  public static void with_dep(int cust_id) {       
	    
	    Scanner scan = new Scanner(System.in);
	    boolean real_acct = true;
	    int account_id = -1;
	    int dummy = -1;
		
		
	 	   try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
	 		   PreparedStatement listAcct = con.prepareStatement("select account_id,account_type from accounts natural join owns where customer_id =?");
	 		   CallableStatement deposit = con.prepareCall("{call deposit(?,?,?,?,?,?)}");
	 		   
	 			   ){ 
	 		  System.out.println("These are the account ID associated with you: ");
			  listAcct.setInt(1, cust_id);
			  ResultSet acctList = listAcct.executeQuery();
			  if(!acctList.next()) {
				  System.out.println("You have no accounts, can't perform this action returning to previous interface");
				  return;
			  }
			  else {
				 do{  
					  System.out.print(acctList.getInt(1));
					  System.out.print(" - ");
					  System.out.print(acctList.getString(2));
					  System.out.println("");
					  }while(acctList.next());  
			  }
			  
			  do {
	 			  System.out.println("Enter account ID you want to perform action on ");
		 	      while(!scan.hasNextInt()) {
					    scan.next();
					    System.out.println("bad input try again");
					}
		 	            account_id = scan.nextInt();
		 	            scan.nextLine();
						ResultSet acctList2 = listAcct.executeQuery();
		 	        
		 	            while(acctList2.next()){
		 	  
		 			 	dummy = acctList2.getInt(1);
		 			 	
		 			 	if(dummy == account_id) {
		 			 	real_acct = false;
		 			    }
		 			 			      
		 			  }
		 	            
						if(real_acct == true) {
							  System.out.println("This is not an account you own, try again:");
						  }
						
	 			 }while(real_acct ==true);
			  
			  
			  PreparedStatement minBal = con.prepareStatement("select min_balance from saving_account where account_id = ?");
			  minBal.setInt(1, account_id);
			  ResultSet balmin = minBal.executeQuery();
		      while(balmin.next()){
		      System.out.println("The Minimum Balance for this account is : "+balmin.getDouble(1)+", If overdrawn, a penalty will be put in place (5% of balance will be deducted)");
		      }
		      minBal.close();
			  
		      String address = get_address();
		      

		      System.out.println("enter the w for withdrawal, d for deposit, anything else to return to previous interface");
	 		  String wOrD = scan.nextLine();
	 		  
	 		  if(wOrD.equals("w")) {
	 			 
			      PreparedStatement currentBal = con.prepareStatement("select get_acct_balance(?) from dual");
			      currentBal.setInt(1, account_id);
			      ResultSet bal = currentBal.executeQuery();
			      while(bal.next()){  
			      System.out.println("Current Balance for this account is : "+bal.getDouble(1));
			      }
			      currentBal.close();
			      
			      boolean badInput = true;
			      double amount = amt_in();
	 		
	 		      CallableStatement withdraw = con.prepareCall("{call withdraw(?,?,?,?,?,?)}");
	 		      
	 		      withdraw.registerOutParameter(4, Types.VARCHAR);
	 		      withdraw.registerOutParameter(5, Types.DOUBLE);
	 		      withdraw.registerOutParameter(6, Types.INTEGER);
	 		      withdraw.setInt(1, account_id);
	 		      withdraw.setDouble(2, amount);
	 		      withdraw.setString(3, address);
	 		      withdraw.execute();
	 		      
	 		     
	 		      String message = withdraw.getString(4);
	 		      double afterbal = withdraw.getDouble(5);
	 		      int transID = withdraw.getInt(6);
	 		      System.out.println(message);
	 		      System.out.println("new balance: "+afterbal);
	 		     if(transID == 0) {
	 		    	  
	 		      }
	 		      else {
	 		    	 System.out.println("Transaction's id is "+transID);
	 		      }
	 		      
	 		      withdraw.close();
		 		
	 			  
	 		  }
	 		  else if(wOrD.equals("d")) {
	 			  
	 			  PreparedStatement tellerCheck = con.prepareStatement("select hasteller from bank_branch where branch_address = ?");
			      tellerCheck.setString(1, address);
			      ResultSet checkTeller = tellerCheck.executeQuery();
			      String hasTeller = null;
			      while(checkTeller.next()) {
			    	  hasTeller = checkTeller.getString(1);
			      }
			      tellerCheck.close();
			      if(hasTeller.equals("no")) {
			    	  System.out.println("the bank address you chose doesnt have a teller, can't perform deposit, returning to previous interface.");
			    	  return;
			    	
			      }
	 			  
			      PreparedStatement currentBal = con.prepareStatement("select get_acct_balance(?) from dual");
			      currentBal.setInt(1, account_id);
			      ResultSet bal = currentBal.executeQuery();

			      while(bal.next()){  
			      System.out.println("Current Balance for this account is : "+bal.getDouble(1));
			      }
			      currentBal.close();
			      
			      double amount = amt_in();
			 		 
	 		      deposit.registerOutParameter(4, Types.VARCHAR);
	 		      deposit.registerOutParameter(5, Types.DOUBLE);
	 		      deposit.registerOutParameter(6, Types.INTEGER);
	 		      deposit.setInt(1,account_id);
	 		      deposit.setDouble(2,amount);
	 		      deposit.setString(3,address);
	 		      deposit.execute();
	 		     
	 		      String message = deposit.getString(4);
	 		      double afterbal = deposit.getDouble(5);
	 		      int transid = deposit.getInt(6);
	 		      
	 		      System.out.println(message);
	 		      System.out.println("new balance: "+afterbal);
	 		      if(transid == 0) {
	 		    	  
	 		      }
	 		      else {
	 		    	 System.out.println("Transaction's id is "+transid);
	 		      }
	 		      

	 		  }
	 		  else {
	 			  
	 		  }
	 			}
	 	   		 catch(Exception e) {
	 	   					e.getMessage();
	 	   				System.out.println("something went wrong, transaction did not occur");
	 	   			}

	}

  
  
  public static int get_cust() {      

	  Scanner scan = new Scanner(System.in);
	  int cust_id = -1;
	  boolean real_cust = true;
	  
	  
	    try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
		PreparedStatement cust_e = con.prepareStatement("select customer_id from customer where customer_id =?");
		 			   ){ 
		 
		do {
		  System.out.print("Enter customer ID: ");
		  while(!scan.hasNextInt()) {
			    scan.next();
			    System.out.println("bad input try again");
			}
			cust_id = scan.nextInt();
			scan.nextLine();
			cust_e.setInt(1, cust_id);
			ResultSet cust = cust_e.executeQuery();
			if(!cust.next()) {
				real_cust = false;
				 System.out.println("This is not a valid customer, try again:");
			}
			else {
			real_cust = true;
			}
		}while(real_cust ==false);
			
	}
	catch(Exception e) {
		e.getMessage();
		System.out.println("something went wrong, transaction did not occur");
	}
	    return cust_id;
	  
	  
  }
  
  
  
  public static void add_account(int cust_id) {      
	  Scanner scan = new Scanner(System.in);
	  double min_bal = 0;
	  double interest = -1;
	  boolean badInput = true;
	  String acct_type = null;
	  boolean c_type = true;
	  
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
			  CallableStatement new_acct = con.prepareCall("{call create_acct(?,?,?,?,?,?)}");
			 			   ){
	  do {
		 System.out.println("enter the account type (saving or checking)");
		 acct_type = scan.nextLine();
		 if(acct_type.equals("saving")) {
	
	 		 do {
		 		  badInput = true;
		 		  System.out.println("Enter minimum balance to set for this account (postive only)");
	 			  while(!scan.hasNextDouble()) {
					    scan.next();
					    System.out.println("not a valid input, try again.");
	 			  }
				  min_bal = scan.nextDouble();
				  scan.nextLine();
				  if(min_bal < 0) {
					  badInput = false;
					  System.out.println("Positive numbers only please, try again.");
					  
				  }
				  
		 		 }while(!badInput);
	 		 
			 c_type = true;
		 }
		 else if(acct_type.equals("checking")) {
			 c_type = true;
		 }
		 else {
			 System.out.println("invalid input try again");
			 c_type = false;
		 }
		  
	  }while(!c_type);
		  
	  
	  do {
 		  badInput = true;
 		  System.out.println("enter interest rate % you want to set (ie. if 15.5% enter 15.5)");
			  while(!scan.hasNextDouble()) {
			    scan.next();
			    System.out.println("not a valid input, try again.");
			  }
		  interest = scan.nextDouble();
		  scan.nextLine();
		  if(interest < 0 || interest > 100) {
			  badInput = false;
			  System.out.println("not valid input, must be postive and between 0-100 try again.");
			  
		  }
		  
 		 }while(!badInput);
	  
	 
		  new_acct.registerOutParameter(5, Types.VARCHAR);
		  new_acct.registerOutParameter(6, Types.INTEGER);
		  new_acct.setInt(1,cust_id);
		  new_acct.setString(2,acct_type);
		  new_acct.setDouble(3,interest);
		  new_acct.setDouble(4,min_bal);
		  new_acct.execute();
		  
		  String message = new_acct.getString(5);
		  int newAcctid = new_acct.getInt(6);
		      
		  System.out.println(message);
		  System.out.println("your new account's id is "+newAcctid);
	      System.out.println("");
	      
	      System.out.println("If you want to make a deposit into this account press y if not press anything else to return to previous interface");
	      String makeDeposit = scan.nextLine();
	      
	      
	      if (makeDeposit.equals("y")){
	    	  with_dep(cust_id);
	      }
		  
		  
	  }
	  catch(Exception e) {
		  e.getMessage();
		  System.out.println("something went wrong, transaction did not occur");
	  }

  }
  
  public static void get_loanAmt() {  
	  Scanner scan = new Scanner(System.in);
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
		  Statement getLoans =con.createStatement();
		   ){
		 
		  String option = null;
		  boolean badInput = true;
		  ResultSet result = null;
	
		  do {
			  badInput = true;
			  System.out.println("if you want to get the total value of all loans outstanding enter t, total value of all mortgages enter m, total value of all unsecured loans enter u ");
			  option = scan.nextLine();
			  if(option.equals("t")) {
				 String q = "select sum(amount) from loans";
				 result = getLoans.executeQuery(q);
				 while(result.next()) {
					 System.out.println("Total amount of loans is: "+result.getDouble(1));
				 }
			  }
			  else if(option.equals("m")) {
				 String q = "select sum(amount) from loans where loan_type = 'mortgage'";
				 result = getLoans.executeQuery(q);
				 while(result.next()) {
					 System.out.println("Total amount of mortagages is: "+result.getDouble(1));
				 }
			  }
			  else if(option.equals("u")){
				 String q = "select sum(amount) from loans where loan_type = 'unsecured'"; 
				 result = getLoans.executeQuery(q);
				 while(result.next()) {
					 System.out.println("Total amount of unsecured loans is: "+result.getDouble(1));
				 }
			  }
			  else {
				  System.out.println("invalid option try again");
				  badInput = false;
			  }
			 
		  }while(!badInput);
		 
		  
		  
	  }
	  catch(Exception e ) {
		  e.getMessage();
		  System.out.println("Something went wrong");
		 
	  }
  }
  
  public static void get_acctAmt() {  
	  Scanner scan = new Scanner(System.in);
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
		  Statement getAcct =con.createStatement();
		   ){
		  String option = null;
		  boolean badInput = true;
		  ResultSet result = null;
	
		  do {
			  badInput = true;
			  System.out.println("if you want to get the total value of all accounts enter t, total value of all saving accounts enter s, total value of all checking accounts enter c ");
			  option = scan.nextLine();
			  if(option.equals("t")) {
				 String q = "select sum(account_balance) from accounts";
				 result = getAcct.executeQuery(q);
				 while(result.next()) {
					 System.out.println("the total value of all accounts: "+result.getDouble(1));
				 }
			  }
			  else if(option.equals("s")) {
				 String q = "select sum(account_balance) from accounts where account_type ='saving'";
				 result = getAcct.executeQuery(q);
				 while(result.next()) {
					 System.out.println("the total value of all saving accounts: "+result.getDouble(1));
				 }
			  }
			  else if(option.equals("c")){
				 String q = "select sum(account_balance) from accounts where account_type ='checking'"; 
				 result = getAcct.executeQuery(q);
				 while(result.next()) {
					 System.out.println("the total value of all checking accounts: "+result.getDouble(1));
				 }
			  }
			  else {
				  System.out.println("invalid option try again");
				  badInput = false;
			  }
			 
		  }while(!badInput);
		 
		  
		  
	  }
	  catch(Exception e) {
		  e.getMessage();
		  System.out.println("something went wrong");
	  }
  }
  public static void newloan(int cust_id) { 
	  Scanner scan = new Scanner(System.in);
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
	      CallableStatement new_loan = con.prepareCall("{call newloan(?,?,?,?,?,?,?,?,?)}");
		   ){
		  System.out.println("what is the value of the loan?");
		  Double amount = amt_in();
		  boolean badInput = true;
		  double interest = -1;
		  do {
	 		  badInput = true;
	 		  System.out.println("enter interest rate % you want to set (ie. if 15.5% enter 15.5)");
				  while(!scan.hasNextDouble()) {
				    scan.next();
				    System.out.println("not a valid input, try again.");
				  }
			  interest = scan.nextDouble();
			  scan.nextLine();
			  if(interest < 0 || interest > 100) {
				  badInput = false;
				  System.out.println("not valid input, must be postive and between 0-100 try again.");
				  
			  }
			  
	 		 }while(!badInput);
		  String loan_type = null;
		  String loan_address = "nothing";
		  Double home_value = -1.00;
		  do {
	 		  badInput = true;
	 		  System.out.println("enter loan type you want, mortgage or unsecured");
			
			  loan_type = scan.nextLine();
			
			  if(loan_type.equals("mortgage")) {
				  System.out.println("enter the home address");
				  loan_address = scan.nextLine();
				  System.out.println("input home value");
				  home_value = amt_in();
				  
			  }
			  else if(loan_type.equals("unsecured")) {
				  
			  }
			  else {
				  badInput = false;
				  System.out.println("not valid input try again.");
				  
			  }
			  
	 		 }while(!badInput);
		  
		  new_loan.registerOutParameter(7, Types.DOUBLE);
		  new_loan.registerOutParameter(8, Types.INTEGER);
		  new_loan.registerOutParameter(9, Types.VARCHAR);
		  new_loan.setInt(1,cust_id);
		  new_loan.setDouble(2,amount);
		  new_loan.setDouble(3,interest);
		  new_loan.setString(4,loan_type);
		  new_loan.setString(5,loan_address);
		  new_loan.setDouble(6,home_value);
		  new_loan.execute();
		  
		  double payment_due = new_loan.getDouble(7);
		  int loan_id = new_loan.getInt(8);
		  String message = new_loan.getString(9);
		  System.out.println(message);
		  System.out.println("new loan's id is: "+loan_id);
		  System.out.println("loan's payment due is: "+payment_due);
	  }
	  catch(Exception e) {
		  e.getMessage();
		  System.out.println("something went wrong");
		  
	  }
  }
  public static double amt_in() {  
	  Scanner scan = new Scanner(System.in);
	  boolean badInput = true;
      double amount = -1;
	 
      do {
    	  badInput = true;
    	  System.out.println("enter amount(positive only)");
    	  while(!scan.hasNextDouble()) {
    		  scan.next();
    		  System.out.println("not a valid input, try again.");
    	  }
    	  amount = scan.nextDouble();
    	  scan.nextLine();
    	  if(amount < 0) {
    		  badInput = false;
    		  System.out.println("Positive numbers only please, try again.");
	  
    	  }
      	}while(!badInput);
      
      return amount;
	  
  }

  public static String get_address() { 
	  Scanner scan = new Scanner(System.in);
	  String address = "empty";
	  
	  try(Connection con = DriverManager.getConnection("jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241",username,password);
	      Statement bankBranch = con.createStatement();  ) 
	  {
	  System.out.println("These are the addresses of bank branches: ");
      String q = "select branch_address,hasteller from bank_branch";
	  ResultSet branchList = bankBranch.executeQuery(q);
      while(branchList.next()){
      System.out.print(branchList.getString(1));
      System.out.print(", Does this address have a teller - "+branchList.getString(2));
      System.out.println("");
      }
      
     
      String dummy = null;
      boolean real_address =true;
      do {
    	  branchList = bankBranch.executeQuery(q);
    	  System.out.println("Enter the address of the branch you want to perform transaction at: ");
	      address = scan.nextLine();
	      while(branchList.next()){
		 	  
 			 	dummy = branchList.getString(1);
 			 	
 			 	if(address.equals(dummy)){
 			 	real_address = false;
 			    }
 			 			      
 			  }
 	            
				if(real_address == true) {
					  System.out.println("This is not a valid address for a branch");
				  }
				
			 }while(real_address ==true);
	  }
	  catch(Exception e) {
		  e.getMessage();
		  System.out.println("something went wrong");
	  }
	  return address;
	      
  }

}
