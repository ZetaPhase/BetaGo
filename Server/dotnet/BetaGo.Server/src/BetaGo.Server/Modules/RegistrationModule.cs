using BetaGo.Server.DataModels.Registration;
using BetaGo.Server.Services.Authentication;
using Nancy;
using Nancy.ModelBinding;
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

                // TODO: Validate parameters!

                // Valdiate username charset
                // Validate phone number

                try
                {
                    var userManagerConn = new WebUserManager();
                    // Validate registration
                    userManagerConn.RegisterUser(req);
                }
                catch (SecurityException secEx)
                {
                    // Registration blocked for security reasons
                    return new Response().WithStatusCode(HttpStatusCode.Unauthorized);
                }

                // Return just the 200 for now
                return new Response();
            });
        }
    }
}