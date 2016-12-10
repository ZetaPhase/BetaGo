using BetaGo.Server.Services.Authentication;
using BetaGo.Server.Utilities;
using Nancy;
using Nancy.Security;

namespace BetaGo.Server.Modules
{
    public class ApiAccessModule : NancyModule
    {
        public ApiAccessModule() : base("/api")
        {
            this.RequiresAuthentication();
            this.RequiresClaims(x => x.Value == ApiClientAuthenticationService.StatelessAuthClaim.Value);

            Get("/userinfo", _ =>
            {
                var user = WebUserManager.FindUserByUsername(Context.CurrentUser.Identity.Name);
                return Response.AsJsonNet(user);
            });
        }
    }
}