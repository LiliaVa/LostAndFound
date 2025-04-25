
package model;

import java.util.Date;


public class FoundItem {
    private int foundItemID;
    private String title;
    private String description;
    private Date dateFound;
    private String location;
    private byte[] image;


    public FoundItem() {
    }


    public FoundItem(int foundItemID, String title, String description, Date dateFound, String location) {
        this.foundItemID = foundItemID;
        this.title = title;
        this.description = description;
        this.dateFound = dateFound;
        this.location = location;
    }


    public int getFoundItemID() {
        return foundItemID;
    }

    public void setFoundItemID(int foundItemID) {
        this.foundItemID = foundItemID;
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

    public Date getDateFound() {
        return dateFound;
    }

    public void setDateFound(Date dateFound) {
        this.dateFound = dateFound;
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
               dateFound != null;
    }


    public String getFormattedDate() {
        if (dateFound == null) {
            return "Unknown date";
        }


        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dateFound);
    }
}
