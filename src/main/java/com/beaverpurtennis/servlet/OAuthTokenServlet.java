package com.beaverpurtennis.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.client.authn.oauth.GoogleOAuthHelper;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;

public class OAuthTokenServlet extends HttpServlet{

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String SCOPES = "https://docs.google.com/feeds https://spreadsheets.google.com/feeds";
	      
	      // STEP 1: Set up the OAuth objects

	      // You first need to initialize a few OAuth-related objects.
	      // GoogleOAuthParameters holds all the parameters related to OAuth.
	      // OAuthSigner is responsible for signing the OAuth base string.
	      GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();

	      // Set your OAuth Consumer Key (which you can register at
	      // https://www.google.com/accounts/ManageDomains).
	      oauthParameters.setOAuthConsumerKey("990144034969-at5ou1sqoc6977blpucf6sqt6cj01oaf.apps.googleusercontent.com");

	      // Initialize the OAuth Signer.  
	      OAuthSigner signer = null;
	      oauthParameters.setOAuthConsumerSecret("AcRJwwRVY6K2UG_J4ylFf9La");
	      signer = new OAuthHmacSha1Signer();

	      // Finally create a new GoogleOAuthHelperObject.  This is the object you
	      // will use for all OAuth-related interaction.
	      GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);


	      // STEP 2: Get the Authorization URL

	      // Set the scope for this particular service.
	      oauthParameters.setScope(SCOPES);

	      // This method also makes a request to get the unauthorized request token,
	      // and adds it to the oauthParameters object, along with the token secret
	      // (if it is present).
	      try {
	    oauthHelper.getUnauthorizedRequestToken(oauthParameters);
	   } catch (OAuthException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }

	      // Get the authorization url.  The user of your application must visit
	      // this url in order to authorize with Google.  If you are building a
	      // browser-based application, you can redirect the user to the authorization
	      // url.
	      String requestUrl = oauthHelper.createUserAuthorizationUrl(oauthParameters);	
	      response.sendRedirect(response.encodeRedirectURL(requestUrl));
	}
}
