using LiteDB;
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

        public Guid Identifier { get; set; }

        [BsonId]
        public ObjectId DatabaseIdentifier { get; set; }
    }
}