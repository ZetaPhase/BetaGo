using Nancy;
using Nancy.Security;

namespace BetaGo.Server.Modules
{
    public class ApiAccessModule : NancyModule
    {
        public ApiAccessModule()
        {
            this.RequiresAuthentication();


        }
    }
}