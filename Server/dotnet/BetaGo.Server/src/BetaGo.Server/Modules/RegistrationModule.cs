using BetaGo.Server.DataModels.Registration;
using Nancy;
using Nancy.ModelBinding;

namespace BetaGo.Server.Modules
{
    public class RegistrationModule : NancyModule
    {
        public RegistrationModule()
        {
            Post("/register", args =>
            {
                var req = this.Bind<RegistrationRequest>();



                // Return just the 200 for now
                return new Response();
            });
        }
    }
}