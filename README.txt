
THINGS TO NOTE:
I implemented all my updates and inserts as procedures. 
There is a get balance function, deposit procedure, withdraw procedure, debit card purchase procedure, credit card purchase procedure,
Create account procedure and new loan procedure. 

INTERESTING CUSTOMER: 

cust_id 120 has no debit card, credit card, no loans and no accounts. 

ER DESIGN NOTES:
Connected Credit Card to customer, because normally credit cares are tied to a single person(for reasons like credit score). Debit cards are tied to accounts because they only exist in relation to a checking account. I didn’t connect debit card directly checking account, but instead to accounts because it would be easier to deal with changing the balance on usage of debit card on if its directly connected to the accounts table, Additionally I can just set a condition that you can only add a credit card to an account where acct_type = checking, to deal with how debit cards are tied to checking. For transaction I have all transaction listed in a transaction table, a gen/spec for purchases on card because they have vendors. I didn't create other gen/spec tables for transaction because they didn't need any other attributes that weren't in the transaction table. 


HOW TO USE:
After logging on, You are given the option to log in as a customer or manager, both have different interface. No matter which you choose you will be given the option to return back to this interface to reselect a different customer, or go to the manager's interface.


CUSTOMER INTERFACE

As a customer you can either choose to input a customer id that exists in the database or you the program will give you an account(cust id 100). You will then proceed throughout the customer interface as that customer you chose.

As customer you can choose to withdraw or deposit, purchase on debit or credit card, take out a new loan and add a new account. 

When withdrawing or deposit you will be given a list of account id's that belong to that customer and their corresponding type to choose from (program prevents you from performing transactions on accounts you don't own, and if you don't have an account you won't be able to proceed and program will return you to previous interface).  If you choose a saving account you will be given the min balance of that account. You also have to choose a location for your withdrawal/deposit, available branches will be displayed along with whether or not they have a teller. If you choose to deposit at a location with no teller you won't be able to. You will also be given the current balance of the account, after you input the amount you will be given your new balance and the transaction's id. withdrawal that puts balance below 0 will be denied.

When purchasing on debit card you will be given a list of your checking accounts that have a debit card associated with them, if you don't have such an account you will be returned to previous interface. You will only be able to select accounts from the list given. Then you will be given the card numbers associated with that account and choose from that list. You will then be given the accounts balance, then input amount and vendor you purchased from. Afterwards you will be given your accounts new balance and the trans Id. Purchases that puts account balance at below 0 will be denied. 

When purchasing on credit card you will be given the cards associated with that customer, choose from that list, if you don't have a card you will be returned to the previous interface. Then you will be given the credit limit, balance, and balance due on that card. After inputting amount and id you will be given the new balance and balance due and the trans id. Purchase over credit limit will be denied. 

When creating a new account you will be asked to input the accounts type (saving or checking). If a saving you will need to input a min balance. you new account ID will be outputted. Afterwards you are given choice of depositing in the account. This will call up the withdraw/deposit interface.

When creating a new loan you will be given asked to input the value of loan, the interest rate, loan type. If loan type is mortgage you will be asked to input an address and home value. After loan is created you will given the loan's id and the payment due.

MANAGER INTERFACE

Here you can select to either check the values of loans and accounts. If you select loans you can choose to see total amount of loans, total amount of mortgages or total amount of unsecured loans. If you select accounts you can choose to see total amount in accounts, total amount in checking accounts or total amount in saving accounts. 

 

DESIGN DECISION:

All inputs for amounts will accept as much decimal spaces as the user inputs, but when the inserted to the database amounts will be rounded to 2 decimal, except interest which will be rounded to 4 decimals. 
All transactions have to happen at a bank branch, except for credit and debit card purchases. 
All branches have at least an ATM. Some don't have a teller. 
No account can have a negative balance, saving or checking, any transactions that put account below 0 will be denied. 
All saving accounts have a min balance and if min balance is overdrawn you will be deducted 5% of your balance after the withdrawal as a penalty. 
All inputs of amount will be positive(program will make sure).

Payment due is 50% of balance for credit cards. 
Interest rates are entered as a percentage, if interest is 10.5% it will be inputed as 10.5. No interest rates above 100. 
monthly payments are the amount of the loan divided by 12, I am assuming all loans are due in 12 month and ignoring interest.  

Decided not to implement vendor's ID, as this would limit the amount of vendors you can purchase from. Instead you can arbitrarily input a vendor's name.

Did not implement a way to share ownership of new loans and account, this could be a future work. 
Did not implement which specific atm or teller you did your transaction with, this could be a future work. 




