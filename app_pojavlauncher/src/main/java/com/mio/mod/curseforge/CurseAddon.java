package com.mio.mod.curseforge;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurseAddon {

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
        @SerializedName("name")
        private String name;
        @SerializedName("slug")
        private String slug;
        @SerializedName("links")
        private Links links;
        @SerializedName("summary")
        private String summary;
        @SerializedName("status")
        private Integer status;
        @SerializedName("downloadCount")
        private Integer downloadCount;
        @SerializedName("isFeatured")
        private Boolean isFeatured;
        @SerializedName("primaryCategoryId")
        private Integer primaryCategoryId;
        @SerializedName("categories")
        private List<Categories> categories;
        @SerializedName("classId")
        private Integer classId;
        @SerializedName("authors")
        private List<Authors> authors;
        @SerializedName("logo")
        private Logo logo;
        @SerializedName("screenshots")
        private List<Screenshots> screenshots;
        @SerializedName("mainFileId")
        private Integer mainFileId;
        @SerializedName("latestFiles")
        private List<LatestFiles> latestFiles;
        @SerializedName("latestFilesIndexes")
        private List<LatestFilesIndexes> latestFilesIndexes;
        @SerializedName("latestEarlyAccessFilesIndexes")
        private List<?> latestEarlyAccessFilesIndexes;
        @SerializedName("dateCreated")
        private String dateCreated;
        @SerializedName("dateModified")
        private String dateModified;
        @SerializedName("dateReleased")
        private String dateReleased;
        @SerializedName("allowModDistribution")
        private Boolean allowModDistribution;
        @SerializedName("gamePopularityRank")
        private Integer gamePopularityRank;
        @SerializedName("isAvailable")
        private Boolean isAvailable;
        @SerializedName("thumbsUpCount")
        private Integer thumbsUpCount;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(Integer downloadCount) {
            this.downloadCount = downloadCount;
        }

        public Boolean getIsFeatured() {
            return isFeatured;
        }

        public void setIsFeatured(Boolean isFeatured) {
            this.isFeatured = isFeatured;
        }

        public Integer getPrimaryCategoryId() {
            return primaryCategoryId;
        }

        public void setPrimaryCategoryId(Integer primaryCategoryId) {
            this.primaryCategoryId = primaryCategoryId;
        }

        public List<Categories> getCategories() {
            return categories;
        }

        public void setCategories(List<Categories> categories) {
            this.categories = categories;
        }

        public Integer getClassId() {
            return classId;
        }

        public void setClassId(Integer classId) {
            this.classId = classId;
        }

        public List<Authors> getAuthors() {
            return authors;
        }

        public void setAuthors(List<Authors> authors) {
            this.authors = authors;
        }

        public Logo getLogo() {
            return logo;
        }

        public void setLogo(Logo logo) {
            this.logo = logo;
        }

        public List<Screenshots> getScreenshots() {
            return screenshots;
        }

        public void setScreenshots(List<Screenshots> screenshots) {
            this.screenshots = screenshots;
        }

        public Integer getMainFileId() {
            return mainFileId;
        }

        public void setMainFileId(Integer mainFileId) {
            this.mainFileId = mainFileId;
        }

        public List<LatestFiles> getLatestFiles() {
            return latestFiles;
        }

        public void setLatestFiles(List<LatestFiles> latestFiles) {
            this.latestFiles = latestFiles;
        }

        public List<LatestFilesIndexes> getLatestFilesIndexes() {
            return latestFilesIndexes;
        }

        public void setLatestFilesIndexes(List<LatestFilesIndexes> latestFilesIndexes) {
            this.latestFilesIndexes = latestFilesIndexes;
        }

        public List<?> getLatestEarlyAccessFilesIndexes() {
            return latestEarlyAccessFilesIndexes;
        }

        public void setLatestEarlyAccessFilesIndexes(List<?> latestEarlyAccessFilesIndexes) {
            this.latestEarlyAccessFilesIndexes = latestEarlyAccessFilesIndexes;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }

        public String getDateReleased() {
            return dateReleased;
        }

        public void setDateReleased(String dateReleased) {
            this.dateReleased = dateReleased;
        }

        public Boolean getAllowModDistribution() {
            return allowModDistribution;
        }

        public void setAllowModDistribution(Boolean allowModDistribution) {
            this.allowModDistribution = allowModDistribution;
        }

        public Integer getGamePopularityRank() {
            return gamePopularityRank;
        }

        public void setGamePopularityRank(Integer gamePopularityRank) {
            this.gamePopularityRank = gamePopularityRank;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }

        public Integer getThumbsUpCount() {
            return thumbsUpCount;
        }

        public void setThumbsUpCount(Integer thumbsUpCount) {
            this.thumbsUpCount = thumbsUpCount;
        }

        public static class Links {
            @SerializedName("websiteUrl")
            private String websiteUrl;
            @SerializedName("wikiUrl")
            private Object wikiUrl;
            @SerializedName("issuesUrl")
            private String issuesUrl;
            @SerializedName("sourceUrl")
            private String sourceUrl;

            public String getWebsiteUrl() {
                return websiteUrl;
            }

            public void setWebsiteUrl(String websiteUrl) {
                this.websiteUrl = websiteUrl;
            }

            public Object getWikiUrl() {
                return wikiUrl;
            }

            public void setWikiUrl(Object wikiUrl) {
                this.wikiUrl = wikiUrl;
            }

            public String getIssuesUrl() {
                return issuesUrl;
            }

            public void setIssuesUrl(String issuesUrl) {
                this.issuesUrl = issuesUrl;
            }

            public String getSourceUrl() {
                return sourceUrl;
            }

            public void setSourceUrl(String sourceUrl) {
                this.sourceUrl = sourceUrl;
            }
        }

        public static class Logo {
            @SerializedName("id")
            private Integer id;
            @SerializedName("modId")
            private Integer modId;
            @SerializedName("title")
            private String title;
            @SerializedName("description")
            private String description;
            @SerializedName("thumbnailUrl")
            private String thumbnailUrl;
            @SerializedName("url")
            private String url;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getModId() {
                return modId;
            }

            public void setModId(Integer modId) {
                this.modId = modId;
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

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public void setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class Categories {
            @SerializedName("id")
            private Integer id;
            @SerializedName("gameId")
            private Integer gameId;
            @SerializedName("name")
            private String name;
            @SerializedName("slug")
            private String slug;
            @SerializedName("url")
            private String url;
            @SerializedName("iconUrl")
            private String iconUrl;
            @SerializedName("dateModified")
            private String dateModified;
            @SerializedName("isClass")
            private Boolean isClass;
            @SerializedName("classId")
            private Integer classId;
            @SerializedName("parentCategoryId")
            private Integer parentCategoryId;

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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getIconUrl() {
                return iconUrl;
            }

            public void setIconUrl(String iconUrl) {
                this.iconUrl = iconUrl;
            }

            public String getDateModified() {
                return dateModified;
            }

            public void setDateModified(String dateModified) {
                this.dateModified = dateModified;
            }

            public Boolean getIsClass() {
                return isClass;
            }

            public void setIsClass(Boolean isClass) {
                this.isClass = isClass;
            }

            public Integer getClassId() {
                return classId;
            }

            public void setClassId(Integer classId) {
                this.classId = classId;
            }

            public Integer getParentCategoryId() {
                return parentCategoryId;
            }

            public void setParentCategoryId(Integer parentCategoryId) {
                this.parentCategoryId = parentCategoryId;
            }
        }

        public static class Authors {
            @SerializedName("id")
            private Integer id;
            @SerializedName("name")
            private String name;
            @SerializedName("url")
            private String url;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class Screenshots {
            @SerializedName("id")
            private Integer id;
            @SerializedName("modId")
            private Integer modId;
            @SerializedName("title")
            private String title;
            @SerializedName("description")
            private String description;
            @SerializedName("thumbnailUrl")
            private String thumbnailUrl;
            @SerializedName("url")
            private String url;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getModId() {
                return modId;
            }

            public void setModId(Integer modId) {
                this.modId = modId;
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

            public String getThumbnailUrl() {
                return thumbnailUrl;
            }

            public void setThumbnailUrl(String thumbnailUrl) {
                this.thumbnailUrl = thumbnailUrl;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class LatestFiles {
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
            private List<?> hashes;
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

            public List<?> getHashes() {
                return hashes;
            }

            public void setHashes(List<?> hashes) {
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
                private Object gameVersionTypeId;

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

                public Object getGameVersionTypeId() {
                    return gameVersionTypeId;
                }

                public void setGameVersionTypeId(Object gameVersionTypeId) {
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

        public static class LatestFilesIndexes {
            @SerializedName("gameVersion")
            private String gameVersion;
            @SerializedName("fileId")
            private Integer fileId;
            @SerializedName("filename")
            private String filename;
            @SerializedName("releaseType")
            private Integer releaseType;

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
            }

            public Integer getFileId() {
                return fileId;
            }

            public void setFileId(Integer fileId) {
                this.fileId = fileId;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public Integer getReleaseType() {
                return releaseType;
            }

            public void setReleaseType(Integer releaseType) {
                this.releaseType = releaseType;
            }
        }
    }
}
