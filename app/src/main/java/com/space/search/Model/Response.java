package com.space.search.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {
    private Collection collection;

    public Collection getCollection() {
        return collection;
    }

    public class Collection {
        private String href;
        private List<Item> items;
        private MetaData metadata;
        private String version;

        public String getHref() {
            return href;
        }

        public List<Item> getItems() {
            return items;
        }

        public MetaData getMetadata() {
            return metadata;
        }

        public String getVersion() {
            return version;
        }

    }

    public class Item {
        private List<Data> data;
        private String href;
        private List<Link> links;

        public List<Data> getData() {
            return data;
        }

        public String getHref() {
            return href;
        }

        public List<Link> getLinks() {
            return links;
        }
    }

    public class Data {
        private String center;
        @SerializedName("date_created")
        private String dateCreated;
        private String description;
        private String[] keywords;
        @SerializedName("media_type")
        private String mediaType;
        @SerializedName("nasa_id")
        private String nasaId;
        private String title;

        public String getCenter() {
            return center;
        }

        public String getDate_created() {
            return dateCreated;
        }

        public String getDescription() {
            return description;
        }

        public String[] getKeywords() {
            return keywords;
        }

        public String getMedia_type() {
            return mediaType;
        }

        public String getNasa_id() {
            return nasaId;
        }

        public String getTitle() {
            return title;
        }
    }

    public class Link {
        private String href;
        private String rel;
        private String render;

        //image
        public String getHref() {
            return href;
        }

        public String getRel() {
            return rel;
        }

        public String getRender() {
            return render;
        }
    }

    public class MetaData {
        @SerializedName("total_hits")
        private int totalHits;

        public int getTotal_hits() {
            return totalHits;
        }
    }
}
