using LiteDB;

namespace BetaGo.Server.Services.Database
{
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