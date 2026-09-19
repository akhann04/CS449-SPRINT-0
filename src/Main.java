public class Main {

    public String checkLength(String password) {

        if (password.length() < 8) {
            return "Password should be at least 8 characters";
        } else {
            return "Password length is valid";
        }
    }
}
