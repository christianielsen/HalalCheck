package com.chris.firebaseauth.map.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NearbyPlaces {
    private List<Object> htmlAttributions = new ArrayList<Object>();
    private String next_page_token;
    private List<Result> results = new ArrayList<Result>();
    private String status;
    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }
    public String getNextPageToken() {
        return next_page_token;
    }
    public void setNextPageToken(String nextPageToken) {
        this.next_page_token = nextPageToken;
    }
    public List<Result> getResults() {
        return results;
    }
    public void setResults(List<Result> results) {
        this.results = results;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public class Geometry {
        private Location location;
        private Viewport viewport;
        public Location getLocation() {
            return location;
        }
        public void setLocation(Location location) {
            this.location = location;
        }
        public Viewport getViewport() {
            return viewport;
        }
        public void setViewport(Viewport viewport) {
            this.viewport = viewport;
        }
    }

    public class Location {
        private Double lat;
        private Double lng;
        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    public class Northeast {
        private Double lat;
        private Double lng;
        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    public class OpeningHours {

        @SerializedName("open_now")
        @Expose
        private Boolean openNow;

        public Boolean getOpenNow() {
            return openNow;
        }

        public void setOpenNow(Boolean openNow) {
            this.openNow = openNow;
        }

    }

    public class Photo {
        private Integer height;
        private List<String> htmlAttributions = new ArrayList<String>();
        private String photoReference;
        private Integer width;
        public Integer getHeight() {
            return height;
        }
        public void setHeight(Integer height) {
            this.height = height;
        }
        public List<String> getHtmlAttributions() {
            return htmlAttributions;
        }
        public void setHtmlAttributions(List<String> htmlAttributions) {
            this.htmlAttributions = htmlAttributions;
        }
        public String getPhotoReference() {
            return photoReference;
        }
        public void setPhotoReference(String photoReference) {
            this.photoReference = photoReference;
        }
        public Integer getWidth() {
            return width;
        }
        public void setWidth(Integer width) {
            this.width = width;
        }
    }

    public class PlusCode {
        private String compoundCode;
        private String globalCode;
        public String getCompoundCode() {
            return compoundCode;
        }
        public void setCompoundCode(String compoundCode) {
            this.compoundCode = compoundCode;
        }
        public String getGlobalCode() {
            return globalCode;
        }
        public void setGlobalCode(String globalCode) {
            this.globalCode = globalCode;
        }
    }

    public class Result {
        private String businessStatus;
        private Geometry geometry;
        private String icon;
        private String iconBackgroundColor;
        private String iconMaskBaseUri;
        private String name;
        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;
        private List<Photo> photos = new ArrayList<Photo>();
        private String placeId;
        private PlusCode plusCode;
        private Double rating;
        private String reference;
        private String scope;
        private List<String> types = new ArrayList<String>();
        @SerializedName("user_ratings_total")
        @Expose
        private Integer userRatingsTotal;
        private String vicinity;
        private Integer priceLevel;
        public String getBusinessStatus() {
            return businessStatus;
        }
        public void setBusinessStatus(String businessStatus) {
            this.businessStatus = businessStatus;
        }
        public Geometry getGeometry() {
            return geometry;
        }
        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }
        public String getIcon() {
            return icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
        public String getIconBackgroundColor() {
            return iconBackgroundColor;
        }
        public void setIconBackgroundColor(String iconBackgroundColor) {
            this.iconBackgroundColor = iconBackgroundColor;
        }
        public String getIconMaskBaseUri() {
            return iconMaskBaseUri;
        }
        public void setIconMaskBaseUri(String iconMaskBaseUri) {
            this.iconMaskBaseUri = iconMaskBaseUri;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public OpeningHours getOpeningHours() {
            return openingHours;
        }
        public void setOpeningHours(OpeningHours openingHours) {
            this.openingHours = openingHours;
        }
        public List<Photo> getPhotos() {
            return photos;
        }
        public void setPhotos(List<Photo> photos) {
            this.photos = photos;
        }
        public String getPlaceId() {
            return placeId;
        }
        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }
        public PlusCode getPlusCode() {
            return plusCode;
        }
        public void setPlusCode(PlusCode plusCode) {
            this.plusCode = plusCode;
        }
        public Double getRating() {
            return rating;
        }
        public void setRating(Double rating) {
            this.rating = rating;
        }
        public String getReference() {
            return reference;
        }
        public void setReference(String reference) {
            this.reference = reference;
        }
        public String getScope() {
            return scope;
        }
        public void setScope(String scope) {
            this.scope = scope;
        }
        public List<String> getTypes() {
            return types;
        }
        public void setTypes(List<String> types) {
            this.types = types;
        }
        public Integer getUserRatingsTotal() {
            return userRatingsTotal;
        }
        public void setUserRatingsTotal(Integer userRatingsTotal) {
            this.userRatingsTotal = userRatingsTotal;
        }
        public String getVicinity() {
            return vicinity;
        }
        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }
        public Integer getPriceLevel() {
            return priceLevel;
        }
        public void setPriceLevel(Integer priceLevel) {
            this.priceLevel = priceLevel;
        }
    }

    public class Southwest {
        private Double lat;
        private Double lng;
        public Double getLat() {
            return lat;
        }
        public void setLat(Double lat) {
            this.lat = lat;
        }
        public Double getLng() {
            return lng;
        }
        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    public class Viewport {
        private Northeast northeast;
        private Southwest southwest;
        public Northeast getNortheast() {
            return northeast;
        }
        public void setNortheast(Northeast northeast) {
            this.northeast = northeast;
        }
        public Southwest getSouthwest() {
            return southwest;
        }
        public void setSouthwest(Southwest southwest) {
            this.southwest = southwest;
        }
    }

}