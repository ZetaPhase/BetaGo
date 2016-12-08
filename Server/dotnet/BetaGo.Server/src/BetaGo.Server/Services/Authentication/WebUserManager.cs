using BetaGo.Server.Services.Database;

namespace BetaGo.Server.Services.Authentication
{
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
    }
}