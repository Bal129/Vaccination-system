package JavaDir;

abstract class Detail {
    private int id;
    private String name, password, phone;

    ////////////// constructor //////////////

    public Detail() {}
    public Detail(int id, String name, String password, String phone) {
        setId(id);
        setName(name);
        setPhone(phone);
        setPassword(password);
    }

    ////////////// getter //////////////

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    ////////////// setter //////////////

    public void setId(int id) {
        if (id < 0)
            throw new IllegalArgumentException("Invalid setId(int id)");
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
