using BetaGo.Server.DataModels.Registration;
using BetaGo.Server.Services.Database;
using System.Security;

namespace BetaGo.Server.Services.Authentication
{
    /// <summary>
    /// A user manager service. Provides access to common operations with users, and abstracts the database
    /// </summary>
    public class WebUserManager
    {
        public RegisteredUser FindUserByUsername(string username)
        {
            RegisteredUser storedUserRecord = null;
            using (var db = new DatabaseAccessService().OpenOrCreateDefault())
            {
                var registeredUsers = db.GetCollection<RegisteredUser>(DatabaseAccessService.UsersCollectionDatabaseKey);
                var userRecord = registeredUsers.FindOne(u => u.Username == username);
                storedUserRecord = userRecord;
            }
            if (storedUserRecord == null)
            {
                return null;
            }
            return storedUserRecord;
        }

        public bool UpdateUserInDatabase(RegisteredUser currentUser)
        {
            bool result;
            using (var db = new DatabaseAccessService().OpenOrCreateDefault())
            {
                var registeredUsers = db.GetCollection<RegisteredUser>(DatabaseAccessService.UsersCollectionDatabaseKey);
                result = registeredUsers.Update(currentUser);
            }
            return result;
        }

        /// <summary>
        /// Attempts to register a new user. Only the username is validated, it is expected that other fields have already been validated!
        /// </summary>
        public RegisteredUser RegisterUser(RegistrationRequest regRequest)
        {
            RegisteredUser newUserRecord = null;
            if (FindUserByUsername(regRequest.Username) != null)
            {
                //BAD! Another conflicting user exists!
                throw new SecurityException("A user with the same username already exists!");
            }
            using (var db = new DatabaseAccessService().OpenOrCreateDefault())
            {
                var registeredUsers = db.GetCollection<RegisteredUser>(DatabaseAccessService.UsersCollectionDatabaseKey);
                // TODO: Maybe calculate cryptographic info
                // Create user
                newUserRecord = new RegisteredUser
                {
                    Username = regRequest.Username,
                    PhoneNumber = regRequest.PhoneNumber
                };
                // Add the user to the database
                registeredUsers.Insert(newUserRecord);

                // Index database
                registeredUsers.EnsureIndex(x => x.Identifier);
            }
            return newUserRecord;
        }
    }
}