using LiteDB;
using Newtonsoft.Json;
using System;

namespace BetaGo.Server.Services.Authentication
{
    /// <summary>
    /// Represents a user who is registered and on record in the database.
    /// </summary>
    public class RegisteredUser
    {
        public string Username { get; set; }

        public string PhoneNumber { get; set; }

        [JsonIgnore]
        public string ApiKey { get; set; }

        [JsonIgnore]
        public byte[] PasswordKey { get; set; }

        [JsonIgnore]
        public byte[] CryptoSalt { get; set; }

        [JsonIgnore]
        public PasswordCryptoConfiguration PasswordCryptoConf { get; set; }

        [JsonIgnore]
        public string Identifier { get; set; }

        [JsonIgnore]
        [BsonId]
        public ObjectId DatabaseIdentifier { get; set; }
    }
}