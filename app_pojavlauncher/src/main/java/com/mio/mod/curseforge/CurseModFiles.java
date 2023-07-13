package com.mio.mod.curseforge;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurseModFiles {

    @SerializedName("data")
    private List<Data> data;
    @SerializedName("pagination")
    private Pagination pagination;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public static class Pagination {
        @SerializedName("index")
        private Integer index;
        @SerializedName("pageSize")
        private Integer pageSize;
        @SerializedName("resultCount")
        private Integer resultCount;
        @SerializedName("totalCount")
        private Integer totalCount;

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getResultCount() {
            return resultCount;
        }

        public void setResultCount(Integer resultCount) {
            this.resultCount = resultCount;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class Data {
        @SerializedName("id")
        private Integer id;
        @SerializedName("gameId")
        private Integer gameId;
        @SerializedName("modId")
        private Integer modId;
        @SerializedName("isAvailable")
        private Boolean isAvailable;
        @SerializedName("displayName")
        private String displayName;
        @SerializedName("fileName")
        private String fileName;
        @SerializedName("releaseType")
        private Integer releaseType;
        @SerializedName("fileStatus")
        private Integer fileStatus;
        @SerializedName("hashes")
        private List<Hashes> hashes;
        @SerializedName("fileDate")
        private String fileDate;
        @SerializedName("fileLength")
        private Integer fileLength;
        @SerializedName("downloadCount")
        private Integer downloadCount;
        @SerializedName("downloadUrl")
        private String downloadUrl;
        @SerializedName("gameVersions")
        private List<String> gameVersions;
        @SerializedName("sortableGameVersions")
        private List<SortableGameVersions> sortableGameVersions;
        @SerializedName("dependencies")
        private List<Dependencies> dependencies;
        @SerializedName("alternateFileId")
        private Integer alternateFileId;
        @SerializedName("isServerPack")
        private Boolean isServerPack;
        @SerializedName("fileFingerprint")
        private Long fileFingerprint;
        @SerializedName("modules")
        private List<Modules> modules;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getGameId() {
            return gameId;
        }

        public void setGameId(Integer gameId) {
            this.gameId = gameId;
        }

        public Integer getModId() {
            return modId;
        }

        public void setModId(Integer modId) {
            this.modId = modId;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Integer getReleaseType() {
            return releaseType;
        }

        public void setReleaseType(Integer releaseType) {
            this.releaseType = releaseType;
        }

        public Integer getFileStatus() {
            return fileStatus;
        }

        public void setFileStatus(Integer fileStatus) {
            this.fileStatus = fileStatus;
        }

        public List<Hashes> getHashes() {
            return hashes;
        }

        public void setHashes(List<Hashes> hashes) {
            this.hashes = hashes;
        }

        public String getFileDate() {
            return fileDate;
        }

        public void setFileDate(String fileDate) {
            this.fileDate = fileDate;
        }

        public Integer getFileLength() {
            return fileLength;
        }

        public void setFileLength(Integer fileLength) {
            this.fileLength = fileLength;
        }

        public Integer getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(Integer downloadCount) {
            this.downloadCount = downloadCount;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public List<String> getGameVersions() {
            return gameVersions;
        }

        public void setGameVersions(List<String> gameVersions) {
            this.gameVersions = gameVersions;
        }

        public List<SortableGameVersions> getSortableGameVersions() {
            return sortableGameVersions;
        }

        public void setSortableGameVersions(List<SortableGameVersions> sortableGameVersions) {
            this.sortableGameVersions = sortableGameVersions;
        }

        public List<Dependencies> getDependencies() {
            return dependencies;
        }

        public void setDependencies(List<Dependencies> dependencies) {
            this.dependencies = dependencies;
        }

        public Integer getAlternateFileId() {
            return alternateFileId;
        }

        public void setAlternateFileId(Integer alternateFileId) {
            this.alternateFileId = alternateFileId;
        }

        public Boolean getIsServerPack() {
            return isServerPack;
        }

        public void setIsServerPack(Boolean isServerPack) {
            this.isServerPack = isServerPack;
        }

        public Long getFileFingerprint() {
            return fileFingerprint;
        }

        public void setFileFingerprint(Long fileFingerprint) {
            this.fileFingerprint = fileFingerprint;
        }

        public List<Modules> getModules() {
            return modules;
        }

        public void setModules(List<Modules> modules) {
            this.modules = modules;
        }

        public static class Hashes {
            @SerializedName("value")
            private String value;
            @SerializedName("algo")
            private Integer algo;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public Integer getAlgo() {
                return algo;
            }

            public void setAlgo(Integer algo) {
                this.algo = algo;
            }
        }

        public static class SortableGameVersions {
            @SerializedName("gameVersionName")
            private String gameVersionName;
            @SerializedName("gameVersionPadded")
            private String gameVersionPadded;
            @SerializedName("gameVersion")
            private String gameVersion;
            @SerializedName("gameVersionReleaseDate")
            private String gameVersionReleaseDate;
            @SerializedName("gameVersionTypeId")
            private Integer gameVersionTypeId;

            public String getGameVersionName() {
                return gameVersionName;
            }

            public void setGameVersionName(String gameVersionName) {
                this.gameVersionName = gameVersionName;
            }

            public String getGameVersionPadded() {
                return gameVersionPadded;
            }

            public void setGameVersionPadded(String gameVersionPadded) {
                this.gameVersionPadded = gameVersionPadded;
            }

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
            }

            public String getGameVersionReleaseDate() {
                return gameVersionReleaseDate;
            }

            public void setGameVersionReleaseDate(String gameVersionReleaseDate) {
                this.gameVersionReleaseDate = gameVersionReleaseDate;
            }

            public Integer getGameVersionTypeId() {
                return gameVersionTypeId;
            }

            public void setGameVersionTypeId(Integer gameVersionTypeId) {
                this.gameVersionTypeId = gameVersionTypeId;
            }
        }

        public static class Dependencies {
            @SerializedName("modId")
            private Integer modId;
            @SerializedName("relationType")
            private Integer relationType;

            public Integer getModId() {
                return modId;
            }

            public void setModId(Integer modId) {
                this.modId = modId;
            }

            public Integer getRelationType() {
                return relationType;
            }

            public void setRelationType(Integer relationType) {
                this.relationType = relationType;
            }
        }

        public static class Modules {
            @SerializedName("name")
            private String name;
            @SerializedName("fingerprint")
            private Long fingerprint;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Long getFingerprint() {
                return fingerprint;
            }

            public void setFingerprint(Long fingerprint) {
                this.fingerprint = fingerprint;
            }
        }
    }
}
