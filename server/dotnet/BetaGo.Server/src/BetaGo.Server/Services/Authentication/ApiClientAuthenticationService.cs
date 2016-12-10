﻿using System.Security.Claims;
using System.Security.Principal;

namespace BetaGo.Server.Services.Authentication
{
    public class ApiClientAuthenticationService
    {
        public static Claim StatelessAuthClaim { get; } = new Claim("authType", "stateless");

        public static ClaimsPrincipal ResolveClientIdentity(string apiKey)
        {
            // Check user records in database
            var u = WebUserManager.FindUserByApiKey(apiKey);
            if (u != null)
            {
                // Give client identity
                var id = new ClaimsPrincipal(new ClaimsIdentity(new GenericIdentity(u.Username, "stateless"), new Claim[] {
                }));
                return id;
            }
            return null;
        }
    }
}