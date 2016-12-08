using LiteDB;
using System;

namespace BetaGo.Server.Services.Authentication
{
    public class RegisteredUser
    {
        public string Username { get; set; }
        public string PhoneNumber { get; set; }

        public Guid Identifier { get; set; }

        [BsonId]
        public ObjectId DatabaseIdentifier { get; set; }
    }
}