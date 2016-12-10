using BetaGo.Server.Services.Authentication;
using Nancy;
using Nancy.Security;

namespace BetaGo.Server.Modules
{
    public class ApiAccessModule : NancyModule
    {
        public ApiAccessModule()
        {
            this.RequiresAuthentication();
            this.RequiresClaims(x => x.Value == ApiClientAuthenticationService.StatelessAuthClaim.Value);
        }
    }
}