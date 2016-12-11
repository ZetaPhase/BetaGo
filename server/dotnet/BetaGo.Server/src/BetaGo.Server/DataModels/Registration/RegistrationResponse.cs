using BetaGo.Server.Services.Authentication;
using Newtonsoft.Json;

namespace BetaGo.Server.DataModels.Registration
{
    public class RegistrationResponse
    {
        [JsonProperty("user")]
        public RegisteredUser User { get; set; }

        [JsonProperty("apikey")]
        public string ApiKey { get; set; }
    }
}