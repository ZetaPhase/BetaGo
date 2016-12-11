using BetaGo.Server.DataModels.Registration;
using BetaGo.Server.Services.Authentication;
using BetaGo.Server.Utilities;
using Nancy;
using Nancy.ModelBinding;
using System;
using System.Security;

namespace BetaGo.Server.Modules
{
    /// <summary>
    /// Registration functionality
    /// </summary>
    public class RemoteAuthModule : NancyModule
    {
        public RemoteAuthModule()
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

                    // Validate password
                    if (req.Password.Length < 8)
                    {
                        throw new SecurityException("Password must be at least 8 characters.");
                    }

                    if (req.PhoneNumber != null && !StringUtils.IsPhoneNumber(req.PhoneNumber))
                    {
                        throw new SecurityException("Phone number was of invalid format.");
                    }

                    // Validate registration
                    var newU = WebUserManager.RegisterUser(req);

                    // Return just the 200 for now
                    return Response.AsJsonNet(new RegistrationResponse
                    {
                        User = newU,
                        ApiKey = newU.ApiKey,
                    });
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
            });
        }
    }
}