using BetaGo.Server.Services.Authentication;
using Nancy;
using Nancy.Authentication.Stateless;
using Nancy.Bootstrapper;
using Nancy.Conventions;
using Nancy.Session;
using Nancy.TinyIoc;

namespace BetaGo.Server
{
    public class Bootstrapper : DefaultNancyBootstrapper
    {
        protected override void ConfigureConventions(NancyConventions nancyConventions)
        {
            base.ConfigureConventions(nancyConventions);
            nancyConventions.StaticContentsConventions.Add(
                StaticContentConventionBuilder.AddDirectory("assets", "wwwroot/assets/")
            );
            nancyConventions.StaticContentsConventions.Add(
                StaticContentConventionBuilder.AddDirectory("static", "wwwroot/static/")
            );
        }

        protected override void ApplicationStartup(TinyIoCContainer container, IPipelines pipelines)
        {
            base.ApplicationStartup(container, pipelines);

            // Enable cookie sessions
            CookieBasedSessions.Enable(pipelines);

            // Enable authentication
            StatelessAuthentication.Enable(pipelines, new StatelessAuthenticationConfiguration(ctx =>
            {
                // Take API from query string
                var apiKey = (string)ctx.Request.Query.apiKey.Value;

                // get user identity
                return ApiClientAuthenticationService.ResolveClientIdentity(apiKey);
            }));
        }
    }
}