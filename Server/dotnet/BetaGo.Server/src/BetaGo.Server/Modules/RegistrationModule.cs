using BetaGo.Server.DataModels.Registration;
using BetaGo.Server.Services.Authentication;
using Nancy;
using Nancy.ModelBinding;
using System;
using System.Security;

namespace BetaGo.Server.Modules
{
    /// <summary>
    /// Registration functionality
    /// </summary>
    public class RegistrationModule : NancyModule
    {
        public RegistrationModule()
        {
            Post("/register", args =>
            {
                var req = this.Bind<RegistrationRequest>();

                try
                {
                    // TODO: Validate parameters!

                    // Valdiate username length, charset
                    if (req.Username.Length < 4)
                    {
                        throw new SecurityException("Username must be at least 4 characters.");
                    }
                    // Validate phone number

                    var userManagerConn = new WebUserManager();
                    // Validate registration
                    userManagerConn.RegisterUser(req);
                }
                catch (NullReferenceException)
                {
                    // A parameter was not provided
                    return new Response().WithStatusCode(HttpStatusCode.BadRequest);
                }
                catch (SecurityException secEx)
                {
                    // Registration blocked for security reasons
                    return Response.AsText(secEx.Message)
                        .WithStatusCode(HttpStatusCode.Unauthorized);
                }

                // Return just the 200 for now
                return new Response();
            });
        }
    }
}