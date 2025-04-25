
package model;

import java.util.Date;


public class LostItem {
    private int lostItemID;
    private String title;
    private String description;
    private Date dateLost;
    private String location;
    private byte[] image;


    public LostItem() {
    }


    public LostItem(int lostItemID, String title, String description, Date dateLost, String location, byte[] image) {
        this.lostItemID = lostItemID;
        this.title = title;
        this.description = description;
        this.dateLost = dateLost;
        this.location = location;
        this.image = image;
    }


    public int getLostItemID() {
        return lostItemID;
    }

    public void setLostItemID(int lostItemID) {
        this.lostItemID = lostItemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateLost() {
        return dateLost;
    }

    public void setDateLost(Date dateLost) {
        this.dateLost = dateLost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }


    public boolean isValid() {

        return title != null && !title.trim().isEmpty() &&
               location != null && !location.trim().isEmpty() &&
               dateLost != null;
    }


    public String getFormattedDate() {
        if (dateLost == null) {
            return "Unknown date";
        }


        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dateLost);
    }
}
