package org.skypay;

import java.time.LocalDate;


        public class Main {
            public static void main(String[] args) {
                Account account = new AccountImpl();

                account.deposit(1000, "10-01-2012");
                account.deposit(2000, "13-01-2012");
                account.withdraw(500, "14-01-2012");

                account.printStatement();
            }
        }