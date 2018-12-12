package lapr.project.model;

import lapr.project.data.SailorDB;

public class Sailor {

    private String name;
    private long id;
    private long rating;
    private long age;

    public Sailor() {


    }

    public Sailor(long id, String name) {
        this.id = id;
        this.name = name;
    }


    public static Sailor getSailor(long id) {
        return new SailorDB().getSailor(id);
    }

    public long getId() {
        return id;
    }

    public void save() {

        try {
            getSailor(this.getId());
        } catch (IllegalArgumentException ex) {
            //Of the record does not exist, save it
            new SailorDB().addSailor(this);
        }

        //TODO: implement the update method
    }

    public String getName() {
        return name;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public void delete(){
        new SailorDB().removeSailor(this.id);
    }
}
