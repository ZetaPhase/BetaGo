using LiteDB;

namespace BetaGo.Server.Services.Database
{
    /// <summary>
    /// A service that contains global database table info, and provides access to the LiteDB store
    /// </summary>
    public class DatabaseAccessService
    {
        public static string UsersCollectionDatabaseKey => "Users";

        public LiteDatabase OpenOrCreateDefault()
        {
            //notebin3.lidb
            return new LiteDatabase("betago_srv.lidb");
        }
    }
}