using System.Security.Claims;
using System.Security.Principal;

namespace BetaGo.Server.Services.Authentication
{
    public class ApiClientAuthenticationService
    {
        public static ClaimsPrincipal ResolveClientIdentity(string apiKey)
        {
            // Check user records in database
            var u = WebUserManager.FindUserByApiKey(apiKey);
            if (u != null)
            {
                // Give client identity
                return new ClaimsPrincipal(new GenericIdentity(u.Username, "stateless"));
            }
            return null;
        }
    }
}