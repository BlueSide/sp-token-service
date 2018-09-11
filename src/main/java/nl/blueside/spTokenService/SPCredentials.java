package nl.blueside.spTokenService;

import javax.persistence.Entity;

@Entity
public class SPCredentials
{
    private String url;
    private String site;
    private String applicationId;
    private String username;
    private String password;

    public SPCredentials(String url, String site, String applicationId, String username, String password)
    {
        this.url = url;
        this.site = site;
        this.applicationId = applicationId;
        this.username = username;
        this.password = password;
    }

    public String getUrl() { return this.url; }
    public void setUrl(String url) { this.url = url; }

    public String getSite() { return this.site; }
    public void setSite(String site) { this.site = site; }
    
    public String getApplicationId() { return this.applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString()
    {
        return new StringBuilder()
            .append("SPCredentials [url=").append(this.url)
            .append(", site=").append(this.site)
            .append(", applicationId=").append(this.applicationId)
            .append(", username=").append(this.username)
            .append(", password=").append(this.password)
            .append("]")
            .toString();
    }
}
