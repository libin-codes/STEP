package week03.activity07;

public class SalaryAccount extends Account {

    private String employerName;
    private int inactiveMonths;

    public SalaryAccount(int accountNumber, String name, int age, double balance, String employerName, int inactiveMonths) {
        super(accountNumber, name, age, balance, "SALARY");
        this.employerName = employerName;
        this.inactiveMonths = inactiveMonths;
    }

    public SalaryAccount(int accountNumber, String name, int age, double balance, String employerName) {
        this(accountNumber, name, age, balance, employerName, 0);
    }

    public SalaryAccount(int accountNumber, String name, int age, double balance) {
        this(accountNumber, name, age, balance, "Infosys", 0);
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public int getInactiveMonths() {
        return inactiveMonths;
    }

    public void setInactiveMonths(int inactiveMonths) {
        this.inactiveMonths = inactiveMonths;
    }
}
