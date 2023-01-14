package at.saunders.data;

public class FlatAdvert {

    public static final String[] HEADERS = {"ADDRESS","TYPE", "AREA", "ROOMS", "PRICE" };
    private String address;
    private String type;
    private String area;
    private String rooms;
    private String price;

    public FlatAdvert(String address, String type, String area, String rooms, String price) {
        this.address = address;
        this.type = type;
        this.area = area;
        this.rooms = rooms;
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    public String getRooms() {
        return rooms;
    }

    public String getPrice() {
        return price;
    }

    public String toString(){
        return "{ \"address\":\"" + address + "\", "
                + " \"type\": \"" + type + "\", "
                + "\"area\":\"" + area + "\", "
                + "\"rooms\":\"" + rooms + "\", "
                + "\"price\": \"" + price + "\" }";
    }

    public String[] toArray() {
        return new String[] {address, type, area, rooms, price};
    }
}
