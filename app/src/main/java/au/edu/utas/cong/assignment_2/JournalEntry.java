package au.edu.utas.cong.assignment_2;

public class JournalEntry {


    public int _id;
    public String title;
    public String bodyText;
    public String date;
    public int mood;
    public String location;
    public String image;
    public JournalEntry( ){

    }
    public JournalEntry(String title, String bodyText, String date, String location, int mood, String image){
        this.title=title;
        this.bodyText=bodyText;
        this.date=date;
        this.location=location;
        this.mood=mood;
        this.image=image;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
