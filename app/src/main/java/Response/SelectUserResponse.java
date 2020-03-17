package Response;


public class SelectUserResponse {
    String userName;
    String password;
    boolean isSlected = false;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public boolean isSlected() {
        return isSlected;
    }

    public void setSlected(boolean slected) {
        isSlected = slected;
    }

    public SelectUserResponse(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public SelectUserResponse(String userName) {
        this.userName = userName;

    }
}
