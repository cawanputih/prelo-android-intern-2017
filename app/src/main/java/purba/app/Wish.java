package purba.app;

/**
 * Created by Purba on 4/12/2017.
 */

public class Wish {
    String name;
    int price;
    String srcimage;
    String id;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSrcimage() {
        return srcimage;
    }

    public void setSrcimage(String srcimage) {
        this.srcimage = srcimage;
    }

    public String getid() {
        return id;
    }

    public void setid(String _id) {
        this.id= _id;
    }
}
