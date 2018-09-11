package nl.blueside.spTokenService;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.Header;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.AuthenticationException;

import javax.naming.ServiceUnavailableException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Date;

public class SPContext
{
    protected AuthenticationResult authentication;

    private SPCredentials credentials;
    private final static String AUTHORITY = "https://login.microsoftonline.com/common/";
    
    private static ArrayList<SPContext> contexts = new ArrayList<SPContext>();
    
    public SPContext(SPCredentials credentials) throws AuthenticationException, Exception
    {
        this.credentials = credentials;
        this.authentication = getAccessTokenFromUserCredentials(credentials);
    }

    public void refreshToken() throws Exception
    {
        //Check for a valid context, renew if necessary
        Date now = new Date();
        if(now.after(this.authentication.getExpiresOnDate()))
        {
            this.authentication = getAccessTokenFromUserCredentials(credentials);
            System.out.println("Refreshed JWT for " + credentials.getUsername());
        }
    }
    
    public static SPContext registerCredentials(String url, String site, String applicationId, String username, String password) throws AuthenticationException, Exception
    {
        return registerCredentials(new SPCredentials(url, site, applicationId, username, password));
    }

    public static SPContext registerCredentials(SPCredentials credentials) throws AuthenticationException, Exception
    {
        SPContext context;
        //SPCredentials credentialsToRegister = new SPCredentials(url, username, password);
        Iterator<SPContext> contextIterator = contexts.iterator();
        while(contextIterator.hasNext())
        {
            context = contextIterator.next();
            if(credentials.equals(context.getCredentials()))
            {
                return context;
            }
        }

        //No existing context found. Create a new one and save for future use
        context = new SPContext(credentials);
        contexts.add(context);
        return context;
    }

    private static AuthenticationResult getAccessTokenFromUserCredentials(SPCredentials credentials) throws AuthenticationException, Exception
    {
        AuthenticationContext context = null;
        AuthenticationResult result = null;
        ExecutorService service = null;
        try
        {
            service = Executors.newFixedThreadPool(1);
            context = new AuthenticationContext(AUTHORITY, false, service);
            Future<AuthenticationResult> future = context.acquireToken(
                credentials.getUrl(), credentials.getApplicationId(), credentials.getUsername(), credentials.getPassword(),
                null);
            result = future.get();
        }
        finally
        {
            service.shutdown();
        }

        if (result == null)
        {
            throw new ServiceUnavailableException("authentication result was null");
        }
        return result;
    }

    public SPCredentials getCredentials()
    {
        return this.credentials;
    }
}
