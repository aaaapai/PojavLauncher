package com.mio.modpack;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurseforgeModpackManifest {
    @SerializedName("minecraft")
    private Minecraft minecraft;
    @SerializedName("manifestType")
    private String manifestType;
    @SerializedName("manifestVersion")
    private Integer manifestVersion;
    @SerializedName("name")
    private String name;
    @SerializedName("version")
    private String version;
    @SerializedName("author")
    private String author;
    @SerializedName("files")
    private List<Files> files;
    @SerializedName("overrides")
    private String overrides;

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public void setMinecraft(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public String getManifestType() {
        return manifestType;
    }

    public void setManifestType(String manifestType) {
        this.manifestType = manifestType;
    }

    public Integer getManifestVersion() {
        return manifestVersion;
    }

    public void setManifestVersion(Integer manifestVersion) {
        this.manifestVersion = manifestVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Files> getFiles() {
        return files;
    }

    public void setFiles(List<Files> files) {
        this.files = files;
    }

    public String getOverrides() {
        return overrides;
    }

    public void setOverrides(String overrides) {
        this.overrides = overrides;
    }

    public static class Minecraft {
        @SerializedName("version")
        private String version;
        @SerializedName("modLoaders")
        private List<ModLoaders> modLoaders;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public List<ModLoaders> getModLoaders() {
            return modLoaders;
        }

        public void setModLoaders(List<ModLoaders> modLoaders) {
            this.modLoaders = modLoaders;
        }

        public static class ModLoaders {
            @SerializedName("id")
            private String id;
            @SerializedName("primary")
            private Boolean primary;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Boolean getPrimary() {
                return primary;
            }

            public void setPrimary(Boolean primary) {
                this.primary = primary;
            }
        }
    }

    public static class Files {
        @SerializedName("projectID")
        private Integer projectID;
        @SerializedName("fileID")
        private Integer fileID;
        @SerializedName("required")
        private Boolean required;

        public Integer getProjectID() {
            return projectID;
        }

        public void setProjectID(Integer projectID) {
            this.projectID = projectID;
        }

        public Integer getFileID() {
            return fileID;
        }

        public void setFileID(Integer fileID) {
            this.fileID = fileID;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }
    }
}
