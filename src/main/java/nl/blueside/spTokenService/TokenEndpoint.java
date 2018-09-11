package nl.blueside.spTokenService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.json.JSONObject;

@RestController
public class TokenEndpoint
{
    @CrossOrigin("http://localhost:4200")
    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody SPCredentials credentials)
    {
        try
        {
            SPContext context = SPContext.registerCredentials(credentials);
            return ResponseEntity.ok(new JSONObject().put("token", context.authentication.getAccessToken()).toString());
        }
        catch(Exception e)
        {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
