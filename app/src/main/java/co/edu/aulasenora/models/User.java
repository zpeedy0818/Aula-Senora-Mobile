package co.edu.aulasenora.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("name")
    private Name name;

    @SerializedName("email")
    private String email;

    @SerializedName("picture")
    private Picture picture;

    // Getters y Setters
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    // Clases Internas para mapear los objetos anidados JSON

    public static class Name {
        @SerializedName("title")
        private String title;

        @SerializedName("first")
        private String first;

        @SerializedName("last")
        private String last;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getFirst() { return first; }
        public void setFirst(String first) { this.first = first; }

        public String getLast() { return last; }
        public void setLast(String last) { this.last = last; }

        public String getFullName() {
            return title + " " + first + " " + last;
        }
    }

    public static class Picture {
        @SerializedName("large")
        private String large;

        @SerializedName("medium")
        private String medium;

        @SerializedName("thumbnail")
        private String thumbnail;

        public String getLarge() { return large; }
        public void setLarge(String large) { this.large = large; }

        public String getMedium() { return medium; }
        public void setMedium(String medium) { this.medium = medium; }

        public String getThumbnail() { return thumbnail; }
        public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
    }
}
