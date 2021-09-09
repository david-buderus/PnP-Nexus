package de.pnp.manager.model.web;

import java.util.Date;
import java.util.List;

public class Release {
    public String url;
    public String assets_url;
    public String upload_url;
    public String html_url;
    public int id;
    public Author author;
    public String node_id;
    public String tag_name;
    public String target_commitish;
    public String name;
    public boolean draft;
    public boolean prerelease;
    public Date created_at;
    public Date published_at;
    public List<Asset> assets;
    public String tarball_url;
    public String zipball_url;
    public String body;
}
