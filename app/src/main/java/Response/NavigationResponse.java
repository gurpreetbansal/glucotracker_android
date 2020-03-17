package Response;

public class NavigationResponse {
    String itemList;

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public NavigationResponse(String itemList) {
        this.itemList = itemList;
    }
}
