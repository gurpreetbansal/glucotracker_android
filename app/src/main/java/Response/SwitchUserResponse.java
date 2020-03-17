package Response;

public class SwitchUserResponse {
    String name;
    String weight;
    String height;
    String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public SwitchUserResponse(String name, String weight, String height, String age) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.age = age;
    }
}
